-- 营销邮件模板表
CREATE TABLE IF NOT EXISTS t_marketing_email_template (
    template_id VARCHAR(36) PRIMARY KEY,
    template_name VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    from_name VARCHAR(100),
    from_email VARCHAR(100) NOT NULL,
    org_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 营销邮件规则表
CREATE TABLE IF NOT EXISTS t_marketing_email_rule (
    rule_id VARCHAR(36) PRIMARY KEY,
    rule_name VARCHAR(100) NOT NULL,
    template_id VARCHAR(36) NOT NULL REFERENCES t_marketing_email_template(template_id),
    trigger_condition JSONB NOT NULL,
    target_users JSONB NOT NULL,
    org_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 营销邮件发送日志表
CREATE TABLE IF NOT EXISTS t_marketing_email_send_log (
    log_id VARCHAR(36) PRIMARY KEY,
    rule_id VARCHAR(36) NOT NULL REFERENCES t_marketing_email_rule(rule_id),
    template_id VARCHAR(36) NOT NULL REFERENCES t_marketing_email_template(template_id),
    to_email VARCHAR(100) NOT NULL,
    to_name VARCHAR(100),
    status VARCHAR(20) NOT NULL,
    error_message TEXT,
    sent_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
); 