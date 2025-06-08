# 用户模块接口文档

## 通用说明

### 接口认证
- 除了登录、注册等公开接口外，其他接口都需要登录认证
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

## 认证接口

### 1. 获取RSA公钥
- 请求路径：`GET /api/v1/auth/public-key`
- 请求参数：无
- 响应数据：
```json
{
    "data": "string"  // RSA公钥字符串
}
```

### 2. 用户登录（其他方式登录默认注册）
- 请求路径：`POST /api/v1/auth/login`
- 请求参数：
```json
{
    "authType": 1,              // 必填，认证类型：1-密码登录，2-Google OAuth，3-其他OAuth
    "userUniq": "string",       // 条件必填，用户名/邮箱/手机号（密码登录时必填）
    "password": "string",       // 条件必填，RSA加密后的密码（密码登录时必填）
    "oauthUserId": "string",    // 条件必填，OAuth用户ID（OAuth登录时必填）
    "oauthAccessToken": "string", // 条件必填，OAuth访问令牌（OAuth登录时必填）
    "email": "string",          // 可选，邮箱登录时使用
    "phone": "string"           // 可选，手机号登录时使用
}
```
- 响应数据：
```json
{
    "token": "string",           // JWT令牌
    "userInfo": {
        "systemUserId": 0,       // 系统用户ID
        "userUniq": "string",    // 用户唯一标识
        "nickname": "string",    // 用户昵称
        "primaryEmail": "string", // 主邮箱
        "phone": "string",       // 手机号
        "avatarUrl": "string",   // 头像URL
        "languagePreference": "string", // 语言偏好
        "timezone": "string",    // 时区
        "authType": 1,          // 认证类型
        "orgId": "string",      // 组织ID
        "emailVerified": true,  // 邮箱是否验证
        "phoneVerified": true   // 手机是否验证
    }
}
```

### 3. 验证令牌(服务间调用不是给前端的)
- 请求路径：`POST /api/v1/auth/validate`
- 请求参数：
```json
{
    "token": "string"  // 必填，JWT令牌
}
```
- 响应数据：
```json
{
    "valid": true,           // 是否有效
    "userId": "string",      // 用户ID（令牌有效时返回）
    "userUniq": "string",    // 用户唯一标识（令牌有效时返回）
    "errorMessage": "string" // 错误信息（令牌无效时返回）
}
```

## 用户管理接口

### 1. 用户注册（注册成功自动登录）
- 请求路径：`POST /api/v1/users/register`
- 请求参数：
```json
{
    "userUniq": "string",          // 可选，用户登录名（不提供则使用系统生成的ID）
    "password": "string",          // 条件必填，RSA加密后的密码（密码注册时必填）
    "authType": 1,                 // 必填，认证类型：1-密码，2-Google OAuth，3-其他OAuth
    "oauthUserId": "string",       // 条件必填，OAuth用户ID（OAuth注册时必填）
    "oauthAccessToken": "string",  // 条件必填，OAuth访问令牌（OAuth注册时必填）
    "nickname": "string",          // 可选，用户昵称
    "avatarUrl": "string",         // 可选，头像URL
    "primaryEmail": "string",      // 必填，主邮箱
    "phone": "string",             // 可选，手机号
    "timezone": "string",          // 可选，时区
    "languagePreference": "string", // 可选，语言偏好
    "userType": 1                  // 必填，用户类型
}
```
- 响应数据：
```json
{
    "token": "string",           // JWT令牌
    "userInfo": {
        "systemUserId": 0,       // 系统用户ID
        "userUniq": "string",    // 用户唯一标识
        "nickname": "string",    // 用户昵称
        "primaryEmail": "string", // 主邮箱
        "phone": "string",       // 手机号
        "avatarUrl": "string",   // 头像URL
        "authType": 1,          // 认证类型
        "userType": 1           // 用户类型
    }
}
```
- 错误码：
```json
{
    "code": 400,
    "message": "错误提示",      // 具体错误信息
    "data": null
}
```

常见错误提示：
| 错误码 | 提示信息 |
|--------|---------|
| 400    | "用户名已存在" |
| 400    | "邮箱或手机号至少需要填写一项" |
| 400    | "密码不能为空" |
| 400    | "OAuth用户ID不能为空" |
| 400    | "OAuth访问令牌不能为空" |
| 400    | "认证类型不能为空" |

### 2. 获取用户信息
- 请求路径：`GET /api/v1/users/{id}`
- 请求参数：路径参数 id
- 响应数据：
```json
{
    "systemUserId": 0,      // 系统用户ID
    "userUniq": "string",   // 用户唯一标识
    "primaryEmail": "string", // 主邮箱
    "nickname": "string",   // 用户昵称
    "phone": "string",      // 手机号
    "avatarUrl": "string",  // 头像URL
    "authType": 1,         // 认证类型
    "userType": 1          // 用户类型
}
``` 