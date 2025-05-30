import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { NgChartsModule } from 'ng2-charts';
import { ChartType, ChartConfiguration } from 'chart.js';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-budget-report',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './budget-report.component.html'
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

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchReport(5, 2025);
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
}
