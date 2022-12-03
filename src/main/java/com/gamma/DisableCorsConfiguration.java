package com.gamma;

import java.util.function.Function;
import javax.validation.constraints.NotNull;
import org.springframework.cloud.gateway.config.GlobalCorsProperties;
import org.springframework.cloud.gateway.handler.FilteringWebHandler;
import org.springframework.cloud.gateway.handler.RoutePredicateHandlerMapping;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author pysga
 * @created 04/12/2022 - 12:12 SA
 * @project gamma-gateway-service
 * @since 1.0
 **/
@Configuration
public class DisableCorsConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowCredentials(true)
            .allowedOrigins("*")
            .allowedHeaders("*")
            .allowedMethods("*")
            .exposedHeaders(HttpHeaders.SET_COOKIE);
    }

    @Bean
    public CorsWebFilter corsWebFilter() {

        // Disable per-request CORS

        return new CorsWebFilter(new UrlBasedCorsConfigurationSource()){
            @Override
            @NotNull
            public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }

    /**
     * HACK ALERT!!!!
     * Spring WebFlux CORS logic is hard-coded, this is a low-level hack to bypass it.
     * see {@link org.springframework.web.reactive.handler.AbstractHandlerMapping#getHandler}.
     *
     */
    @Bean
    @Primary
    public RoutePredicateHandlerMapping NoCorsRoutePredicateHandlerMapping(
        FilteringWebHandler webHandler, RouteLocator routeLocator,
        GlobalCorsProperties globalCorsProperties, Environment environment) {
        return new RoutePredicateHandlerMapping(webHandler, routeLocator,
            globalCorsProperties, environment){
            @Override
            @NotNull
            public Mono<Object> getHandler(@NotNull ServerWebExchange exchange) {
                return getHandlerInternal(exchange).map(Function.identity());
            }
        };
    }

}