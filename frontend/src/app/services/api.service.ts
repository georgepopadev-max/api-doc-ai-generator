import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { timeout, catchError } from 'rxjs/operators';
import { Project, GeneratedDoc, CreateProjectRequest, SourceUpload } from '../models/project.model';
import { MOCK_PROJECTS, MOCK_DOCS } from './mock-data';

@Injectable({ providedIn: 'root' })
export class ApiService {

  constructor(private http: HttpClient) {}

  private get baseUrl(): string {
    return this.getApiUrl();
  }

  getApiUrl(): string {
    const env = (window as any).__VERCEL_ENV__;
    if (env?.API_URL_BACK) {
      return env.API_URL_BACK;
    }
    return '/api';
  }

  private withTimeout<T>(obs: Observable<T>): Observable<T> {
    return obs.pipe(
      timeout(5000),
      catchError((err: HttpErrorResponse) => {
        console.warn('[ApiService] Request failed, using mock fallback:', err.message);
        return of();
      })
    );
  }

  getProjects(): Observable<Project[]> {
    return this.withTimeout(
      this.http.get<Project[]>(`${this.baseUrl}/projects`)
    );
  }

  getProject(id: string): Observable<Project> {
    return this.http.get<Project>(`${this.baseUrl}/projects/${id}`).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  createProject(request: CreateProjectRequest): Observable<Project> {
    return this.http.post<Project>(`${this.baseUrl}/projects`, request).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  deleteProject(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/projects/${id}`).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  uploadSource(projectId: string, filename: string, fileSize: number, sourceCode: string): Observable<SourceUpload> {
    return this.http.post<SourceUpload>(`${this.baseUrl}/projects/${projectId}/upload`, {
      filename, fileSize, sourceCode
    }).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  getProjectUploads(projectId: string): Observable<SourceUpload[]> {
    return this.http.get<SourceUpload[]>(`${this.baseUrl}/projects/${projectId}/uploads`).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  getProjectDocs(projectId: string): Observable<GeneratedDoc[]> {
    return this.http.get<GeneratedDoc[]>(`${this.baseUrl}/projects/${projectId}/docs`).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }

  getLatestDoc(projectId: string): Observable<GeneratedDoc> {
    return this.http.get<GeneratedDoc>(`${this.baseUrl}/projects/${projectId}/docs/latest`).pipe(
      timeout(5000),
      catchError(() => of())
    );
  }
}
