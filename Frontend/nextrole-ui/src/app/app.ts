import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Button } from './components/global/button/button';
import { Card } from './components/global/card/card';
import { Modal } from './components/global/modal/modal';
import { Inputs } from './components/global/input/input';
import { CreateResumeModal } from './components/resume/create-resume-modal/create-resume-modal';
import { Resume } from './pages/resume/resume';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Button, Card, Modal, Inputs, CreateResumeModal, Resume],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('nextrole-ui');
}
