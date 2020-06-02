package com.yun.nls.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author zhouyaoming
 * @version 1.0.0
 * @date 2020/5/8 9:15
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebAppRootContext implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("org.apache.tomcat.websocket.textBufferSize");
        servletContext.addListener(WebAppRootListener.class);
        // 配置socket的信息发送data 大小
        servletContext.setInitParameter("org.apache.tomcat.websocket.textBufferSize","10240000");
    }
}
