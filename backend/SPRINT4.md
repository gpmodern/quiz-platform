# Sprint 4

Goals addressed in this sprint:

* **Quiz search/filter** – clients can search quizzes by title and/or category.
* Added repository query methods using Spring Data derived queries.
* Extended `QuizService` and `QuizServiceImpl` with `searchQuizzes` logic handling various parameter combinations.
* New controller endpoint `GET /api/quizzes/search` with optional `title` and `category` params.
* Service/controller tests verify all search behaviors (title only, category only, both, none).

The system still passes the full test suite and the coverage report includes the new code paths.