import { JobStatus } from "../enums/jobEntry-status.enums";

export const JobStatusInfo = {
  [JobStatus.DRAFT]: {
    label: 'Draft',
    description: 'Application not yet submitted. Simply storing your notes here.',
    color: '#9E9E9E',
  },

  [JobStatus.SUBMITTED]: {
    label: 'Submitted',
    description: 'Application has been received by the employer',
    color: '#2196F3',
  },

  [JobStatus.REVIEWING]: {
    label: 'Reviewing',
    description: 'Hiring team is evaluating your application',
    color: '#FF9800',
  },

  [JobStatus.INTERVIEWING]: {
    label: 'Interviewing',
    description: 'You have been selected for an interview!',
    color: '#9C27B0',
  },

  [JobStatus.OFFERED]: {
    label: 'Offered',
    description: 'Job offer has been extended. Congratulations!',
    color: '#00BCD4',
  },

  [JobStatus.HIRED]: {
    label: 'Hired',
    description: 'You have accepted the offer. Congratulations!',
    color: '#4CAF50',
  },

  [JobStatus.REJECTED]: {
    label: 'Rejected',
    description: 'Your application is no longer being considered',
    color: '#F44336',
  },

  [JobStatus.WITHDRAWN]: {
    label: 'Withdrawn',
    description: "You're canceling your application.",
    color: '#607D8B',
  },
} as const;
