package com.utility.tasklist.tasklist_wrapper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utility.tasklist.tasklist_wrapper.config.UrlConfig;
import com.utility.tasklist.tasklist_wrapper.dto.CorrelateMessageRequest;
import com.utility.tasklist.tasklist_wrapper.dto.EvaluateDecisionRequest;
import com.utility.tasklist.tasklist_wrapper.dto.ProcessInstancesRequest;
import com.utility.tasklist.tasklist_wrapper.dto.StartProcessInstanceRequest;
import com.utility.tasklist.tasklist_wrapper.dto.UpdateProcessInstanceVariablesRequest;
import com.utility.tasklist.tasklist_wrapper.dto.publicationMessageRequest;
import com.utility.tasklist.tasklist_wrapper.dto.SearchProcessInstancesRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;


import io.camunda.zeebe.client.ZeebeClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class CamundaTaskService {

    private static final Logger logger = LoggerFactory.getLogger(CamundaTaskService.class);

    private final WebClient webClient;
    private final ZeebeClient zeebeClient;

    public CamundaTaskService(WebClient webClient, ZeebeClient zeebeClient) {
        this.webClient = webClient;
        this.zeebeClient = zeebeClient;
    }
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Correlate a message to a process instance using Camunda's message API.
     *
     * @param request the correlation parameters (name, correlationKey)
     * @return the response from Camunda
     */
    public Object correlateMessage(CorrelateMessageRequest request) {
        try {
            logger.info("Correlating message via Camunda API at {}", UrlConfig.ZEEBEE_MESSAGE_API_BASE_URL);
            ResponseEntity<Object> response = webClient.post()
                    .uri(UrlConfig.ZEEBEE_MESSAGE_API_BASE_URL)
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully correlated message: {}", request.getName());
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to correlate message: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to correlate message in Camunda.");
            }
        } catch (Exception ex) {
            logger.error("Exception while correlating message: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error correlating message in Camunda", ex);
        }
    }

    public Object publicationMessage(publicationMessageRequest request) {
        try {
            logger.info("Publishing message via Camunda API at {}", UrlConfig.ZEEBEE_MESSAGE_PUBLISH_BASE_URL);
            // Default TTL to 0 if not provided to satisfy Camunda API's non-null expectation
            if (request.getTimeToLive() == null) {
                request.setTimeToLive(0L);
            }
            ResponseEntity<Object> response = webClient.post()
                    .uri(UrlConfig.ZEEBEE_MESSAGE_PUBLISH_BASE_URL)
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully published message: {}", request.getName());
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to publish message: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to publish message in Camunda.");
            }
        } catch (WebClientResponseException wex) {
            String body = wex.getResponseBodyAsString();
            logger.error("Camunda publish message API error: status={}, body={}", wex.getRawStatusCode(), body);
            throw new RuntimeException("Camunda error (" + wex.getRawStatusCode() + "): " + body, wex);
        } catch (Exception ex) {
            logger.error("Exception while publishing message: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error publishing message in Camunda", ex);
        }
    }

    /**
     * Update process instance variables using Zeebe client (self-managed gateway).
     * If local=true, variables are set on the element scope; otherwise process instance scope.
     */
    public void updateProcessInstanceVariables(long processInstanceKey, UpdateProcessInstanceVariablesRequest request) {
        try {
            logger.info("Setting variables via Zeebe client for processInstanceKey={} local={}", processInstanceKey, request.getLocal());
            zeebeClient
                .newSetVariablesCommand(processInstanceKey)
                .variables(request.getVariables())
                .local(Boolean.TRUE.equals(request.getLocal()))
                .send()
                .join();
        } catch (Exception ex) {
            logger.error("Exception while setting process instance variables: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error setting process instance variables via Zeebe client", ex);
        }
    }

    /**
     * Start a process instance.
     * POST http://localhost:8088/v2/process-instances
     */
    public Object startProcess(StartProcessInstanceRequest request) {
        try {
            logger.info("Starting process via Camunda API at {}", UrlConfig.ZEEBEE_PROCESS_API_BASE_URL);
            ResponseEntity<Object> response = webClient.post()
                    .uri(UrlConfig.ZEEBEE_PROCESS_API_BASE_URL)
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully started process: {}", request.getProcessDefinitionId());
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to start process: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to start process in Camunda.");
            }
        } catch (Exception ex) {
            logger.error("Exception while starting process: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error starting process in Camunda", ex);
        }
    }

    /**
     * Cancel a process instance by key.
     * POST http://localhost:8088/v2/process-instances/{processInstanceKey}/cancellation
     */
    public Object cancelProcessInstance(String processInstanceKey) {
        try {
            String url = String.format(UrlConfig.ZEEBEE_PROCESS_CANCELLATION_URL, processInstanceKey);
            logger.info("Cancelling process instance via Camunda API at {}", url);
            ResponseEntity<Object> response = webClient.post()
                    .uri(url)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully cancelled process instance: {}", processInstanceKey);
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to cancel process: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to cancel process instance in Camunda.");
            }
        } catch (Exception ex) {
            logger.error("Exception while cancelling process instance: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error cancelling process instance in Camunda", ex);
        }
    }



    /**
     * Update all variables for a given element instance (or process instance) scope.
     * PUT http://localhost:8088/v2/element-instances/{elementInstanceKey}/variables
     */
    public void updateElementInstanceVariables(String elementInstanceKey, com.utility.tasklist.tasklist_wrapper.dto.UpdateElementInstanceVariablesRequest request) {
        try {
            String url = String.format(UrlConfig.ZEEBEE_ELEMENT_INSTANCE_VARIABLES_URL, elementInstanceKey);
            logger.info("Updating variables for element instance via Camunda API at {}", url);
            ResponseEntity<Void> response = webClient.put()
                    .uri(url)
                    .bodyValue(request)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            if (response == null || !response.getStatusCode().is2xxSuccessful()) {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                logger.error("Failed to update variables: HTTP {}", status);
                throw new RuntimeException("Failed to update element instance variables in Camunda.");
            }
        } catch (Exception ex) {
            logger.error("Exception while updating element instance variables: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error updating element instance variables in Camunda", ex);
        }
    }

    /**
     * Search process instances with filter, size, searchAfter and sort.
     * POST http://localhost:8081/v1/variable/search
     */
    public Object searchProcessInstancesVariable(String requestBody) {
        try {
            // Logging the raw JSON request
            System.out.println("Request JSON = " + requestBody);
            System.out.println("Searching process instances via API at " + UrlConfig.ZEEBEE_PROCESS_SEARCH_URL);

            ResponseEntity<Object> response = webClient.post()
                    .uri(UrlConfig.ZEEBEE_PROCESS_SEARCH_URL)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                System.err.println("Failed to search process instances: HTTP " + status + " - " + body);
                throw new RuntimeException("Failed to search process instances.");
            }
        } catch (Exception ex) {
            System.err.println("Exception while searching process instances: " + ex.getMessage());
            throw new RuntimeException("Error searching process instances", ex);
        }
    }

    /**
     * Search process instances with filter, size, searchAfter and sort.
     * POST http://localhost:8081/v1/process-instance/search
     */
    public Object searchProcessInstances(String jsonRequest) {
    try {
        // Log JSON being sent to Camunda
        System.out.println("Request sent to Camunda API:\n" + jsonRequest);

        // Send JSON string directly; WebClient won't try to map it to DTO
        ResponseEntity<Object> response = webClient.post()
                .uri("http://localhost:8081/v1/process-instances/search")
                .header("Content-Type", "application/json")
                .bodyValue(jsonRequest)
                .retrieve()
                .toEntity(Object.class)
                .block();

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Response from Camunda API: " + response.getBody());
            return response.getBody();
        } else {
            String status = (response == null) ? "null response" : response.getStatusCode().toString();
            Object body = (response == null) ? null : response.getBody();
            throw new RuntimeException("Failed to search process instances: HTTP " + status + " - " + body);
        }
    } catch (Exception ex) {
        throw new RuntimeException("Error searching process instances", ex);
    }
}








    /**
     * Get process instance by key.
     * GET http://localhost:8080/v1/process-instances/{key}
     */
    public Object getProcessInstanceByKey(long key) {
        try {
            String url = String.format(UrlConfig.ZEEBEE_PROCESS_BY_KEY_URL, key);
            logger.info("Fetching process instance by key via API at {}", url);
            ResponseEntity<Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to get process instance by key: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to get process instance by key.");
            }
        } catch (Exception ex) {
            logger.error("Exception while getting process instance by key: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error getting process instance by key", ex);
        }
    }

     /**
     * Get process-definitions by key.
     * GET http://localhost:8080/v1/process-definitions/{key}
     */
    public Object getprocessDefinitionsBykey(long key) {
        try {
            String url = String.format(UrlConfig.ZEEBEE_PROCESS_DEFINITION_BY_KEY_URL, key);
            logger.info("Fetching process instance by key via API at {}", url);
            ResponseEntity<Object> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to get process definition by key: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to get process definition by key.");
            }
        } catch (Exception ex) {
            logger.error("Exception while getting process definition by key: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error getting process definition by key", ex);
        }
    }

     /**
     * Get process-definitions as XML.
     * GET http://localhost:8080/v1/process-definitions/{key}/xml
     */
    /**
 * Get process-definitions as XML.
 * GET http://localhost:8080/v1/process-definitions/{key}/xml
 */
