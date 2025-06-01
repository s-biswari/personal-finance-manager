import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { NgChartsModule } from 'ng2-charts';
import { ChartType, ChartConfiguration } from 'chart.js';
import { environment } from '../../environments/environment';

interface Budget {
  id: number;
  category: { id: number; name: string };
  month: number;
  year: number;
  amount: number;
}

interface Category {
  id: number;
  name: string;
}

@Component({
  selector: 'app-budget-report',
  standalone: true,
  imports: [CommonModule, FormsModule, NgChartsModule],
  templateUrl: './budget-report.component.html',
  styleUrls: ['./budget-report.component.css']
})
export class BudgetReportComponent implements OnInit {
  public chartData: ChartConfiguration<'bar'>['data'] = {
    labels: [],
    datasets: [
      { data: [], label: 'Budget Amount', backgroundColor: 'rgba(54,162,235,0.6)' },
      { data: [], label: 'Actual Spending', backgroundColor: 'rgba(255,99,132,0.6)' }
    ]
  };
  public chartType: ChartType = 'bar';
  month: number = new Date().getMonth() + 1;
  year: number = new Date().getFullYear();
  budgets: Budget[] = [];
  categories: Category[] = [];
  budgetForm = { id: null as number | null, categoryId: null as number | null, month: this.month, year: this.year, amount: null as number | null };
  isEdit = false;
  error = '';
  success = '';
  loading = false;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  ngOnInit(): void {
    this.fetchReport(this.month, this.year);
    this.fetchBudgets();
    this.fetchCategories();
  }

  fetchReport(month: number, year: number): void {
    this.http.get<any[]>(`${environment.apiBaseUrl}api/budgets/report?month=${month}&year=${year}`)
      .subscribe(data => {
        const labels = data.map(d => d.categoryName);
        const budget = data.map(d => d.budgetAmount);
        const spent = data.map(d => d.actualSpent);
        this.chartData = {
          labels,
          datasets: [
            { data: budget, label: 'Budget Amount', backgroundColor: 'rgba(54,162,235,0.6)' },
            { data: spent, label: 'Actual Spending', backgroundColor: 'rgba(255,99,132,0.6)' }
          ]
        };
      });
  }

  fetchBudgets() {
    this.http.get<Budget[]>(`${environment.apiBaseUrl}api/budgets`).subscribe({
      next: data => this.budgets = data,
      error: () => this.error = 'Failed to load budgets.'
    });
  }

  fetchCategories() {
    this.http.get<Category[]>(`${environment.apiBaseUrl}api/categories`).subscribe({
      next: data => this.categories = data,
      error: () => this.error = 'Failed to load categories.'
    });
  }

  onFilter() {
    this.fetchReport(this.month, this.year);
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
    this.http.post<Budget>(`${environment.apiBaseUrl}api/budgets?${params}`, {}, {}).subscribe({
      next: budget => {
        this.success = id ? 'Budget updated!' : 'Budget added!';
        this.loading = false;
        this.isEdit = false;
        this.budgetForm = { id: null, categoryId: null, month: this.month, year: this.year, amount: null };
        this.fetchBudgets();
        setTimeout(() => this.success = '', 1500);
      },
      error: () => {
        this.error = 'Failed to save budget.';
        this.loading = false;
      }
    });
  }

  editBudget(budget: Budget) {
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
    this.budgetForm = { id: null, categoryId: null, month: this.month, year: this.year, amount: null };
    this.error = '';
    this.success = '';
  }

  goBack() {
    this.router.navigate(['/landing']);
  }

  // --- Professional summary and progress methods ---
  getTotalBudgeted(): number {
    return this.budgets.reduce((sum, b) => sum + (b.amount || 0), 0);
  }
  getTotalSpent(): number {
    // For the selected month/year, sum actual spent for each budgeted category
    return (this.chartData.datasets[1]?.data as number[] ?? []).reduce((sum, v) => sum + (v || 0), 0);
  }
  getTotalRemaining(): number {
    return this.getTotalBudgeted() - this.getTotalSpent();
  }
  getPercentUsed(): number {
    const budgeted = this.getTotalBudgeted();
    if (!budgeted) return 0;
    return Math.min(100, (this.getTotalSpent() / budgeted) * 100);
  }
  getSpentForBudget(budget: any): number {
    // Find the spent for this budget's category in the current chartData
    const idx = (this.chartData.labels as string[] | undefined)?.findIndex(
      l => l === budget.category.name
    );
    if (idx === undefined || idx === -1) return 0;
    return (this.chartData.datasets[1]?.data as number[])[idx] || 0;
  }
  getProgressPercent(budget: any): number {
    const spent = this.getSpentForBudget(budget);
    if (!budget.amount) return 0;
    return Math.min(100, (spent / budget.amount) * 100);
  }
  isOverspent(budget: any): boolean {
    return this.getSpentForBudget(budget) > budget.amount;
  }
  getProgressTooltip(budget: any): string {
    const spent = this.getSpentForBudget(budget);
    return `Spent: ${spent} / Budget: ${budget.amount}`;
  }
}
