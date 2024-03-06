package com.ankush.poc.service;

import com.ankush.poc.job.EmailJob;
import com.ankush.poc.job.listners.JobListnerImpl;
import com.ankush.poc.job.listners.TriggerListnerImpl;
import com.ankush.poc.payload.EmailSchedulerRequest;
import com.ankush.poc.payload.EmailSchedulerResponse;
import com.ankush.poc.service.interfaces.JobScheduleService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class JobScheduleServiceImpl implements JobScheduleService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobListnerImpl jobListner;

    @Autowired
    private TriggerListnerImpl triggerListner;

    @Override
    public EmailSchedulerResponse scheduleEmailJob(EmailSchedulerRequest request) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(request.getDateTime(),request.getTimeZone());
       if(zonedDateTime.isBefore(ZonedDateTime.now(request.getTimeZone()))){
           throw new RuntimeException("Job Scheduling Time must be after of the current Time");
       }
        JobDetail jobDetail = getJobDetail(request);
        Trigger trigger = getTrigger(jobDetail,zonedDateTime);
        try {
            scheduler.getListenerManager().addTriggerListener(triggerListner);
            scheduler.getListenerManager().addJobListener(jobListner);
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return EmailSchedulerResponse.builder()
                .jobId(trigger.getJobKey().getName())
                .jobGroup(trigger.getJobKey().getGroup())
                .message("Successfully scheduled")
                .success(true)
                .build();
    }

    private Trigger getTrigger(JobDetail jobDetail, ZonedDateTime zonedDateTime) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("email-triggers")
                .withDescription("Send Email Trigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .startAt(Date.from(zonedDateTime.toInstant()))
                .build();
    }

    private JobDetail getJobDetail(EmailSchedulerRequest request) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("email",request.getEmail());
        jobDataMap.put("subject",request.getSubject());
        jobDataMap.put("body",request.getBody());
       return JobBuilder.newJob(EmailJob.class)
               .setJobData(jobDataMap)
               .requestRecovery()
               .storeDurably()
               .withDescription("Send Email Job")
               .withIdentity(UUID.randomUUID().toString(),"email-jobs")
               .build();
    }

}
