-- 组织表
CREATE TABLE marketing_organization (
    id VARCHAR(100) PRIMARY KEY,                -- 组织ID
    org_code VARCHAR(50) NOT NULL,              -- 组织编码
    org_name VARCHAR(100) NOT NULL,             -- 组织名称
    org_type INT NOT NULL,                      -- 组织类型：1=ENTERPRISE, 2=TEAM, 3=PERSONAL，可扩展
    status INT DEFAULT 1,                       -- 状态：1=ACTIVE, 2=INACTIVE, 3=SUSPENDED，可扩展
    max_users INT DEFAULT 5,                    -- 最大用户数
    max_templates INT DEFAULT 100,              -- 最大模板数
    max_rules INT DEFAULT 50,                   -- 最大规则数
    settings JSON,                              -- 组织设置
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    UNIQUE KEY uk_org_code (org_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 用户表
CREATE TABLE marketing_user (
    id VARCHAR(100) PRIMARY KEY,                -- 用户ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    user_uniq VARCHAR(100) NOT NULL,            -- 用户唯一标识
    user_type INT NOT NULL,                     -- 用户类型：1=LOCAL_USER, 2=GOOGLE_USER, 3=MICROSOFT_USER, 4=GITHUB_USER, 5=APPLE_USER, 6=GUEST_USER，可扩展
    user_role INT NOT NULL,                     -- 用户角色：1=ADMIN, 2=EDITOR, 3=VIEWER，可扩展
    sys_user_id VARCHAR(100),                   -- 系统用户ID
    extends_info TEXT,                          -- 用户扩展信息，存储JSON格式的扩展字段
    
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
    email_verified TINYINT(1) DEFAULT 0,        -- 邮箱是否验证
    phone_verified TINYINT(1) DEFAULT 0,        -- 手机是否验证
    
    -- 审计字段
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,              -- 最后登录时间
    
    -- 约束
    UNIQUE KEY uk_user_uniq (user_uniq),
    UNIQUE KEY uk_primary_email (primary_email),
    UNIQUE KEY uk_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 模板表
CREATE TABLE marketing_email_template (
    id VARCHAR(100) PRIMARY KEY,                -- 模板ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    template_code VARCHAR(50) NOT NULL,         -- 模板业务编码
    user_id VARCHAR(100) NOT NULL,              -- 创建者ID
    template_name VARCHAR(100) NOT NULL,        -- 模板名称
    input_conditions JSON,                      -- 输入条件
    generated_content JSON,                     -- AI生成的最终内容
    template_status INT DEFAULT 1,              -- 状态：1=DRAFT, 2=ACTIVE, 3=INACTIVE，可扩展
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    version INT DEFAULT 1,                      -- 版本号
    UNIQUE KEY uk_template_code_org (org_id, template_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 规则表
CREATE TABLE marketing_email_rule (
    id VARCHAR(100) PRIMARY KEY,                -- 规则ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    rule_name VARCHAR(100) NOT NULL,            -- 规则名称
    template_id VARCHAR(100) NOT NULL,          -- 关联的邮件模板ID
    rule_conditions JSON NOT NULL,              -- 规则条件
    rule_type INT NOT NULL,                     -- 规则类型：1=SCHEDULED, 2=EVENT_TRIGGERED，可扩展
    event_type INT,                             -- 事件类型：1=USER_REGISTER, 2=ORDER_COMPLETE, 3=CART_ABANDON，可扩展
    target_emails JSON NOT NULL,                -- 目标邮箱列表
    rule_status INT DEFAULT 2,                  -- 状态：1=ACTIVE, 2=INACTIVE, 3=PAUSED，可扩展
    priority INT DEFAULT 0,                     -- 规则优先级
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,           -- 创建人user_id，关联marketing_user.id
    updated_by VARCHAR(100) NOT NULL,           -- 最后修改人user_id，关联marketing_user.id
    UNIQUE KEY uk_rule_name_org (org_id, rule_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 发送日志表
CREATE TABLE marketing_email_send_log (
    id VARCHAR(100) PRIMARY KEY,                -- 日志ID
    org_id VARCHAR(100) NOT NULL,               -- 关联的组织ID
    rule_id VARCHAR(100) NOT NULL,              -- 关联的规则ID
    template_id VARCHAR(100) NOT NULL,          -- 关联的模板ID
    recipient_user_id VARCHAR(100) NOT NULL,    -- 接收用户ID
    trigger_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    send_status INT NOT NULL,                   -- 发送状态：1=PENDING, 2=SENDING, 3=SENT, 4=FAILED，可扩展
    send_result TEXT,                           -- 发送结果详情
    error_message TEXT,                         -- 错误信息
    retry_count INT DEFAULT 0,                  -- 重试次数
    sent_at TIMESTAMP NULL,                     -- 实际发送时间
    open_count INT DEFAULT 0,                   -- 邮件打开次数
    click_count INT DEFAULT 0,                  -- 链接点击次数
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
ALTER TABLE marketing_organization ADD INDEX idx_org_status (status);
ALTER TABLE marketing_organization ADD INDEX idx_org_type (org_type);
ALTER TABLE marketing_organization ADD INDEX idx_org_created_by (created_by);
ALTER TABLE marketing_organization ADD INDEX idx_org_updated_by (updated_by);

ALTER TABLE marketing_user ADD INDEX idx_user_org (org_id);
ALTER TABLE marketing_user ADD INDEX idx_user_type (user_type);
ALTER TABLE marketing_user ADD INDEX idx_user_role (user_role);
ALTER TABLE marketing_user ADD INDEX idx_user_status (account_status);
ALTER TABLE marketing_user ADD INDEX idx_user_sys_id (sys_user_id);

ALTER TABLE marketing_email_template ADD INDEX idx_template_org (org_id);
ALTER TABLE marketing_email_template ADD INDEX idx_template_status (template_status);
ALTER TABLE marketing_email_template ADD INDEX idx_template_user (user_id);
ALTER TABLE marketing_email_template ADD INDEX idx_template_created_by (created_by);
ALTER TABLE marketing_email_template ADD INDEX idx_template_updated_by (updated_by);

ALTER TABLE marketing_email_rule ADD INDEX idx_rule_org (org_id);
ALTER TABLE marketing_email_rule ADD INDEX idx_rule_status (rule_status);
ALTER TABLE marketing_email_rule ADD INDEX idx_rule_type (rule_type);
ALTER TABLE marketing_email_rule ADD INDEX idx_rule_template (template_id);
ALTER TABLE marketing_email_rule ADD INDEX idx_rule_created_by (created_by);
ALTER TABLE marketing_email_rule ADD INDEX idx_rule_updated_by (updated_by);

ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_org (org_id);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_status (send_status);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_time (trigger_time);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_rule (rule_id);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_template (template_id);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_recipient (recipient_user_id);
ALTER TABLE marketing_email_send_log ADD INDEX idx_send_log_sent_time (sent_at);

-- 创建触发器函数（MySQL不需要，因为使用了ON UPDATE CURRENT_TIMESTAMP） 