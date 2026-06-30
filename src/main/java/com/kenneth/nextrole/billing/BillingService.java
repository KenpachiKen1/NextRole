package com.kenneth.nextrole.billing;

import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Service.CustomerService;
import com.kenneth.nextrole.SubscriptionStatus;
import com.kenneth.nextrole.billing.dto.CancelSubscriptionRequest;
import com.kenneth.nextrole.billing.dto.CreateCheckoutSessionResponse;
import com.kenneth.nextrole.billing.dto.SubscriptionResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    private final StripeService stripeService;
    private final CustomerService customerService;

    public BillingService(StripeService stripeService,
                          CustomerService customerService) {
        this.stripeService = stripeService;
        this.customerService = customerService;
    }

    public SubscriptionResponse toResponse(Customer customer){

            return SubscriptionResponse.builder().subscriptionStatus(customer.getSubscriptionStatus()).
                    cancelAtPeriodEnd(customer.isCancelAtPeriodEnd()).currentPeriodEnd(customer.getCurrentPeriodEnd()).planName("NextRole+").build();
    }

    public CreateCheckoutSessionResponse createCheckout(User user) throws StripeException {

        Customer dbCustomer = user.getCustomer();

        if (dbCustomer == null) {
            com.stripe.model.Customer stripeCustomer =
                    stripeService.createCustomer(user);

            dbCustomer = customerService.createCustomer(user, stripeCustomer.getId());
            user.setCustomer(dbCustomer);
        }

        return stripeService.createCheckoutSession(dbCustomer.getStripeCustomerId());
    }


    public SubscriptionResponse getSubscription(User user){
        if(user.getCustomer() == null){
            return SubscriptionResponse.builder().subscriptionStatus(SubscriptionStatus.FREE).planName("NextRole Free Version").build();
        }
        return toResponse(user.getCustomer());
    }

    public SubscriptionResponse cancelSubscription(User user, CancelSubscriptionRequest request) throws StripeException {
        if(user.getCustomer() == null){
            return SubscriptionResponse.builder().subscriptionStatus(SubscriptionStatus.FREE).planName("NextRole Free Version").build();
        }

        Customer customer = user.getCustomer();

        Subscription sub = stripeService.cancelSubscription(customer, request.getComment());

        customer.setSubscriptionStatus(SubscriptionStatus.FREE);
        customer.setCancelAtPeriodEnd(false);
        customer.setCurrentPeriodEnd(null);

        return toResponse(customer);

    }
}