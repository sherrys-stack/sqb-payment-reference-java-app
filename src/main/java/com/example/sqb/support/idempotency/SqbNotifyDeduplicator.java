package com.example.sqb.support.idempotency;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SqbNotifyDeduplicator {
    private final Set<String> handledSn = ConcurrentHashMap.newKeySet();

    public boolean firstSeen(String sn) {
        return handledSn.add(sn);
    }
}
