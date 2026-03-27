package com.example.sqb.support.credential;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class TerminalCredentialStore {
    private final AtomicReference<TerminalCredential> credentialRef = new AtomicReference<>();

    public Optional<TerminalCredential> get() {
        return Optional.ofNullable(credentialRef.get());
    }

    public void save(TerminalCredential credential) {
        credentialRef.set(credential);
    }

    public void rotateKey(String newTerminalKey) {
        credentialRef.updateAndGet(old -> {
            if (old == null) {
                return null;
            }
            return new TerminalCredential(old.terminalSn(), newTerminalKey);
        });
    }
}
