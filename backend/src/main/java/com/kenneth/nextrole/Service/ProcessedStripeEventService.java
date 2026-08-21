package com.kenneth.nextrole.Service;

import com.kenneth.nextrole.Model.ProcessedStripeEvents;
import com.kenneth.nextrole.Repository.ProcessedEventsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProcessedStripeEventService {

    private final ProcessedEventsRepository processedEventsRepository;

    public ProcessedStripeEventService(ProcessedEventsRepository processedEventsRepository) {
        this.processedEventsRepository = processedEventsRepository;
    }

    /**
     * Returns true if this Stripe event has already been processed.
     */
    public boolean hasProcessedEvent(String stripeEventId) {
        return processedEventsRepository
                .existsProcessedStripeEventsByStripeEventId(stripeEventId);
    }

    /**
     * Marks a Stripe event as successfully processed.
     * Call this ONLY after all business logic completes successfully.
     */
    public void markEventProcessed(String stripeEventId, String eventType) {

        ProcessedStripeEvents event = new ProcessedStripeEvents();

        event.setStripeEventId(stripeEventId);
        event.setEventType(eventType);
        event.setCreatedAt(LocalDateTime.now());

        processedEventsRepository.save(event);
    }

}