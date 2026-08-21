package com.kenneth.nextrole.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter //generates getters and setters
@AllArgsConstructor //all args constructor with complete body
@NoArgsConstructor //no argument constructor
@Builder //builds out the user -> think Model.objects.create... from django
@Table(name = "ProcessedStripeEvents")
public class ProcessedStripeEvents {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String stripeEventId;

    private String eventType;

    private LocalDateTime createdAt;

}
