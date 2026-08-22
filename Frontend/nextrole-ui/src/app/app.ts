import { Component, HostListener, computed, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';

const MIN_SUPPORTED_WIDTH = 768;

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected readonly title = signal('nextrole-ui');

  protected screenWidth = signal(window.innerWidth);
  protected isScreenTooSmall = computed(() => this.screenWidth() < MIN_SUPPORTED_WIDTH);

  @HostListener('window:resize')
  onResize() {
    this.screenWidth.set(window.innerWidth);
  }
}
