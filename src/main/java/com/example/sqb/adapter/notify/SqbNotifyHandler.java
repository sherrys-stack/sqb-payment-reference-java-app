package com.example.sqb.adapter.notify;

import com.example.sqb.protocol.dto.notify.NotifyPayload;
import com.example.sqb.support.idempotency.SqbNotifyDeduplicator;

public class SqbNotifyHandler {
    private final SqbNotifyDeduplicator deduplicator;

    public SqbNotifyHandler(SqbNotifyDeduplicator deduplicator) {
        this.deduplicator = deduplicator;
    }

    public boolean handle(NotifyPayload payload) {
        if (payload == null || payload.sn() == null) {
            return false;
        }
        return deduplicator.firstSeen(payload.sn());
    }
}
