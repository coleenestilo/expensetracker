import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Account } from '../models/account';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/accounts';

  getAll(): Observable<Account[]> {
    return this.http.get<Account[]>(this.baseUrl);
  }

  create(account: Partial<Account>): Observable<Account> {
    return this.http.post<Account>(this.baseUrl, account);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  update(id: string, account: Partial<Account>): Observable<Account> {
    return this.http.put<Account>(`${this.baseUrl}/${id}`, account);
  }
}