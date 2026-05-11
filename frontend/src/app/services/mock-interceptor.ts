import { HttpInterceptorFn } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { delay } from 'rxjs/operators';
import { MOCK_PROJECTS, MOCK_DOCS } from './mock-data';

function randomDelay(): number {
  return 200 + Math.floor(Math.random() * 300);
}

function mockResponse<T>(data: T, status = 200) {
  return of(new HttpResponse<T>({ status, body: data })).pipe(delay(randomDelay()));
}

export const mockInterceptor: HttpInterceptorFn = (req, next) => {
  const url = req.url;

  // GET /api/projects
  if (req.method === 'GET' && url.match(/\/api\/projects$/)) {
    return mockResponse(MOCK_PROJECTS);
  }

  // GET /api/projects/:id
  const projectMatch = url.match(/\/api\/projects\/([^/]+)$/);
  if (req.method === 'GET' && projectMatch && !url.includes('/docs') && !url.includes('/upload')) {
    const project = MOCK_PROJECTS.find(p => p.id === projectMatch[1]);
    return mockResponse(project || MOCK_PROJECTS[0]);
  }

  // POST /api/projects
  if (req.method === 'POST' && url.match(/\/api\/projects$/)) {
    const body = req.body as Record<string, unknown>;
    const newProject = {
      id: 'proj-new-' + Date.now(),
      name: body['name'],
      description: body['description'],
      endpointCount: 0,
      status: 'idle',
      lastGenerated: null,
      aiProvider: body['aiProvider'],
      uploads: []
    };
    return mockResponse(newProject, 201);
  }

  // DELETE /api/projects/:id
  if (req.method === 'DELETE' && url.match(/\/api\/projects\/([^/]+)$/)) {
    return mockResponse(null, 204);
  }

  // GET /api/projects/:id/uploads
  if (req.method === 'GET' && url.match(/\/api\/projects\/([^/]+)\/uploads$/)) {
    return mockResponse([
      {
        id: 'upload-1',
        filename: 'AuthController.java',
        fileSize: 12400,
        uploadedAt: '2026-05-03T10:30:00Z',
        controllerCount: 1,
        endpointCount: 4
      }
    ]);
  }

  // POST /api/projects/:id/upload
  if (req.method === 'POST' && url.match(/\/api\/projects\/([^/]+)\/upload$/)) {
    return mockResponse({ id: 'upload-new', filename: 'uploaded.java', fileSize: 5000, uploadedAt: new Date().toISOString(), controllerCount: 1, endpointCount: 3 }, 201);
  }

  // GET /api/projects/:id/docs
  if (req.method === 'GET' && url.match(/\/api\/projects\/([^/]+)\/docs$/)) {
    const match = url.match(/\/api\/projects\/([^/]+)\/docs$/);
    const projectId = match?.[1] ?? 'proj-auth-001';
    const docs = MOCK_DOCS[projectId] || MOCK_DOCS['proj-auth-001'];
    return mockResponse(docs);
  }

  // GET /api/projects/:id/docs/latest
  if (req.method === 'GET' && url.match(/\/api\/projects\/([^/]+)\/docs\/latest$/)) {
    const match = url.match(/\/api\/projects\/([^/]+)\/docs\/latest$/);
    const projectId = match?.[1] ?? 'proj-auth-001';
    const docs = MOCK_DOCS[projectId] || MOCK_DOCS['proj-auth-001'];
    return mockResponse(docs[0] || MOCK_DOCS['proj-auth-001'][0]);
  }

  // Default: pass through
  return next(req);
};