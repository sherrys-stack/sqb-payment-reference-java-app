package com.example.sqb.protocol.security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SqbCallbackVerifier {

    public boolean verify(String authorizationHeader, byte[] body, String pemPublicKey) {
        try {
            String signatureValue = extractSignature(authorizationHeader);
            PublicKey publicKey = parsePublicKey(pemPublicKey);
            Signature verifier = Signature.getInstance("SHA256withRSA");
            verifier.initVerify(publicKey);
            verifier.update(body);
            return verifier.verify(Base64.getDecoder().decode(signatureValue));
        } catch (Exception e) {
            return false;
        }
    }

    private String extractSignature(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new IllegalArgumentException("Authorization header missing");
        }
        String[] parts = authorizationHeader.trim().split("\\s+");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid authorization format");
        }
        return parts[1];
    }

    private PublicKey parsePublicKey(String pem) throws Exception {
        String normalized = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(normalized.getBytes(StandardCharsets.UTF_8));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
