package com.central.log.service;

import com.central.log.model.Audit;

/**
 * 审计日志接口
 * @Author 高杰
 * @create 2023/8/28 13:45
 */
public interface IAuditService {
    void save(Audit audit);
}
