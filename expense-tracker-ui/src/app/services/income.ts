import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Income } from '../models/income';

@Injectable({ providedIn: 'root' })
export class IncomeService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/incomes';

  getAll(): Observable<Income[]> {
    return this.http.get<Income[]>(this.baseUrl);
  }

  create(income: Partial<Income>): Observable<Income> {
    return this.http.post<Income>(this.baseUrl, income);
  }

  update(id: string, income: Partial<Income>): Observable<Income> {
    return this.http.put<Income>(`${this.baseUrl}/${id}`, income);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}