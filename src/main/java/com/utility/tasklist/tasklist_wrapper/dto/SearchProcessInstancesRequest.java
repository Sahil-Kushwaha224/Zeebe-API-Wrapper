package com.utility.tasklist.tasklist_wrapper.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProcessInstancesRequest {
    private Filter filter;
    private Integer size;
    private List<Object> searchAfter;
    private List<SortOption> sort;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter {
        private String name;
        private String value; 
        private Long key;
        private Integer processVersion;
        private String bpmnProcessId;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SortOption {
        private String field;
        private String order;
    }
}
