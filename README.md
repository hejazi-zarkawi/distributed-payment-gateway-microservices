# Distributed Payment Gateway

A distributed payment gateway built with **Java 21, Spring Boot, Spring Cloud, PostgreSQL, Redis, and Kafka**, designed around a microservices architecture.

This repository represents the evolution of the original payment gateway from a modular monolithic architecture into independently deployable services with centralized authentication, service discovery, centralized configuration, distributed rate limiting, idempotency, asynchronous event processing, and merchant-context propagation.

> 🚧 **Status: Active Development**
>
> The core microservices architecture is implemented and running. Advanced distributed-system capabilities such as Saga orchestration, distributed scheduler locking, circuit breakers, bulkheads, observability, Kubernetes deployment, and load testing are planned as the next stages.

---

## Architecture

```text
                                  ┌──────────────────┐
                                  │      Client      │
                                  └────────┬─────────┘
                                           │
                                           ▼
                              ┌────────────────────────┐
                              │      API Gateway       │
                              │                        │
                              │ • JWT Authentication   │
                              │ • API Key Validation   │
                              │ • Request Filtering    │
                              │ • Rate Limiting        │
                              │ • Merchant Resolution  │
                              │ • Context Propagation  │
                              └───────────┬────────────┘
                                          │
                    ┌─────────────────────┼─────────────────────┐
                    │                     │                     │
                    ▼                     ▼                     ▼
             ┌─────────────┐      ┌─────────────┐      ┌─────────────┐
             │  Merchant   │      │   Payment   │      │    Vault    │
             │   Service   │      │   Service   │      │   Service   │
             └──────┬──────┘      └──────┬──────┘      └──────┬──────┘
                    │                    │                    │
                    └────────────────────┼────────────────────┘
                                         │
                                         ▼
                                ┌──────────────────┐
                                │    Operations    │
                                │     Service      │
                                └────────┬─────────┘
                                         │
                                         ▼
                                      Kafka
                                         │
                              ┌──────────┴──────────┐
                              ▼                     ▼
                       Webhook Delivery       Async Processing
                              │
                              ▼
                     Retry / Dead Letter


       ┌───────────────────┐             ┌───────────────────┐
       │   Config Server   │             │  Discovery Server │
       │  Spring Cloud     │             │      Eureka       │
       └───────────────────┘             └───────────────────┘


                     Shared Infrastructure
              ┌──────────────┬──────────────┐
              ▼              ▼              ▼
          PostgreSQL       Redis          Kafka
```

### Request flow

Authentication and request-level cross-cutting concerns are centralized at the API Gateway.

```text
Client
  │
  ▼
API Gateway
  │
  ├── JWT / API Key validation
  ├── Rate limiting
  ├── Merchant resolution
  ├── Merchant ID propagation
  └── Key ID propagation
  │
  ▼
Downstream Service
  │
  ▼
MerchantContextFilter
  │
  ▼
MerchantContext
  │
  ▼
Business Logic
```

The gateway propagates merchant-related metadata through request headers. Downstream services reconstruct the request-scoped merchant context through the shared `common-lib`.

---

# Project Structure

```text
distributed-payment-gateway/
│
├── api-gateway-service/
│
├── common-lib/
│
├── config-service/
│
├── discovery-service/
│
├── merchant-service/
│
├── payment-service/
│
├── vault-service/
│
├── operations-service/
│
└── README.md
```

The repository follows a **monorepo structure**, while each Spring Boot application remains an independently deployable service.

---

# Services

| Service | Responsibility |
|---|---|
| `api-gateway-service` | Central request handling, authentication, validation, and merchant propagation |
| `config-service` | Centralized configuration using Spring Cloud Config |
| `discovery-service` | Service registration and discovery using Eureka |
| `merchant-service` | Merchant and API key management |
| `payment-service` | Order and payment lifecycle processing |
| `vault-service` | Card tokenization and secure payment handling |
| `operations-service` | Webhooks, retries, dead-letter processing, and settlement-related operations |
| `common-lib` | Shared merchant context, authentication filters, rate limiting, idempotency, and Redis infrastructure |

---

# Technology Stack

### Language

- Java 21

### Framework

