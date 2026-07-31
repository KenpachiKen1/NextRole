import { Component, inject, signal, OnInit,computed } from '@angular/core';
import { CalendarService } from '../../services/calendarService';
import { CalendarDay } from '../../models/calendar.models';
import { Button } from '../../components/global/button/button';
import { DatePipe } from '@angular/common';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-calendar',
  imports: [Button, DatePipe, NgClass],
  templateUrl: './calendar.html',
  styleUrl: './calendar.css',
})
export class Calendar implements OnInit {
  private calendarService = inject(CalendarService);

  private today = new Date();

  year = signal(this.today.getFullYear());
  month = signal(this.today.getMonth());

  displayDate = computed(() => new Date(this.year(), this.month(), 1));

  grid = signal<CalendarDay[]>([]);

  ngOnInit() {
    this.loadMonth();
  }

  daysOfTheWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

  nextMonth() {
    if (this.month() === 11) {
      this.month.set(0);
      this.year.set(this.year() + 1);
    } else {
      this.month.set(this.month() + 1);
    }

    this.loadMonth();
  }

  prevMonth() {
    if (this.month() === 0) {
      this.month.set(11);
      this.year.set(this.year() - 1);
    } else {
      this.month.set(this.month() - 1);
    }

    this.loadMonth();
  }

  loadMonth() {
    const days = this.calendarService.getGridCalendarDays(this.year(), this.month());
    this.grid.set(days);
  }
}
