package com.utility.tasklist.tasklist_wrapper.service;

import com.utility.tasklist.tasklist_wrapper.config.UrlConfig;
import com.utility.tasklist.tasklist_wrapper.dto.CorrelateMessageRequest;
import com.utility.tasklist.tasklist_wrapper.dto.StartProcessInstanceRequest;
import com.utility.tasklist.tasklist_wrapper.dto.MigrateProcessInstanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CamundaTaskService {

    private static final Logger logger = LoggerFactory.getLogger(CamundaTaskService.class);

    private final WebClient webClient;

    public CamundaTaskService(WebClient webClient) {
        this.webClient = webClient;
    }

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
     * Migrate a process instance by key.
     * POST http://localhost:8088/v2/process-instances/{processInstanceKey}/migration
     */
    public Object migrateProcessInstance(String processInstanceKey, MigrateProcessInstanceRequest migrationRequestBody) {
        try {
            String url = String.format(UrlConfig.ZEEBEE_PROCESS_MIGRATION_URL, processInstanceKey);
            logger.info("Migrating process instance via Camunda API at {}", url);
            ResponseEntity<Object> response = webClient.post()
                    .uri(url)
                    .bodyValue(migrationRequestBody)
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            if (response != null && response.getStatusCode().is2xxSuccessful()) {
                logger.debug("Successfully migrated process instance: {}", processInstanceKey);
                return response.getBody();
            } else {
                String status = (response == null) ? "null response" : response.getStatusCode().toString();
                Object body = (response == null) ? null : response.getBody();
                logger.error("Failed to migrate process: HTTP {} - {}", status, body);
                throw new RuntimeException("Failed to migrate process instance in Camunda.");
            }
        } catch (Exception ex) {
            logger.error("Exception while migrating process instance: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error migrating process instance in Camunda", ex);
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
}