# QuizPlatform App Flow

## 1. User Journey Overview
The application supports two primary user journeys:

1. **Learner journey**
   - Register or log in
   - Browse available quizzes
   - Open a quiz
   - Answer questions
   - Submit the quiz and see the score

2. **Admin journey**
   - Log in as an admin
   - Open the admin panel
   - Create or manage quizzes
   - Add questions to quizzes
   - Review and remove quiz content as needed

## 2. Screens and Pages

### A. Authentication Screens
- **Login Screen**
  - Entry point for returning users
  - Accepts email and password
  - Redirects to dashboard after successful login

- **Register Screen**
  - Allows new users to create an account
  - Collects username, email, and password
  - Redirects users to the app after successful registration

### B. Learner Screens
- **Dashboard Screen**
  - Shows available quizzes
  - Supports search/filtering
  - Lets users open a quiz to begin taking it

- **Quiz Taking Screen**
  - Displays one question at a time
  - Allows answer selection
  - Supports navigation between questions
  - Allows final submission

- **Result / Submission Screen**
  - Shows score and total questions answered
  - Confirms quiz completion

### C. Admin Screens
- **Admin Panel**
  - Main management area for quiz content
  - Allows creating a new quiz
  - Allows adding questions to a selected quiz
  - Allows deleting quizzes

## 3. App Flow

### Guest / New User Flow
1. User opens the app
2. User lands on the login page
3. If new, user clicks register
4. User creates an account
5. User is redirected to the dashboard

### Learner Flow
1. User logs in
2. User sees the dashboard with quizzes
3. User searches or browses quizzes
4. User opens a quiz
5. User starts answering questions
6. User submits the quiz
7. User sees the result score

### Admin Flow
1. Admin logs in
2. Admin opens the admin panel
3. Admin creates a quiz
4. Admin adds questions to the quiz
5. Admin saves content and confirms the quiz is available
6. Admin can later edit or delete the quiz

## 4. Current Implementation Status
### Implemented
- Login and registration screens
- Dashboard with quiz browsing
- Quiz detail / quiz-taking screen
- Admin panel for quiz creation and question addition
- Backend authentication and quiz APIs
- Search and quiz submission flow

### Ongoing / Needs Improvement
- Stronger validation and error handling
- Better admin route protection
- More polished UX and loading states
- Better handling of quiz result history and review

## 5. Future Enhancements
- User profile and account management
- Quiz result history and analytics
- Pagination and advanced filtering
- Timed quizzes and scoring rules
- Better admin editing experience for existing quiz questions
