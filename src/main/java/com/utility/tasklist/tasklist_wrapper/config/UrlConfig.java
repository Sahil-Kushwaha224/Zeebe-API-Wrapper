package com.utility.tasklist.tasklist_wrapper.config;

public class UrlConfig {
    public static final String ZEEBEE_MESSAGE_API_BASE_URL = "http://localhost:8088/v2/messages/correlation";
    public static final String ZEEBEE_PROCESS_API_BASE_URL = "http://localhost:8088/v2/process-instances";
    public static final String ZEEBEE_PROCESS_CANCELLATION_URL = "http://localhost:8088/v2/process-instances/%s/cancellation";
    public static final String ZEEBEE_PROCESS_MIGRATION_URL = "http://localhost:8088/v2/process-instances/%s/migration";
    public static final String ZEEBEE_ELEMENT_INSTANCE_VARIABLES_URL = "http://localhost:8088/v2/element-instances/%s/variables";
}