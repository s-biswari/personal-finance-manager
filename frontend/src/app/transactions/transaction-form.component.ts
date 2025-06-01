import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { environment } from '../../environments/environment';

interface TransactionForm {
  id?: number;
  description: string;
  amount: number;
  date: string;
  categoryId: number | null;
}

interface Category {
  id: number;
  name: string;
}

@Component({
  selector: 'app-transaction-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="transaction-form-container">
      <div class="form-card">
        <h2>{{ isEdit ? 'Edit' : 'Add' }} Transaction</h2>
        <form (ngSubmit)="onSubmit()" *ngIf="form">
          <div class="form-group">
            <label class="floating-label">Description
              <input [(ngModel)]="form.description" name="description" required placeholder=" " />
              <span class="input-icon">📝</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Amount
              <input type="number" [(ngModel)]="form.amount" name="amount" required placeholder=" " />
              <span class="input-icon">💰</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Date
              <input type="date" [(ngModel)]="form.date" name="date" required placeholder=" " />
              <span class="input-icon">📅</span>
            </label>
          </div>
          <div class="form-group">
            <label class="floating-label">Category
              <select [(ngModel)]="form.categoryId" name="categoryId" required>
                <option value="" disabled selected>Select category</option>
                <option *ngFor="let cat of categories" [value]="cat.id">{{ cat.name }}</option>
              </select>
              <span class="input-icon">🏷️</span>
            </label>
          </div>
          <div class="form-actions">
            <button type="submit" class="submit-btn" [disabled]="loading">{{ isEdit ? 'Update' : 'Add' }}</button>
            <button type="button" class="cancel-btn" (click)="router.navigate(['/transactions'])">Cancel</button>
          </div>
          <div *ngIf="error" class="error">{{ error }}</div>
          <div *ngIf="success" class="success">{{ success }}</div>
          <div *ngIf="loading" class="loading">Saving...</div>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .transaction-form-container {
      max-width: 500px;
      margin: 2rem auto;
      padding: 2rem;
      background: #232526;
      border-radius: 18px;
      box-shadow: 0 4px 24px 0 rgba(0,0,0,0.07);
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
    .submit-btn {
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
    .submit-btn:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    .submit-btn:hover:not(:disabled) {
      background: linear-gradient(90deg, #38b2ac 0%, #4f8cff 100%);
    }
    .cancel-link {
      color: #bfc1c8;
      text-decoration: none;
      font-weight: 600;
      font-size: 1rem;
      transition: color 0.2s;
    }
    .cancel-link:hover {
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
export class TransactionFormComponent implements OnInit {
  form: TransactionForm = { description: '', amount: 0, date: '', categoryId: null };
  categories: Category[] = [];
  error = '';
  success = '';
  loading = false;
  isEdit = false;

  constructor(
    private readonly http: HttpClient,
    public readonly router: Router,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.loadCategories();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.http.get<any>(`${environment.apiBaseUrl}api/transactions/${id}`).subscribe({
        next: txn => {
          this.form = {
            id: txn.id,
            description: txn.description,
            amount: txn.amount,
            date: txn.date ? txn.date.substring(0, 10) : '',
            categoryId: txn.categoryId ?? null
          };
        },
        error: () => this.error = 'Failed to load transaction.'
      });
    }
  }

  loadCategories() {
    this.http.get<Category[]>(`${environment.apiBaseUrl}api/categories`).subscribe({
      next: data => this.categories = data,
      error: () => this.error = 'Failed to load categories.'
    });
  }

  onSubmit() {
    this.error = '';
    this.success = '';
    this.loading = true;
    if (!this.form.description || !this.form.amount || !this.form.date || !this.form.categoryId) {
      this.error = 'All fields are required.';
      this.loading = false;
      return;
    }
    if (this.isEdit && this.form.id) {
      this.http.put(`${environment.apiBaseUrl}api/transactions/${this.form.id}`, this.form).subscribe({
        next: () => {
          this.loading = false;
          this.success = 'Transaction updated successfully!';
          setTimeout(() => this.router.navigate(['/transactions']), 1500);
        },
        error: () => {
          this.loading = false;
          this.error = 'Failed to update transaction.';
        }
      });
    } else {
      this.http.post(`${environment.apiBaseUrl}api/transactions`, this.form).subscribe({
        next: () => {
          this.loading = false;
          this.success = 'Transaction added successfully!';
          setTimeout(() => this.router.navigate(['/transactions']), 1500);
        },
        error: () => {
          this.loading = false;
          this.error = 'Failed to add transaction.';
        }
      });
    }
  }
}
