package com.example.sqb.support.status;

public record ParsedResult(
        boolean communicationSuccess,
        boolean businessSuccess,
        OrderStatus orderStatus,
        boolean terminal,
        String message) {
}
