package com.example.sqb.protocol.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqbCallbackVerifierTest {

    @Test
    void shouldVerifySha256WithRsaSignature() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        byte[] body = "{\"sn\":\"n001\"}".getBytes(StandardCharsets.UTF_8);
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(keyPair.getPrivate());
        signer.update(body);
        String sig = Base64.getEncoder().encodeToString(signer.sign());

        String pem = "-----BEGIN PUBLIC KEY-----\n" + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()) + "\n-----END PUBLIC KEY-----";
        SqbCallbackVerifier verifier = new SqbCallbackVerifier();

        assertTrue(verifier.verify("sqb " + sig, body, pem));
        assertFalse(verifier.verify("sqb bad", body, pem));
    }
}
