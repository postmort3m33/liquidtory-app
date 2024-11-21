package com.liquidtory.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry
                .addViewController("/{path1:[\\w]+}")
                .setViewName("forward:/");

        registry
                .addViewController("/{path1}/{path2:[\\w]+}")
                .setViewName("forward:/");

        registry
                .addViewController("/{path1}/{path2}/{path3:[\\w]+}")
                .setViewName("forward:/");
    }
}