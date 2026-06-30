package com.kenneth.nextrole.billing;


//API SET UP AT START UP DUE TO Stripe Config!

/*
This service is purely for talking to the Stripe API
 */
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.billing.dto.CreateCheckoutSessionResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.SubscriptionCancelParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;

/*
    The purpose of this service is to:
    initialize Stripe

    create a Checkout Session

    retrieve customers

    create customers

    create Billing Portal sessions later

    process webhook events
 */

import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${stripe.price}")
    private String price;

    @Value("${stripe.product}")
    private String product;
    /*
    createUser()

    - Takes in an authenticated user

    parses that user's info so (first name, last name, email...)

    creates the Stripe customer using this information

    returns the customer
     */


    public Customer createCustomer(User user) throws StripeException {
        String fullName = user.getFirstName() + " " + user.getLastName();

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName(fullName)
                .setEmail(user.getEmail())
                .build();

        return Customer.create(params);
    }


    public CreateCheckoutSessionResponse toResponse(Session session){
            return CreateCheckoutSessionResponse.builder().
                    checkoutURL(session.getUrl()).build();
    }


    public Customer getOrCreateCustomer(User user) throws StripeException {
        /*
        If the customer exists with the user,
         just retrieve the customer,
         or create it then retrieve
         */
        if (user.getCustomer() != null){
            String id = user.getCustomer().getStripeCustomerId();
            return Customer.retrieve(id);
        }

        return createCustomer(user);

    }




    /*
    createCheckoutSession()
        Needs:

        - authenticated user

        - Stripe Customer

        - Price ID

        - success URL

        - cancel URL

        Returns:

        Checkout URL
     */

    public CreateCheckoutSessionResponse createCheckoutSession(String stripeCustomerId) throws StripeException {

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setCustomer(stripeCustomerId)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice(price)
                                .build()
                )
                .setSuccessUrl("https://example.com")
                .setCancelUrl("https://example.com")
                .build();

        Session session = Session.create(params);

        return CreateCheckoutSessionResponse.builder()
                .checkoutURL(session.getUrl())
                .build();
    }

    public String getSubscriptionId (String checkoutSessionId) throws StripeException {
        Session session = Session.retrieve(checkoutSessionId);
        return session.getSubscription();
    }

    public Subscription cancelSubscription(com.kenneth.nextrole.Model.Customer customer, String comment) throws StripeException {

        Subscription resource = Subscription.retrieve(customer.getStripeSubscriptionId());

        /*
        Want to update the feedback to be dynamic, maybe add a list of ENUM values and comment
         */

        SubscriptionCancelParams params = SubscriptionCancelParams.builder().setCancellationDetails(
                        SubscriptionCancelParams.CancellationDetails.builder()
                                .setFeedback(SubscriptionCancelParams.CancellationDetails.Feedback.TOO_EXPENSIVE)
                                .setComment(comment)
                                .build()
                ).
                build();
        return resource.cancel(params);
    }





}
