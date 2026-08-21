package com.kenneth.nextrole.Controller;


import com.kenneth.nextrole.Service.ProcessedStripeEventService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.kenneth.nextrole.billing.WebhookService;
@RestController
@RequestMapping("/api/stripe")
public class WebhookController {


    String endpointSecret = ""; //will update this later

    private final WebhookService webhookService;
    private final ProcessedStripeEventService stripeEventService;
    public WebhookController(WebhookService webhookService, ProcessedStripeEventService stripeEventService){
        this.webhookService = webhookService;
        this.stripeEventService = stripeEventService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        try {
            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    endpointSecret
            );

            if(stripeEventService.hasProcessedEvent(event.getId())){
                return ResponseEntity.status(HttpStatus.OK).body("Processed event already");
            }

            webhookService.handleEvent(event);
            stripeEventService.markEventProcessed(event.getId(), event.getType());

            return ResponseEntity.ok("Webhook processed.");

        } catch (SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("Invalid Stripe signature.");
        }
    }
}
