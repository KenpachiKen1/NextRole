import { JobStatus } from "../enums/jobEntry-status.enums";

export interface CreateJobEntryRequest {
  jobPostingId: number;
  resumeId: number;
  notes: string;
  status: JobStatus;
}

export interface UpdateJobEntryRequest {
  resumeId: number;
  notes: string;
  status: JobStatus;
}

export interface JobEntryResponse {
  id: number;
  jobPostingId: number;
  jobTitle: string;
  resumeId: number;
  resumeTitle: string;
  notes: string;
  status: JobStatus;
  appliedAt: string;
}
