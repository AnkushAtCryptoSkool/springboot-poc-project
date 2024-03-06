package com.ankush.poc.job.listners;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobListnerImpl implements JobListener {
    @Override
    public String getName() {
        return "JobListnerImpl";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        log.info("jobToBeExecuted function of JobListnerImpl called !!");

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        log.info("jobExecutionVetoed function of JobListnerImpl called !!");

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        log.info("jobWasExecuted function of JobListnerImpl called !!");
    }
}
