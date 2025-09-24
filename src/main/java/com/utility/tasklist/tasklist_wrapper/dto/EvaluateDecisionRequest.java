package com.utility.tasklist.tasklist_wrapper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluateDecisionRequest {

    private String decisionDefinitionId;   // optional
    private String decisionDefinitionKey;  // optional
    private String tenantId;               // optional
    private Map<String, Object> variables; // input variables
}
