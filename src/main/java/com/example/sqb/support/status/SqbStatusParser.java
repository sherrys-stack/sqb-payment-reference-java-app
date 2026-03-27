package com.example.sqb.support.status;

import com.fasterxml.jackson.databind.JsonNode;

public class SqbStatusParser {

    public ParsedResult parse(JsonNode root) {
        String comm = text(root, "result_code");
        if (!"200".equals(comm)) {
            return new ParsedResult(false, false, OrderStatus.UNKNOWN, true,
                    "communication failed: " + text(root, "error_message"));
        }

        JsonNode biz = root.path("biz_response");
        String bizCode = text(biz, "result_code");
        if (!"200".equals(bizCode)) {
            return new ParsedResult(true, false, OrderStatus.UNKNOWN, true,
                    "business failed: " + text(biz, "error_message"));
        }

        OrderStatus orderStatus = OrderStatus.fromValue(text(biz, "order_status"));
        return new ParsedResult(true, true, orderStatus, orderStatus.isTerminal(), "ok");
    }

    private String text(JsonNode node, String field) {
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() ? null : value.asText();
    }
}
