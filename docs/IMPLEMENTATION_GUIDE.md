# ServiceCounter (Balcão) — Guia de Implementação

## Visão do sistema
Um “balcão de atendimento” com múltiplas filas, tickets, regras de prioridade por score e fluxo de atendimento via sessões.
Inclui painel real-time via SSE e simulação de atendentes (workers).
Desafio extra: reserva concorrente de assentos (locks/transações/idempotência).

Stack sugerida:
- Spring Boot (Web, Validation, Data JPA)
- PostgreSQL (source of truth)
- Flyway (migrations)
- Docker Compose (dev/prod)
- SSE para painel (WebSocket opcional depois)

---

## Fase 0 — Fundação
### Objetivo
Rodar a aplicação com base sólida: estrutura, migrations, entidades iniciais, padrão de erros, health/version, seed e smoke test.

### Checklist
1. Estrutura de pacotes por feature:
   - serviceDesk, queue, ticket, rules, sessions, events, seats, shared
2. Perfis + env:
   - application-dev.yml / application-prod.yml
3. Flyway V1:
   - tabelas base: service_desk, queue, ticket
   - constraints e índices essenciais
4. Entidades JPA iniciais
5. Tratamento global de erros + request_id
6. Endpoints:
   - GET /health
   - GET /version
7. Seed dev:
   - cria 1 desk e filas NORMAL/PRIORITY/RETURN
8. Smoke test:
   - subir app + criar desk/queues via seed sem gambiarra

Pronto quando:
- `docker compose up` + `curl /health` ok
- seed cria dados e você consegue listar

---

## Fase 1 — Service Desk + Queues
### Objetivo
Administrar balcão e filas.

### Implementar
#### Service Desk
- POST /desks
- GET /desks
- GET /desks/{id}
- PATCH /desks/{id}
- DELETE /desks/{id} (soft delete => is_active=false)

Regras:
- desk inexistente => 404
- payload inválido => 400/422

#### Queues
- POST /desks/{deskId}/queues
- GET /desks/{deskId}/queues
- PATCH /queues/{queueId}

Regras:
- code único por desk (NORMAL/PRIORITY/RETURN) => conflito 409
- queue precisa pertencer ao desk correto nos fluxos => 404/409 conforme caso

Pronto quando:
- você cria 1 desk e 3 filas e lista por desk

---

## Fase 2 — Tickets (MVP)
### Objetivo
Colocar pessoas na fila e enxergar estado.

### Implementar
- POST /queues/{queueId}/tickets
- GET /tickets (filtros: deskId, queueId, status, dateFrom/dateTo)
- PATCH /tickets/{id}/cancel
- PATCH /tickets/{id}/triage

Regras:
- ticket começa em WAITING
- ordering padrão: WAITING por created_at asc (sem regras ainda)
- cancel só se status permitir (WAITING/CALLED com regras claras) => 409/422
- triage update altera triage_level/flags

Pronto quando:
- cria 100 tickets e lista WAITING ordenado por created_at

---

## Fase 3 — Regras de prioridade (score)
### Objetivo
Configurar prioridades por ruleset e recalcular score.

### Implementar
- POST /desks/{deskId}/rulesets
- POST /rulesets/{rulesetId}/rules
- POST /desks/{deskId}/rulesets/{rulesetId}/activate (somente 1 ativo por desk)
- POST /desks/{deskId}/tickets/recompute-score (para WAITING)

Regras:
- 1 ruleset ativo por desk
- regra tem condição+ação em JSON (validação mínima)

Pronto quando:
- muda regra e vê ordem mudar (score + tie-break created_at)

---

## Fase 4 — Chamar próximo ticket (concorrência)
### Objetivo
Endpoint crítico: worker pede o próximo ticket sem duplicar.

### Implementar
- POST /desks/{deskId}/next-ticket (body: workerId, queueCodes opcional)

Regras:
- pega somente tickets WAITING elegíveis
- ordenação: priority_score desc, created_at asc
- "um ticket não pode ser pego duas vezes" (lock transacional, ex.: FOR UPDATE SKIP LOCKED)
- ao pegar: ticket => CALLED + called_at; criar session status CALLED

Pronto quando:
- 50 requests simultâneas e nenhum ticket duplicado

---

## Fase 5 — Fluxo do atendimento (sessions)
### Objetivo
Completar ciclo.

### Implementar
- POST /sessions/{id}/start  => IN_SERVICE + started_at
- POST /sessions/{id}/finish => DONE + ended_at e ticket DONE
- POST /sessions/{id}/no-show => NO_SHOW; ticket -> RETURN ou DONE (regra simples)
- GET /sessions (filtros por deskId, workerId, date)

Regras:
- máquina de estados (transições válidas) => 409/422
- consistência session-ticket

Pronto quando:
- chamar -> iniciar -> finalizar funciona e fica consistente

---

## Fase 6 — Painel real-time (SSE)
### Objetivo
Atualizar painel em tempo real.

### Implementar
- GET /desks/{deskId}/events/stream (SSE)
- GET /desks/{deskId}/snapshot (estado atual)

Regras:
- emitir eventos nas mudanças relevantes (ticket/session)
- payload padronizado (type, entityId, timestamp, data)

Pronto quando:
- painel atualiza ao chamar próximo ticket

---

## Fase 7 — Workers simulados
### Objetivo
Carga e validação.

### Implementar
- CLI/script ou endpoint: POST /simulations/workers
- simular N workers consumindo fila
- métricas básicas: throughput, tempo médio até called

Pronto quando:
- 20–50 workers sem inconsistência

---

## Fase 8 — Reservas concorrentes de assentos (extra)
### Objetivo
Problema clássico de lock/idempotência.

### Implementar
- entidades: event, seat, reservation
- POST /events/{eventId}/seats/{seatId}/reserve
- POST /reservations/{id}/confirm
- POST /reservations/{id}/cancel
- job/endpoint para expirar reservas

Regras:
- 100 requisições simultâneas no mesmo assento => só 1 reserva vence
- conflito => 409

---

## Fase 9 — Qualidade e hardening
- logs estruturados + request_id
- métricas/tracing (opcional)
- testes: unit, integração, concorrência, carga (k6)