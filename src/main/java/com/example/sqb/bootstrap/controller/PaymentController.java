package com.example.sqb.bootstrap.controller;

import com.example.sqb.bootstrap.facade.SqbPaymentFacade;
import com.example.sqb.protocol.dto.payment.*;
import com.example.sqb.support.polling.PollingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    private final SqbPaymentFacade paymentFacade;

    public PaymentController(SqbPaymentFacade paymentFacade) {
        this.paymentFacade = paymentFacade;
    }

    /**
     * ⚠️ 收钱吧无沙盒环境，以下接口为真实交易，请勿在生产凭证上做无意义测试。
     */
    @PostMapping("/pay")
    public PollingResult<QueryResponse> pay(@RequestBody PayRequest request) {
        return paymentFacade.payWithAutoPolling(request);
    }

    @PostMapping("/precreate")
    public PollingResult<QueryResponse> precreate(@RequestBody PrecreateRequest request) {
        return paymentFacade.precreateWithAutoPolling(request);
    }

    @PostMapping("/query")
    public QueryResponse query(@RequestBody QueryRequest request) {
        return paymentFacade.query(request);
    }

    @PostMapping("/refund")
    public RefundResponse refund(@RequestBody RefundRequest request) {
        return paymentFacade.refund(request);
    }

    @PostMapping("/cancel")
    public CancelResponse cancel(@RequestBody CancelRequest request) {
        return paymentFacade.cancel(request);
    }
}
