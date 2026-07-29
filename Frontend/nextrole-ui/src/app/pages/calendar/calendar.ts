import { Component, inject } from '@angular/core';
import { CalendarService } from '../../services/calendarService';
@Component({
  selector: 'app-calendar',
  imports: [],
  templateUrl: './calendar.html',
  styleUrl: './calendar.css',
})
export class Calendar {
  private calendarService = inject(CalendarService)
  
}
