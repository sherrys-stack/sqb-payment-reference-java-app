package com.example.sqb.protocol.dto.payment;

public record RefundResponse(String result_code, BizResponse biz_response) {
    public record BizResponse(String result_code, String refund_status, String error_message) {}
}
