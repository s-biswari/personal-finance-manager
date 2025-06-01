import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { environment } from '../../environments/environment';
import { AuthService } from '../services/auth.service';

interface Transaction {
  id: number;
  description: string;
  amount: number;
  date: string;
  categoryName: string;
}

@Component({
  selector: 'app-transaction-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <div class="transaction-container">
      <div class="transaction-header">
        <h2>💸 Transactions</h2>
        <div>
          <button class="add-btn" routerLink="/transactions/add">+ Add Transaction</button>
          <button class="logout-btn" (click)="logout()">Logout</button>
        </div>
      </div>
      <div class="transaction-card">
        <table *ngIf="transactions.length > 0; else noTxns">
          <thead>
            <tr>
              <th>Date</th>
              <th>Description</th>
              <th>Category</th>
              <th>Amount</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr *ngFor="let txn of transactions">
              <td>{{ txn.date | date:'mediumDate' }}</td>
              <td>{{ txn.description }}</td>
              <td><span class="category-chip">{{ txn.categoryName }}</span></td>
              <td>
                <span [ngClass]="{'amount-pos': txn.amount > 0, 'amount-neg': txn.amount < 0}">
                  {{ txn.amount | currency:'USD':'symbol':'1.2-2' }}
                </span>
              </td>
              <td>
                <button class="edit-btn" (click)="editTransaction(txn.id)">Edit</button>
                <button class="delete-btn" (click)="deleteTransaction(txn.id)">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
        <ng-template #noTxns><p class="no-txns">No transactions found.</p></ng-template>
      </div>
    </div>
  `,
  styles: [`
    .transaction-container {
      max-width: 900px;
      margin: 2rem auto;
      padding: 1.5rem;
      background: #232526;
      border-radius: 18px;
      box-shadow: 0 4px 24px 0 rgba(0,0,0,0.07);
      color: #f5f6fa;
    }
    .transaction-header h2 {
      font-size: 2rem;
      font-weight: 700;
      color: #f5f6fa;
      margin: 0;
    }
    .transaction-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 1.5rem;
    }
    .add-btn {
      background: linear-gradient(90deg, #4f8cff 0%, #38b2ac 100%);
      color: #fff;
      border: none;
      border-radius: 6px;
      padding: 0.6rem 1.2rem;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: background 0.2s;
      box-shadow: 0 2px 8px 0 rgba(79,140,255,0.08);
    }
    .add-btn:hover {
      background: linear-gradient(90deg, #38b2ac 0%, #4f8cff 100%);
    }
    .logout-btn {
      background: #e53e3e;
      color: #fff;
      border: none;
      border-radius: 6px;
      padding: 0.6rem 1.2rem;
      font-size: 1rem;
      font-weight: 600;
      margin-left: 1rem;
      cursor: pointer;
      transition: background 0.2s;
      box-shadow: 0 2px 8px 0 rgba(255,0,0,0.2);
    }
    .logout-btn:hover {
      background: #c53030;
    }
    .transaction-card {
      background: #414345;
      border-radius: 12px;
      box-shadow: 0 2px 12px 0 rgba(0,0,0,0.04);
      padding: 1.5rem;
      color: #f5f6fa;
    }
    table {
      width: 100%;
      border-collapse: separate;
      border-spacing: 0 0.5rem;
      color: #f5f6fa;
    }
    th, td {
      padding: 0.75rem 1rem;
      text-align: left;
    }
    th {
      background: #232526;
      color: #bfc1c8;
      font-weight: 700;
      border-bottom: 2px solid #393e46;
    }
    tr {
      background: #232526;
      border-radius: 8px;
      transition: box-shadow 0.2s;
    }
    tr:hover {
      box-shadow: 0 2px 8px 0 rgba(56,178,172,0.08);
      background: #414345;
    }
    .category-chip {
      display: inline-block;
      background: #393e46;
      color: #bfc1c8;
      border-radius: 12px;
      padding: 0.2rem 0.8rem;
      font-size: 0.95rem;
      font-weight: 500;
    }
    .amount-pos {
      color: #38a169;
      font-weight: 600;
    }
    .amount-neg {
      color: #e53e3e;
      font-weight: 600;
    }
    .edit-btn, .delete-btn {
      border: none;
      border-radius: 5px;
      padding: 0.4rem 0.9rem;
      font-size: 0.98rem;
      margin-right: 0.5rem;
      cursor: pointer;
      font-weight: 500;
      transition: background 0.2s, color 0.2s;
    }
    .edit-btn {
      background: #393e46;
      color: #4f8cff;
    }
    .edit-btn:hover {
      background: #232526;
      color: #38b2ac;
    }
    .delete-btn {
      background: #e53e3e;
      color: #fff;
    }
    .delete-btn:hover {
      background: #c53030;
      color: #fff;
    }
    .no-txns {
      text-align: center;
      color: #bfc1c8;
      font-size: 1.1rem;
      margin: 2rem 0 0 0;
    }
  `]
})
export class TransactionListComponent implements OnInit {
  transactions: Transaction[] = [];

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router,
    private readonly auth: AuthService
  ) {}

  ngOnInit() {
    this.fetchTransactions();
  }

  fetchTransactions() {
    this.http.get<Transaction[]>(`${environment.apiBaseUrl}api/transactions`).subscribe({
      next: data => this.transactions = data,
      error: err => console.error('Failed to load transactions', err)
    });
  }

  editTransaction(id: number) {
    this.router.navigate(['/transactions/edit', id]);
  }

  deleteTransaction(id: number) {
    if (confirm('Are you sure you want to delete this transaction?')) {
      this.http.delete(`${environment.apiBaseUrl}api/transactions/${id}`).subscribe({
        next: () => this.fetchTransactions(),
        error: err => alert('Failed to delete transaction')
      });
    }
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
