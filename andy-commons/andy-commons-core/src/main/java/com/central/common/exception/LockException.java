package com.central.common.exception;

/**
 * 分布式锁异常
 * @Author 高杰
 * @create 2023/8/28 15:42
 */
public class LockException extends RuntimeException{
    private static final long serialVersionUID = 6610083281801529147L;

    public LockException(String message) {
        super(message);
    }
}
