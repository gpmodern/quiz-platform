import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { QuizService, QuizDto, QuestionDto, AnswerDto } from '../../../core/services/quiz.service';

@Component({
  selector: 'app-quiz-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './quiz-detail.component.html',
  styleUrl: './quiz-detail.component.css'
})
export class QuizDetailComponent implements OnInit {
  quiz = signal<QuizDto | null>(null);
  questions = signal<QuestionDto[]>([]);
  currentQuestionIndex = signal(0);
  selectedAnswers = signal<Record<number, string>>({});
  loading = signal(false);
  submitting = signal(false);
  error = signal<string | null>(null);
  quizStarted = signal(false);
  quizSubmitted = signal(false);
  score = signal<number | null>(null);
  totalQuestions = signal(0);

  constructor(
    private quizService: QuizService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      const quizId = params['id'];
      this.loadQuiz(quizId);
      this.loadQuestions(quizId);
    });
  }

  loadQuiz(id: number): void {
    this.loading.set(true);
    this.quizService.getQuizById(id).subscribe({
      next: (quiz: QuizDto) => {
        this.quiz.set(quiz);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.error.set('Failed to load quiz');
        this.loading.set(false);
      }
    });
  }

  loadQuestions(id: number): void {
    this.quizService.getQuizQuestions(id).subscribe({
      next: (questions: QuestionDto[]) => {
        this.questions.set(questions);
        this.totalQuestions.set(questions.length);
      },
      error: (err: any) => {
        this.error.set('Failed to load questions');
      }
    });
  }

  startQuiz(): void {
    this.quizStarted.set(true);
  }

  getCurrentQuestion(): QuestionDto | null {
    const questions = this.questions();
    return questions[this.currentQuestionIndex()] || null;
  }

  selectAnswer(answer: string): void {
    const current = this.getCurrentQuestion();
    if (current?.id) {
      const answers = { ...this.selectedAnswers() };
      answers[current.id] = answer;
      this.selectedAnswers.set(answers);
    }
  }

  isAnswerSelected(answer: string): boolean {
    const current = this.getCurrentQuestion();
    return current?.id ? this.selectedAnswers()[current.id] === answer : false;
  }

  nextQuestion(): void {
    if (this.currentQuestionIndex() < this.questions().length - 1) {
      this.currentQuestionIndex.update((val) => val + 1);
    }
  }

  previousQuestion(): void {
    if (this.currentQuestionIndex() > 0) {
      this.currentQuestionIndex.update((val) => val - 1);
    }
  }

  goToQuestion(index: number): void {
    this.currentQuestionIndex.set(index);
  }

  submitQuiz(): void {
    if (!this.quiz()?.id) return;

    this.submitting.set(true);
    this.error.set(null);

    const answers: AnswerDto[] = Object.entries(this.selectedAnswers()).map(([questionId, answer]) => ({
      questionId: Number(questionId),
      answer
    }));

    this.quizService.takeQuiz(this.quiz()!.id!, { answers }).subscribe({
      next: (response: { score: number }) => {
        this.score.set(response.score);
        this.quizSubmitted.set(true);
        this.submitting.set(false);
      },
      error: (err: any) => {
        this.error.set('Failed to submit quiz');
        this.submitting.set(false);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  getAnsweredCount(): number {
    return Object.keys(this.selectedAnswers()).length;
  }

  getProgressPercent(): number {
    return (this.getAnsweredCount() / this.totalQuestions()) * 100;
  }
}
