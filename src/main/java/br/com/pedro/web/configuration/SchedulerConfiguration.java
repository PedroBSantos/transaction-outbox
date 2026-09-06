package br.com.pedro.web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfiguration {

    @Bean
    TaskScheduler userTransactionOutBoxScheduler() {
        var scheduler = new ThreadPoolTaskScheduler();
        scheduler.setVirtualThreads(true);
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("user-created-outbox-scheduler-");
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setRemoveOnCancelPolicy(true);
        return scheduler;
    }
}
