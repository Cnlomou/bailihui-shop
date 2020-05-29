package com.bailihui.shop.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Cnlomou
 * @create 2020/5/25 20:46
 */
@Data
@AllArgsConstructor
public class Result<T> {
    public static final Result SUCC = new Result<>(true, 0, "SUCCESS", null);
    private boolean flag;
    /**
     * 0    正常
     * -1   正常错误
     * 1    未授权
     */
    private int status;
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public Result(boolean flag, String msg, T data) {
        this.flag = flag;
        this.msg = msg;
        this.status = flag ? 0 : -1;
        this.data = data;
    }

    public static Result success() {
        return Result.SUCC;
    }

    public static <D> Result<D> success(D data) {
        return new Result<>(true, "SUCCESS", data);
    }


    public static Result failure(String msg) {
        return failure(0, msg);
    }

    public static Result failure(Integer status, String msg) {
        return new Result<>(true, status, msg, null);
    }
}
