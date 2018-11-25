package com.keepgulp.proxypool.proxypoolserver.server.spider;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyStatus;
import com.keepgulp.proxypool.proxypoolserver.server.spider.crawl.HttpManager;
import com.keepgulp.proxypool.proxypoolserver.server.spider.validator.ProxyValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CheckValidatorConsumer implements Runnable{

    private BlockingQueue<ProxyBD> resourceQueue;
    private BlockingQueue<ProxyBD> storageeQueue;
    private ExecutorService executorService;

    public CheckValidatorConsumer(BlockingQueue<ProxyBD> rq, BlockingQueue<ProxyBD> sq, int nThreads) {
        this.resourceQueue = rq;
        this.storageeQueue = sq;
        nThreads = (nThreads == 0) ? 5 : nThreads;
        executorService = Executors.newFixedThreadPool(nThreads);
    }
     @Override
    public void run() {
         try{
             while(true) {
                 if(resourceQueue.isEmpty()) {
                     TimeUnit.SECONDS.sleep(1);
                 }else {
                     executorService.execute(new Runnable() {
                         @Override
                         public void run() {
                             ProxyBD proxy = null;
                             try {
                                 log.info(Thread.currentThread().getName() + "--->> UncheckProxyQueues' length: " + resourceQueue.size() +
                                         "\t" + "AvailableProxyQueue's length: " + storageeQueue.size());
                                 proxy = resourceQueue.take();
                             } catch (InterruptedException e) {
//                                 e.printStackTrace();
                                 return;
                             }
                             ProxyStatus status = ProxyValidator.checkHttpProxy(HttpManager.selfIP, proxy, true);
                             if(status.isUseable()) {
                                 proxy.setCountry("11");
                                 proxy.setArea("aaa");
                                 proxy.setProtocol(1);
                                 proxy.setTypes(status.getTypes());
                                 proxy.setSpeed(status.getSpeed());
                                 storageeQueue.add(proxy);
                                 log.info("http:--->>>>>>"+proxy);
                             }
                         }
                     });
                 }
             }
         }catch(InterruptedException e) {
//             e.printStackTrace();
         }
    }
}
