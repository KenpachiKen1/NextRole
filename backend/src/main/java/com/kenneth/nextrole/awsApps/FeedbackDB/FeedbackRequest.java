package com.kenneth.nextrole.awsApps.FeedbackDB;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@DynamoDbBean
public class FeedbackRequest {

    private UUID id;

    @NotBlank
    @Length(min = 20)
    private String feedbackMessage;

    private MessageTypes type;

    private LocalDateTime createdAt;

    private Long userId;

    @DynamoDbPartitionKey
    public UUID getId() {
        return id;
    }
}