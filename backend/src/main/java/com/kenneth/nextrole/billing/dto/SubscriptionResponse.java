package com.kenneth.nextrole.billing.dto;

import com.kenneth.nextrole.SubscriptionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionResponse {

        private SubscriptionStatus subscriptionStatus;
        private String planName;
        private LocalDateTime currentPeriodEnd;
        private boolean cancelAtPeriodEnd;


}
