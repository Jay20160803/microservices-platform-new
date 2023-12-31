package com.central.log.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审计日志
 * @Author 高杰
 * @create 2023/8/28 11:54
 */
@Data
public class Audit {
    /**
     * 操作时间
     */
    private LocalDateTime timestamp;
    /**
     * 应用名
     */
    private String applicationName;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 租户id
     */
    private String clientId;
    /**
     * 操作信息
     */
    private String operation;
}
