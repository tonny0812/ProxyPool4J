package com.proxy.pool.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/11/22.
 */
@Component
public class ScheduleJobs {
    @Autowired
    ProxyManager proxyManager;

    /**
     * 每六个小时跑一次任务
     */
    @Scheduled(cron="${cronJob.schedule}")
    public void cronJob() {
        System.out.println("Job Start...");
        System.out.println("Job End...");
    }
}
