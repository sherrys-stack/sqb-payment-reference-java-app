package com.example.sqb.protocol.dto.payment;

public record PayRequest(String terminal_sn, String client_sn, String total_amount, String dynamic_id, String subject, String operator) {
}
