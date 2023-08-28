package com.central.common.exception;

/**
 * @Author 高杰
 * @create 2023/8/28 15:40
 */
public class BusinessException extends RuntimeException{

    private static final long serialVersionUID = 6610083281801529147L;

    public BusinessException(String message) {
        super(message);
    }
}
