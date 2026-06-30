package com.kenneth.nextrole.billing;


import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.CustomerRepository;
import com.kenneth.nextrole.Repository.UserRepository;
import com.kenneth.nextrole.Service.CustomerService;
import com.kenneth.nextrole.SubscriptionStatus;
import com.stripe.model.Event;
import com.stripe.model.Invoice;
import com.stripe.model.Subscription;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.stripe.model.checkout.Session;

@Service
public class WebhookService {

    private final CustomerService customerService;

    public WebhookService(CustomerService customerService) {
        this.customerService = customerService;

    }

    @Transactional
    public void handleEvent(Event event) {

        switch (event.getType()) {

            case "checkout.session.completed":
                handleCheckoutSessionCompleted(event);
                break;

            case "customer.subscription.created":
                handleSubscriptionCreated(event);
                break;

            case "customer.subscription.updated", "customer.subscription.deleted":
                handleSubscriptionUpdated(event);
                break;

            case "invoice.payment_failed":
                handlePaymentFailed(event);
                break;
        }
    }

    private void handleCheckoutSessionCompleted(Event event) {

        Session session =
                (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow();

        String customerId = session.getCustomer();

        Customer customer = customerService.getByStripeCustomerId(customerId);

        if (session.getSubscription() != null) {
            customerService.setSubscriptionId(
                    customer,
                    session.getSubscription()
            );
        }
    }

    private void handleSubscriptionCreated(Event event) {

        Subscription subscription =
                (Subscription) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow();

        Customer customer =
                customerService.getByStripeCustomerId(subscription.getCustomer());

        customerService.setSubscriptionId(
                customer,
                subscription.getId()
        );
    }

    private void handleSubscriptionUpdated(Event event) {

        Subscription subscription =
                (Subscription) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow();

        Customer customer =
                customerService.getByStripeCustomerId(subscription.getCustomer());

        customerService.setSubscriptionId(
                customer,
                subscription.getId()
        );

        customerService.updateSubscriptionStatus(
                customer,
                mapStripeStatus(subscription.getStatus())
        );
    }

    private void handlePaymentFailed(Event event) {

        Invoice invoice =
                (Invoice) event.getDataObjectDeserializer()
                        .getObject()
                        .orElseThrow();

        Customer customer =
                customerService.getByStripeCustomerId(invoice.getCustomer());

        customerService.updateSubscriptionStatus(
                customer,
                SubscriptionStatus.PAST_DUE
        );
    }

    private SubscriptionStatus mapStripeStatus(String status) {
        return switch (status) {
            case "active", "trialing" -> SubscriptionStatus.SUBSCRIBED;
            case "incomplete", "past_due", "unpaid" -> SubscriptionStatus.PENDING;
            case "canceled", "incomplete_expired" -> SubscriptionStatus.CANCELED;
            default -> SubscriptionStatus.FREE;
        };
    }
}