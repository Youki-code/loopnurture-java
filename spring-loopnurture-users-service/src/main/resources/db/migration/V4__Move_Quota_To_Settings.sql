-- 将现有的配额数据迁移到settings JSON中
UPDATE marketing_organization
SET settings = JSON_SET(
    COALESCE(settings, '{}'),
    '$.quotaSettings',
    JSON_OBJECT(
        'maxUsers', max_users,
        'maxTemplates', max_templates,
        'maxRules', max_rules
    )
)
WHERE max_users IS NOT NULL
   OR max_templates IS NOT NULL
   OR max_rules IS NOT NULL;

-- 删除原有的配额字段
ALTER TABLE marketing_organization
    DROP COLUMN max_users,
    DROP COLUMN max_templates,
    DROP COLUMN max_rules; 