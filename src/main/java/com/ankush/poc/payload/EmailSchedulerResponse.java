package com.ankush.poc.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSchedulerResponse {

    private String jobId;
    private String jobGroup;
    private String message;
    private Boolean success;

    public EmailSchedulerResponse(String message, Boolean success) {
        this.message = message;
        this.success = success;
    }
}