- Spring Boot 4.1.1
- Spring Web / MVC
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Cloud Config
- Spring Cloud Netflix Eureka
- Spring Cloud OpenFeign

### Microservices Infrastructure

- Spring Cloud Gateway
- Spring Cloud Config Server
- Netflix Eureka
- OpenFeign
- Shared common library

### Security

- JWT Bearer Authentication
- API Key Authentication
- Merchant Context Propagation
- Request-scoped security metadata

### Persistence

- PostgreSQL
- Redis

### Messaging

- Apache Kafka

### Observability

- Spring Boot Actuator

### Build

- Maven

---

# API Gateway

The API Gateway acts as the primary entry point for external requests.

It currently handles:

- Request routing
- JWT authentication
- API key authentication
- Request validation
- Merchant resolution
- Merchant metadata propagation
- Gateway-side request filtering
- Rate limiting

The gateway supports two authentication mechanisms.

### JWT Authentication

```text
Authorization: Bearer <token>
```

JWT validation occurs at the gateway before the request is forwarded to downstream services.

### API Key Authentication

```text
Authorization: Basic <base64(keyId:secret)>
```

API keys are validated against merchant metadata and cached authorization data.

Redis participates in the API-key validation and caching path.

---

# Merchant Context Propagation

Merchant context is propagated from the API Gateway to downstream services.

The gateway adds merchant-related metadata to the request before forwarding it.

Current propagated headers include:

```text
X-Merchant-Id
X-Key-Id
X-User-Role
X-Environment
```

The downstream services use `MerchantContextFilter` from the shared library to reconstruct the request-scoped `MerchantContext`.

This allows business logic to access merchant information without repeatedly parsing the raw request headers.

### Flow

```text
API Gateway
    │
    ├── Authenticate
    ├── Resolve merchant
    ├── Resolve key
    │
    ▼
Add merchant metadata to headers
    │
    ▼
Downstream Service
    │
    ▼
MerchantContextFilter
    │
    ▼
MerchantContext
```

### Security consideration

The implementation includes a trust mechanism for inbound merchant headers.

These headers must not be trusted if downstream services can be directly exposed or if the API Gateway can be bypassed.

---

# Common Library

`common-lib` contains cross-cutting functionality shared by multiple services.

Current responsibilities include:

- Merchant context
- Merchant context filter
- Authentication-related filters
- Rate limiting
- Idempotency
- Redis integration abstractions

The library is consumed by the service modules so that cross-cutting functionality does not need to be duplicated across every service.

---

# Rate Limiting

The project implements Redis-backed rate limiting through the shared library.

Redis stores limiter state, allowing rate-limit information to be shared across service instances.

Current rate-limiting functionality is used for:

- API key usage
- Request throughput
- Shared request-rate enforcement

When a configured limit is exceeded, requests are rejected or throttled according to the configured limiter implementation.

### Simplified flow

```text
Client
   │
   ▼
API Gateway
   │
   ▼
Rate Limiter
   │
   ├── Limit available ──► Continue
   │
   └── Limit exceeded ───► Reject / Throttle
```

Redis provides the shared state required by the limiter.

---

# Idempotency

The shared library also provides request idempotency.

Requests use the:

```text
X-Idempotency-Key
```

header.

### Idempotency flow

```text
Client
   │
   │ X-Idempotency-Key
   ▼
Idempotency Layer
   │
   ▼
Redis
   │
   ├── Key in progress
   │       └── Treat as duplicate/in-progress
   │
   ├── Completed request
   │       └── Replay stored response
   │
   └── New request
           │
           ▼
       Process request
           │
           ▼
       Store result
```

Completed responses are stored in Redis and can be replayed for duplicate requests.

This provides protection against duplicate processing when clients retry requests.

---

# Payment Domain

The platform contains a payment domain with explicit lifecycle management and asynchronous processing.

### Implemented capabilities

- Merchant management
- API key management
- Payment initiation
- Payment authorization
- Payment capture
- Card tokenization / vaulting
- Refund flow and refund handling
- Settlement processing
- Webhook creation and delivery
- Payment state transitions
- Outbox/event publication
- Asynchronous event processing

---

# Payment State Machine

The payment lifecycle is controlled using an explicit state machine.

Current payment states include:

