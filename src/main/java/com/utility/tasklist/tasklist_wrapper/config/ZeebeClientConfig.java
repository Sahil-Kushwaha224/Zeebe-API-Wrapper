package com.utility.tasklist.tasklist_wrapper.config;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.ZeebeClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZeebeClientConfig {

    @Bean
    public ZeebeClient zeebeClient(
            @Value("${zeebe.gateway.address:127.0.0.1:26500}") String address,
            @Value("${zeebe.gateway.plaintext:true}") boolean plaintext
    ) {
        ZeebeClientBuilder builder = ZeebeClient.newClientBuilder()
                .gatewayAddress(address);
        if (plaintext) {
            builder.usePlaintext();
        }
        return builder.build();
    }
}