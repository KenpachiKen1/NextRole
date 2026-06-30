package com.kenneth.nextrole.Model;
import com.kenneth.nextrole.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter @Setter //generates getters and setters
@AllArgsConstructor //all args constructor with complete body
@NoArgsConstructor //no argument constructor
@Builder //builds out the user -> think Model.objects.create... from django
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true)
    private String stripeCustomerId;

    @Column(unique = true)
    private String stripeSubscriptionId;

    private String stripePriceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private SubscriptionStatus subscriptionStatus = SubscriptionStatus.FREE;


    private LocalDateTime currentPeriodEnd = null;

    private boolean cancelAtPeriodEnd;
}
