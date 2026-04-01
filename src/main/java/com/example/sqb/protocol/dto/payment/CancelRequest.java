package com.example.sqb.protocol.dto.payment;

public record CancelRequest(String terminal_sn, String sn, String client_sn, String operator) {
    public CancelRequest(String terminal_sn, String client_sn) {
        this(terminal_sn, null, client_sn, null);
    }
}
