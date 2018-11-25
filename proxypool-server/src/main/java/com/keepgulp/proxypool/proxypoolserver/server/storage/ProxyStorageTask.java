package com.keepgulp.proxypool.proxypoolserver.server.storage;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.util.ParserUtil;
import com.keepgulp.proxypool.proxypoolserver.service.ProxyService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ProxyStorageTask implements Runnable {

    private ProxyService proxyService;

    private BlockingQueue<ProxyBD> queue;

    public ProxyStorageTask(BlockingQueue<ProxyBD> q, ProxyService proxyService){
        this.queue = q;
        this.proxyService = proxyService;
    }

    @Override
    public void run() {
        while(true) {
            if(queue.isEmpty()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                ProxyBD proxyBD = queue.poll();
                if(null != proxyBD)
                    this.proxyService.save(ParserUtil.parseToProxy(proxyBD));
            }
        }
    }
}
