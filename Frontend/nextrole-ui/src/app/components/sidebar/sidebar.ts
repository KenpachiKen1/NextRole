import { Component, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { UserService } from '../../services/userService';
import { UserResponse } from '../../models/user.model';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar implements OnInit {
  private userService = inject(UserService);

  currentUser = this.userService.currentUser;
  pageLoadError = '';
  ngOnInit() {
    this.userService.getCurrUserProfile().subscribe({
      next: (user) => {
        this.currentUser.set(user);
      },
      error: (err) => {
        this.pageLoadError = err;
      },
    });
  }
}
