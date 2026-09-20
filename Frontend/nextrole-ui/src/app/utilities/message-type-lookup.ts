import { MessageTypes } from '../enums/messageTypes.enums';

export const MessageTypeInfo = {
  [MessageTypes.SUGGESTION]: { label: 'Suggestion' },
  [MessageTypes.BUG]: { label: 'Bug' },
  [MessageTypes.FEEDBACK]: { label: 'General Feedback' },
  [MessageTypes.QUESTION]: { label: 'Question' },
  [MessageTypes.FEATURE_REQUEST]: { label: 'Feature Request' },
  [MessageTypes.COMPLAINTS]: { label: 'Complaint' },
} as const;
