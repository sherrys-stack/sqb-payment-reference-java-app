package com.example.sqb.adapter.terminal;

import com.example.sqb.bootstrap.config.SqbProperties;
import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.terminal.ActivateRequest;
import com.example.sqb.protocol.dto.terminal.ActivateResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbActivateAdapter {
    private final SqbHttpClient httpClient;
    private final SqbProperties properties;
    private final TerminalCredentialStore credentialStore;

    public SqbActivateAdapter(SqbHttpClient httpClient, SqbProperties properties, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.properties = properties;
        this.credentialStore = credentialStore;
    }

    public ActivateResponse activate(ActivateRequest request) {
        ActivateResponse response = httpClient.post("/terminal/activate", request,
                properties.getVendorSn(), properties.getVendorKey(), ActivateResponse.class);
        if (response != null && response.biz_response() != null && "200".equals(response.biz_response().result_code())) {
            credentialStore.save(new TerminalCredential(response.biz_response().terminal_sn(), response.biz_response().terminal_key()));
        }
        return response;
    }
}
