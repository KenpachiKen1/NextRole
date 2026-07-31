import { Injectable } from '@angular/core';
import { CalendarDay } from '../models/calendar.models';

@Injectable({
  providedIn: 'root',
})
export class CalendarService {
  getGridCalendarDays(year: number, month: number): CalendarDay[] {
  
    const targetMonthIndex = month;

    const firstDayOfWeek = new Date(year, targetMonthIndex, 1).getDay();

    const startDate = new Date(year, targetMonthIndex, 1);
    startDate.setDate(startDate.getDate() - firstDayOfWeek);

    const today = new Date();
    const totalGridCells = 42;

    const days: CalendarDay[] = [];

    for (let i = 0; i < totalGridCells; i++) {
      const cellDate = new Date(startDate);

      days.push({
        date: cellDate,
        jobEntries: [],
        isCurrentMonth: cellDate.getMonth() === targetMonthIndex,
        isCurrentDay: this.isSameDay(cellDate, today),
      });

      startDate.setDate(startDate.getDate() + 1);
    }

    return days;
  }

  
  private isSameDay(a: Date, b: Date): boolean {
    return (
      a.getFullYear() === b.getFullYear() &&
      a.getMonth() === b.getMonth() &&
      a.getDate() === b.getDate()
    );
  }

  pastOrPresentDay(today = new Date(), gridDate: Date): boolean {
    const target = new Date(gridDate.getTime());
    const current = new Date(today.getTime());

    // Normalize both dates to midnight
    target.setHours(0, 0, 0, 0);
    current.setHours(0, 0, 0, 0);

    // Less than or equal to now safely checks past or present
    return target.getTime() <= current.getTime();
  }
}