public String getProcessDefinitionAsXml(long key) {   
    try {
        String url = String.format(UrlConfig.ZEEBEE_PROCESS_DEFINITION_AS_XML_URL, key);
        logger.info("Fetching process definition by key via API at {}", url);

        ResponseEntity<String> response = webClient.get()
                .uri(url)
                .retrieve()
                .toEntity(String.class)   
                .block();

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            String status = (response == null) ? "null response" : response.getStatusCode().toString();
            String body = (response == null) ? null : response.getBody();
            logger.error("Failed to get process definition by key: HTTP {} - {}", status, body);
            throw new RuntimeException("Failed to get process definition by key.");
        }
    } catch (Exception ex) {
        logger.error("Exception while getting process definition by key: {}", ex.getMessage(), ex);
        throw new RuntimeException("Error getting process definition by key", ex);
    }
}


public Object evaluateDecision(EvaluateDecisionRequest request) {
    try {
        logger.info("Evaluating decision via API at {}", UrlConfig.ZEEBEE_EVALUATE);

        ResponseEntity<Object> response = webClient.post()
                .uri(UrlConfig.ZEEBEE_EVALUATE) 
                .bodyValue(request)
                .retrieve()
                .toEntity(Object.class)
                .block();

        if (response != null && response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            String status = (response == null) ? "null response" : response.getStatusCode().toString();
            Object body = (response == null) ? null : response.getBody();
            logger.error("Failed to evaluate decision: HTTP {} - {}", status, body);
            throw new RuntimeException("Failed to evaluate decision.");
        }
    } catch (Exception ex) {
        logger.error("Exception while evaluating decision: {}", ex.getMessage(), ex);
        throw new RuntimeException("Error evaluating decision", ex);
    }
}





}