package com.hao.digitalsignature.config;

import com.hao.digitalsignature.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void  addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**");
    }
}
