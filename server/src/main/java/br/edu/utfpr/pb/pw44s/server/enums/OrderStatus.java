package br.edu.utfpr.pb.pw44s.server.enums;

public enum OrderStatus {

    PROCESSING("Processing"),
    PAID("Paid"),
    SHIPPED("Shipped"),
    RECEIVE("Receive"),
    CANCELED("Canceled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
