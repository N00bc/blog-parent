package com.cyn.blog.anno;

import com.alibaba.fastjson.JSON;
import com.cyn.blog.utils.HttpContextUtils;
import com.cyn.blog.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Godc
 * @description:
 * @date 2022/8/8 14:52
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.cyn.blog.anno.LogAnno)")
    private void pt() {
    }

    @Around("pt()")
    public Object logOut(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executeTime = System.currentTimeMillis() - beginTime;
        recordLog(joinPoint, executeTime);
        return proceed;
    }

    private void recordLog(ProceedingJoinPoint proceedingJoinPoint, long time) {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        LogAnno annotation = method.getAnnotation(LogAnno.class);
        log.info("====================log start====================");
        log.info("module:{}", annotation.module());
        log.info("func:{}", annotation.func());

        // 请求的方法名
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String signatureName = signature.getName();
        log.info("request method:{}", className + "." + signatureName + "()");

        // 请求参数
        Object[] args = proceedingJoinPoint.getArgs();
        String params = JSON.toJSONString(args[0]);
        log.info("params:{}", params);

        // 获取ip地址
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        log.info("ip:{}", IPUtils.getIpAddr(httpServletRequest));

        log.info("execute time:{}ms", time);
        log.info("====================log end====================");
    }
}
