# GENERATION_GUIDE

本文档记录了“收钱吧支付参考应用”通过**自然语言 Prompt + Skills 工作流**逐步生成的过程。

## 总体说明
- 项目目标：演示如何用技能包驱动 AI 生成收钱吧支付接入代码。
- 生成策略：先搭骨架，再按模块逐步补齐，每步都执行 `mvn compile` / `mvn test`。
- 架构：`protocol -> adapter -> support -> bootstrap` 四层。

## Step 0：项目骨架
- Skill：无（基础工程初始化）
- Prompt：创建 Spring Boot 3.2 + Java 17 + Maven 工程，配置 OkHttp/Jackson 与配置类。
- 结果：生成 `pom.xml`、启动类、配置绑定、自动配置、`application.yml`。
- 设计决策：所有凭证项支持环境变量注入。

## Step 1：签名模块（sqb-signing）
- Skill 路径：`sqb-api-skills/sqb-signing/SKILL.md`
- Prompt 关键词：`MD5 签名 / 请求签名 / signing`
- 结果：`SqbSignUtil` 提供 `md5Sign` 与 `buildAuthorization`。
- 决策：签名统一执行 `MD5(body + key)` 并输出 32 位小写 hex。

## Step 2：状态判定（sqb-status-parsing）
- Skill 路径：`sqb-api-skills/sqb-status-parsing/SKILL.md`
- Prompt 关键词：`三层判定 / 状态解析`
- 结果：`SqbStatusParser`、`ParsedResult(record)`、`OrderStatus(enum)`。
- 决策：终态包含 `PAID/PAY_CANCELED/REFUNDED`，其余可轮询。

## Step 3：终端激活（sqb-activate）
- Skill 路径：`sqb-api-skills/sqb-activate/SKILL.md`
- 结果：`ActivateRequest/Response`、`SqbActivateAdapter`、`TerminalCredentialStore`、`TerminalController.activate`。
- 决策：激活必须使用 vendor 凭证签名。

## Step 4：终端签到（sqb-checkin）
- Skill 路径：`sqb-api-skills/sqb-checkin/SKILL.md`
- 结果：`CheckinRequest/Response`、`SqbCheckinAdapter`、`TerminalController.checkin`。
- 决策：签到成功后原子更新 terminal key。

## Step 5：B 扫 C 付款码支付（sqb-pay）
- Skill 路径：`sqb-api-skills/sqb-pay/SKILL.md`
- 结果：支付/查询 DTO 与 Adapter、轮询模块、`SqbPaymentFacade`、`PaymentController`。
- 决策：默认轮询策略 `60s 内 3s`，之后 `10s`，最长 `120s`。

## Step 6：预下单 + 退款 + 撤单（sqb-precreate/sqb-refund/sqb-cancel）
- Skill 路径：
  - `sqb-api-skills/sqb-precreate/SKILL.md`
  - `sqb-api-skills/sqb-refund/SKILL.md`
  - `sqb-api-skills/sqb-cancel/SKILL.md`
- 结果：新增 DTO/Adapter，并扩展 Facade/Controller。
- 决策：预下单默认轮询策略 `30s 内 2s`，之后 `5s`，最长 `240s`。

## Step 7：异步回调（sqb-notify + sqb-callback-verify）
- Skill 路径：
  - `sqb-api-skills/sqb-notify/SKILL.md`
  - `sqb-api-skills/sqb-callback-verify/SKILL.md`
- 结果：`SqbCallbackVerifier`、`NotifyPayload`、`SqbNotifyDeduplicator`、`SqbNotifyHandler`、`NotifyController`。
- 决策：先验签后处理，失败立即拒绝；成功返回纯文本 `success`。

## Step 8：文档收尾
- 结果：完善 README（风险提示、架构、端点、上线检查清单）。

## 迭代建议
1. 按同样步骤将本项目拆分成你自己的业务子域 Prompt 模板。
2. 每个 Skill 只让 AI 生成“一个明确模块”，并立即编译测试。
3. 将每次 Prompt 与改动摘要沉淀到 `GENERATION_GUIDE.md`，方便复现。
