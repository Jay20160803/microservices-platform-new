package com.central.log.annotation;

import java.lang.annotation.*;

/**
 * @Author 高杰
 * @create 2023/8/28 11:46
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    /**
     * 操作信息
     */
    String operation();
}