```text
CREATED
AUTHORIZING
AUTHORIZED
CAPTURING
CAPTURED
FAILED
CANCELLED
REFUNDED
PARTIALLY_REFUNDED
SETTLED
AUTH_EXPIRED
```

The state machine validates allowed transitions and throws an explicit invalid-transition exception when an invalid transition is attempted.

This prevents the payment lifecycle from entering unsupported states.

---

# Redis

Redis is used throughout the platform for multiple distributed infrastructure concerns.

Current uses include:

- API key validation and caching
- Idempotency
- Rate limiting
- Webhook retry queues
- Retry scheduling and reprocessing support

Redis therefore acts as more than a conventional application cache in this architecture.

---

# Kafka

Kafka provides asynchronous event-driven processing.

### Producers

Payment and order-related events are published from the payment domain.

Outbox-style event publication is implemented for downstream processing.

### Consumers

The operations flow consumes events related to:

- Payments
- Orders
- Refunds
- Settlements

These events drive asynchronous webhook generation and delivery workflows.

### Webhook processing

The asynchronous pipeline supports:

- Webhook delivery
- Retry processing
- Dead-letter handling

```text
Payment Service
      │
      ▼
   Kafka Event
      │
      ▼
Operations Service
      │
      ├── Webhook Delivery
      │
      ├── Retry
      │
      └── Dead Letter
```

---

# Config Server

The `config-service` runs Spring Cloud Config Server.

It acts as the centralized configuration backbone for the microservices.

The configuration server reads configuration from an external Git-based configuration source, while individual services consume configuration from the Config Server.

Default server port:

```text
8888
```

---

# Service Discovery

The `discovery-service` provides service registration and discovery using Netflix Eureka.

Default Eureka port:

```text
8761
```

Services register themselves with Eureka and use service discovery for locating other services.

---

# Inter-Service Communication

The project uses multiple communication patterns.

### Synchronous communication

HTTP-based communication is used for synchronous service interactions.

Spring Cloud OpenFeign is used where applicable.

### Asynchronous communication

Kafka is used for asynchronous event-driven workflows, particularly around payment, order, refund, settlement, and webhook processing.

```text
Synchronous:
Service A ─────HTTP─────► Service B


Asynchronous:
Service A ─────► Kafka ─────► Service B
```

---

# Data & Persistence

The current infrastructure consists of:

### PostgreSQL

Primary relational persistence for application and payment-related data.

### Redis

Used for:

- Caching
- Idempotency
- Rate limiting
- API-key validation
- Retry processing

### Kafka

Used for asynchronous event processing and service communication.

> The current implementation is not a strict database-per-service architecture. PostgreSQL remains the primary relational persistence infrastructure across the system.

---

# Resilience & Observability

## Currently Implemented

- Redis-backed rate limiting
- Redis-backed idempotency
- Webhook retry handling
- Dead-letter processing
- Gateway-level request validation
- Centralized authentication
- Shared infrastructure components

## Current Observability

Spring Boot Actuator is currently included.

The following have **not yet been implemented**:

- Prometheus
- Grafana
- OpenTelemetry
- Distributed tracing

---

# Testing

The repository currently contains Spring Boot smoke tests and service-level test coverage.

A comprehensive end-to-end test suite and Testcontainers-based infrastructure testing are not currently implemented.

---

# Running the Project

## Prerequisites

- Java 21
- Maven
- PostgreSQL
- Redis
- Kafka

Infrastructure services should be available before starting the Spring Boot applications.

The default infrastructure endpoints include:

```text
Config Server:  http://localhost:8888
Eureka Server:  http://localhost:8761
```

## Recommended Startup Order

```text
1. PostgreSQL
2. Redis
3. Kafka
4. config-service
5. discovery-service
6. merchant-service
7. payment-service
8. vault-service
9. operations-service
10. api-gateway-service
```

## Run a Service

From the individual service directory:

```bash
mvn spring-boot:run
```

For example:

```bash
cd config-service
mvn spring-boot:run
```

Then repeat the process for the remaining services.

---

# API Overview

The project currently contains service-specific routes including:

```text
/v1/auth
/v1/payments
/v1/vault
```

as well as merchant/API-key management endpoints and internal:

```text
/internal/...
```

