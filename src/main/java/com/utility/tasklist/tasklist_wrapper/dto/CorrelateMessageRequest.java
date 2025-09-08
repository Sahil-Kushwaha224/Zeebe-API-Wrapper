package com.utility.tasklist.tasklist_wrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

/**
 * Request DTO for message correlation operations.
 * Used to correlate messages with process instances in Camunda.
 */
public class CorrelateMessageRequest {
    
    @JsonProperty("name")
    private String name;
    
    @JsonProperty("correlationKey")
    private String correlationKey;

   
}
