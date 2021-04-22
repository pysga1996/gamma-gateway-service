package com.gamma;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.factory.RedirectToGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

@Log4j2
@Configuration
public class CustomBeanConfig {

    @Bean
    public KeyResolver addressKeyResolver() {
        return exchange -> {
//            RedirectToGatewayFilterFactory
            ServerHttpRequest request = exchange.getRequest();
            String remoteAddr = (request.getHeaders().getFirst("X-FORWARDED-FOR"));
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddress() !=null ? request.getRemoteAddress().getAddress().getHostAddress() : "1";
            }
            log.debug("Filter request address: {}", remoteAddr);
            return Mono.just(remoteAddr);
        };
    }
}
