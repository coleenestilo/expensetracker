import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Expense } from '../models/expense';

@Injectable({ providedIn: 'root' })
export class ExpenseService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/expenses';

  getAll(): Observable<Expense[]> {
    return this.http.get<Expense[]>(this.baseUrl);
  }

  create(expense: Partial<Expense>): Observable<Expense> {
    return this.http.post<Expense>(this.baseUrl, expense);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  update(id: string, expense: Partial<Expense>): Observable<Expense> {
    return this.http.put<Expense>(`${this.baseUrl}/${id}`, expense);
  }
}