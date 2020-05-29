package com.bailihui.shop.exception;

/**
 * @author Cnlomou
 * @create 2020/5/27 10:07
 */
public class UnSupportPatternException extends RuntimeException {
    public UnSupportPatternException(String message) {
        super(message);
    }

    public UnSupportPatternException(String message, Throwable cause) {
        super(message, cause);
    }
}
