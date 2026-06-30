package com.kenneth.nextrole;


import lombok.Getter;

@Getter
public enum SubscriptionStatus {

    FREE("Free Account", "You are currently on the free plan", "#9E9E9E"),
    SUBSCRIBED("Premium Account", "You are currently subscribed to NextRole Premium", "#FFD700"),
    PENDING("Pending", "Your subscription is being processed", "#FF9800"),
    TRIALING("Trial Account", "You are currently on a free trial of NextRole Premium", "#4CAF50"),
    CANCELED("Canceled", "Your subscription has been canceled", "#757575"),
    PAST_DUE("Past Due", "Your payment is past due. Please update your billing information", "#F44336");

    private final String display;
    private final String description;
    private final String hexCode;

    SubscriptionStatus(String display, String description, String hexCode) {
        this.display = display;
        this.description = description;
        this.hexCode = hexCode;
    }
}