package com.utility.tasklist.tasklist_wrapper.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MigrateProcessInstanceRequest {
    private String targetProcessDefinitionKey;
    private List<MappingInstruction> mappingInstructions;
    private Integer operationReference;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MappingInstruction {
        private String sourceElementId;
        private String targetElementId;
    }
}