package com.utility.tasklist.tasklist_wrapper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class publicationMessageRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("correlationKey")
    private String correlationKey;

    @JsonProperty("timeToLive")
    private Long timeToLive; // optional, milliseconds

    @JsonProperty("messageId")
    private String messageId; // optional idempotency key

    @JsonProperty("variables")
    private Map<String, Object> variables; // optional variables payload

    @JsonProperty("tenantId")
    private String tenantId; // optional for multi-tenancy
}