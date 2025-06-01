import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf, RouterModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <h2>Sign In</h2>
        <form (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label class="floating-label">Username
              <input [(ngModel)]="username" name="username" required placeholder=" " autocomplete="username" />
              <span class="input-icon">👤</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Password
              <input [type]="showPassword ? 'text' : 'password'" [(ngModel)]="password" name="password" required placeholder=" " autocomplete="current-password" />
              <span class="input-icon">🔒</span>
            </label>
            <label class="show-password-label">
              <input type="checkbox" [(ngModel)]="showPassword" name="showPassword" /> Show Password
            </label>
          </div>
          <div class="form-actions">
            <button type="submit" class="login-btn" [disabled]="loading">Login</button>
            <a routerLink="/register" class="register-link">Create account</a>
          </div>
          <div *ngIf="error" class="error">{{ error }}</div>
          <div *ngIf="loading" class="loading">Signing in...</div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(120deg, #232526 60%, #414345 100%);
    }
    .login-card {
      background: #232526;
      border-radius: 18px;
      box-shadow: 0 4px 24px 0 rgba(56,178,172,0.10);
      padding: 2.5rem 2.5rem 2rem 2.5rem;
      min-width: 350px;
      max-width: 420px;
      width: 100%;
      color: #f5f6fa;
    }
    h2 {
      text-align: center;
      color: #f5f6fa;
      margin-bottom: 2rem;
      font-size: 2rem;
      font-weight: 700;
    }
    .form-group {
      margin-bottom: 1.5rem;
      position: relative;
    }
    .floating-label {
      display: flex;
      flex-direction: column;
      position: relative;
      font-weight: 600;
      color: #bfc1c8;
      font-size: 1rem;
    }
    .floating-label input {
      margin-top: 0.5rem;
      padding: 0.7rem 2.2rem 0.7rem 1rem;
      border: 1px solid #393e46;
      border-radius: 8px;
      font-size: 1.05rem;
      background: #414345;
      color: #f5f6fa;
      transition: border 0.2s;
    }
    .floating-label input:focus {
      border: 1.5px solid #4f8cff;
      outline: none;
      background: #232526;
    }
    .input-icon {
      position: absolute;
      right: 1rem;
      top: 2.2rem;
      font-size: 1.2rem;
      color: #bfc1c8;
      pointer-events: none;
    }
    .form-actions {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 1.2rem;
    }
    .login-btn {
      background: linear-gradient(90deg, #4f8cff 0%, #38b2ac 100%);
      color: #fff;
      border: none;
      border-radius: 6px;
      padding: 0.5rem 1.2rem;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
    }
    .login-btn:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    .login-btn:hover:not(:disabled) {
      background: linear-gradient(90deg, #38b2ac 0%, #4f8cff 100%);
    }
    .register-link {
      color: #bfc1c8;
      text-decoration: none;
      font-weight: 600;
      font-size: 1rem;
      transition: color 0.2s;
    }
    .register-link:hover {
      color: #4f8cff;
      text-decoration: underline;
    }
    .error {
      color: #e53e3e;
      margin-top: 1rem;
      text-align: center;
      font-weight: 600;
    }
    .loading {
      color: #4f8cff;
      margin-top: 1rem;
      text-align: center;
      font-weight: 600;
    }
    .show-password-label {
      display: flex;
      align-items: center;
      font-size: 0.98rem;
      color: #bfc1c8;
      margin-top: 0.3rem;
      margin-left: 0.1rem;
      user-select: none;
      cursor: pointer;
    }
    .show-password-label input[type="checkbox"] {
      margin-right: 0.5em;
      accent-color: #4f8cff;
    }
  `]
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';
  loading = false;
  showPassword = false;

  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  onSubmit() {
    this.error = '';
    this.loading = true;
    this.auth.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/landing']);
      },
      error: err => {
        this.error = err.error?.message ?? 'Login failed';
        this.loading = false;
      }
    });
  }
}
