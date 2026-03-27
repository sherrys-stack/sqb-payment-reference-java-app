package com.example.sqb.bootstrap.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sqb")
public class SqbProperties {
    private String apiBase;
    private String vendorSn;
    private String vendorKey;
    private String terminalSn;
    private String terminalKey;
    private String deviceId;
    private String publicKeyPath;

    public String getApiBase() { return apiBase; }
    public void setApiBase(String apiBase) { this.apiBase = apiBase; }
    public String getVendorSn() { return vendorSn; }
    public void setVendorSn(String vendorSn) { this.vendorSn = vendorSn; }
    public String getVendorKey() { return vendorKey; }
    public void setVendorKey(String vendorKey) { this.vendorKey = vendorKey; }
    public String getTerminalSn() { return terminalSn; }
    public void setTerminalSn(String terminalSn) { this.terminalSn = terminalSn; }
    public String getTerminalKey() { return terminalKey; }
    public void setTerminalKey(String terminalKey) { this.terminalKey = terminalKey; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getPublicKeyPath() { return publicKeyPath; }
    public void setPublicKeyPath(String publicKeyPath) { this.publicKeyPath = publicKeyPath; }
}
