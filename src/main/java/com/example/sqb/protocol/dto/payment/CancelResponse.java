package com.example.sqb.protocol.dto.payment;

public record CancelResponse(String result_code, BizResponse biz_response) {
    public record BizResponse(String result_code, String order_status, String error_message) {}
}
