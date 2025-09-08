package com.utility.tasklist.tasklist_wrapper.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Service for obtaining and caching OAuth2 access tokens from Camunda Identity.
 * Uses the client credentials grant to fetch tokens and caches them until expiry.
 */
@Service
public class CamundaTokenService {

    /** Logger for logging important events and errors */
    private static final Logger logger = LoggerFactory.getLogger(CamundaTokenService.class);

    /** Camunda Identity token endpoint URL, injected from application properties */
    @Value("${camunda.identity.token-url}")
    private String tokenUrl;

    /** OAuth2 client ID, injected from application properties */
    @Value("${camunda.identity.client-id}")
    private String clientId;

    /** OAuth2 client secret, injected from application properties */
    @Value("${camunda.identity.client-secret}")
    private String clientSecret;

    /** RestTemplate for making HTTP requests */
    private final RestTemplate restTemplate = new RestTemplate();

    /** Cached access token */
    private String cachedToken;
    /** Expiry time of the cached token (epoch millis) */
    private long tokenExpiryTime = 0;

    /**
     * Retrieves a valid access token, using the cached token if not expired.
     * If the token is expired or missing, fetches a new one from Camunda Identity.
     * @return a valid OAuth2 access token as a String
     * @throws RuntimeException if token retrieval fails
     */
    public synchronized String getAccessToken() {
        long now = System.currentTimeMillis();
        // Use cached token if still valid (with 1 minute buffer)
        if (cachedToken != null && now < tokenExpiryTime - 60000) {
            logger.debug("Using cached Camunda access token.");
            return cachedToken;
        }

        logger.info("Fetching new Camunda access token from Identity.");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Prepare form parameters for client credentials grant
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);

        // Build the form body as a URL-encoded string
        StringBuilder body = new StringBuilder();
        params.forEach((k, v) -> body.append(k).append("=").append(v).append("&"));
        body.deleteCharAt(body.length() - 1); // remove trailing '&'

        HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

        try {
            // Make the POST request to fetch the token
            ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map bodyMap = response.getBody();
                cachedToken = (String) bodyMap.get("access_token");
                Integer expiresIn = bodyMap.get("expires_in") instanceof Integer
                        ? (Integer) bodyMap.get("expires_in")
                        : Integer.parseInt(bodyMap.get("expires_in").toString());
                tokenExpiryTime = now + expiresIn * 1000L;
                logger.info("Fetched new Camunda access token, expires in {} seconds.", expiresIn);
                return cachedToken;
            } else {
                logger.error("Failed to fetch token: HTTP {} - {}", response.getStatusCode(), response.getBody());
                throw new RuntimeException("Failed to fetch Camunda access token.");
            }
        } catch (Exception ex) {
            logger.error("Exception while fetching Camunda access token: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error fetching Camunda access token", ex);
        }
    }
}