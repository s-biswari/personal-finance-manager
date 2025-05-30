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
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  role = '';
  error = '';
  success = '';

  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  onSubmit() {
    this.error = '';
    this.success = '';
    this.auth.register(this.username, this.email, this.password, this.role).subscribe({
      next: () => {
        this.success = 'Registration successful! Please login.';
        this.router.navigate(['/login']);
      },
      error: err => this.error = err.error?.message ?? 'Registration failed'
    });
  }
}
