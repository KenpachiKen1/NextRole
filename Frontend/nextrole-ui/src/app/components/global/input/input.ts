import { Component, Input } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-input',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './input.html',
  styleUrl: './input.css',
})
export class Inputs {
  @Input() label = '';
  @Input() placeholder = '';
  @Input() type = 'text';
  @Input() control!: FormControl;
}
