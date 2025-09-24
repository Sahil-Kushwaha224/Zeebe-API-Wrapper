package com.utility.tasklist.tasklist_wrapper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import javax.print.DocFlavor.STRING;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessInstancesRequest {

    private Filter filter;
    private Integer size;
    private List<Object> searchAfter;
    private List<Sort> sort;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Filter {
        private Long key;
        private Integer processVersion;
        private String processVersionTag;
        private String bpmnProcessId;
        private Long parentKey;
        private Long parentFlowNodeInstanceKey;
        private String startDate; 
        private String endDate;
        private String state; 
        private Boolean incident;
        private Long processDefinitionKey;
        // private String tenantId; 
        private Long parentProcessInstanceKey;

        // // ✅ Setter with conversion for tenantId
        // public void setTenantId(String tenantId) {
        //     if ("default".equalsIgnoreCase(tenantId)) {
        //         this.tenantId = "<default>";
        //     } else {
        //         this.tenantId = tenantId;
        //     }
        // }
    }

    // Make this static but **not public**
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sort {
        private String field; // e.g., "startDate"
        private String order; // ASC or DESC
    }
}
