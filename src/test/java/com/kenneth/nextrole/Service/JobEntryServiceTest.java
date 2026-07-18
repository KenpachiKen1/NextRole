package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;
import com.kenneth.nextrole.Model.JobPosting;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.JobEntryRepository;
import com.kenneth.nextrole.Repository.JobPostingRepository;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.dto.jobentry.CreateJobEntryRequest;
import com.kenneth.nextrole.dto.jobentry.JobEntryResponse;
import com.kenneth.nextrole.dto.jobentry.UpdateJobEntryRequest;
import com.kenneth.nextrole.exception.JobEntryNotFoundException;
import com.kenneth.nextrole.exception.ResumeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class JobEntryServiceTest {

    @Mock private JobEntryRepository jobEntryRepository;
    @Mock private ResumeRepository resumeRepository;
    @Mock private JobPostingRepository jobPostingRepository;

    @InjectMocks
    private JobEntryService jobEntryService;

    @Test
    void createEntry_defaultsStatusToSubmitted_whenNoStatusProvided() {
        CreateJobEntryRequest request = new CreateJobEntryRequest();
        request.setNotes("Applied via referral");
        // request.getStatus() left null on purpose

        User user = mock(User.class);
        Resume resume = Resume.builder().resumeTitle("Main Resume").s3ObjectKey("randomKey").
                build();
        JobPosting posting = JobPosting.builder().title("Backend Engineer").build();

        when(resumeRepository.findById(10L)).thenReturn(Optional.of(resume));
        when(jobPostingRepository.findById(20L)).thenReturn(Optional.of(posting));
        when(jobEntryRepository.save(any(JobEntry.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        jobEntryService.createEntry(request, user, 10L, 20L);

        ArgumentCaptor<JobEntry> captor = ArgumentCaptor.forClass(JobEntry.class);
        verify(jobEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(JobStatus.SUBMITTED);
    }

    @Test
    void createEntry_throwsResumeNotFound_whenResumeMissing() {
        CreateJobEntryRequest request = new CreateJobEntryRequest();
        User user = mock(User.class);

        when(resumeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobEntryService.createEntry(request, user, 999L, 20L))
                .isInstanceOf(ResumeNotFoundException.class);

        verify(jobEntryRepository, never()).save(any());
    }

    @Test
    void updateEntry_throwsAccessDenied_whenUserDoesNotOwnEntry() {
        User owner = mock(User.class);
        when(owner.getId()).thenReturn(1L);

        User intruder = mock(User.class);
        when(intruder.getId()).thenReturn(2L);

        JobEntry entry = JobEntry.builder().user(owner).build();
        UpdateJobEntryRequest request = new UpdateJobEntryRequest();

        when(jobEntryRepository.findById(5L)).thenReturn(Optional.of(entry));

        assertThatThrownBy(() -> jobEntryService.updateEntry(5L, request, intruder))
                .isInstanceOf(AccessDeniedException.class);

        verify(jobEntryRepository, never()).save(any());
    }

    @Test
    void updateEntry_updatesNotesAndStatus_whenUserIsOwner() {
        User owner = mock(User.class);
        when(owner.getId()).thenReturn(1L);

        JobPosting posting = JobPosting.builder().title("Backend Engineer").build();
        Resume resume = Resume.builder().resumeTitle("Main Resume").s3ObjectKey("randomKey")
                .build();
        JobEntry entry = JobEntry.builder()
                .user(owner)
                .jobPosting(posting)
                .resumeUsed(resume)
                .status(JobStatus.SUBMITTED)
                .build();

        UpdateJobEntryRequest request = new UpdateJobEntryRequest();
        request.setNotes("Had a great first interview");
        request.setStatus(JobStatus.INTERVIEWING);

        when(jobEntryRepository.findById(5L)).thenReturn(Optional.of(entry));
        when(jobEntryRepository.save(entry)).thenReturn(entry);

        JobEntryResponse response = jobEntryService.updateEntry(5L, request, owner);

        assertThat(entry.getNotes()).isEqualTo("Had a great first interview");
        assertThat(entry.getStatus()).isEqualTo(JobStatus.INTERVIEWING);
        assertThat(response).isNotNull();
    }

    @Test
    void updateEntry_throwsJobEntryNotFound_whenEntryMissing() {
        User user = mock(User.class);
        UpdateJobEntryRequest request = new UpdateJobEntryRequest();

        when(jobEntryRepository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobEntryService.updateEntry(404L, request, user))
                .isInstanceOf(JobEntryNotFoundException.class);
    }

    @Test
    void jobEntryBuilder_appliesStatusDefault_ofSubmitted() {
        JobEntry entry = JobEntry.builder().build();

        assertThat(entry.getStatus()).isEqualTo(JobStatus.SUBMITTED);
    }
}