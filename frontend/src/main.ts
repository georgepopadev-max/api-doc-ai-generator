import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { DashboardComponent } from './app/features/dashboard/dashboard.component';
import { UploadComponent } from './app/features/upload/upload.component';
import { GenerationComponent } from './app/features/generation/generation.component';
import { EditorComponent } from './app/features/editor/editor.component';
import { HistoryComponent } from './app/features/history/history.component';
import { environment } from './environments/environment';
import { mockInterceptor } from './app/services/mock-interceptor';

const providers: any[] = [
  provideAnimations(),
  provideRouter([
    { path: '', component: DashboardComponent },
    { path: 'upload/:projectId', component: UploadComponent },
    { path: 'generate/:projectId', component: GenerationComponent },
    { path: 'editor/:docId', component: EditorComponent },
    { path: 'history/:projectId', component: HistoryComponent },
  ])
];

if (environment.mock) {
  providers.push(provideHttpClient(withInterceptors([mockInterceptor])));
} else {
  providers.push(provideHttpClient());
}

bootstrapApplication(AppComponent, { providers }).catch(err => console.error(err));