package com.example.sqb.adapter.payment;

import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.payment.RefundRequest;
import com.example.sqb.protocol.dto.payment.RefundResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbRefundAdapter {
    private final SqbHttpClient httpClient;
    private final TerminalCredentialStore credentialStore;

    public SqbRefundAdapter(SqbHttpClient httpClient, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.credentialStore = credentialStore;
    }

    public RefundResponse refund(RefundRequest request) {
        validateRequest(request);
        TerminalCredential credential = credentialStore.get().orElseThrow(() -> new IllegalStateException("Terminal credential not initialized"));
        return httpClient.post("/upay/v2/refund", request, credential.terminalSn(), credential.terminalKey(), RefundResponse.class);
    }

    private void validateRequest(RefundRequest request) {
        boolean hasSn = request != null && request.sn() != null && !request.sn().isBlank();
        boolean hasClientSn = request != null && request.client_sn() != null && !request.client_sn().isBlank();
        if (hasSn == hasClientSn) {
            throw new IllegalArgumentException("Exactly one of sn or client_sn is required");
        }
        if (request == null || request.operator() == null || request.operator().isBlank()) {
            throw new IllegalArgumentException("operator is required");
        }
    }
}
