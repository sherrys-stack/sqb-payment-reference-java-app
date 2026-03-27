package com.example.sqb.protocol.dto.terminal;

public record ActivateRequest(String app_id, String code, String device_id) {
}
