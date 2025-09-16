package com.utility.tasklist.tasklist_wrapper.dto;

import java.util.List;

/**
 * Request payload for searching process instances.
 * Mirrors Operate-like search capabilities.
 */
public class SearchProcessInstancesRequest {

    private Filter filter;
    private Integer size; // page size
    private List<Object> searchAfter; // cursor for pagination
    private List<SortOption> sort; // sorting

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<Object> getSearchAfter() {
        return searchAfter;
    }

    public void setSearchAfter(List<Object> searchAfter) {
        this.searchAfter = searchAfter;
    }

    public List<SortOption> getSort() {
        return sort;
    }

    public void setSort(List<SortOption> sort) {
        this.sort = sort;
    }

    /**
     * Nested filter criteria.
     */
    public static class Filter {
        private Long key;
        private Integer processVersion;
        private String processVersionTag;
        private String bpmnProcessId;
        private Long parentKey;
        private Long parentFlowNodeInstanceKey;
        private String startDate; // ISO-8601 string
        private String endDate;   // ISO-8601 string
        private String state;     // ACTIVE, COMPLETED, CANCELED
        private Boolean incident;
        private Long processDefinitionKey;
        private String tenantId;
        private Long parentProcessInstanceKey;

        public Long getKey() {
            return key;
        }

        public void setKey(Long key) {
            this.key = key;
        }

        public Integer getProcessVersion() {
            return processVersion;
        }

        public void setProcessVersion(Integer processVersion) {
            this.processVersion = processVersion;
        }

        public String getProcessVersionTag() {
            return processVersionTag;
        }

        public void setProcessVersionTag(String processVersionTag) {
            this.processVersionTag = processVersionTag;
        }

        public String getBpmnProcessId() {
            return bpmnProcessId;
        }

        public void setBpmnProcessId(String bpmnProcessId) {
            this.bpmnProcessId = bpmnProcessId;
        }

        public Long getParentKey() {
            return parentKey;
        }

        public void setParentKey(Long parentKey) {
            this.parentKey = parentKey;
        }

        public Long getParentFlowNodeInstanceKey() {
            return parentFlowNodeInstanceKey;
        }

        public void setParentFlowNodeInstanceKey(Long parentFlowNodeInstanceKey) {
            this.parentFlowNodeInstanceKey = parentFlowNodeInstanceKey;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public Boolean getIncident() {
            return incident;
        }

        public void setIncident(Boolean incident) {
            this.incident = incident;
        }

        public Long getProcessDefinitionKey() {
            return processDefinitionKey;
        }

        public void setProcessDefinitionKey(Long processDefinitionKey) {
            this.processDefinitionKey = processDefinitionKey;
        }

        public String getTenantId() {
            return tenantId;
        }

        public void setTenantId(String tenantId) {
            this.tenantId = tenantId;
        }

        public Long getParentProcessInstanceKey() {
            return parentProcessInstanceKey;
        }

        public void setParentProcessInstanceKey(Long parentProcessInstanceKey) {
            this.parentProcessInstanceKey = parentProcessInstanceKey;
        }
    }

    /**
     * Sort option specifying field and order.
     */
    public static class SortOption {
        private String field;
        private String order; // ASC or DESC

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}