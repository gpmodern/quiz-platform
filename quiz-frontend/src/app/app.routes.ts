import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { QuizDetailComponent } from './features/quiz/quiz-detail/quiz-detail.component';
import { AdminPanelComponent } from './features/admin/admin-panel/admin-panel.component';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
  },
  {
    path: 'quiz/:id',
    component: QuizDetailComponent,
  },
  {
    path: 'admin',
    component: AdminPanelComponent,
  },
  { path: '**', redirectTo: '/login' }
];
