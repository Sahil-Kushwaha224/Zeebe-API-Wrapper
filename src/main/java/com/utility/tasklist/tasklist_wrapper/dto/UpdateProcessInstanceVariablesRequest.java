package com.utility.tasklist.tasklist_wrapper.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProcessInstanceVariablesRequest {
    private Map<String, Object> variables;
    private Boolean local = false; // default false (update at process instance scope)
}