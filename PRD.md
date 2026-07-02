# QuizPlatform PRD

## Problem Statement
Existing quiz systems are often fragmented, difficult to administer, and hard to use for both instructors and learners. This project aims to provide a single web-based platform where administrators can create and manage quizzes and questions, and learners can securely authenticate, browse quizzes, take them, and receive immediate scoring.

## Target Users
- **Administrators / Instructors**: People who create quizzes, add questions, and manage quiz content.
- **Registered Learners / Students**: Users who log in, browse available quizzes, take quizzes, and view results.
- **Developers / Maintainers**: Engineers who extend the platform, add new features, and keep the system secure.

## Core Features
- **User authentication and registration** using JWT-based login and registration APIs.
- **Role-based access control** for admin features and user quiz-taking.
- **Quiz management**: create, read, update, and delete quizzes.
- **Question management**: add, update, and delete questions related to a quiz.
- **Search and discovery**: search quizzes by filters such as title and category.
- **Quiz attempt workflow**: learners retrieve quiz questions without correct answers, submit answers, and receive scored results.
- **Admin UI** for quiz creation and question authoring.
- **Learner UI** for dashboard browsing, search, and taking quizzes.
- **Backend and frontend integration** across Spring Boot and Angular.

## Why It Matters
- **Improves assessment efficiency** by enabling online quiz creation and delivery in one place.
- **Reduces manual work** for teachers and admins by supporting content management and search.
- **Increases learner engagement** with an interactive quiz-taking experience and immediate feedback.
- **Supports secure usage** through JWT authentication and role enforcement.
- **Provides a maintainable foundation** using modern, widely supported technologies: Spring Boot `3.5.11`, Java `17`, Angular `21.2.0`, TypeScript `5.9.2`, npm `10.9.2`.

## Technical Context
- **Backend**: Spring Boot `3.5.11`, Java `17` target, Spring Data JPA, Spring Security, JWT, Lombok, Log4j2 `2.20.0`, MySQL connector.
- **Frontend**: Angular `21.2.0`, Angular CLI `21.2.0`, TypeScript `5.9.2`, Bootstrap `5.3.8`, RxJS `7.8.0`, Vitest.
- **Installed system versions** on this machine: Java `21.0.1`, Maven `3.9.12`, Node `22.14.0`, npm `10.9.2`, Angular CLI `21.2.0`.

## Success Criteria
- Backend exposes secure REST APIs for authentication, quiz CRUD, question CRUD, search, and quiz submission.
- Frontend supports user login/register, quiz discovery, quiz taking, and admin quiz editing.
- Admin-only endpoints are protected and only accessible by admin users.
- Learner quiz submissions are scored correctly and persisted.
- The project is runnable locally with clearly documented commands for both backend and frontend.
