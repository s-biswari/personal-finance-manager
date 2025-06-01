import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

interface Category {
  id: number;
  name: string;
}

@Component({
  selector: 'app-category-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-management.component.html',
  styleUrls: ['./category-management.component.css']
})
export class CategoryManagementComponent implements OnInit {
  categories: Category[] = [];
  categoryForm: Partial<Category> = { id: undefined, name: '' };
  isEdit = false;
  error = '';
  success = '';
  loading = false;

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  ngOnInit(): void {
    this.fetchCategories();
  }

  fetchCategories() {
    this.http.get<Category[]>(`${environment.apiBaseUrl}api/categories`).subscribe({
      next: data => this.categories = data,
      error: () => this.error = 'Failed to load categories.'
    });
  }

  submitCategory() {
    this.error = '';
    this.success = '';
    this.loading = true;
    if (!this.categoryForm.name || this.categoryForm.name.trim().length === 0) {
      this.error = 'Category name is required.';
      this.loading = false;
      return;
    }
    if (this.isEdit && this.categoryForm.id) {
      this.http.put(`${environment.apiBaseUrl}api/categories/${this.categoryForm.id}`, { name: this.categoryForm.name }).subscribe({
        next: () => {
          this.success = 'Category updated!';
          this.loading = false;
          this.isEdit = false;
          this.categoryForm = { id: undefined, name: '' };
          this.fetchCategories();
          setTimeout(() => this.success = '', 1500);
        },
        error: () => {
          this.error = 'Failed to update category.';
          this.loading = false;
        }
      });
    } else {
      this.http.post(`${environment.apiBaseUrl}api/categories`, { name: this.categoryForm.name }).subscribe({
        next: () => {
          this.success = 'Category added!';
          this.loading = false;
          this.categoryForm = { id: undefined, name: '' };
          this.fetchCategories();
          setTimeout(() => this.success = '', 1500);
        },
        error: () => {
          this.error = 'Failed to add category.';
          this.loading = false;
        }
      });
    }
  }

  editCategory(category: Category) {
    this.isEdit = true;
    this.categoryForm = { id: category.id, name: category.name };
  }

  cancelEdit() {
    this.isEdit = false;
    this.categoryForm = { id: undefined, name: '' };
    this.error = '';
    this.success = '';
  }

  goBack() {
    this.router.navigate(['/landing']);
  }
}
