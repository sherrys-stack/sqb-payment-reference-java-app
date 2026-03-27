package com.example.sqb.bootstrap.config;

import com.example.sqb.adapter.notify.SqbNotifyHandler;
import com.example.sqb.adapter.payment.*;
import com.example.sqb.adapter.terminal.SqbActivateAdapter;
import com.example.sqb.adapter.terminal.SqbCheckinAdapter;
import com.example.sqb.bootstrap.facade.SqbPaymentFacade;
import com.example.sqb.protocol.client.SqbHttpClient;
import com.example.sqb.protocol.security.SqbCallbackVerifier;
import com.example.sqb.support.credential.TerminalCredentialStore;
import com.example.sqb.support.idempotency.SqbNotifyDeduplicator;
import com.example.sqb.support.polling.SqbPollingRunner;
import com.example.sqb.support.status.SqbStatusParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqbComponentConfiguration {
    @Bean
    public SqbHttpClient sqbHttpClient(OkHttpClient okHttpClient, ObjectMapper objectMapper, SqbProperties properties) {
        return new SqbHttpClient(okHttpClient, objectMapper, properties);
    }

    @Bean
    public TerminalCredentialStore terminalCredentialStore() { return new TerminalCredentialStore(); }

    @Bean
    public SqbStatusParser sqbStatusParser() { return new SqbStatusParser(); }

    @Bean
    public SqbPollingRunner sqbPollingRunner() { return new SqbPollingRunner(); }

    @Bean
    public SqbNotifyDeduplicator sqbNotifyDeduplicator() { return new SqbNotifyDeduplicator(); }

    @Bean
    public SqbCallbackVerifier sqbCallbackVerifier() { return new SqbCallbackVerifier(); }

    @Bean
    public SqbActivateAdapter sqbActivateAdapter(SqbHttpClient c, SqbProperties p, TerminalCredentialStore s) { return new SqbActivateAdapter(c, p, s); }

    @Bean
    public SqbCheckinAdapter sqbCheckinAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbCheckinAdapter(c, s); }

    @Bean
    public SqbPayAdapter sqbPayAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbPayAdapter(c, s); }

    @Bean
    public SqbQueryAdapter sqbQueryAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbQueryAdapter(c, s); }

    @Bean
    public SqbPrecreateAdapter sqbPrecreateAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbPrecreateAdapter(c, s); }

    @Bean
    public SqbRefundAdapter sqbRefundAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbRefundAdapter(c, s); }

    @Bean
    public SqbCancelAdapter sqbCancelAdapter(SqbHttpClient c, TerminalCredentialStore s) { return new SqbCancelAdapter(c, s); }

    @Bean
    public SqbNotifyHandler sqbNotifyHandler(SqbNotifyDeduplicator d) { return new SqbNotifyHandler(d); }

    @Bean
    public SqbPaymentFacade sqbPaymentFacade(SqbPayAdapter p, SqbQueryAdapter q, SqbPrecreateAdapter pre,
                                             SqbRefundAdapter r, SqbCancelAdapter c, SqbPollingRunner runner) {
        return new SqbPaymentFacade(p, q, pre, r, c, runner);
    }
}
