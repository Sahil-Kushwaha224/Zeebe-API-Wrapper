package com.utility.tasklist.tasklist_wrapper.controller;

import com.utility.tasklist.tasklist_wrapper.dto.CorrelateMessageRequest;
import com.utility.tasklist.tasklist_wrapper.dto.StartProcessInstanceRequest;
import com.utility.tasklist.tasklist_wrapper.dto.publicationMessageRequest;
import com.utility.tasklist.tasklist_wrapper.service.CamundaTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for exposing endpoints to interact with Camunda APIs.
 * Provides endpoints for message correlation, starting and cancelling process instances.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping({"/messages", ""})
public class MessageController {

    /** Service for interacting with Camunda APIs */
    private final CamundaTaskService camundaTaskService;

    /**
     * Constructor-based dependency injection for CamundaTaskService.
     *
     * @param camundaTaskService the service to interact with Camunda
     */
    public MessageController(CamundaTaskService camundaTaskService) {
        this.camundaTaskService = camundaTaskService;
    }

    /**
     * Correlate a message to a process instance.
     * 
     * POST /messages/correlation
     */
    @PostMapping("/correlation")
    public ResponseEntity<?> correlateMessage(@RequestBody CorrelateMessageRequest request) {
        try {
            Object result = camundaTaskService.correlateMessage(request);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to correlate message: " + ex.getMessage());
        }
    }

    /**
     * Publish a message to a process instance.
     * 
     * POST /messages/publication
     */
    @PostMapping("/publication")
    public ResponseEntity<?> publicationMessage(@RequestBody publicationMessageRequest request) {
        try {
            Object result = camundaTaskService.publicationMessage(request);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to publish message: " + ex.getMessage());
        }
    }

    /**
     * Start a process instance.
     * Exposed at root-level: POST /process-instances
     */
    @PostMapping("/process-instances")
    public ResponseEntity<?> startProcess(@RequestBody StartProcessInstanceRequest request) {
        try {
            Object result = camundaTaskService.startProcess(request);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to start process: " + ex.getMessage());
        }
    }

    /**
     * Cancel a process instance by key.
     * Exposed at: POST /process-instances/{processInstanceKey}/cancellation
     */
    @PostMapping("/process-instances/{processInstanceKey}/cancellation")
    public ResponseEntity<?> cancelProcess(@PathVariable String processInstanceKey) {
        try {
            camundaTaskService.cancelProcessInstance(processInstanceKey);
            return ResponseEntity.ok("instance cancel successful");
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to cancel process: " + ex.getMessage());
        }
    }



    /**
     * Update all variables of an element instance (or process instance) scope.
     * PUT /v2/element-instances/{elementInstanceKey}/variables (proxy)
     */
    @PutMapping("/v2/element-instances/{elementInstanceKey}/variables")
    public ResponseEntity<?> updateElementInstanceVariables(
            @PathVariable String elementInstanceKey,
            @RequestBody com.utility.tasklist.tasklist_wrapper.dto.UpdateElementInstanceVariablesRequest request) {
        try {
            camundaTaskService.updateElementInstanceVariables(elementInstanceKey, request);
            return ResponseEntity.noContent().build(); // 204
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to update variables: " + ex.getMessage());
        }
    }
}