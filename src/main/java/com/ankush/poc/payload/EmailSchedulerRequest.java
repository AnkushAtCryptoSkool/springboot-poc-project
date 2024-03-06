package com.ankush.poc.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailSchedulerRequest {

    private String email;
    private String subject;
    private String body;
    private LocalDateTime dateTime;
    private ZoneId timeZone;

}
