package com.gittors.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author zlliu
 * @date 2019/1/24 10:09
 */
@Configuration
public class Config {

    /**
     * 配置限流key：根据请求url
     *
     * @return
     */
    @Bean
    KeyResolver apiKeyResolver() {
        return exchange ->
                Mono.just(exchange.getRequest().getPath().value());
    }
}
