package com.ankush.poc.service.interfaces;

import com.ankush.poc.payload.EmailSchedulerRequest;
import com.ankush.poc.payload.EmailSchedulerResponse;

public interface JobScheduleService {

    EmailSchedulerResponse scheduleEmailJob(EmailSchedulerRequest request);

}
