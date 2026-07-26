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
      'w-60 h-12 rounded-2xl text-2xl shadow-2xl transition-all duration-150 ease-in-out cursor-pointer hover:-translate-y-1 hover:shadow-xl';

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
