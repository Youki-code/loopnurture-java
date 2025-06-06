-- 创建营销用户表
CREATE TABLE t_marketing_user (
    system_user_id BIGINT PRIMARY KEY,
    user_uniq VARCHAR(50) NOT NULL,
    auth_type VARCHAR(20) NOT NULL,
    password VARCHAR(100),
    oauth_user_id VARCHAR(100),
    oauth_access_token TEXT,
    primary_email VARCHAR(100),
    phone VARCHAR(20),
    extends JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_uniq UNIQUE (user_uniq),
    CONSTRAINT uk_oauth_user UNIQUE (oauth_user_id, auth_type)
);

-- 创建组织表
CREATE TABLE t_organization (
    org_id VARCHAR(64) PRIMARY KEY,
    org_name VARCHAR(100) NOT NULL,
    org_type VARCHAR(20) NOT NULL,
    owner_user_id BIGINT NOT NULL,
    status INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    CONSTRAINT fk_owner_user FOREIGN KEY (owner_user_id) REFERENCES t_marketing_user(system_user_id)
);

-- 创建用户组织关系表
CREATE TABLE t_user_organization (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    org_id VARCHAR(64) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT NOT NULL,
    CONSTRAINT uk_user_org UNIQUE (user_id, org_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES t_marketing_user(system_user_id),
    CONSTRAINT fk_org FOREIGN KEY (org_id) REFERENCES t_organization(org_id)
); 