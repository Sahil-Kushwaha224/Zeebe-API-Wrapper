package com.utility.tasklist.tasklist_wrapper.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body to start a process instance.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartProcessInstanceRequest {
    private String processDefinitionId; // e.g. "customer_onboarding"
    private Map<String, Object> variables; // e.g. {"customerName":"Sahil","orderId":"ORD123"}

    
}