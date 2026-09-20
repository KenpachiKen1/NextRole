import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-button',
  imports: [],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {
  @Input() variant: 'primary' | 'secondary' | 'danger' | 'create' | 'edit' = 'primary';

  get buttonClasses() {
    const baseClasses =
      'px-5 py-2.5 rounded-lg text-sm font-medium whitespace-nowrap transition-colors duration-150 ease-in-out cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed';

    const variants = {
      primary: 'text-[#F3E9CD] bg-[#1B2430] hover:bg-[#2A3543]',

      secondary: 'border border-[#E4DDCC] bg-white text-[#5B6270] hover:bg-[#FAF8F2]',

      danger: 'border-2 border-[#B7412C] text-[#B7412C] hover:text-white hover:bg-[#B7412C]',

      create: 'text-white bg-[#8A6A1B] hover:bg-[#6E5415]',

      edit: 'border border-[#B9932A] text-[#8A6A1B] bg-white hover:bg-[#B9932A]/10',
    };

    return `${baseClasses} ${variants[this.variant]}`;
  }
}
