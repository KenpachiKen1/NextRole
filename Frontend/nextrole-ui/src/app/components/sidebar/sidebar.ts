import { Component, inject} from '@angular/core';
import { UserService } from '../../services/userService';
import { UserResponse } from '../../models/user.model';
import { OnInit } from '@angular/core';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar implements OnInit{
  private userService = inject(UserService);

  currentUser = this.userService.currentUser;
  pageLoadError = ''
  ngOnInit() {
    this.userService.getCurrUserProfile().subscribe({
      next: (user) => {

        this.currentUser.set(user)
        console.log("called curr user")
        console.log(this.currentUser)
      },
      error: (err) => {
        this.pageLoadError = err
      },

    })
  }
}
