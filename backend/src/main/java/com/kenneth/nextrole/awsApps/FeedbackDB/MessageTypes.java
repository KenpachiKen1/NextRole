package com.kenneth.nextrole.awsApps.FeedbackDB;

public enum MessageTypes {
    SUGGESTION("Suggestion"),
    BUG("Bug"),
    QUESTION("Question"),
    FEEDBACK("Feedback"),
    FEATURE_REQUEST("Feature Request"),
    COMPLAINTS("Complaints");

    private final String type;

    MessageTypes(String type){
        this.type = type;
    }
}
