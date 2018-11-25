package com.keepgulp.proxypool.proxypoolserver.server;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.CheckValidatorConsumer;
import com.keepgulp.proxypool.proxypoolserver.server.spider.CrawlerProducer;
import com.keepgulp.proxypool.proxypoolserver.server.spider.site.SitePool;
import com.keepgulp.proxypool.proxypoolserver.server.storage.ProxyStorageTask;
import com.keepgulp.proxypool.proxypoolserver.service.ProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static com.keepgulp.proxypool.proxypoolserver.server.Config.crawleProxyThreadNum;

@Slf4j
@Component
public class ScheduleManager implements ApplicationRunner {

    @Autowired
    private ProxyService proxyService;

    public static Set<ProxyBD> set = new HashSet<ProxyBD>();

    // 待验证代理队列
    private static BlockingQueue<ProxyBD> checkWaitingFreshQueue = new LinkedBlockingDeque<ProxyBD>();

    // 已验证待检查代理队列
    private static BlockingQueue<ProxyBD> checkWaitingOldQueue = new LinkedBlockingDeque<ProxyBD>();


    @Override
    public void run(ApplicationArguments args) throws Exception {

        CrawlerProducer producer = new CrawlerProducer(checkWaitingFreshQueue, SitePool.siteMap.keySet(), crawleProxyThreadNum);
        //开启 producer线程向队列中生产消息
        new Thread(producer).start();

        CheckValidatorConsumer consumer = new CheckValidatorConsumer(checkWaitingFreshQueue, checkWaitingOldQueue, Config.cheakProxyThreadNum);
        //开启 consumer线程 中队列中消费消息
        new Thread(consumer, "Check-Proxy1").start();
        new Thread(consumer, "Check-Proxy2").start();

        ProxyStorageTask proxyStorageTask = new ProxyStorageTask(checkWaitingOldQueue, proxyService);

        new Thread(proxyStorageTask, "Storage").start();

        log.info("Producer and Consumer has been started");

    }
}
