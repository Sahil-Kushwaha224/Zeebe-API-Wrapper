package com.utility.tasklist.tasklist_wrapper.config;

public class UrlConfig {
    public static final String ZEEBEE_MESSAGE_API_BASE_URL = "http://localhost:8088/v2/messages/correlation";
    public static final String ZEEBEE_MESSAGE_PUBLISH_BASE_URL = "http://localhost:8088/v2/messages/publication";

    public static final String ZEEBEE_PROCESS_API_BASE_URL = "http://localhost:8088/v2/process-instances";
    public static final String ZEEBEE_PROCESS_CANCELLATION_URL = "http://localhost:8088/v2/process-instances/%s/cancellation";

    public static final String ZEEBEE_ELEMENT_INSTANCE_VARIABLES_URL = "http://localhost:8088/v2/element-instances/%s/variables";

    // Search and lookup endpoints
    public static final String ZEEBEE_PROCESS_SEARCH_URL = "http://localhost:8081/v1/variables/search";
    public static final String ZEEBEE_PROCESS_BY_KEY_URL = "http://localhost:8081/v1/process-instances/%s";

    public static final String ZEEBEE_PROCESS_DEFINITION_BY_KEY_URL = "http://localhost:8081//v1/process-definitions/%s";
        public static final String ZEEBEE_PROCESS_DEFINITION_AS_XML_URL = "http://localhost:8081//v1/process-definitions/%s/xml";


}