The repository does not currently contain a centralized Swagger/OpenAPI configuration.

---

# Security Considerations

The current security architecture follows a gateway-centric model.

- JWT authentication is handled at the gateway.
- API keys are validated at the gateway.
- Merchant identity is propagated downstream.
- Downstream services reconstruct merchant context using the shared library.
- Merchant-related headers must not be trusted if the gateway can be bypassed.
- Secrets and credentials are intentionally not included in this README.

---

# Monolith → Microservices

This repository represents the architectural evolution of the original Distributed Payment Gateway from a modular monolith into a microservices-based system.

| Concern | Monolith | Microservices |
|---|---|---|
| Deployment | Single application | Independent service instances |
| Authentication | Application-level | Centralized gateway authentication |
| Configuration | Application configuration | Spring Cloud Config |
| Communication | In-process calls | REST + Kafka |
| Scaling | Scale application as a unit | Scale services independently |
| Service discovery | Not required | Eureka |
| Failure boundaries | Shared process | Service-level boundaries |
| Cross-cutting logic | Application modules | Shared common library |
| Async processing | Internal processing | Kafka-based event processing |

The migration is focused not only on splitting the application into multiple services, but also on addressing distributed-system concerns such as authentication boundaries, request propagation, idempotency, rate limiting, asynchronous processing, and service discovery.

---

# Completed Features

- [x] Microservices architecture
- [x] Independent Spring Boot services
- [x] Spring Cloud Config
- [x] Eureka service discovery
- [x] API Gateway
- [x] JWT authentication
- [x] API key authentication
- [x] Merchant context propagation
- [x] Shared common library
- [x] Redis integration
- [x] Redis-backed rate limiting
- [x] Redis-backed idempotency
- [x] PostgreSQL persistence
- [x] Kafka event-driven processing
- [x] Payment initiation
- [x] Payment authorization
- [x] Payment capture
- [x] Payment state machine
- [x] Card tokenization / vault flow
- [x] Refund flow and refund processing
- [x] Settlement processing
- [x] Webhook delivery
- [x] Webhook retries
- [x] Dead-letter processing
- [x] Outbox-style event publication
- [x] Spring Boot Actuator

---

# Roadmap

The following capabilities are planned for future iterations:

- [ ] Saga orchestration
- [ ] Distributed transaction workflow
- [ ] Redis-based distributed scheduler locking
- [ ] Circuit breakers
- [ ] Bulkheads
- [ ] Advanced resilience mechanisms
- [ ] Distributed tracing
- [ ] OpenTelemetry
- [ ] Prometheus
- [ ] Grafana
- [ ] Centralized observability
- [ ] Kubernetes deployment
- [ ] Helm charts
- [ ] Horizontal scaling
- [ ] Load testing
- [ ] Performance benchmarking
- [ ] Broader automated testing
- [ ] Further resilience hardening

---

# Engineering Focus

This project is primarily focused on understanding and implementing backend and distributed-system concepts in a realistic payment-platform domain.

The architecture explores problems such as:

- How should authentication be centralized?
- How can merchant context be propagated safely across services?
- How can duplicate payment requests be prevented?
- How can rate limits be shared across multiple instances?
- When should communication be synchronous versus asynchronous?
- How should payment state transitions be controlled?
- How should webhook failures be retried?
- How should failed asynchronous events be isolated?
- How should distributed transactions be handled?
- How can services remain resilient when dependencies fail?
- How can a distributed system be observed and scaled?

The upcoming development stages focus on implementing these distributed-system patterns rather than simply increasing the number of services.

---

# Project Status

The core microservices foundation is currently operational.

The project has progressed from:

```text
Modular Monolith
       │
       ▼
Microservices Architecture
       │
       ├── API Gateway
       ├── Service Discovery
       ├── Config Server
       ├── Independent Services
       ├── Shared Infrastructure
       ├── Redis
       └── Kafka
       │
       ▼
Next Stage
       │
       ├── Saga
       ├── Distributed Locking
       ├── Circuit Breaker
       ├── Bulkhead
       ├── Observability
       ├── Kubernetes
       └── Load Testing
```

The system is currently a working microservices foundation with additional distributed-system and operational capabilities being developed incrementally.