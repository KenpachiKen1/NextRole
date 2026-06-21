package com.kenneth.nextrole.dto.jobentry;
import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

//should never actually put whole entities into the dto's just their references
@Getter
@Setter
@Builder
public class JobEntryResponse {
    private Long id;

    private Long jobPostingId;
    private String jobTitle;

    private Long resumeId;
    private String resumeTitle;

    private String notes;

    private JobStatus status;

    private LocalDateTime appliedAt;


}
