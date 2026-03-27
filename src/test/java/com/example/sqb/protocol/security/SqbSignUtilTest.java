package com.example.sqb.protocol.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqbSignUtilTest {

    @Test
    void shouldGenerate32LowercaseMd5() {
        String sign = SqbSignUtil.md5Sign("{\"a\":1}", "secret");
        assertEquals(32, sign.length());
        assertTrue(sign.matches("[0-9a-f]{32}"));
    }

    @Test
    void shouldBuildAuthorization() {
        String auth = SqbSignUtil.buildAuthorization("sn001", "{}", "key");
        assertTrue(auth.startsWith("sn001 "));
    }
}
