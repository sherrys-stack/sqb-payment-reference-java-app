package com.example.sqb.support.polling;

public record PollingResult<T>(boolean completed, boolean timeout, T value, String message) {
    public static <T> PollingResult<T> success(T value) {
        return new PollingResult<>(true, false, value, "completed");
    }

    public static <T> PollingResult<T> timedOut(T latestValue) {
        return new PollingResult<>(false, true, latestValue, "timeout");
    }
}
