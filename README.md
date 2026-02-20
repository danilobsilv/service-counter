# ServiceCounter

ServiceCounter is a robust backend system designed to manage service desks with intelligent real-time queues. It handles ticket issuance, configurable priority rules (SLA/triage), multiple queues, and live dashboard updates via SSE, with a strong focus on concurrency and performance under load.

## 🚀 Overview

The system allows for a "service counter" environment where multiple queues (Normal, Priority, Return) can operate under a single desk. It features a dynamic scoring engine to prioritize tickets and a transactional mechanism to ensure no ticket is called twice by different workers.

## 🛠 Tech Stack

* **Framework:** Spring Boot 4.0.2
* **Database:** PostgreSQL (Source of Truth)
* **Migrations:** Flyway
* **Containerization:** Docker & Docker Compose
* **Real-time:** SSE (Server-Sent Events)
* **Language:** Java 21

## 🏗 Project Structure

The project follows a feature-based package structure for better modularity:

* **serviceDesk**: Administration of physical or virtual service counters.
* **queue**: Management of different queue types (NORMAL, PRIORITY, RETURN).
* **ticket**: Lifecycle of a customer ticket (WAITING, CALLED, IN_SERVICE, etc.).
* **rules**: Rulesets and priority score computation.
* **sessions**: Tracking of the actual service performed by a worker.
* **events**: Domain event publishing for real-time updates.
* **seats**: Concurrent seat reservation system (Extra feature).
* **shared**: Global exception handling, logging filters, and base entities.

## 🚦 Core Features

### 1. Queue Management

* Create and manage multiple queues per desk.
* Unique code constraints per desk to avoid duplication.

### 2. Intelligent Prioritization

* **Rulesets**: Only one active ruleset per desk.
* **Score Computation**: Dynamic recalculation of ticket scores based on custom rules (JSON-based conditions/actions).

### 3. High Concurrency Handling

* **Next Ticket**: Uses transactional locks (`FOR UPDATE SKIP LOCKED`) to allow multiple workers to request the next ticket simultaneously without conflicts.
* **Seat Reservation**: Atomic reservation flow to prevent double-booking.

### 4. Real-time Dashboard

* Standardized SSE stream for events like `ticket.created`, `ticket.called`, and `session.started`.
* Snapshot endpoint to retrieve the current state of the desk.

## 📁 API Endpoints (Summary)

| Category | Endpoint | Method |
| --- | --- | --- |
| **Health** | `/health` | `GET` |
| **Desks** | `/desks` | `POST`, `GET` |
| **Queues** | `/desks/{deskId}/queues` | `POST`, `GET` |
| **Tickets** | `/queues/{queueId}/tickets` | `POST` |
| **Service** | `/desks/{deskId}/next-ticket` | `POST` |
| **Sessions** | `/sessions/{id}/start` | `POST` |
| **Real-time** | `/desks/{deskId}/events/stream` | `GET` |

> For a full list of contracts, see [API_CONTRACTS.md](https://www.google.com/search?q=docs/API_CONTRACTS.md).

## 🐳 Getting Started

### Prerequisites

* Docker and Docker Compose
* Java 21 (for local development)

### Running with Docker

1. **Development Environment**:
Includes PostgreSQL, Redis, and pgAdmin.
```bash
docker-compose -f docker-compose.dev.yml up -d

```


2. **Production Environment**:
Builds the application and runs the full stack.
```bash
docker-compose -f docker-compose.prod.yml up -d

```



### Development Seed

When running in the `dev` profile, the system automatically seeds a principal desk with three default queues (NORMAL, PRIORITY, RETURN) to facilitate immediate testing.

## ⚠️ Error Model

The API returns a standardized error format including:

* `timestamp`, `status`, `error`, `message`, `path`, and `requestId`.
* Common status codes: 400 (Invalid), 404 (Not Found), 409 (Conflict), and 422 (Business Rule).