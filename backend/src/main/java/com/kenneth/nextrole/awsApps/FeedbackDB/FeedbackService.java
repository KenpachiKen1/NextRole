package com.kenneth.nextrole.awsApps.FeedbackDB;

import com.kenneth.nextrole.exception.FeedbackStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
public class FeedbackService {

    private final DynamoDbTable<FeedbackRequest> feedbackTable;

    public FeedbackService(DynamoDbEnhancedClient enhancedClient, @Value("${aws.dynamodb.feedback-table}") String tableName) {
        this.feedbackTable = enhancedClient.table(tableName, TableSchema.fromBean(FeedbackRequest.class));
    }

    public String addFeedback(FeedbackRequest request) {
        request.setId(UUID.randomUUID());
        request.setCreatedAt(LocalDateTime.now());

        try {
            this.feedbackTable.putItem(request);
            return "Feedback Sent!";
        } catch (DynamoDbException e) {
            log.error("DynamoDB error while saving feedback", e);
            throw new FeedbackStorageException("Failed to save feedback.", e);
        }
    }
}
