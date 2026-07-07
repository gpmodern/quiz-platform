# QuizPlatform PRD

## Problem Statement
Quiz platforms are often fragmented, hard to manage, and difficult to use for both instructors and learners. QuizPlatform aims to provide a simple, secure, and modern web app where administrators can create and manage quizzes, while students can register, discover quizzes, take them, and receive immediate results.

## Target Users
- **Administrators / Instructors**: Create quizzes, add questions, and manage content.
- **Learners / Students**: Register, browse available assessments, take quizzes, and view results.
- **Developers / Maintainers**: Extend the product, improve reliability, and keep the platform secure and maintainable.

## Core Features
- User registration and login with JWT-based authentication.
- Role-based access control for admin and learner flows.
- Quiz creation, viewing, updating, and deletion.
- Question creation, update, and deletion for each quiz.
- Search and discovery of quizzes.
- Quiz-taking flow with answer submission and scoring.
- Separate admin and learner experiences in the frontend.

## Why It Matters
- Reduces manual effort for instructors by centralizing quiz creation and management.
- Improves learner engagement through a straightforward online assessment experience.
- Keeps the system secure with authentication and authorization built into the core workflow.
- Gives the team a practical full-stack foundation for future growth.

## Current Release Scope
- Backend: Spring Boot, Spring Security, JWT, JPA, MySQL-compatible persistence, and REST APIs.
- Frontend: Angular-based UI with login, registration, dashboard, quiz-taking, and admin management views.
- Tooling: Maven for backend builds and npm/Angular CLI for frontend development.

## Technical Context
- **Backend**: Spring Boot `3.5.11`, Java target `17`, Spring Data JPA, Spring Security, JWT, Lombok, Log4j2 `2.20.0`, MySQL connector.
- **Frontend**: Angular `21.2.0`, Angular CLI `21.2.0`, TypeScript `5.9.2`, Bootstrap `5.3.8`, RxJS `7.8.0`, Vitest.
- **Installed system versions**: Java `21.0.1`, Maven `3.9.12`, Node `22.14.0`, npm `10.9.2`, Angular CLI `21.2.0`.

## Success Criteria
- Secure backend APIs support authentication, quiz CRUD, question management, search, and quiz submission.
- Frontend supports login, registration, quiz discovery, quiz-taking, and admin quiz editing.
- Admin-only workflows are protected.
- Quiz submissions are scored correctly and persisted.
- The application runs locally with clear setup instructions for both backend and frontend.
