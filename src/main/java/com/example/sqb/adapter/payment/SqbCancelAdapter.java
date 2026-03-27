package com.example.sqb.adapter.payment;

import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.payment.CancelRequest;
import com.example.sqb.protocol.dto.payment.CancelResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbCancelAdapter {
    private final SqbHttpClient httpClient;
    private final TerminalCredentialStore credentialStore;

    public SqbCancelAdapter(SqbHttpClient httpClient, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.credentialStore = credentialStore;
    }

    public CancelResponse cancel(CancelRequest request) {
        TerminalCredential credential = credentialStore.get().orElseThrow(() -> new IllegalStateException("Terminal credential not initialized"));
        return httpClient.post("/upay/v2/cancel", request, credential.terminalSn(), credential.terminalKey(), CancelResponse.class);
    }
}
