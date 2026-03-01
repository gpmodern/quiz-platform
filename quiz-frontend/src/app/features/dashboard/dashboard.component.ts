import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { QuizService, QuizDto } from '../../core/services/quiz.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  quizzes = signal<QuizDto[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  searchQuery = signal('');
  currentUser = signal<any>(null);

  constructor(
    private quizService: QuizService,
    private authService: AuthService
  ) {
    this.currentUser.set(this.authService.getCurrentUser());
  }

  ngOnInit(): void {
    this.loadQuizzes();
  }

  loadQuizzes(): void {
    this.loading.set(true);
    this.error.set(null);
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

  searchQuizzes(): void {
    if (!this.searchQuery().trim()) {
      this.loadQuizzes();
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.quizService.searchQuizzes(this.searchQuery()).subscribe({
      next: (quizzes: QuizDto[]) => {
        this.quizzes.set(quizzes);
        this.loading.set(false);
      },
      error: (err: any) => {
        this.error.set('Search failed');
        this.loading.set(false);
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
