package com.example.sqb.support.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SqbStatusParserTest {
    private final SqbStatusParser parser = new SqbStatusParser();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldParseTerminalPaidStatus() throws Exception {
        String json = """
                {"result_code":"200","biz_response":{"result_code":"PAY_SUCCESS","data":{"order_status":"PAID"}}}
                """;
        ParsedResult result = parser.parse(mapper.readTree(json));
        assertTrue(result.communicationSuccess());
        assertTrue(result.businessSuccess());
        assertEquals(OrderStatus.PAID, result.orderStatus());
        assertTrue(result.terminal());
    }

    @Test
    void shouldParseNonTerminalCreatedStatus() throws Exception {
        String json = """
                {"result_code":"200","biz_response":{"result_code":"PAY_SUCCESS","data":{"order_status":"CREATED"}}}
                """;
        ParsedResult result = parser.parse(mapper.readTree(json));
        assertFalse(result.terminal());
        assertEquals(OrderStatus.CREATED, result.orderStatus());
    }

    @Test
    void shouldHandlePendingBizResultCode() throws Exception {
        String json = """
                {"result_code":"200","biz_response":{"result_code":"PAY_IN_PROGRESS"}}
                """;
        ParsedResult result = parser.parse(mapper.readTree(json));
        assertTrue(result.communicationSuccess());
        assertFalse(result.businessSuccess());
        assertFalse(result.terminal());
    }

    @Test
    void shouldHandleCommunicationFailure() throws Exception {
        String json = """
                {"result_code":"400","error_message":"network error"}
                """;
        ParsedResult result = parser.parse(mapper.readTree(json));
        assertFalse(result.communicationSuccess());
        assertTrue(result.terminal());
    }
}
