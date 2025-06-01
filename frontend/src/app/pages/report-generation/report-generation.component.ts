import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-report-generation',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-generation.component.html',
  styleUrls: ['./report-generation.component.css']
})
export class ReportGenerationComponent {
  month: number = new Date().getMonth() + 1;
  year: number = new Date().getFullYear();
  format: string = 'pdf';
  errorMessage: string | null = null;
  loading = false;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  generateReport() {
    this.errorMessage = null;
    this.loading = true;
    const url = `${environment.apiBaseUrl}download-report?month=${this.month}&year=${this.year}&format=${this.format}`;
    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (response) => {
        const blob = new Blob([response], { type: this.format === 'pdf' ? 'application/pdf' : 'application/vnd.ms-excel' });
        const url2 = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url2;
        a.download = `report_${this.month}_${this.year}.${this.format}`;
        a.click();
        window.URL.revokeObjectURL(url2);
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to generate the report. Please try again later.';
        this.loading = false;
      }
    });
  }

  goBack() {
    this.router.navigate(['/landing']);
  }
}
