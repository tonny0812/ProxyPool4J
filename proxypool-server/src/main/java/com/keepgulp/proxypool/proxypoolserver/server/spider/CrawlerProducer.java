package com.keepgulp.proxypool.proxypoolserver.server.spider;

import com.keepgulp.proxypool.proxypoolserver.server.ScheduleManager;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.PageBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.crawl.HttpManager;
import com.keepgulp.proxypool.proxypoolserver.server.spider.parser.ProxyListPageParser;
import com.keepgulp.proxypool.proxypoolserver.server.spider.parser.ProxyListPageParserFactory;
import com.keepgulp.proxypool.proxypoolserver.server.spider.site.SitePool;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CrawlerProducer implements Runnable {

    protected Set<String> urls;
    private BlockingQueue<ProxyBD> queue;
    private ExecutorService executorService;

    public CrawlerProducer(BlockingQueue<ProxyBD> q, Set<String> urls, int nThread){
        this.queue = q;
        this.urls = urls;
        executorService = Executors.newFixedThreadPool(nThread);
    }

    @Override
    public void run() {
        //获取IP及PORT，并将获取到的原始数据加入到队列中

        for(final Iterator<String> iterator = urls.iterator(); iterator.hasNext();) {
            executorService.execute(new Crawler(iterator.next()));
        }

    }

    class Crawler implements Runnable {
        private String url;
        public Crawler(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            final long requestStartTime = System.currentTimeMillis();
            final HttpGet tempRequest = null;
            try {
                PageBD page = HttpManager.get().getWebPage(url);
                int status = page.getStatusCode();
                long requestEndTime = System.currentTimeMillis();
                String logStr = Thread.currentThread().getName() + " "  +
                        "  executing request " + page.getUrl()  + " response statusCode:" + status +
                        "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
                log.info(logStr);
                if(status == HttpStatus.SC_OK){
                    handle(page, url);
                }
            } catch (IOException e) {

            } finally {
                if (tempRequest != null){
                    tempRequest.releaseConnection();
                }
            }
        }

        private void handle(PageBD page, String url){
            if (page == null || StringUtils.isEmpty(page.getHtml())){
                return ;
            }
            ProxyListPageParser parser = ProxyListPageParserFactory.getProxyListPageParser(SitePool.siteMap.get(url));
            if (parser!=null) {
                List<ProxyBD> proxyList = parser.parse(page.getHtml());
                if(!CollectionUtils.isEmpty(proxyList)) {
                    for(ProxyBD p : proxyList){
                        if(!ScheduleManager.set.contains(p)) {
                            ScheduleManager.set.add(p);
                            queue.add(p);
                        }
                    }
                }
            }
        }
    }
}
