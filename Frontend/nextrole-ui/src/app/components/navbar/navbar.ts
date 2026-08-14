import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [FormsModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  private router = inject(Router);

  searchTerm = '';

  search() {
    const q = this.searchTerm.trim();
    if (!q) return;

    this.router.navigate(['/job-postings'], { queryParams: { q } });
  }
}
