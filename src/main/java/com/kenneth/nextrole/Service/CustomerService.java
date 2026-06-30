package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import com.kenneth.nextrole.Repository.CustomerRepository;
import com.kenneth.nextrole.SubscriptionStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(User user, String stripeCustomerId) {

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setStripeCustomerId(stripeCustomerId);
        customer.setSubscriptionStatus(SubscriptionStatus.FREE);

        customerRepository.save(customer);

        return customer;
    }

    public Customer getByStripeCustomerId(String stripeCustomerId) {
        return customerRepository.findByStripeCustomerId(stripeCustomerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public void updateSubscriptionStatus(Customer customer, SubscriptionStatus status) {
        customer.setSubscriptionStatus(status);
        customerRepository.save(customer);
    }

    public void setSubscriptionId(Customer customer, String subscriptionId) {
        customer.setStripeSubscriptionId(subscriptionId);
        customerRepository.save(customer);
    }
}
