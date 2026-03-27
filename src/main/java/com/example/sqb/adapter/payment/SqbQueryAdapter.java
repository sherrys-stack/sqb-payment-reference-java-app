package com.example.sqb.adapter.payment;

import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.payment.QueryRequest;
import com.example.sqb.protocol.dto.payment.QueryResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbQueryAdapter {
    private final SqbHttpClient httpClient;
    private final TerminalCredentialStore credentialStore;

    public SqbQueryAdapter(SqbHttpClient httpClient, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.credentialStore = credentialStore;
    }

    public QueryResponse query(QueryRequest request) {
        TerminalCredential credential = credentialStore.get().orElseThrow(() -> new IllegalStateException("Terminal credential not initialized"));
        return httpClient.post("/upay/v2/query", request, credential.terminalSn(), credential.terminalKey(), QueryResponse.class);
    }
}
