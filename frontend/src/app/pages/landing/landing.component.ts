import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  standalone: true,
  template: `
    <div class="landing-container">
      <h2>Welcome!</h2>
      <div class="landing-actions">
        <button (click)="goTo('/report-generation')">Generate Report</button>
        <button (click)="goTo('/transactions')">Transactions</button>
        <button (click)="goTo('/budget-report')">Budget Report</button>
        <button (click)="goTo('/manage-budgets')">Manage Budgets</button>
        <button (click)="goTo('/category-management')">Category Management</button>
        <button (click)="goTo('/register')">Register</button>
        <button (click)="goTo('/login')">Logout</button>
      </div>
    </div>
  `,
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  constructor(private readonly router: Router) {}

  goTo(path: string) {
    this.router.navigate([path]);
  }
}
