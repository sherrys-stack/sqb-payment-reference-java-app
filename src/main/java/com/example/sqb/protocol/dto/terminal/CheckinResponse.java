package com.example.sqb.protocol.dto.terminal;

public record CheckinResponse(String result_code, BizResponse biz_response) {
    public record BizResponse(String result_code, String terminal_key, String error_message) {}
}
