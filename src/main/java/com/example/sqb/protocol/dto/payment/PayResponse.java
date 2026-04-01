package com.example.sqb.protocol.dto.payment;

public record PayResponse(String result_code, BizResponse biz_response) {
    public record BizResponse(String result_code, String error_message, Data data) {}
    public record Data(String order_status, String sn, String client_sn) {}
}
