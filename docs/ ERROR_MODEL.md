# Error Model

## Formato padrão
{
  "timestamp": "2026-02-10T12:34:56Z",
  "status": 409,
  "error": "CONFLICT",
  "message": "Queue code already exists for this desk",
  "path": "/desks/{id}/queues",
  "requestId": "..."
}

## Códigos
- 400: payload inválido / parse
- 404: recurso não encontrado
- 409: conflito de concorrência/unique/estado
- 422: regra de negócio / transição inválida