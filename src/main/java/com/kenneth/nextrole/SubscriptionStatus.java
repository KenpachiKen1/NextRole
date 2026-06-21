package com.kenneth.nextrole;


import lombok.Getter;

@Getter
public enum SubscriptionStatus {
    FREE("Free Account", "You are currently on the free plan", "#9E9E9E"),
    SUBSCRIBED("Premium Account", "You are currently subscribed to NextRole Premium", "#FFD700");


    private final String display;
    private final String description;
    private final String hexCode;

    SubscriptionStatus(String display, String description, String hexCode) {
        this.display = display;
        this.description = description;
        this.hexCode = hexCode;
    }
}