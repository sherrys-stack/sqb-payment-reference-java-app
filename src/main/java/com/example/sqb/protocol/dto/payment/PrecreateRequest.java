package com.example.sqb.protocol.dto.payment;

public record PrecreateRequest(String terminal_sn, String client_sn, String total_amount, String payway, String subject, String operator) {
}
