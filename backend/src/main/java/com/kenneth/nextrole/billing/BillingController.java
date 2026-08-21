package com.kenneth.nextrole.billing;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.billing.dto.CancelSubscriptionRequest;
import com.kenneth.nextrole.billing.dto.CreateCheckoutSessionResponse;
import com.kenneth.nextrole.billing.dto.SubscriptionResponse;
import com.kenneth.nextrole.security.CustomUserPrincipal;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping("/checkout")
    public CreateCheckoutSessionResponse createCheckout(
            @AuthenticationPrincipal User user
    ) throws StripeException {

        return billingService.createCheckout(user);
    }

    @GetMapping("/subscription")
    public SubscriptionResponse getSubscription(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return billingService.getSubscription(principal.getUser());
    }


    /*
    Cancel a user's subscription
     */
    @PostMapping("/cancel")
    public SubscriptionResponse cancelSubscription(@AuthenticationPrincipal CustomUserPrincipal user, CancelSubscriptionRequest request) throws StripeException {
        return billingService.cancelSubscription(user.getUser(), request);
    }

}