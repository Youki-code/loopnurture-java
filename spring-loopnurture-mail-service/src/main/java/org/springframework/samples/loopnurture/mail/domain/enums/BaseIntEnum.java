package org.springframework.samples.loopnurture.mail.domain.enums;

/**
 * 基础整数枚举接口
 * 用于统一处理使用整数作为代码的枚举类型
 * 实现此接口的枚举类必须：
 * 1. 提供 (Integer code, String description) 构造器
 * 2. 实现 getCode() 和 getDescription() 方法
 */
public interface BaseIntEnum {
    /**
     * 获取枚举代码
     */
    Integer getCode();

    /**
     * 获取枚举描述
     */
    String getDescription();

    default Integer getValue() {
        return getCode();
    }
} 