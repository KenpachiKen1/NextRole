package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.Customer;
import com.kenneth.nextrole.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUser (User user);
    Optional <Customer> findById (String id);

    Optional<Customer> findByStripeCustomerId (String id);
}
