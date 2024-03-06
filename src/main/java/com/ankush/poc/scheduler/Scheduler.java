package com.ankush.poc.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@EnableAsync
public class Scheduler {

//   @Scheduled(fixedRate = 1000)
//   @Scheduled(fixedDelay = 1000, initialDelay = 10000)
    // Using Java Duration UNIT
//    @Scheduled(fixedRateString = "PT02S") // run every 2 sec for 2 min -> PT02M
//    @Scheduled(fixedDelayString = "PT02S") // run every 2 sec of delay for 2 min -> PT02M
//      @Scheduled(cron = "${cron.execution.value}")
    //    @Async
   public void scheduler() throws InterruptedException {
       LocalDateTime localDateTime = LocalDateTime.now();
       DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
       String formattedDate = dateTimeFormatter.format(localDateTime);
       log.info("scheduler is called at : {}",localDateTime);
      Thread.sleep(1000);
   }

}
