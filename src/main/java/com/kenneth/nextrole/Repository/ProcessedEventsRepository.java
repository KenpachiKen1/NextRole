package com.kenneth.nextrole.Repository;

import com.kenneth.nextrole.Model.ProcessedStripeEvents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedEventsRepository extends JpaRepository<ProcessedStripeEvents, Long> {

    boolean existsProcessedStripeEventsByStripeEventId (String stripeEventId);

    Optional<ProcessedStripeEvents> findProcessedStripeEventsByStripeEventId(String stripeEventId);


}
