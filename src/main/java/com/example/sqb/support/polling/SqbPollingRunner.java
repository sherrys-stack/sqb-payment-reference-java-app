package com.example.sqb.support.polling;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SqbPollingRunner {

    public <T> PollingResult<T> poll(Supplier<T> querySupplier, Predicate<T> finished, PollingConfig config) {
        Instant start = Instant.now();
        T latest = null;
        while (Duration.between(start, Instant.now()).compareTo(config.timeout()) < 0) {
            latest = querySupplier.get();
            if (finished.test(latest)) {
                return PollingResult.success(latest);
            }

            Duration elapsed = Duration.between(start, Instant.now());
            Duration sleepFor = elapsed.compareTo(config.fastPhase()) < 0 ? config.fastInterval() : config.slowInterval();
            try {
                Thread.sleep(sleepFor.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new PollingResult<>(false, false, latest, "interrupted");
            }
        }
        return PollingResult.timedOut(latest);
    }
}
