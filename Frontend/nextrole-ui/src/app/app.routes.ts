// app.routes.ts
import { Routes } from '@angular/router';
import { Register } from './pages/register/register';
import { Resume } from './pages/resume/resume';
import { DashboardLayout } from './layout/dashboard-layout/dashboard-layout';
import { Calendar } from './pages/calendar/calendar';
import { Profile } from './pages/profile/profile';
import { Login } from './pages/login/login';
import { Enrichment } from './pages/enrichment/enrichment';

export const routes: Routes = [
  {
    path: '',
    component: DashboardLayout,
    children: [
      { path: 'calendar', component: Calendar },
      { path: 'resume', component: Resume },
      { path: 'ai-hub', component: Enrichment },
      { path: 'settings', component: Profile },
    ],
  },

  { path: 'login', component: Login },
  { path: 'register', component: Register },

  { path: '', redirectTo: 'login', pathMatch: 'full' },
];
