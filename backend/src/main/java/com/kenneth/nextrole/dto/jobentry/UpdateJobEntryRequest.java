package com.kenneth.nextrole.dto.jobentry;

import com.kenneth.nextrole.JobStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateJobEntryRequest {

    private Long resumeId;
    private String notes;
    private JobStatus status;

}
