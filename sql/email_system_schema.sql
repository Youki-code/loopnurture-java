-- 用户类型枚举值定义：
-- 1: LOCAL_USER (本地注册用户)
-- 2: GOOGLE_USER (Google账号用户)
-- 3: MICROSOFT_USER (Microsoft账号用户)
-- 4: GITHUB_USER (GitHub账号用户)
-- 5: APPLE_USER (Apple账号用户)
-- 6: GUEST_USER (临时访客)

-- 组织表
CREATE TABLE marketing_organization (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid(), -- 组织ID
    org_code VARCHAR(50) NOT NULL,              -- 组织编码
    org_name VARCHAR(100) NOT NULL,             -- 组织名称
    org_type INT NOT NULL,                      -- 组织类型：1=ENTERPRISE, 2=TEAM, 3=PERSONAL，可扩展
    status INT DEFAULT 1,                       -- 状态：1=ACTIVE, 2=INACTIVE, 3=SUSPENDED，可扩展
    max_users INT DEFAULT 5,                    -- 最大用户数
    max_templates INT DEFAULT 100,              -- 最大模板数
    max_rules INT DEFAULT 50,                   -- 最大规则数
    settings JSONB,                             -- 组织设置
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    CONSTRAINT org_code_unique UNIQUE (org_code)
);

-- 1. 用户表
CREATE TABLE marketing_user (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid(), -- 用户ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    user_uniq VARCHAR(100) NOT NULL,            -- 用户唯一标识
    user_type INT NOT NULL,                     -- 用户类型：1=LOCAL_USER, 2=GOOGLE_USER, 3=MICROSOFT_USER, 4=GITHUB_USER, 5=APPLE_USER, 6=GUEST_USER，可扩展
    user_role INT NOT NULL,                     -- 用户角色：1=ADMIN, 2=EDITOR, 3=VIEWER，可扩展
    sys_user_id VARCHAR(100),                   -- 系统用户ID
    oauth_provider_id VARCHAR(100),             -- OAuth提供商用户ID
    oauth_access_token TEXT,                    -- OAuth访问令牌
    oauth_refresh_token TEXT,                   -- OAuth刷新令牌
    oauth_expires_at TIMESTAMP WITH TIME ZONE,  -- OAuth令牌过期时间
    
    -- 基本信息
    nickname VARCHAR(50),                       -- 用户昵称
    avatar_url VARCHAR(255),                    -- 头像URL
    
    -- 联系方式
    primary_email VARCHAR(255),                 -- 主要邮箱
    backup_email VARCHAR(255),                  -- 备用邮箱
    phone VARCHAR(20),                          -- 手机号码
    telephone VARCHAR(20),                      -- 固定电话

    -- 个人设置偏好
    language_preference INT,                    -- 语言偏好：1=ZH_CN, 2=EN_US, 3=JA_JP, 4=KO_KR，可扩展
    timezone VARCHAR(50),                       -- 用户时区
    
    -- 账户状态
    account_status INT DEFAULT 1,               -- 账户状态：1=ACTIVE, 2=INACTIVE, 3=SUSPENDED，可扩展
    email_verified BOOLEAN DEFAULT false,       -- 邮箱是否验证
    phone_verified BOOLEAN DEFAULT false,       -- 手机是否验证
    
    -- 审计字段
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP WITH TIME ZONE,     -- 最后登录时间
    
    -- 约束
    CONSTRAINT user_uniq_unique UNIQUE (user_uniq),
    CONSTRAINT primary_email_unique UNIQUE (primary_email),
    CONSTRAINT phone_unique UNIQUE (phone),
);

-- 2. 营销邮件基础表
CREATE TABLE marketing_email_template (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid(), -- 模板ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    template_code VARCHAR(50) NOT NULL,         -- 模板业务编码
    user_id VARCHAR(100) NOT NULL,              -- 创建者ID
    template_name VARCHAR(100) NOT NULL,        -- 模板名称
    input_conditions JSONB,                     -- 输入条件
    generated_content JSONB,                    -- AI生成的最终内容
    template_status INT DEFAULT 1,              -- 状态：1=DRAFT, 2=ACTIVE, 3=INACTIVE，可扩展
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    version INT DEFAULT 1,                      -- 版本号
    CONSTRAINT template_code_org_unique UNIQUE (org_id, template_code)
);

