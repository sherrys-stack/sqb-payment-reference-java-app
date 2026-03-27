package com.example.sqb.protocol.dto.payment;

public record PrecreateResponse(String result_code, BizResponse biz_response) {
    public record BizResponse(String result_code, String order_status, String order_sn, String qr_code, String error_message) {}
}
