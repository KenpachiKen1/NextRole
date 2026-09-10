package com.kenneth.nextrole.Service;


import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.Resume;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.JobEntryRepository;
import com.kenneth.nextrole.Repository.PasswordResetRespository;
import com.kenneth.nextrole.Repository.ResumeRepository;
import com.kenneth.nextrole.Repository.UserRepository;
import com.kenneth.nextrole.awsApps.S3Service;
import com.kenneth.nextrole.billing.StripeService;
import com.kenneth.nextrole.dto.user.UpdateUserRequest;
import com.kenneth.nextrole.dto.user.UserResponse;
import com.kenneth.nextrole.exception.EmailAlreadyExistsException;
import com.kenneth.nextrole.exception.UsernameAlreadyExistsException;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;
    private final JobEntryRepository jobEntryRepository;
    private final PasswordResetRespository passwordResetRepository;
    private final S3Service s3Service;
    private final StripeService stripeService;

    public UserService(
            UserRepository userRepository,
            ResumeRepository resumeRepository,
            JobEntryRepository jobEntryRepository,
            PasswordResetRespository passwordResetRepository,
            S3Service s3Service,
            StripeService stripeService
    ) {
        this.userRepository = userRepository;
        this.resumeRepository = resumeRepository;
        this.jobEntryRepository = jobEntryRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.s3Service = s3Service;
        this.stripeService = stripeService;
    }


    public UserResponse toResponse(User user){
        return UserResponse.builder().id(user.getId()).
                username(user.getUsername())
                .first_name(user.getFirstName()).last_name(user.getLastName())
                .email(user.getEmail())
                .profilePhoto(user.getProfilePhoto()).build();
    }

    /*
    Pass the email from the controller to here to authenticate.
     */

    public UserResponse getUserProfile(User user){
        return toResponse(user);
    }


    @Transactional
    public UserResponse updateCurrentUser(UpdateUserRequest request, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found")); //global Exception handler auto calls it

        if (request.getUsername() != null) {
            if (userRepository.existsByUsernameIgnoreCase(request.getUsername())
                    && !user.getUsername().equalsIgnoreCase(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already in use");
            }
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            if (userRepository.existsByEmail(request.getEmail())
                    && !user.getEmail().equalsIgnoreCase(request.getEmail())) {
                throw new EmailAlreadyExistsException("Email already in use!");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getProfilePhoto() != null) {
            user.setProfilePhoto(request.getProfilePhoto());
        }

        user = userRepository.save(user);
        return toResponse(user);
    }


    /*
    Wipes everything tied to the account immediately: cancels any active Stripe
    subscription, deletes resume files from S3, then deletes job entries, resumes,
    the password-reset record, and finally the user row itself (which cascades to
    the Customer/billing row). We keep no retention period, so this needs to be a
    real, complete delete rather than a soft-delete/flag.
     */
    @Transactional
    public void deleteAccount(String email){

        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new EntityNotFoundException("User not found"));

        Customer customer = user.getCustomer();
        if (customer != null && customer.getStripeSubscriptionId() != null) {
            try {
                stripeService.cancelSubscription(customer, "Account deleted by user");
            } catch (StripeException e) {
                throw new RuntimeException("Failed to cancel subscription while deleting account", e);
            }
        }

        for (Resume resume : resumeRepository.findByUserId(user.getId())) {
            s3Service.deleteResume(resume.getS3ObjectKey());
        }

        jobEntryRepository.deleteAll(jobEntryRepository.findByUser_Id(user.getId()));
        resumeRepository.deleteAll(resumeRepository.findByUserId(user.getId()));
        passwordResetRepository.deleteByEmail(email);

        userRepository.delete(user);

        SecurityContextHolder.clearContext();
    }

}
