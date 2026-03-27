package com.example.sqb.protocol.client;

import com.example.sqb.bootstrap.config.SqbProperties;
import com.example.sqb.protocol.security.SqbSignUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class SqbHttpClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;
    private final SqbProperties properties;

    public SqbHttpClient(OkHttpClient okHttpClient, ObjectMapper objectMapper, SqbProperties properties) {
        this.okHttpClient = okHttpClient;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    public <T> T post(String path, Object requestDto, String sn, String key, Class<T> responseType) {
        try {
            String jsonBody = objectMapper.writeValueAsString(requestDto);
            Request request = new Request.Builder()
                    .url(properties.getApiBase() + path)
                    .addHeader("Authorization", SqbSignUtil.buildAuthorization(sn, jsonBody, key))
                    .post(RequestBody.create(jsonBody, JSON))
                    .build();
            try (Response response = okHttpClient.newCall(request).execute()) {
                if (response.body() == null) {
                    throw new IllegalStateException("Empty response body");
                }
                return objectMapper.readValue(response.body().string(), responseType);
            }
        } catch (IOException e) {
            throw new IllegalStateException("SQB HTTP request failed", e);
        }
    }
}
