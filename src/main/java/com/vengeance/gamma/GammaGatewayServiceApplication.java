package com.vengeance.gamma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GammaGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GammaGatewayServiceApplication.class, args);
    }

}
