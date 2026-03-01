# Sprint 2

In Sprint 2 we added CRUD operations for quizzes and corresponding tests:

- **New service methods**:
  - `findById(Long)`
  - `updateQuiz(Long, CreateQuizRequest)`
  - `deleteQuiz(Long)`
- **Controller endpoints**:
  - `GET /api/quizzes/{id}`
  - `PUT /api/quizzes/{id}`
  - `DELETE /api/quizzes/{id}`
- **Error handling** via `ResourceNotFoundException` (404 responses for missing resources).
- **Tests** for both service and controller covering success and not-found scenarios.

Coverage reports were regenerated and branch coverage improved accordingly.