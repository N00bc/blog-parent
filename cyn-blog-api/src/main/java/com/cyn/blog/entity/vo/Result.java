package com.cyn.blog.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author G0dc
 * @description: 返回前端的vo
 * @date 2022/7/25 15:34
 */
@Data
@AllArgsConstructor
public class Result {
    private boolean success;

    private Integer code;

    private String msg;

    private Object data;


    public static Result success(Object data) {
        return new Result(true, 200, "success", data);
    }

    public static Result fail(Integer code, String msg) {
        return new Result(false, code, msg, null);
    }

}
