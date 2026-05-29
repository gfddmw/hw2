package com.example.hotelarchitect.config;

import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class TimeoutConfig {

    @Bean
    public RestClientCustomizer restClientCustomizer() {
        return builder -> {
            // 使用 JDK 原生的 SimpleClientHttpRequestFactory 设置超时，具有 100% 的类路径安全性
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setReadTimeout(120000); // 设置读取超时为 120 秒
            factory.setConnectTimeout(120000); // 设置连接超时为 120 秒
            builder.requestFactory(factory);
        };
    }
}
