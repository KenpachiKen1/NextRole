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
      'px-5 py-2.5 rounded-lg text-sm font-semibold whitespace-nowrap shadow-md transition-all duration-150 ease-in-out cursor-pointer hover:-translate-y-0.5 hover:shadow-lg';

    const variants = {
      primary: 'text-white bg-linear-to-r from-blue-600 to-indigo-600',

      secondary: 'text-white bg-linear-to-r from-slate-500 to-slate-600',

      danger:
        'border-2 border-red-500 text-red-500 hover:text-white bg-linear-to-r from-red-500 to-rose-600 bg-size-[0%_100%] hover:bg-size-[100%_100%] bg-no-repeat bg-left',

      create:
        'border-2 border-emerald-500 text-emerald-500 hover:text-white bg-linear-to-r from-emerald-500 to-green-600 bg-size-[0%_100%] hover:bg-size-[100%_100%] bg-no-repeat bg-left',

      edit: 'border-2 border-amber-500 text-amber-500 hover:text-white bg-linear-to-r from-amber-500 to-yellow-600 bg-size-[0%_100%] hover:bg-size-[100%_100%] bg-no-repeat bg-left',
    };

    return `${baseClasses} ${variants[this.variant]}`;
  }
}
