# 邮件模块接口文档

## 通用说明

### 接口认证
- 所有接口都需要登录认证
- 在请求头中需要携带 token：`Authorization: Bearer {token}`

### 响应格式
```json
{
    "code": 0,           // 响应码：0-成功，非0-失败
    "message": "success", // 响应消息
    "data": {}           // 响应数据
}
```

### 通用错误码
| 错误码 | 说明 |
|--------|------|
| 401    | 未登录或token已过期 |
| 403    | 没有权限 |
| 404    | 资源不存在 |
| 500    | 服务器内部错误 |

## 营销邮件模板接口

### 1. 创建邮件模板
- 请求路径：`POST /api/v1/marketing/template/createTemplate`
- 请求参数：
```json
{
    "templateName": "string",     // 可选，模板名称，不传则自动生成
    "contentType": 1,            // 必填，内容类型：1-文本，2-HTML
    "contentTemplate": "string", // 必填，模板内容
    "inputContent": "string",    // 必填，输入内容
    "aiStrategyVersion": "string", // 可选，AI策略版本
    "extendsInfo": {}           // 可选，扩展信息
}
```
- 响应数据：
```json
{
    "templateId": "string"      // 模板ID
}
```

### 2. 修改邮件模板
- 请求路径：`POST /api/v1/marketing/template/modifyTemplate`
- 请求参数：
```json
{
    "templateId": "string",      // 必填，模板ID
    "templateName": "string",    // 必填，模板名称
    "contentType": 1,           // 必填，内容类型：1-文本，2-HTML
    "contentTemplate": "string", // 必填，模板内容
    "inputContent": "string",    // 必填，输入内容
    "enableStatus": 1,          // 可选，启用状态：1-启用，0-禁用
    "aiStrategyVersion": "string", // 可选，AI策略版本
    "extendsInfo": {}           // 可选，扩展信息
}
```
- 响应数据：同创建接口

### 3. 删除邮件模板
- 请求路径：`POST /api/v1/marketing/template/deleteTemplate`
- 请求参数：
```json
{
    "templateId": "string"  // 必填，模板ID
}
```
- 响应数据：无

### 4. 获取模板详情
- 请求路径：`POST /api/v1/marketing/template/getTemplateDetail`
- 请求参数：
```json
{
    "templateId": "string"  // 必填，模板ID
}
```
- 响应数据：
```json
{
    "orgCode": "string",         // 组织编码
    "templateId": "string",      // 模板ID
    "templateName": "string",    // 模板名称
    "contentType": 1,           // 内容类型：1-文本，2-HTML
    "contentTemplate": "string", // 模板内容
    "inputContent": "string",    // 输入内容
    "aiStrategyVersion": "string", // AI策略版本
    "enableStatus": 1,          // 启用状态：1-启用，0-禁用
    "extendsInfo": {},          // 扩展信息
    "createdAt": "2024-01-01T00:00:00", // 创建时间
    "updatedAt": "2024-01-01T00:00:00", // 更新时间
    "createdBy": "string",      // 创建人ID
    "updatedBy": "string"       // 更新人ID
}
```

### 5. 分页查询模板列表
- 请求路径：`POST /api/v1/marketing/template/pageTemplates`
- 请求参数：
```json
{
    "orgCode": "string",        // 可选，组织编码，没有取登录用户的绑定的组织
    "templateId": "string",     // 可选，模板ID
    "templateName": "string",   // 可选，模板名称
    "contentType": 1,          // 可选，内容类型
    "enableStatusList": [1],   // 可选，启用状态列表
    "pageNum": 1,             // 必填，页码
    "pageSize": 10           // 必填，每页大小
}
```
- 响应数据：
```json
{
    "total": 100,           // 总记录数
    "pages": 10,           // 总页数
    "content": []          // 数据列表，元素同详情接口
}
```

## 邮件发送规则接口

