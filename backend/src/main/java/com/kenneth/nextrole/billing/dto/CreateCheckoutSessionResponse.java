package com.kenneth.nextrole.billing.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCheckoutSessionResponse {

    private String checkoutURL;
}
