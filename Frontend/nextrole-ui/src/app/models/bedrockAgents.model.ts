
export interface ResumeFeedbackResponse {
  overrallResumeScore: number;
  strengths: string[];
  weaknesses: string[];
  atsWarnings: string[];
  bestResumeBullet: string;
}

export interface Recommendation {
  title: string;
  description: string;
}

export interface ResumeTailoringResponse {
  matchScore: number;
  strengths: string[];
  missingKeywords: string[];
  recommendations: Recommendation[];
}

export interface TechnicalQuestions{
    question: string
    reason: string
}

export interface BehavioralQuestions{
    question: string
    reason: string
}


export interface InterviewPrepResponse{
    technicalQuestions: TechnicalQuestions[]
    behavioralQuestions: BehavioralQuestions[]
}