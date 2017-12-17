package com.proxy.pool.manager;

import com.proxy.pool.spider.BD.ProxyBD;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class ScheduleManager {

    public static Set<ProxyBD> set = new HashSet<ProxyBD>();

    public static final String TEST_HTTP_HEADER = "http://httpbin.org/get";
    public static final String TEST_HTTPS_HEADER = "https://httpbin.org/get";
    public static final String TEST_IP = "http://httpbin.org/ip";
    public static void main(String[] args) {
                 //创建大小为10的 BlockingQueue
                 BlockingQueue<ProxyBD> queue1 = new PriorityBlockingQueue<ProxyBD>();
                 BlockingQueue<ProxyBD> queue2 = new PriorityBlockingQueue<ProxyBD>();
                 CrawlerProducer producer = new CrawlerProducer(queue1, "http://www.xicidaili.com/wt/1.html");
                 CheckValidatorConsumer consumer = new CheckValidatorConsumer(queue1,queue2);
                 //开启 producer线程向队列中生产消息
                 new Thread(producer).start();
                 //开启 consumer线程 中队列中消费消息
                 new Thread(consumer).start();
                 System.out.println("Producer and Consumer has been started");
             }
}
