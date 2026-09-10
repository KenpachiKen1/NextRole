import { MessageTypes } from "../enums/messageTypes.enums";

export interface FeedbackRequest {
    id?: string;
    feedbackMessage: string;
    type?: MessageTypes
    createdAt?: Date;
    userId?: number;
}