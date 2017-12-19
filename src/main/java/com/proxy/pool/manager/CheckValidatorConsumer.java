package com.proxy.pool.manager;

import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.BD.ProxyStatus;
import com.proxy.pool.spider.crawl.HttpManager;
import com.proxy.pool.validator.Validator;
import org.apache.http.HttpHost;

import java.util.concurrent.BlockingQueue;

public class CheckValidatorConsumer implements Runnable{
    private BlockingQueue<ProxyBD> resourceQueue;
    private BlockingQueue<ProxyBD> storageeQueue;

    public CheckValidatorConsumer(BlockingQueue<ProxyBD> rq, BlockingQueue<ProxyBD> sq) {
        this.resourceQueue = rq;
        this.storageeQueue = sq;
    }
     @Override
    public void run() {
         try{
             ProxyBD proxy;
             while(true) {
                 if(resourceQueue.isEmpty()) {
                     Thread.sleep(10000);
                 }else {
                     while(!resourceQueue.isEmpty()){
                         System.out.println(Thread.currentThread().getName() + " Queue's length:"+ resourceQueue.size());
                         proxy = resourceQueue.take();
                         ProxyStatus status = Validator.checkHttpProxy(HttpManager.selfIP, proxy, true);
                         if(status.isUseable()) {
                             proxy.setCountry("11");
                             proxy.setArea("aaa");
                             proxy.setProtocol(1);
                             proxy.setTypes(status.getTypes());
                             proxy.setSpeed(status.getSpeed());
                             storageeQueue.add(proxy);
                             System.out.println(proxy);
                         }
                         ProxyStatus status2 = Validator.checkHttpProxy(HttpManager.selfIP, proxy, false);
                         if(status2.isUseable()) {
                             proxy.setCountry("11");
                             proxy.setArea("aaa");
                             proxy.setProtocol(2);
                             proxy.setTypes(status.getTypes());
                             proxy.setSpeed(status.getSpeed());
                             storageeQueue.add(proxy);
                             System.out.println(proxy);
                         }
                     }
                 }
             }
         }catch(InterruptedException e) {
             e.printStackTrace();
         }
    }
}
