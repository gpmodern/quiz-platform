import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface QuestionDto {
  id?: number;
  questionText: string;
  questionType: string;
  options?: string[];
  correctAnswer?: string;
}

export interface QuizDto {
  id?: number;
  title: string;
  description?: string;
  questions?: QuestionDto[];
  createdBy?: string;
  createdAt?: string;
}

export interface CreateQuizRequest {
  title: string;
  description?: string;
}

export interface AnswerDto {
  questionId: number;
  answer: string;
}

export interface TakeQuizRequest {
  answers: AnswerDto[];
}

export interface TakeQuizResponse {
  score: number;
  totalQuestions: number;
}

@Injectable({
  providedIn: 'root'
})
export class QuizService {
  private apiUrl = 'http://localhost:8080/api/quizzes';

  constructor(private http: HttpClient) {}

  // Quiz management
  createQuiz(req: CreateQuizRequest): Observable<QuizDto> {
    return this.http.post<QuizDto>(this.apiUrl, req);
  }

  getAllQuizzes(): Observable<QuizDto[]> {
    return this.http.get<QuizDto[]>(this.apiUrl);
  }

  getQuizById(id: number): Observable<QuizDto> {
    return this.http.get<QuizDto>(`${this.apiUrl}/${id}`);
  }

  updateQuiz(id: number, quiz: QuizDto): Observable<QuizDto> {
    return this.http.put<QuizDto>(`${this.apiUrl}/${id}`, quiz);
  }

  deleteQuiz(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  searchQuizzes(query: string): Observable<QuizDto[]> {
    return this.http.get<QuizDto[]>(`${this.apiUrl}/search`, {
      params: { query }
    });
  }

  // Question management
  addQuestion(quizId: number, question: QuestionDto): Observable<QuestionDto> {
    return this.http.post<QuestionDto>(`${this.apiUrl}/${quizId}/questions`, question);
  }

  updateQuestion(
    quizId: number,
    questionId: number,
    question: QuestionDto
  ): Observable<QuestionDto> {
    return this.http.put<QuestionDto>(
      `${this.apiUrl}/${quizId}/questions/${questionId}`,
      question
    );
  }

  deleteQuestion(quizId: number, questionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${quizId}/questions/${questionId}`);
  }

  // Quiz attempts
  takeQuiz(quizId: number, request: TakeQuizRequest): Observable<TakeQuizResponse> {
    return this.http.post<TakeQuizResponse>(`${this.apiUrl}/${quizId}/take`, request);
  }

  getQuizQuestions(quizId: number): Observable<QuestionDto[]> {
    return this.http.get<QuestionDto[]>(`${this.apiUrl}/${quizId}/take`);
  }
}
