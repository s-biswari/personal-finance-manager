import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-budget-manage',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './budget-manage.component.html',
  styleUrls: ['./budget-report.component.css'] // reuse styles
})
export class BudgetManageComponent implements OnInit {
  budgets: any[] = [];
  categories: any[] = [];
  budgetForm = { id: null as number | null, categoryId: null as number | null, month: new Date().getMonth() + 1, year: new Date().getFullYear(), amount: null as number | null };
  isEdit = false;
  error = '';
  success = '';
  loading = false;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  ngOnInit(): void {
    this.fetchBudgets();
    this.fetchCategories();
  }

  fetchBudgets() {
    this.http.get<any[]>(`${environment.apiBaseUrl}api/budgets`).subscribe({
      next: data => this.budgets = data,
      error: () => this.error = 'Failed to load budgets.'
    });
  }

  fetchCategories() {
    this.http.get<any[]>(`${environment.apiBaseUrl}api/categories`).subscribe({
      next: data => this.categories = data,
      error: () => this.error = 'Failed to load categories.'
    });
  }

  submitBudget() {
    this.error = '';
    this.success = '';
    this.loading = true;
    const { categoryId, month, year, amount, id } = this.budgetForm;
    if (!categoryId || !month || !year || !amount) {
      this.error = 'All fields are required.';
      this.loading = false;
      return;
    }
    const params = `categoryId=${categoryId}&month=${month}&year=${year}&amount=${amount}`;
    this.http.post<any>(`${environment.apiBaseUrl}api/budgets?${params}`, {}, {}).subscribe({
      next: budget => {
        this.success = id ? 'Budget updated!' : 'Budget added!';
        this.loading = false;
        this.isEdit = false;
        this.budgetForm = { id: null, categoryId: null, month: new Date().getMonth() + 1, year: new Date().getFullYear(), amount: null };
        this.fetchBudgets();
        setTimeout(() => this.success = '', 1500);
      },
      error: () => {
        this.error = 'Failed to save budget.';
        this.loading = false;
      }
    });
  }

  editBudget(budget: any) {
    this.isEdit = true;
    this.budgetForm = {
      id: budget.id,
      categoryId: budget.category.id,
      month: budget.month,
      year: budget.year,
      amount: budget.amount
    };
  }

  deleteBudget(id: number) {
    if (!confirm('Delete this budget?')) return;
    this.http.delete(`${environment.apiBaseUrl}api/budgets/${id}`).subscribe({
      next: () => {
        this.success = 'Budget deleted!';
        this.fetchBudgets();
        setTimeout(() => this.success = '', 1500);
      },
      error: () => this.error = 'Failed to delete budget.'
    });
  }

  cancelEdit() {
    this.isEdit = false;
    this.budgetForm = { id: null, categoryId: null, month: new Date().getMonth() + 1, year: new Date().getFullYear(), amount: null };
    this.error = '';
    this.success = '';
  }

  goBack() {
    this.router.navigate(['/landing']);
  }
}
