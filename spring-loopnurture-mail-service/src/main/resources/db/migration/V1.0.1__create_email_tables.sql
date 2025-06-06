-- 创建邮件模板表
CREATE TABLE t_email_template (
    template_id VARCHAR(64) PRIMARY KEY COMMENT '模板ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码',
    subject VARCHAR(200) NOT NULL COMMENT '邮件主题',
    content TEXT NOT NULL COMMENT '模板内容',
    parameter_definition TEXT COMMENT '参数定义',
    status VARCHAR(20) NOT NULL COMMENT '模板状态',
    org_id VARCHAR(64) NOT NULL COMMENT '所属组织ID',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    created_by VARCHAR(64) NOT NULL COMMENT '创建人ID',
    updated_by VARCHAR(64) NOT NULL COMMENT '更新人ID',
    UNIQUE KEY uk_org_code (org_id, template_code),
    INDEX idx_org_id (org_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件模板表';

-- 创建邮件发送记录表
CREATE TABLE t_email_log (
    log_id VARCHAR(64) PRIMARY KEY COMMENT '记录ID',
    org_id VARCHAR(64) NOT NULL COMMENT '所属组织ID',
    template_id VARCHAR(64) NOT NULL COMMENT '模板ID',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码',
    subject VARCHAR(200) NOT NULL COMMENT '邮件主题',
    to_email VARCHAR(100) NOT NULL COMMENT '收件人邮箱',
    to_name VARCHAR(50) COMMENT '收件人姓名',
    content TEXT NOT NULL COMMENT '发送内容',
    parameters TEXT COMMENT '替换参数',
    status VARCHAR(20) NOT NULL COMMENT '发送状态',
    fail_reason VARCHAR(500) COMMENT '失败原因',
    retry_count INT NOT NULL DEFAULT 0 COMMENT '重试次数',
    next_retry_time DATETIME COMMENT '下次重试时间',
    created_at DATETIME NOT NULL COMMENT '创建时间',
    updated_at DATETIME NOT NULL COMMENT '更新时间',
    created_by VARCHAR(64) NOT NULL COMMENT '创建人ID',
    updated_by VARCHAR(64) NOT NULL COMMENT '更新人ID',
    INDEX idx_org_id (org_id),
    INDEX idx_template_id (template_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送记录表';

-- 创建营销邮件模板表
CREATE TABLE t_marketing_email_template (
    template_id VARCHAR(64) PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    template_code VARCHAR(50) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    from_name VARCHAR(100) NOT NULL,
    from_email VARCHAR(100) NOT NULL,
    status INTEGER NOT NULL DEFAULT 0,
    org_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64) NOT NULL,
    CONSTRAINT uk_org_code UNIQUE (org_id, template_code)
);

-- 创建营销邮件规则表
CREATE TABLE t_marketing_email_rule (
    rule_id VARCHAR(64) PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL,
    template_id VARCHAR(64) NOT NULL,
    trigger_condition JSONB,
    target_audience JSONB,
    send_time_config JSONB,
    status INTEGER NOT NULL DEFAULT 0,
    org_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64) NOT NULL,
    CONSTRAINT fk_template FOREIGN KEY (template_id) REFERENCES t_marketing_email_template(template_id)
);

-- 创建营销邮件发送日志表
CREATE TABLE t_marketing_email_send_log (
    log_id VARCHAR(64) PRIMARY KEY,
    rule_id VARCHAR(64) NOT NULL,
    template_id VARCHAR(64) NOT NULL,
    to_email VARCHAR(100) NOT NULL,
    to_name VARCHAR(100),
    from_email VARCHAR(100) NOT NULL,
    from_name VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    status INTEGER NOT NULL DEFAULT 0,
    fail_reason TEXT,
    retry_count INTEGER NOT NULL DEFAULT 0,
    next_retry_time TIMESTAMP,
    org_id VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(64) NOT NULL,
    updated_by VARCHAR(64) NOT NULL,
    CONSTRAINT fk_rule FOREIGN KEY (rule_id) REFERENCES t_marketing_email_rule(rule_id),
    CONSTRAINT fk_template_log FOREIGN KEY (template_id) REFERENCES t_marketing_email_template(template_id)
); 