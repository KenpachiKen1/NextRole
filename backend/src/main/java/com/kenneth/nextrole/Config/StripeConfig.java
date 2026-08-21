package com.kenneth.nextrole.Config;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class StripeConfig {

    @Value("${stripe.secret}")
    private String stripeKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeKey;
    }
}
