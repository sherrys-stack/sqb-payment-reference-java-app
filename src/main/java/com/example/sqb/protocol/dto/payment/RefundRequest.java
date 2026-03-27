package com.example.sqb.protocol.dto.payment;

public record RefundRequest(String terminal_sn, String client_sn, String refund_request_no, String refund_amount) {
}
