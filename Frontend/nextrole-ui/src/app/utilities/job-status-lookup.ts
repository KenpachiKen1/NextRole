import { JobStatus } from "../enums/jobEntry-status.enums";

export const JobStatusInfo = {
  [JobStatus.DRAFT]: {
    label: 'Draft',
    description: 'Application not yet submitted. Simply storing your notes here.',
    color: '#757575',
  },

  [JobStatus.SUBMITTED]: {
    label: 'Submitted',
    description: 'Application has been received by the employer',
    color: '#1565C0',
  },

  [JobStatus.REVIEWING]: {
    label: 'Reviewing',
    description: 'Hiring team is evaluating your application',
    color: '#B75B00',
  },

  [JobStatus.INTERVIEWING]: {
    label: 'Interviewing',
    description: 'You have been selected for an interview!',
    color: '#9C27B0',
  },

  [JobStatus.OFFERED]: {
    label: 'Offered',
    description: 'Job offer has been extended. Congratulations!',
    color: '#006C7A',
  },

  [JobStatus.HIRED]: {
    label: 'Hired',
    description: 'You have accepted the offer. Congratulations!',
    color: '#2E7D32',
  },

  [JobStatus.REJECTED]: {
    label: 'Rejected',
    description: 'Your application is no longer being considered',
    color: '#B7412C',
  },

  [JobStatus.WITHDRAWN]: {
    label: 'Withdrawn',
    description: "You're canceling your application.",
    color: '#4A5D68',
  },
} as const;
