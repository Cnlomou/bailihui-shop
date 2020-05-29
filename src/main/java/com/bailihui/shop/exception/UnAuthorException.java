package com.bailihui.shop.exception;

/**
 * @author Cnlomou
 * @create 2020/5/26 21:00
 */
public class UnAuthorException extends RuntimeException {
    public UnAuthorException(String message) {
        super(message);
    }

    public UnAuthorException(String message, Throwable cause) {
        super(message, cause);
    }
}
