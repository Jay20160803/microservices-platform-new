package com.central.common.exception;

/**
 * 幂等性异常
 * @Author 高杰
 * @create 2023/8/28 15:41
 */
public class IdempotencyException extends RuntimeException{

    private static final long serialVersionUID = 6610083281801529147L;

    public IdempotencyException(String message) {
        super(message);
    }
}
