package com.utility.tasklist.tasklist_wrapper.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;

import com.utility.tasklist.tasklist_wrapper.service.CamundaTokenService;

@Configuration
public class WebClientConfig {

    @Autowired
    private CamundaTokenService camundaTokenService;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .filter((request, next) -> {
                    String token;
                    try {
                        token = camundaTokenService.getAccessToken();
                    } catch (Exception ex) {
                        throw new RuntimeException("Could not obtain access token for Camunda API", ex);
                    }

                    ClientRequest newRequest = ClientRequest.from(request)
                            .header("Authorization", "Bearer " + token)
                            .build();

                    return next.exchange(newRequest);
                }).build();
    }
}
