package com.bailihui.shop.exception;

/**
 * @author Cnlomou
 * @create 2020/5/28 10:02
 */
public class NotPermissionException extends RuntimeException {
    public NotPermissionException(String message) {
        super(message);
    }

    public NotPermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