### 1. 创建发送规则
- 请求路径：`POST /api/v1/email/rules/createRule`
- 请求参数：
```json
{
    "ruleName": "string",       // 可选，规则名称，不传则自动生成
    "templateId": "string",     // 必填，模板ID
    "ruleType": 1,             // 必填，规则类型：1-CRON，2-固定频率，3-固定延迟
    "cronExpression": "string", // 条件必填，CRON表达式（规则类型为1时必填）
    "fixedRate": 3600,         // 条件必填，固定频率，单位：秒（规则类型为2时必填）
    "fixedDelay": 3600,        // 条件必填，固定延迟，单位：秒（规则类型为3时必填）
    "recipients": ["string"],   // 必填，收件人列表
    "cc": ["string"],          // 可选，抄送列表
    "bcc": ["string"],         // 可选，密送列表
    "startTime": "2024-01-01T00:00:00", // 可选，生效开始时间
    "endTime": "2024-12-31T23:59:59",   // 可选，生效结束时间
    "maxExecutions": 100       // 可选，最大执行次数
}
```
- 响应数据：
```json
{
    "ruleId": "string"      // 规则ID
}
```

### 2. 修改发送规则
- 请求路径：`POST /api/v1/email/rules/modifyRule`
- 请求参数：
```json
{
    "ruleId": "string",        // 必填，规则ID
    "ruleName": "string",      // 必填，规则名称
    "templateId": "string",    // 必填，模板ID
    "ruleType": 1,            // 必填，规则类型（本次立即发送1就行）
    "cronExpression": "string", // 条件必填，同创建
    "fixedRate": 3600,        // 条件必填，同创建
    "fixedDelay": 3600,       // 条件必填，同创建
    "recipients": ["string"],  // 必填，收件人列表
    "cc": ["string"],         // 可选，抄送列表
    "bcc": ["string"],        // 可选，密送列表
    "startTime": "2024-01-01T00:00:00", // 可选，生效开始时间
    "endTime": "2024-12-31T23:59:59",   // 可选，生效结束时间
    "maxExecutions": 100,     // 可选，最大执行次数
    "enableStatus": 1         // 可选，启用状态：1-启用，0-禁用
}
```
- 响应数据：同创建接口

### 3. 删除发送规则
- 请求路径：`POST /api/v1/email/rules/deleteRule`
- 请求参数：
```json
{
    "ruleId": "string"  // 必填，规则ID
}
```
- 响应数据：无

### 4. 获取规则详情
- 请求路径：`POST /api/v1/email/rules/getRuleDetail`
- 请求参数：
```json
{
    "ruleId": "string"  // 必填，规则ID
}
```
- 响应数据：
```json
{
    "ruleId": "string",        // 规则ID
    "orgCode": "string",       // 组织编码
    "ruleName": "string",      // 规则名称
    "templateId": "string",    // 模板ID
    "ruleType": 1,            // 规则类型（本次立即发送1就行）
    "cronExpression": "string", // CRON表达式
    "fixedRate": 3600,        // 固定频率，单位：秒
    "fixedDelay": 3600,       // 固定延迟，单位：秒
    "recipients": ["string"],  // 收件人列表
    "cc": ["string"],         // 抄送列表
    "bcc": ["string"],        // 密送列表
    "startTime": "2024-01-01T00:00:00", // 生效开始时间
    "endTime": "2024-12-31T23:59:59",   // 生效结束时间
    "maxExecutions": 100,     // 最大执行次数
    "executionCount": 0,      // 已执行次数
    "lastExecutionTime": "2024-01-01T00:00:00", // 上次执行时间
    "nextExecutionTime": "2024-01-01T01:00:00", // 下次执行时间
    "enableStatus": 1,        // 启用状态：1-启用，0-禁用
    "createdAt": "2024-01-01T00:00:00", // 创建时间
    "updatedAt": "2024-01-01T00:00:00", // 更新时间
    "createdBy": "string",    // 创建人ID
    "updatedBy": "string"     // 更新人ID
}
```

### 5. 分页查询规则列表
- 请求路径：`POST /api/v1/email/rules/pageRules`
- 请求参数：
```json
{
    "ruleName": "string",      // 可选，规则名称
    "templateId": "string",    // 可选，模板ID
    "ruleType": 1,            // 可选，规则类型
    "recipientType": 1,       // 可选，收件人类型
    "enableStatusList": [1],   // 可选，启用状态列表：1-启用，0-禁用
    "pageNum": 1,             // 必填，页码
    "pageSize": 10            // 必填，每页大小
}
```
- 响应数据：
```json
{
    "total": 100,           // 总记录数
    "pages": 10,           // 总页数
    "content": []          // 数据列表，元素同详情
}
```

### 6. 手动触发立即执行
- 请求路径：`POST /api/v1/email/rules/executeRule`
- 请求参数：
```json
{
    "ruleId": "string"  // 必填，规则ID
}
```
- 响应数据：无 