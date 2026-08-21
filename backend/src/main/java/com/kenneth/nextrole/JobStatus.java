package com.kenneth.nextrole;

import lombok.Getter;

@Getter
public enum JobStatus {
    DRAFT("Draft", "Application not yet submitted. Simply storing your notes here.", "#9E9E9E"),
    SUBMITTED("Submitted", "Application has been received by the employer", "#2196F3"),
    REVIEWING("Reviewing", "Hiring team is evaluating your application", "#FF9800"),
    INTERVIEWING("Interviewing", "You have been selected for an interview!", "#9C27B0"),
    OFFERED("Offered", "Job offer has been extended. Congratulations!", "#00BCD4"),
    HIRED("Hired", "You have accepted the offer. Congratulations!", "#4CAF50"),
    REJECTED("Rejected", "Your application is no longer being considered", "#F44336"),
    WITHDRAWN("Withdrawn", "You're canceling your application.", "#607D8B");

    private final String displayName;
    private final String description;
    private final String hexCodeColors;

    JobStatus(String displayName, String description, String hexCodeColors) {
        this.displayName = displayName;
        this.description = description;
        this.hexCodeColors = hexCodeColors;
    }
}