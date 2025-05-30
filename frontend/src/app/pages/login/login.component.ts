import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, NgIf, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private readonly auth: AuthService, private readonly router: Router) {}

  onSubmit() {
    this.error = '';
    this.auth.login(this.username, this.password).subscribe({
      next: () => { this.router.navigate(['/']); },
      error: err => this.error = err.error?.message ?? 'Login failed'
    });
  }
}