-- 3. 营销邮件推送规则表
CREATE TABLE marketing_email_rule (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid(), -- 规则ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    rule_name VARCHAR(100) NOT NULL,            -- 规则名称
    template_id VARCHAR(100) NOT NULL,          -- 关联的邮件模板ID
    rule_conditions JSONB NOT NULL,             -- 规则条件
    rule_type INT NOT NULL,                     -- 规则类型：1=SCHEDULED, 2=EVENT_TRIGGERED，可扩展
    event_type INT,                             -- 事件类型：1=USER_REGISTER, 2=ORDER_COMPLETE, 3=CART_ABANDON，可扩展
    target_emails JSONB NOT NULL,               -- 目标邮箱列表
    rule_status INT DEFAULT 2,                  -- 状态：1=ACTIVE, 2=INACTIVE, 3=PAUSED，可扩展
    priority INT DEFAULT 0,                     -- 规则优先级
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    CONSTRAINT rule_name_org_unique UNIQUE (org_id, rule_name)
);

-- 4. 营销邮件推送记录表
CREATE TABLE marketing_email_send_log (
    id VARCHAR(100) PRIMARY KEY DEFAULT gen_random_uuid(), -- 日志ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    rule_id VARCHAR(100) NOT NULL,              -- 关联的规则ID
    template_id VARCHAR(100) NOT NULL,          -- 关联的模板ID
    recipient_user_id VARCHAR(100) NOT NULL,    -- 接收用户ID
    trigger_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    send_status INT NOT NULL,                   -- 发送状态：1=PENDING, 2=SENDING, 3=SENT, 4=FAILED，可扩展
    send_result TEXT,                           -- 发送结果详情
    error_message TEXT,                         -- 错误信息
    retry_count INT DEFAULT 0,                  -- 重试次数
    sent_at TIMESTAMP WITH TIME ZONE,           -- 实际发送时间
    open_count INT DEFAULT 0,                   -- 邮件打开次数
    click_count INT DEFAULT 0,                  -- 链接点击次数
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX idx_org_status ON marketing_organization(status);
CREATE INDEX idx_org_type ON marketing_organization(org_type);
CREATE INDEX idx_org_created_by ON marketing_organization(created_by);
CREATE INDEX idx_org_updated_by ON marketing_organization(updated_by);

CREATE INDEX idx_user_org ON marketing_user(org_id);
CREATE INDEX idx_user_type ON marketing_user(user_type);
CREATE INDEX idx_user_role ON marketing_user(user_role);
CREATE INDEX idx_user_status ON marketing_user(account_status);
CREATE INDEX idx_user_sys_id ON marketing_user(sys_user_id) WHERE sys_user_id IS NOT NULL;
CREATE INDEX idx_user_oauth ON marketing_user(oauth_provider_id) WHERE oauth_provider_id IS NOT NULL;
CREATE INDEX idx_user_email ON marketing_user(primary_email) WHERE primary_email IS NOT NULL;
CREATE INDEX idx_user_phone ON marketing_user(phone) WHERE phone IS NOT NULL;

CREATE INDEX idx_template_org ON marketing_email_template(org_id);
CREATE INDEX idx_template_status ON marketing_email_template(template_status);
CREATE INDEX idx_template_user ON marketing_email_template(user_id);
CREATE INDEX idx_template_created_by ON marketing_email_template(created_by);
CREATE INDEX idx_template_updated_by ON marketing_email_template(updated_by);

CREATE INDEX idx_rule_org ON marketing_email_rule(org_id);
CREATE INDEX idx_rule_status ON marketing_email_rule(rule_status);
CREATE INDEX idx_rule_type ON marketing_email_rule(rule_type);
CREATE INDEX idx_rule_template ON marketing_email_rule(template_id);
CREATE INDEX idx_rule_created_by ON marketing_email_rule(created_by);
CREATE INDEX idx_rule_updated_by ON marketing_email_rule(updated_by);
CREATE INDEX idx_rule_conditions_schedule ON marketing_email_rule USING gin ((rule_conditions->'schedule'));
CREATE INDEX idx_rule_conditions_event ON marketing_email_rule USING gin ((rule_conditions->'event'));

CREATE INDEX idx_send_log_org ON marketing_email_send_log(org_id);
CREATE INDEX idx_send_log_status ON marketing_email_send_log(send_status);
CREATE INDEX idx_send_log_time ON marketing_email_send_log(trigger_time);
CREATE INDEX idx_send_log_rule ON marketing_email_send_log(rule_id);
CREATE INDEX idx_send_log_template ON marketing_email_send_log(template_id);
CREATE INDEX idx_send_log_recipient ON marketing_email_send_log(recipient_user_id);
CREATE INDEX idx_send_log_sent_time ON marketing_email_send_log(sent_at) WHERE sent_at IS NOT NULL;

-- 创建更新时间触发器
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_org_updated_at
    BEFORE UPDATE ON marketing_organization
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_marketing_user_updated_at
    BEFORE UPDATE ON marketing_user
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_marketing_email_template_updated_at
    BEFORE UPDATE ON marketing_email_template
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_marketing_email_rule_updated_at
    BEFORE UPDATE ON marketing_email_rule
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column(); 