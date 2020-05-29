package com.bailihui.shop.util;

import java.util.function.Predicate;

/**
 * @author Cnlomou
 * @create 2020/5/29 23:00
 */
public class Util {
    public static Predicate<String> matchPre(String path){
        return  s -> s.startsWith(path);
    }

    public static Predicate<String> matchSuf(String path){
        return  s -> s.endsWith(path);
    }
}
