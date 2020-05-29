package com.bailihui.shop.util;

import com.bailihui.shop.exception.NotPermissionException;
import io.jsonwebtoken.lang.Assert;

/**
 * @author Cnlomou
 * @create 2020/5/28 10:09
 */
public class Asserts {
    static public void isTrue(boolean expression, String message) {
        Assert.isTrue(expression, message);
    }

    static public void isFalse(boolean expression, String message) {
        Assert.isTrue(!expression, message);
    }

    static public void checkPermission(boolean expression, String message) {
        if (!expression)
            throw new NotPermissionException(message);
    }

    static public void checkPermission(boolean expression) {
        checkPermission(expression, "权限不足");
    }

    static public void check(boolean expression,String msg){
        if(!expression)
            throw new IllegalStateException(msg);
    }


}
