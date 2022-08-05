package com.cyn.blog.config;

import com.cyn.blog.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 15:11
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;
    /**
     * 跨域配置
     *
     * @param registry:
     * @return void
     * @author G0dc
     * @date 2022/7/25 15:12
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(loginInterceptor)
                // TODO 后续按需求添加路径
                // 添加评论需要用户信息
                .addPathPatterns("/comments/create/change")
                // 发布文章需要用户信息
                .addPathPatterns("/articles/publish");

    }
}
