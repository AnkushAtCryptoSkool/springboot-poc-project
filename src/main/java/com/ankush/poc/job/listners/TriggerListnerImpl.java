package com.ankush.poc.job.listners;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TriggerListnerImpl implements TriggerListener {
    @Override
    public String getName() {
        return "TriggerListnerImpl";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        log.info("triggerFired function of TriggerListnerImpl called !!");

    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        log.info("vetoJobExecution function of TriggerListnerImpl called !!");

        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("triggerMisfired function of TriggerListnerImpl called !!");

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, Trigger.CompletedExecutionInstruction completedExecutionInstruction) {
        log.info("triggerComplete function of TriggerListnerImpl called !!");

    }
}
