package com.cyn.blog.handler;

import com.cyn.blog.entity.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 23:39
 */
@ControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result doException(Exception exception) {
        exception.printStackTrace();
        return Result.fail(-999, "系统异常");
    }
}
