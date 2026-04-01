package com.example.sqb.support.status;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Set;

public class SqbStatusParser {
    private static final Set<String> BIZ_DEFINITE_FAIL_CODES = Set.of(
            "PAY_FAIL", "PRECREATE_FAIL", "REFUND_FAIL", "CANCEL_ABORT_ERROR", "FAIL");
    private static final Set<String> BIZ_UNCERTAIN_CODES = Set.of(
            "PAY_FAIL_ERROR", "PAY_IN_PROGRESS", "REFUND_IN_PROGRESS", "REFUND_FAIL_ERROR", "CANCEL_ERROR");

    public ParsedResult parse(JsonNode root) {
        String comm = text(root, "result_code");
        if (!"200".equals(comm)) {
            return new ParsedResult(false, false, OrderStatus.UNKNOWN, true,
                    "communication failed: " + text(root, "error_message"));
        }

        JsonNode biz = root.path("biz_response");
        String bizCode = text(biz, "result_code");
        if (BIZ_DEFINITE_FAIL_CODES.contains(bizCode)) {
            return new ParsedResult(true, false, OrderStatus.UNKNOWN, true,
                    "business failed: " + text(biz, "error_message"));
        }
        if (BIZ_UNCERTAIN_CODES.contains(bizCode)) {
            return new ParsedResult(true, false, OrderStatus.UNKNOWN, false,
                    "business pending: " + bizCode);
        }

        JsonNode data = biz.path("data");
        String orderStatusText = text(data, "order_status");
        if (orderStatusText == null) {
            // Compatible with flattened response bodies.
            orderStatusText = text(biz, "order_status");
        }
        OrderStatus orderStatus = OrderStatus.fromValue(orderStatusText);
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
