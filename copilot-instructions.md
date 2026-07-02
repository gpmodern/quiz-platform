---
name: quizplatform-copilot-instructions
description: "Project-wide guidance for Copilot in the QuizPlatform repo, with Java Spring Boot backend and Angular frontend conventions."
---

## Project context
This repository contains a Java Spring Boot backend and an Angular frontend.

- Backend: Spring Boot `3.5.11`, Java target `17`, Spring Data JPA, Spring Security, JWT, Lombok, Log4j2, MySQL connector.
- Frontend: Angular `21.2.0`, TypeScript `5.9.2`, Bootstrap `5.3.8`, RxJS `7.8.0`, Angular CLI `21.2.0`, npm `10.9.2`.
- Installed system versions: Java `21.0.1`, Maven `3.9.12`, Node `22.14.0`, npm `10.9.2`, Angular CLI `21.2.0`.

## Copilot behavior
- Prefer clean architecture and separation of concerns.
- For backend work, use Spring Boot REST controllers, services, repositories, DTOs, validation, and security best practices.
- For frontend work, use Angular modules, components, services, reactive forms, routing, and maintainable state flow.
- When integrating backend and frontend, include clear API contracts and sample HTTP request payloads.
- Provide runnable examples and mention the required stack versions when relevant.
- Keep explanations concise but technical enough for implementation.

## Coding guidelines
- Use dependency injection rather than manual object construction.
- Use `@RestController`, `@Service`, `@Repository`, and Spring Data JPA repositories where applicable.
- Use `@Validated`, `@Valid`, and `@RequestBody` for input validation.
- Use Angular `FormGroup` / `FormControl` and reactive validation for forms.
- Keep controllers thin; put business logic in service classes.
- Favor DTOs on the API boundary to avoid leaking internal entities.
- Keep frontend components reusable and avoid overly large component classes.

## Testing and quality
- Add unit tests for service logic and controller endpoints where appropriate.
- Use Spring Boot test support for backend tests and Angular test utilities for frontend.
- When suggesting scripts or commands, reference `npm`, `ng`, and `mvn` as appropriate.

## Avoid
- Avoid generating code that depends on newer versions than the repo already specifies.
- Avoid using deprecated Spring APIs or Angular APIs.
- Avoid unrelated non-web tasks or unsupported frameworks.

## Useful versions for this repo
- Java: 17
- Spring Boot: 3.5.11
- Angular: 21.2.0
- TypeScript: 5.9.2
- npm: 10.9.2
- Bootstrap: 5.3.8
- RxJS: 7.8.0
- Log4j2: 2.20.0
