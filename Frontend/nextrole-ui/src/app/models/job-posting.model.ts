export interface CreateJobPostingRequest {
  title: string;
  location: string;
  salary: number;
  postingUrl: string;
  employmentType: string;
  companyId: number;
  requisitionCode: string;
}

export interface UpdateJobPostingRequest {
  title: string;
  location: string;
  salary: number;
  postingUrl: string;
  requisitionCode: string;
}

export interface JobPostingResponse {
  id: number;
  title: string;
  location: string;
  salary: number;
  postingUrl: string;
  companyId: number;
  companyName: string;
  reqCode: string;
}
