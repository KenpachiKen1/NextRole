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
  salary: number | null;
  postingUrl: string;
  companyId: number | null;
  companyName: string | null;
  reqCode: string;
  requiredSkills: string[];
  preferredSkills: string[];
  jobDescription: string;
}