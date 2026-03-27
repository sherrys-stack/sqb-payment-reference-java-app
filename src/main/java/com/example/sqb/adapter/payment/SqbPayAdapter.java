package com.example.sqb.adapter.payment;

import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.payment.PayRequest;
import com.example.sqb.protocol.dto.payment.PayResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbPayAdapter {
    private final SqbHttpClient httpClient;
    private final TerminalCredentialStore credentialStore;

    public SqbPayAdapter(SqbHttpClient httpClient, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.credentialStore = credentialStore;
    }

    public PayResponse pay(PayRequest request) {
        TerminalCredential credential = credentialStore.get().orElseThrow(() -> new IllegalStateException("Terminal credential not initialized"));
        return httpClient.post("/upay/v2/pay", request, credential.terminalSn(), credential.terminalKey(), PayResponse.class);
    }
}
