-- 删除原有的OAuth相关字段
ALTER TABLE marketing_user
    DROP COLUMN IF EXISTS oauth_provider_id,
    DROP COLUMN IF EXISTS oauth_access_token,
    DROP COLUMN IF EXISTS oauth_refresh_token,
    DROP COLUMN IF EXISTS oauth_expires_at;

-- 添加extends_info字段
ALTER TABLE marketing_user
    ADD COLUMN extends_info TEXT AFTER sys_user_id;

-- 删除相关索引
DROP INDEX IF EXISTS idx_user_oauth ON marketing_user; 