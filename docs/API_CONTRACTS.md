# API Contracts (resumo)

## Health/Version
- GET /health -> 200 {status:"UP"}
- GET /version -> 200 {name, version, commit?}

## Service Desk
- POST /desks
- GET /desks
- GET /desks/{id}
- PATCH /desks/{id}
- DELETE /desks/{id} (soft)

## Queues
- POST /desks/{deskId}/queues
- GET /desks/{deskId}/queues
- PATCH /queues/{queueId}

## Tickets
- POST /queues/{queueId}/tickets
- GET /tickets?deskId&queueId&status&dateFrom&dateTo
- PATCH /tickets/{id}/cancel
- PATCH /tickets/{id}/triage

## Rules
- POST /desks/{deskId}/rulesets
- POST /rulesets/{rulesetId}/rules
- POST /desks/{deskId}/rulesets/{rulesetId}/activate
- POST /desks/{deskId}/tickets/recompute-score

## Next Ticket (concorrência)
- POST /desks/{deskId}/next-ticket

## Sessions
- POST /sessions/{id}/start
- POST /sessions/{id}/finish
- POST /sessions/{id}/no-show
- GET /sessions?deskId&workerId&dateFrom&dateTo

## SSE
- GET /desks/{deskId}/events/stream
- GET /desks/{deskId}/snapshot