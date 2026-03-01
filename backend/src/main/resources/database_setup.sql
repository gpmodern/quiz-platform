DROP DATABASE IF EXISTS QuizPlatform;
CREATE DATABASE QuizPlatform;
USE QuizPlatform;

CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('user', 'admin') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE Quizzes (
    quiz_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES Users(user_id)
);
CREATE TABLE Questions (
    question_id INT AUTO_INCREMENT PRIMARY KEY,
    quiz_id INT,
    question_text TEXT NOT NULL,
    question_type ENUM('multiple_choice', 'true_false') NOT NULL,
    -- created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);
CREATE TABLE Options (
    option_id INT AUTO_INCREMENT PRIMARY KEY,
    question_id INT,
    option_text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE
);
CREATE TABLE QuizAttempts (
    attempt_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    quiz_id INT,
    score FLOAT,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES Quizzes(quiz_id) ON DELETE CASCADE
);
CREATE TABLE UserAnswers (
    answer_id INT AUTO_INCREMENT PRIMARY KEY,
    attempt_id INT,
    question_id INT,
    selected_option_id INT,
    FOREIGN KEY (attempt_id) REFERENCES QuizAttempts(attempt_id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES Questions(question_id) ON DELETE CASCADE,
    FOREIGN KEY (selected_option_id) REFERENCES Options(option_id) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO Users (username, password, email, role) VALUES
('admin', 'adminpass', 'admin@email.com', 'admin'),
('user1', 'user1pass', 'user1@email.com', 'user'),
('user2', 'user2pass', 'user2@email.com', 'user'),
('user3', 'user3pass', 'user3@email.com', 'user'),
('user4', 'user4pass', 'user4@email.com', 'user');

INSERT INTO Quizzes (title, description, category, created_by) VALUES
('General Knowledge Quiz', 'Test your general knowledge with this quiz!', 'General Knowledge', 1),
('Science Quiz', 'Challenge your science knowledge with this quiz!', 'Science', 1);

INSERT INTO Questions (quiz_id, question_text, question_type) VALUES
(1, 'What is the capital of France?', 'multiple_choice'),
(1, 'Which planet is known as the Red Planet?', 'multiple_choice'),
(2, 'What is the chemical symbol for water?', 'multiple_choice'),
(2, 'True or False: The Earth is flat.', 'true_false');

INSERT INTO Options (question_id, option_text, is_correct) VALUES
(1, 'Paris', TRUE),
(1, 'London', FALSE),
(1, 'Berlin', FALSE),
(1, 'Madrid', FALSE),
(2, 'Earth', FALSE),
(2, 'Mars', TRUE),
(2, 'Jupiter', FALSE),
(2, 'Saturn', FALSE),
(3, 'H2O', TRUE),
(3, 'O2', FALSE),
(3, 'CO2', FALSE),
(3, 'NaCl', FALSE),
(4, 'True', FALSE),
(4, 'False', TRUE);

INSERT INTO QuizAttempts (user_id, quiz_id, score) VALUES
(2, 1, 80.0),
(3, 1, 90.0),
(2, 2, 70.0),
(3, 2, 85.0);

INSERT INTO UserAnswers (attempt_id, question_id, selected_option_id) VALUES
(1, 1, 1), -- user2 selected 'Paris' for question 1
(1, 2, 6), -- user2 selected 'Mars' for question 2
(2, 1, 1), -- user3 selected 'Paris' for question 1
(2, 2, 6), -- user3 selected 'Mars' for question 2
(3, 3, 9), -- user2 selected 'H2O' for question 3
(3, 4, 13), -- user2 selected 'False' for question 4
(4, 3, 9), -- user3 selected 'H2O' for question 3
(4, 4, 13); -- user3 selected 'False' for question 4