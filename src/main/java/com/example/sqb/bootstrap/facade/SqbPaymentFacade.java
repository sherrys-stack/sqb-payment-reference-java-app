package com.example.sqb.bootstrap.facade;

import com.example.sqb.adapter.payment.*;
import com.example.sqb.protocol.dto.payment.*;
import com.example.sqb.support.polling.PollingConfig;
import com.example.sqb.support.polling.PollingResult;
import com.example.sqb.support.polling.SqbPollingRunner;
import com.example.sqb.support.status.OrderStatus;

public class SqbPaymentFacade {
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
        String status = payResponse != null && payResponse.biz_response() != null ? payResponse.biz_response().order_status() : null;
        if (OrderStatus.fromValue(status).isTerminal()) {
            return PollingResult.success(new QueryResponse("200", new QueryResponse.BizResponse("200", status,
                    payResponse.biz_response() == null ? null : payResponse.biz_response().order_sn(), null)));
        }
        return pollingRunner.poll(
                () -> queryAdapter.query(new QueryRequest(payRequest.terminal_sn(), payRequest.client_sn())),
                resp -> OrderStatus.fromValue(resp.biz_response() == null ? null : resp.biz_response().order_status()).isTerminal(),
                PollingConfig.payDefault());
    }

    public PollingResult<QueryResponse> precreateWithAutoPolling(PrecreateRequest precreateRequest) {
        precreateAdapter.precreate(precreateRequest);
        return pollingRunner.poll(
                () -> queryAdapter.query(new QueryRequest(precreateRequest.terminal_sn(), precreateRequest.client_sn())),
                resp -> OrderStatus.fromValue(resp.biz_response() == null ? null : resp.biz_response().order_status()).isTerminal(),
                PollingConfig.precreateDefault());
    }

    public QueryResponse query(QueryRequest request) { return queryAdapter.query(request); }
    public RefundResponse refund(RefundRequest request) { return refundAdapter.refund(request); }
    public CancelResponse cancel(CancelRequest request) { return cancelAdapter.cancel(request); }
}
