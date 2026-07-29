import { JobEntryResponse } from "./job-entry.model"


export interface CalendarDay{
    jobEntries: JobEntryResponse[]
    date: Date
    isCurrentMonth: boolean
    isCurrentDay: boolean
}