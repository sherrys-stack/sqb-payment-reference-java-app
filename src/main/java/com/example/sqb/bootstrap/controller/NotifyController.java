package com.example.sqb.bootstrap.controller;

import com.example.sqb.adapter.notify.SqbNotifyHandler;
import com.example.sqb.bootstrap.config.SqbProperties;
import com.example.sqb.protocol.dto.notify.NotifyPayload;
import com.example.sqb.protocol.security.SqbCallbackVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
public class NotifyController {
    private final SqbCallbackVerifier verifier;
    private final SqbNotifyHandler handler;
    private final SqbProperties properties;
    private final ObjectMapper objectMapper;

    public NotifyController(SqbCallbackVerifier verifier, SqbNotifyHandler handler, SqbProperties properties, ObjectMapper objectMapper) {
        this.verifier = verifier;
        this.handler = handler;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/notify", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> notify(@RequestHeader(value = "Authorization", required = false) String authorization,
                                         @RequestBody byte[] rawBody) throws IOException {
        String publicKey = Files.readString(Paths.get(properties.getPublicKeyPath()));
        if (!verifier.verify(authorization, rawBody, publicKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid signature");
        }

        NotifyPayload payload = objectMapper.readValue(rawBody, NotifyPayload.class);
        handler.handle(payload);
        return ResponseEntity.ok("success");
    }
}
