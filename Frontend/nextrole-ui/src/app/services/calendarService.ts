import { inject, Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root',
})
export class CalendarService {

getGridCalendarDays(year: number, month: number): Date[] {
  const days: Date[] = [];

  const targetMonthIndex = month - 1; //date does 0 based indexing

  const firstDayOfWeek = new Date(year, targetMonthIndex, 1).getDay();

  const startDate = new Date(year, targetMonthIndex, 1);
  startDate.setDate(startDate.getDate() - firstDayOfWeek);

  const totalGridCells = 42;

  for (let i = 0; i < totalGridCells; i++) {
    days.push(new Date(startDate));
    startDate.setDate(startDate.getDate() + 1);
  }

  return days;
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
