package com.example.sqb.support.status;

import java.util.Set;

public enum OrderStatus {
    CREATED,
    PAYING,
    PAY_ERROR,
    REFUND_ERROR,
    PAID,
    PAY_CANCELED,
    REFUNDED,
    PARTIAL_REFUNDED,
    CANCELED,
    CANCEL_ERROR,
    UNKNOWN;

    private static final Set<OrderStatus> TERMINAL = Set.of(PAID, PAY_CANCELED, REFUNDED, PARTIAL_REFUNDED, CANCELED);

    public boolean isTerminal() {
        return TERMINAL.contains(this);
    }

    public static OrderStatus fromValue(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }
        try {
            return OrderStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ignored) {
            return UNKNOWN;
        }
    }
}
