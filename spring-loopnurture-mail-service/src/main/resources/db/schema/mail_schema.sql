/*
 * Mail Module Database Schema
 * 邮件模块数据库表结构
 */

-- =============================================
-- Mail Module Tables (邮件模块表)
-- =============================================

-- 创建更新时间触发器函数
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 创建营销邮件模板表
CREATE TABLE marketing_email_template (
    id VARCHAR(36) PRIMARY KEY,                 -- 模板ID
    org_code VARCHAR(36) NOT NULL,              -- 组织编码
    template_id VARCHAR(50) NOT NULL,           -- 模板编码
    template_name VARCHAR(100) NOT NULL,        -- 模板名称
    content_type SMALLINT NOT NULL,             -- 内容类型：1=TEXT, 2=HTML
    content_template TEXT NOT NULL,             -- 内容模板
    ai_strategy_version VARCHAR(50),            -- AI策略版本
    enable_status SMALLINT NOT NULL DEFAULT 1,  -- 启用状态：1=ENABLED, 0=DISABLED
    extends_info TEXT,                          -- 扩展信息（JSON格式）
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36) NOT NULL,            -- 创建人ID
    updated_by VARCHAR(36) NOT NULL,            -- 更新人ID
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    
    CONSTRAINT uk_marketing_template_id UNIQUE (org_code, template_id)
);

-- 创建营销邮件模板索引
CREATE INDEX idx_marketing_email_template_org ON marketing_email_template(org_code);
CREATE INDEX idx_marketing_email_template_status ON marketing_email_template(enable_status);

-- 创建营销邮件模板更新时间触发器
CREATE TRIGGER update_marketing_email_template_updated_at
    BEFORE UPDATE ON marketing_email_template
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 创建邮件发送记录表
CREATE TABLE email_send_record (
    id VARCHAR(36) PRIMARY KEY,                 -- 记录ID
    org_code VARCHAR(36) NOT NULL,              -- 组织编码
    rule_id VARCHAR(36),                        -- 规则ID
    template_id VARCHAR(50) NOT NULL,           -- 模板编码
    subject TEXT NOT NULL,                      -- 邮件主题
    content TEXT NOT NULL,                      -- 邮件内容
    recipients TEXT NOT NULL,                   -- 收件人列表，多个用逗号分隔
    cc TEXT,                                    -- 抄送列表，多个用逗号分隔
    bcc TEXT,                                   -- 密送列表，多个用逗号分隔
    status SMALLINT NOT NULL,                   -- 发送状态：1=PENDING, 2=SENDING, 3=SENT, 4=FAILED
    error_message TEXT,                         -- 错误信息
    send_time TIMESTAMP,                        -- 发送时间
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36) NOT NULL,            -- 创建人ID
    updated_by VARCHAR(36) NOT NULL,            -- 更新人ID
    deleted TINYINT(1) NOT NULL DEFAULT 0
);

-- 创建邮件发送记录索引
CREATE INDEX idx_email_record_org ON email_send_record(org_code);
CREATE INDEX idx_email_record_template ON email_send_record(template_id);
CREATE INDEX idx_email_record_status ON email_send_record(status);
CREATE INDEX idx_email_record_send_time ON email_send_record(send_time);

-- 创建邮件发送记录更新时间触发器
CREATE TRIGGER update_email_send_record_updated_at
    BEFORE UPDATE ON email_send_record
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- 创建邮件发送规则表
CREATE TABLE email_send_rule (
    id VARCHAR(36) PRIMARY KEY,                 -- 规则ID
    org_code VARCHAR(36) NOT NULL,              -- 组织编码
    rule_id VARCHAR(50) NOT NULL,               -- 规则编码
    rule_name VARCHAR(100) NOT NULL,            -- 规则名称
    template_id VARCHAR(50) NOT NULL,           -- 模板编码
    rule_type SMALLINT NOT NULL,                -- 规则类型：1=IMMEDIATE, 2=CRON, 3=FIXED_RATE, 4=FIXED_DELAY
    extends_info TEXT,                          -- 扩展信息（JSON格式）
    start_time TIMESTAMP,                       -- 规则生效开始时间
    end_time TIMESTAMP,                         -- 规则生效结束时间
    max_executions INTEGER,                     -- 最大执行次数，null表示无限制
    execution_count INTEGER DEFAULT 0,          -- 已执行次数
    last_execution_time TIMESTAMP,              -- 上次执行时间
    next_execution_time TIMESTAMP,              -- 下次执行时间
    enable_status SMALLINT NOT NULL DEFAULT 1,  -- 启用状态：1=ENABLED, 0=DISABLED
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(36) NOT NULL,            -- 创建人ID
    updated_by VARCHAR(36) NOT NULL,            -- 更新人ID
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    
    CONSTRAINT uk_email_send_rule_id UNIQUE (org_code, rule_id)
);

-- 创建邮件发送规则索引
CREATE INDEX idx_email_rule_org ON email_send_rule(org_code);
CREATE INDEX idx_email_rule_template ON email_send_rule(template_id);
CREATE INDEX idx_email_rule_type ON email_send_rule(rule_type);
CREATE INDEX idx_email_rule_next_execution ON email_send_rule(next_execution_time);

-- 创建邮件发送规则更新时间触发器
CREATE TRIGGER update_email_send_rule_updated_at
    BEFORE UPDATE ON email_send_rule
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column(); 