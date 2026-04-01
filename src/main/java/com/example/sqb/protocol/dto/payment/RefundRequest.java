package com.example.sqb.protocol.dto.payment;

public record RefundRequest(
        String terminal_sn,
        String sn,
        String client_sn,
        String refund_request_no,
        String refund_amount,
        String operator) {

    public RefundRequest(String terminal_sn, String client_sn, String refund_request_no, String refund_amount) {
        this(terminal_sn, null, client_sn, refund_request_no, refund_amount, null);
    }
}
