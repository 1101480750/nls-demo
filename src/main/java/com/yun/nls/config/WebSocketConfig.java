package com.yun.nls.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;


/**
 * websocket配置
 *
 * @author zhouyaoming
 * @version 1.0.0
 * @date 2020/5/6 14:48
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){return new ServerEndpointExporter();}
}
