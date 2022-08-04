package com.cyn.blog.interceptor;

import com.alibaba.fastjson.JSON;
import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.gloableEnum.ErrorCode;
import com.cyn.blog.service.SysUserService;
import com.cyn.blog.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/28 15:17
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录拦截器
     *
     * @param request:
     * @param response:
     * @param handler:
     * @return boolean
     * @author G0dc
     * @date 2022/7/28 15:37
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 没搞明白
        if (!(handler instanceof HandlerMethod)) return true;
        // 1.获取请求头中的token
        String token = request.getHeader("Authorization");

        log.info("================request start================");
        log.info("request uri{}", request.getRequestURI());
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("================ request end================");

        // 2.判断token是否为空，非空则根据token解析出用户信息
        if (StringUtils.isBlank(token)) {
            // 若token为空 则返回错误信息 状态为未登录
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSON.toJSONString(Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg())));
            return false;
        }
        // 解析token
        SysUser sysUser = sysUserService.checkToken(token);
        if (sysUser == null) {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.getWriter().write(JSON.toJSONString(Result.fail(ErrorCode.ACCOUNT_EXPIRED.getCode(), ErrorCode.ACCOUNT_EXPIRED.getMsg())));
            return false;
        }
        // 3.将当前用户存入ThreadLocal中
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 使用完毕后需要将ThreadLocal中的信息删除，否则会出现内存泄露风险
        UserThreadLocal.remove();
    }
}
