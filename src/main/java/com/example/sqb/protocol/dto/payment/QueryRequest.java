package com.example.sqb.protocol.dto.payment;

public record QueryRequest(String terminal_sn, String sn, String client_sn) {
    public QueryRequest(String terminal_sn, String client_sn) {
        this(terminal_sn, null, client_sn);
    }

    public static QueryRequest bySn(String terminalSn, String sn) {
        return new QueryRequest(terminalSn, sn, null);
    }

    public static QueryRequest byClientSn(String terminalSn, String clientSn) {
        return new QueryRequest(terminalSn, null, clientSn);
    }
}
