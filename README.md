# 收钱吧支付参考应用（Skills 驱动生成）

> ⚠️ **重要警告：收钱吧无沙盒环境，所有交易都是真实扣款。测试后请及时退款，避免资金损失。**

## 项目简介
这是一个通过 **自然语言指令 + Skills 技能包** 逐步生成的 Spring Boot 参考实现，目标是演示可复用的支付集成生成流程，而不是仅交付一次性的手写代码。

## 5 分钟快速开始
1. 准备 Java 17 + Maven。
2. 复制 `src/main/resources/application-example.yml` 到本地配置并填入真实凭证。
3. 启动：
   ```bash
   mvn spring-boot:run
   ```
4. 访问 `http://localhost:8080`。

## REST API 示例（curl）
```bash
# 激活
curl -X POST http://localhost:8080/api/terminal/activate \
  -H 'Content-Type: application/json' \
  -d '{"app_id":"2021001","code":"ACTIVATE_CODE","device_id":"DEVICE_001"}'

# 签到
curl -X POST http://localhost:8080/api/terminal/checkin \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"TERMINAL_SN","device_id":"DEVICE_001"}'

# 付款码支付（B扫C）
curl -X POST http://localhost:8080/api/payment/pay \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"T1","client_sn":"C202603270001","total_amount":"1","dynamic_id":"1345...","subject":"test","operator":"op01"}'

# 查询
curl -X POST http://localhost:8080/api/payment/query \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"T1","client_sn":"C202603270001"}'

# 预下单（C扫B）
curl -X POST http://localhost:8080/api/payment/precreate \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"T1","client_sn":"C202603270002","total_amount":"1","payway":"3","subject":"test","operator":"op01"}'

# 退款
curl -X POST http://localhost:8080/api/payment/refund \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"T1","client_sn":"C202603270001","refund_request_no":"R20260327001","refund_amount":"1"}'

# 撤单
curl -X POST http://localhost:8080/api/payment/cancel \
  -H 'Content-Type: application/json' \
  -d '{"terminal_sn":"T1","client_sn":"C202603270001"}'

# 异步回调
curl -X POST http://localhost:8080/api/notify \
  -H 'Authorization: sqb <base64-signature>' \
  -H 'Content-Type: application/json' \
  -d '{"sn":"N001","order_sn":"SQB123","client_sn":"C1","order_status":"PAID","total_amount":"1"}'
```

## 4 层架构
- `protocol`：协议对象、签名/验签、HTTP client
- `adapter`：各支付能力的 API 适配
- `support`：状态解析、轮询、幂等、凭证存储
- `bootstrap`：配置、控制器、编排门面

## 包结构图
```text
com.example.sqb
├── protocol
├── adapter
├── support
└── bootstrap
```

## 生产上线检查清单
- [ ] 所有凭证使用安全存储（KMS/密钥服务）而非明文。
- [ ] 回调验签失败立即告警并拒绝。
- [ ] 回调处理具备幂等与重放防护。
- [ ] 支付/预下单轮询超时后有人工兜底流程。
- [ ] 交易金额全部使用“分”的字符串，禁止浮点换算。
- [ ] 测试交易完成后执行退款。

## Skills 仓库
- `sqb-payment-skills`（请替换为你们的真实仓库地址）
