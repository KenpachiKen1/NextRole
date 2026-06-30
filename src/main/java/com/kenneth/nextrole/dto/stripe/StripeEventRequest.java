package com.kenneth.nextrole.dto.stripe;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StripeEventRequest {
    private String stripeEventId;
    private String eventType;
}
