package com.example.sqb.support.polling;

import java.time.Duration;

public record PollingConfig(Duration fastInterval, Duration slowInterval, Duration fastPhase, Duration timeout) {
    public static PollingConfig payDefault() {
        return new PollingConfig(Duration.ofSeconds(3), Duration.ofSeconds(10), Duration.ofSeconds(60), Duration.ofSeconds(120));
    }

    public static PollingConfig precreateDefault() {
        return new PollingConfig(Duration.ofSeconds(2), Duration.ofSeconds(5), Duration.ofSeconds(30), Duration.ofSeconds(240));
    }
}
