import { Routes } from '@angular/router';
import { BudgetReportComponent } from './budget-report/budget-report.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { TransactionListComponent } from './transactions/transaction-list.component';
import { TransactionFormComponent } from './transactions/transaction-form.component';
import { ReportGenerationComponent } from './pages/report-generation/report-generation.component';
import { BudgetManageComponent } from './budget-report/budget-manage.component';
import { CategoryManagementComponent } from './pages/category-management/category-management.component';

export const routes: Routes = [
    {
        path: 'budget-report',
        component: BudgetReportComponent
    },
    {
        path: 'transactions',
        component: TransactionListComponent
    },
    {
        path: 'transactions/add',
        component: TransactionFormComponent
    },
    {
        path: 'transactions/edit/:id',
        component: TransactionFormComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: '',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'report-generation',
        component: ReportGenerationComponent
    },
    {
        path: 'landing',
        loadComponent: () => import('./pages/landing/landing.component').then(m => m.LandingComponent)
    },
    {
        path: 'manage-budgets',
        component: BudgetManageComponent
    },
    {
        path: 'category-management',
        component: CategoryManagementComponent
    }
];
