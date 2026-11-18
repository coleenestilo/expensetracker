import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AccountService } from './services/account';
import { ExpenseService } from './services/expense';
import { IncomeService } from './services/income';
import { Account } from './models/account';
import { Expense } from './models/expense';
import { Income } from './models/income';

@Component({
  selector: 'app-root',
  imports: [FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  private accountService = inject(AccountService);
  private expenseService = inject(ExpenseService);
  private incomeService = inject(IncomeService);

  accounts = signal<Account[]>([]);
  expenses = signal<Expense[]>([]);
  incomes = signal<Income[]>([]);
  error = signal('');

  // account form
  accountName = '';
  accountBalance: number | null = null;
  editingAccount = signal<Account | null>(null);

  // expense form
  expenseDescription = '';
  expenseAmount: number | null = null;
  expenseCategory = '';
  expenseAccountId = '';
  editingExpense = signal<Expense | null>(null);

  // income form
  incomeDescription = '';
  incomeAmount: number | null = null;
  incomeCategory = '';
  incomeAccountId = '';
  editingIncome = signal<Income | null>(null);

  ngOnInit() {
    this.loadAll();
  }

  loadAll() {
    this.accountService.getAll().subscribe(data => this.accounts.set(data));
    this.expenseService.getAll().subscribe(data => this.expenses.set(data));
    this.incomeService.getAll().subscribe(data => this.incomes.set(data));
  }

  showError(err: any) {
    const body = err.error;
    this.error.set(body ? Object.values(body).join(', ') : 'Something went wrong');
  }

  // ACCOUNTS SERVICES
  saveAccount() {
  this.error.set('');
  const editing = this.editingAccount();
  const body = {
    name: this.accountName,
    balance: this.accountBalance ?? undefined
  };

  const request = editing
    ? this.accountService.update(editing.id, body)
    : this.accountService.create(body);

  request.subscribe({
    next: () => {
      this.resetAccountForm();
      this.loadAll();
    },
    error: err => this.showError(err)
  });
}

  startEditAccount(account: Account) {
    this.error.set('');
    this.editingAccount.set(account);
    this.accountName = account.name;
    this.accountBalance = account.balance;
  }

  resetAccountForm() {
    this.editingAccount.set(null);
    this.accountName = '';
    this.accountBalance = null;
  }

  deleteAccount(id: string) {
    this.error.set('');
    this.accountService.delete(id).subscribe({
      next: () => {
        if (this.editingAccount()?.id === id) {
          this.resetAccountForm();
        }
        this.loadAll();
      },
      error: err => this.showError(err)
    });
  }

  // EXPENSES SERVICES

  saveExpense() {
    this.error.set('');
    const editing = this.editingExpense();
    const body = {
      description: this.expenseDescription,
      amount: this.expenseAmount ?? undefined,
      category: this.expenseCategory,
      accountId: this.expenseAccountId,
      date: editing?.date
    };

    const request = editing
      ? this.expenseService.update(editing.id, body)
      : this.expenseService.create(body);

    request.subscribe({
      next: () => {
        this.resetExpenseForm();
        this.loadAll();
      },
      error: err => this.showError(err)
    });
  }

  startEditExpense(expense: Expense) {
    this.error.set('');
    this.editingExpense.set(expense);
    this.expenseDescription = expense.description;
    this.expenseAmount = expense.amount;
    this.expenseCategory = expense.category;
    this.expenseAccountId = expense.accountId;
  }

  resetExpenseForm() {
    this.editingExpense.set(null);
    this.expenseDescription = '';
    this.expenseAmount = null;
    this.expenseCategory = '';
    this.expenseAccountId = '';
  }

  deleteExpense(id: string) {
    this.error.set('');
    this.expenseService.delete(id).subscribe({
      next: () => {
        if (this.editingExpense()?.id === id) {
          this.resetExpenseForm();
        }
        this.loadAll();
      },
      error: err => this.showError(err)
    });
  }

  // INCOMES SERVICES

  saveIncome() {
    this.error.set('');
    const editing = this.editingIncome();
    const body = {
      description: this.incomeDescription,
      amount: this.incomeAmount ?? undefined,
      category: this.incomeCategory,
      accountId: this.incomeAccountId,
      date: editing?.date
    };

    const request = editing
      ? this.incomeService.update(editing.id, body)
      : this.incomeService.create(body);

    request.subscribe({
      next: () => {
        this.resetIncomeForm();
        this.loadAll();
      },
      error: err => this.showError(err)
    });
  }

  startEditIncome(income: Income) {
    this.error.set('');
    this.editingIncome.set(income);
    this.incomeDescription = income.description;
    this.incomeAmount = income.amount;
    this.incomeCategory = income.category;
    this.incomeAccountId = income.accountId;
  }

  resetIncomeForm() {
    this.editingIncome.set(null);
    this.incomeDescription = '';
    this.incomeAmount = null;
    this.incomeCategory = '';
    this.incomeAccountId = '';
  }

  deleteIncome(id: string) {
    this.error.set('');
    this.incomeService.delete(id).subscribe({
      next: () => {
        if (this.editingIncome()?.id === id) {
          this.resetIncomeForm();
        }
        this.loadAll();
      },
      error: err => this.showError(err)
    });
  }
}