package com.example.sqb.protocol.dto.notify;

public record NotifyPayload(String sn, String order_sn, String client_sn, String order_status, String total_amount) {
}
