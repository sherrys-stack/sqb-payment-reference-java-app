package com.example.sqb.bootstrap.facade;

import com.example.sqb.adapter.payment.*;
import com.example.sqb.protocol.dto.payment.*;
import com.example.sqb.support.polling.PollingConfig;
import com.example.sqb.support.polling.PollingResult;
import com.example.sqb.support.polling.SqbPollingRunner;
import com.example.sqb.support.status.OrderStatus;

import java.util.Set;

public class SqbPaymentFacade {
    private static final Set<String> BIZ_DEFINITE_FAIL_CODES = Set.of(
            "PAY_FAIL", "PRECREATE_FAIL", "REFUND_FAIL", "CANCEL_ABORT_ERROR", "FAIL");

    private final SqbPayAdapter payAdapter;
    private final SqbQueryAdapter queryAdapter;
    private final SqbPrecreateAdapter precreateAdapter;
    private final SqbRefundAdapter refundAdapter;
    private final SqbCancelAdapter cancelAdapter;
    private final SqbPollingRunner pollingRunner;

    public SqbPaymentFacade(SqbPayAdapter payAdapter, SqbQueryAdapter queryAdapter, SqbPrecreateAdapter precreateAdapter,
                            SqbRefundAdapter refundAdapter, SqbCancelAdapter cancelAdapter, SqbPollingRunner pollingRunner) {
        this.payAdapter = payAdapter;
        this.queryAdapter = queryAdapter;
        this.precreateAdapter = precreateAdapter;
        this.refundAdapter = refundAdapter;
        this.cancelAdapter = cancelAdapter;
        this.pollingRunner = pollingRunner;
    }

    public PollingResult<QueryResponse> payWithAutoPolling(PayRequest payRequest) {
        PayResponse payResponse = payAdapter.pay(payRequest);
        String payBizCode = payResponse != null && payResponse.biz_response() != null ? payResponse.biz_response().result_code() : null;
        String payStatus = payResponse != null && payResponse.biz_response() != null && payResponse.biz_response().data() != null
                ? payResponse.biz_response().data().order_status() : null;
        if (BIZ_DEFINITE_FAIL_CODES.contains(payBizCode) || OrderStatus.fromValue(payStatus).isTerminal()) {
            return new PollingResult<>(true, false, toQueryResponse(payResponse), completionMessage(payStatus, payBizCode));
        }
        return pollingRunner.poll(
                () -> queryAdapter.query(QueryRequest.byClientSn(payRequest.terminal_sn(), payRequest.client_sn())),
                resp -> OrderStatus.fromValue(readOrderStatus(resp)).isTerminal(),
                PollingConfig.payDefault());
    }

    public PollingResult<QueryResponse> precreateWithAutoPolling(PrecreateRequest precreateRequest) {
        PrecreateResponse precreateResponse = precreateAdapter.precreate(precreateRequest);
        String precreateBizCode = precreateResponse != null && precreateResponse.biz_response() != null
                ? precreateResponse.biz_response().result_code() : null;
        String precreateStatus = precreateResponse != null && precreateResponse.biz_response() != null && precreateResponse.biz_response().data() != null
                ? precreateResponse.biz_response().data().order_status() : null;
        if (BIZ_DEFINITE_FAIL_CODES.contains(precreateBizCode) || OrderStatus.fromValue(precreateStatus).isTerminal()) {
            return new PollingResult<>(true, false, toQueryResponse(precreateResponse), completionMessage(precreateStatus, precreateBizCode));
        }
        return pollingRunner.poll(
                () -> queryAdapter.query(QueryRequest.byClientSn(precreateRequest.terminal_sn(), precreateRequest.client_sn())),
                resp -> OrderStatus.fromValue(readOrderStatus(resp)).isTerminal(),
                PollingConfig.precreateDefault());
    }

    public QueryResponse query(QueryRequest request) { return queryAdapter.query(request); }
    public RefundResponse refund(RefundRequest request) { return refundAdapter.refund(request); }
    public CancelResponse cancel(CancelRequest request) { return cancelAdapter.cancel(request); }

    private static String readOrderStatus(QueryResponse response) {
        if (response == null || response.biz_response() == null || response.biz_response().data() == null) {
            return null;
        }
        return response.biz_response().data().order_status();
    }

    private static QueryResponse toQueryResponse(PayResponse payResponse) {
        if (payResponse == null || payResponse.biz_response() == null) {
            return new QueryResponse(null, null);
        }
        PayResponse.BizResponse biz = payResponse.biz_response();
        PayResponse.Data data = biz.data();
        QueryResponse.Data queryData = data == null ? null : new QueryResponse.Data(data.order_status(), data.sn(), data.client_sn());
        return new QueryResponse(payResponse.result_code(), new QueryResponse.BizResponse(biz.result_code(), biz.error_message(), queryData));
    }

    private static QueryResponse toQueryResponse(PrecreateResponse precreateResponse) {
        if (precreateResponse == null || precreateResponse.biz_response() == null) {
            return new QueryResponse(null, null);
        }
        PrecreateResponse.BizResponse biz = precreateResponse.biz_response();
        PrecreateResponse.Data data = biz.data();
        QueryResponse.Data queryData = data == null ? null : new QueryResponse.Data(data.order_status(), data.sn(), data.client_sn());
        return new QueryResponse(precreateResponse.result_code(), new QueryResponse.BizResponse(biz.result_code(), biz.error_message(), queryData));
    }

    private static String normalizeStatus(String status) {
        return status == null || status.isBlank() ? "UNKNOWN" : status;
    }

    private static String completionMessage(String status, String bizCode) {
        if (BIZ_DEFINITE_FAIL_CODES.contains(bizCode)) {
            return "completed:FAILED:" + bizCode;
        }
        OrderStatus orderStatus = OrderStatus.fromValue(status);
        if (orderStatus == OrderStatus.PAY_CANCELED || orderStatus == OrderStatus.PAY_ERROR || orderStatus == OrderStatus.CANCEL_ERROR) {
            return "completed:FAILED:" + normalizeStatus(status);
        }
        return "completed:SUCCESS:" + normalizeStatus(status);
    }
}
