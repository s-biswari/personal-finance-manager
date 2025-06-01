import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, NgIf, RouterModule],
  template: `
    <div class="register-container">
      <div class="register-card">
        <h2>Create Account</h2>
        <form (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label class="floating-label">Username
              <input [(ngModel)]="username" name="username" required placeholder=" " autocomplete="username" />
              <span class="input-icon">👤</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Email
              <input type="email" [(ngModel)]="email" name="email" required placeholder=" " autocomplete="email" />
              <span class="input-icon">📧</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Password
              <input type="password" [(ngModel)]="password" name="password" required placeholder=" " autocomplete="new-password" />
              <span class="input-icon">🔒</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Role
              <select [(ngModel)]="role" name="role" required disabled>
                <option value="ROLE_USER">User</option>
              </select>
              <span class="input-icon">🛡️</span>
            </label>
          </div>
          <div class="form-actions">
            <button type="submit" class="register-btn" [disabled]="loading">Register</button>
            <a routerLink="/login" class="login-link">Already have an account?</a>
          </div>
          <div *ngIf="error" class="error">{{ error }}</div>
          <div *ngIf="success" class="success">{{ success }}</div>
          <div *ngIf="loading" class="loading">Registering...</div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .register-container {
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(120deg, #232526 60%, #414345 100%);
    }
    .register-card {
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
    .floating-label input,
    .floating-label select {
      margin-top: 0.5rem;
      padding: 0.7rem 2.2rem 0.7rem 1rem;
      border: 1px solid #393e46;
      border-radius: 8px;
      font-size: 1.05rem;
      background: #414345;
      color: #f5f6fa;
      transition: border 0.2s;
    }
    .floating-label input:focus,
    .floating-label select:focus {
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
    .register-btn {
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
    .register-btn:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    .register-btn:hover:not(:disabled) {
      background: linear-gradient(90deg, #38b2ac 0%, #4f8cff 100%);
    }
    .login-link {
      color: #bfc1c8;
      text-decoration: none;
      font-weight: 600;
      font-size: 1rem;
      transition: color 0.2s;
    }
    .login-link:hover {
      color: #4f8cff;
      text-decoration: underline;
    }
    .error {
      color: #e53e3e;
      margin-top: 1rem;
      text-align: center;
      font-weight: 600;
    }
    .success {
      color: #38a169;
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
  `]
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  role = 'ROLE_USER'; // Default to User
  error = '';
  success = '';
  loading = false;

  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  onSubmit() {
    this.error = '';
    this.success = '';
    this.loading = true;
    this.auth.register(this.username, this.email, this.password, this.role).subscribe({
      next: () => {
        this.success = 'Registration successful! Please login.';
        this.router.navigate(['/login']);
      },
      error: err => this.error = err.error?.message ?? 'Registration failed',
      complete: () => this.loading = false
    });
  }
}
