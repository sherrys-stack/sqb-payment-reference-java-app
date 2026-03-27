package com.example.sqb.adapter.terminal;

import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.dto.terminal.CheckinRequest;
import com.example.sqb.protocol.dto.terminal.CheckinResponse;
import com.example.sqb.support.credential.TerminalCredential;
import com.example.sqb.support.credential.TerminalCredentialStore;

public class SqbCheckinAdapter {
    private final SqbHttpClient httpClient;
    private final TerminalCredentialStore credentialStore;

    public SqbCheckinAdapter(SqbHttpClient httpClient, TerminalCredentialStore credentialStore) {
        this.httpClient = httpClient;
        this.credentialStore = credentialStore;
    }

    public CheckinResponse checkin(CheckinRequest request) {
        TerminalCredential credential = credentialStore.get().orElseThrow(() -> new IllegalStateException("Terminal credential not initialized"));
        CheckinResponse response = httpClient.post("/terminal/checkin", request,
                credential.terminalSn(), credential.terminalKey(), CheckinResponse.class);
        if (response != null && response.biz_response() != null && "200".equals(response.biz_response().result_code())) {
            credentialStore.rotateKey(response.biz_response().terminal_key());
        }
        return response;
    }
}
