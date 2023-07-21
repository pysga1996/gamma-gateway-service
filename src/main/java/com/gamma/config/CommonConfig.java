package com.gamma.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

/**
  * @created 25/04/2021 - 12:27:28 SA
  * @project vengeance
  * @author thanhvt
  * @description
  * @since 1.0
**/
@Log4j2
@Configuration
public class CommonConfig {

    @Bean
    public KeyResolver addressKeyResolver() {
        return exchange -> {
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
