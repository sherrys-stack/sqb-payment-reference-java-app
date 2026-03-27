package com.example.sqb.protocol.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public final class SqbSignUtil {
    private SqbSignUtil() {}

    public static String md5Sign(String requestBody, String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest((requestBody + key).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("MD5 signing failed", e);
        }
    }

    public static String buildAuthorization(String sn, String requestBody, String key) {
        return sn + " " + md5Sign(requestBody, key);
    }
}
