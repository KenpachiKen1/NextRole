package com.kenneth.nextrole.dto.jobentry;

import com.kenneth.nextrole.JobStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateJobEntryRequest {

    @NotNull
    private Long jobPostingId;
    @NotNull
    private Long resumeId;
    private String notes;
    private JobStatus status;

}
