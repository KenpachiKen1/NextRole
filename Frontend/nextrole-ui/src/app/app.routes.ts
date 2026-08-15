// app.routes.ts
import { Routes } from '@angular/router';
import { Register } from './pages/register/register';
import { Resume } from './pages/resume/resume';
import { DashboardLayout } from './layout/dashboard-layout/dashboard-layout';
import { Calendar } from './pages/calendar/calendar';
import { Profile } from './pages/profile/profile';
import { Login } from './pages/login/login';
import { Enrichment } from './pages/enrichment/enrichment';
import { JobPostings } from './pages/job-postings/job-postings';
import { Subscription } from './pages/subscription/subscription';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },
  { path: 'register', component: Register },

  {
    path: '',
    component: DashboardLayout,
    children: [
      { path: 'calendar', component: Calendar },
      { path: 'resume', component: Resume },
      { path: 'job-postings', component: JobPostings },
      { path: 'ai-hub', component: Enrichment },
      { path: 'settings', component: Profile },
      { path: 'subscription', component: Subscription },
    ],
  },
];
