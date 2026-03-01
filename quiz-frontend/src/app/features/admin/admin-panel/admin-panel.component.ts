import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { QuizService, QuizDto, CreateQuizRequest, QuestionDto } from '../../../core/services/quiz.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-panel.component.html',
  styleUrl: './admin-panel.component.css'
})
export class AdminPanelComponent implements OnInit {
  quizzes = signal<QuizDto[]>([]);
  currentUser = signal<any>(null);
  activeTab = signal<'quizzes' | 'create'>('quizzes');
  loading = signal(false);
  submitting = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  
  quizForm: FormGroup;
  questionForm: FormGroup;
  selectedQuiz = signal<QuizDto | null>(null);
  newQuestions = signal<Partial<QuestionDto>[]>([]);

  constructor(
    private formBuilder: FormBuilder,
    private quizService: QuizService,
    private authService: AuthService,
    private router: Router
  ) {
    this.currentUser.set(this.authService.getCurrentUser());

    this.quizForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['']
    });

    this.questionForm = this.formBuilder.group({
      questionText: ['', Validators.required],
      questionType: ['mc', Validators.required],
      options: ['', Validators.required],
      correctAnswer: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAdmin()) {
      this.router.navigate(['/dashboard']);
      return;
    }
    this.loadQuizzes();
  }

  loadQuizzes(): void {
    this.loading.set(true);
    this.quizService.getAllQuizzes().subscribe({
      next: (quizzes: QuizDto[]) => {
        this.quizzes.set(quizzes);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.error.set('Failed to load quizzes');
        this.loading.set(false);
      }
    });
  }

  get qf() {
    return this.quizForm.controls;
  }

  get questf() {
    return this.questionForm.controls;
  }

  createQuiz(): void {
    this.error.set(null);
    this.success.set(null);

    if (this.quizForm.invalid) {
      return;
    }

    this.submitting.set(true);
    const request: CreateQuizRequest = {
      title: this.quizForm.get('title')?.value,
      description: this.quizForm.get('description')?.value
    };

    this.quizService.createQuiz(request).subscribe({
      next: (quiz: QuizDto) => {
        this.selectedQuiz.set(quiz);
        this.newQuestions.set([]);
        this.quizForm.reset();
        this.success.set('Quiz created successfully! Now add questions.');
        this.submitting.set(false);
        this.loadQuizzes();
      },
      error: (err: any) => {
        this.error.set('Failed to create quiz');
        this.submitting.set(false);
      }
    });
  }

  addQuestionForm(): void {
    if (this.questionForm.invalid) {
      return;
    }

    const optionsString = this.questionForm.get('options')?.value;
    const options = optionsString.split(',').map((opt: string) => opt.trim());

    const question: Partial<QuestionDto> = {
      questionText: this.questionForm.get('questionText')?.value,
      questionType: this.questionForm.get('questionType')?.value,
      options,
      correctAnswer: this.questionForm.get('correctAnswer')?.value
    };

    this.newQuestions.update((qs) => [...qs, question]);
    this.questionForm.reset({ questionType: 'mc' });
  }

  removeQuestion(index: number): void {
    this.newQuestions.update((qs) => qs.filter((_, i) => i !== index));
  }

  saveQuestions(): void {
    if (!this.selectedQuiz()?.id) {
      this.error.set('No quiz selected');
      return;
    }

    this.submitting.set(true);
    this.error.set(null);

    let saved = 0;
    const total = this.newQuestions().length;

    if (total === 0) {
      this.success.set('No new questions to save');
      this.submitting.set(false);
      return;
    }

    this.newQuestions().forEach((question) => {
      this.quizService.addQuestion(this.selectedQuiz()!.id!, question as QuestionDto).subscribe({
        next: () => {
          saved++;
          if (saved === total) {
            this.success.set(`${saved} questions added successfully!`);
            this.newQuestions.set([]);
            this.checkQuestionsCount();
            this.submitting.set(false);
          }
        },
        error: () => {
          this.error.set(`Failed to save question`);
          this.submitting.set(false);
        }
      });
    });
  }

  checkQuestionsCount(): void {
    if (this.selectedQuiz()?.id) {
      this.quizService.getQuizById(this.selectedQuiz()!.id!).subscribe({
        next: (quiz: QuizDto) => {
          this.selectedQuiz.set(quiz);
        }
      });
    }
  }

  deleteQuiz(id: number): void {
    if (!confirm('Are you sure you want to delete this quiz?')) {
      return;
    }

    this.quizService.deleteQuiz(id).subscribe({
      next: () => {
        this.success.set('Quiz deleted successfully');
        this.loadQuizzes();
      },
      error: () => {
        this.error.set('Failed to delete quiz');
      }
    });
  }

  selectQuiz(quiz: QuizDto): void {
    this.selectedQuiz.set(quiz);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
