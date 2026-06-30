package com.kenneth.nextrole.Controller;
import com.kenneth.nextrole.JobStatus;
import com.kenneth.nextrole.Model.JobEntry;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Service.JobEntryService;
import com.kenneth.nextrole.dto.jobentry.CreateJobEntryRequest;
import com.kenneth.nextrole.dto.jobentry.JobEntryResponse;
import com.kenneth.nextrole.dto.jobentry.UpdateJobEntryRequest;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobEntry")
public class JobEntryController {

    private final JobEntryService js;

    public JobEntryController (JobEntryService js){
        this.js = js;
    }

    //@PathVariable for specific entity tasks, RequestParam for filtering
    @GetMapping("/getEntry/{id}")
    public ResponseEntity <JobEntryResponse> getEntry(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal, @PathVariable Long id){
        Long uid = customUserPrincipal.getUser().getId();
        JobEntryResponse response = js.getJobEntry(id, uid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PostMapping("/create-entry/{jobPostingID}/{resumeId}/")
    public ResponseEntity <JobEntryResponse> createJobEntry(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal,
                                                            CreateJobEntryRequest request,
                                                            @PathVariable Long jobPostingID, @PathVariable Long resumeId){
        User user = customUserPrincipal.getUser();
        JobEntryResponse response = js.createEntry(request, user, resumeId, jobPostingID);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/updateJobEntry/{entryId}")
    public ResponseEntity <JobEntryResponse> updateEntry(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal,
                                                         @PathVariable Long entryId, UpdateJobEntryRequest request){
        User user = customUserPrincipal.getUser();
        JobEntryResponse response = js.updateEntry(entryId, request, user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/deleteEntry/{postingId}")
    public ResponseEntity <List<JobEntryResponse>> deleteEntry (@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal,
                                                                @PathVariable Long postingId){

       Long uid = customUserPrincipal.getUser().getId();
       List <JobEntryResponse> entries = js.deleteByUserIdAndJobPostingId(uid, postingId);
       return ResponseEntity.status(HttpStatus.OK).body(entries);
    }


    @GetMapping("/jobEntryList")
    public ResponseEntity <List<JobEntryResponse>> getEntries(@AuthenticationPrincipal CustomUserPrincipal customUserPrincipal, @RequestParam JobStatus status, @RequestParam String title){
        User user = customUserPrincipal.getUser();

        // GET /job-entries/filter?title=engineer&status=SUBMITTED
        if (status != null && title != null){
            List <JobEntryResponse> entries =  js.findByUser_IdAndJobPosting_TitleContainingIgnoreCaseAndStatus(user.getId(), title, status);
            return ResponseEntity.status(HttpStatus.OK).body(entries);
        }
        // GET /job-entries/status?status=SUBMITTED
        if(status != null){
            List <JobEntryResponse> entries = js.findByUser_IdAndStatus(user.getId(), status);
            return ResponseEntity.status(HttpStatus.OK).body(entries);
        }
        // GET /job-entries/title?title=Technology&Development&Program
        else if (title != null){
            List <JobEntryResponse> entries = js.findByJobPosting_TitleContainingIgnoreCaseAndUser_Id(title, user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(entries);
        }
        //If params are empty return all
        List <JobEntryResponse> entries = js.findByUserId(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(entries);
    }



}
