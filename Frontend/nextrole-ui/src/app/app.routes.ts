// app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
    title: 'NextroleUi - Login',
  },
  {
    path: 'register',
    loadComponent: () => import('./pages/register/register').then((m) => m.Register),
    title: 'NextroleUi - Register',
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./pages/forgot-pw/forgot-pw').then((m) => m.ForgotPw),
    title: 'NextroleUi - Forgot Password',
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./pages/reset-pw/reset-pw').then((m) => m.ResetPw),
    title: 'NextroleUi - Reset Password',
  },

  {
    path: '',
    loadComponent: () =>
      import('./layout/dashboard-layout/dashboard-layout').then((m) => m.DashboardLayout),
    children: [
      {
        path: 'calendar',
        loadComponent: () => import('./pages/calendar/calendar').then((m) => m.Calendar),
        title: 'NextroleUi - Calendar',
      },
      {
        path: 'resume',
        loadComponent: () => import('./pages/resume/resume').then((m) => m.Resume),
        title: 'NextroleUi - Resume',
      },
      {
        path: 'job-postings',
        loadComponent: () =>
          import('./pages/job-postings/job-postings').then((m) => m.JobPostings),
        title: 'NextroleUi - Job Postings',
      },
      {
        path: 'ai-hub',
        loadComponent: () => import('./pages/enrichment/enrichment').then((m) => m.Enrichment),
        title: 'NextroleUi - AI Hub',
      },
      {
        path: 'settings',
        loadComponent: () => import('./pages/profile/profile').then((m) => m.Profile),
        title: 'NextroleUi - Settings',
      },
      {
        path: 'subscription',
        loadComponent: () =>
          import('./pages/subscription/subscription').then((m) => m.Subscription),
        title: 'NextroleUi - Subscription',
      },
    ],
  },

  {
    path: '**',
    loadComponent: () => import('./pages/not-found/not-found').then((m) => m.NotFound),
    title: 'NextroleUi - Page Not Found',
  },
];
