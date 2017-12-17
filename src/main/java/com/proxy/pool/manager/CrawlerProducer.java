package com.proxy.pool.manager;

import com.proxy.pool.spider.BD.PageBD;
import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.crawl.HttpManager;
import com.proxy.pool.spider.parser.ProxyListPageParser;
import com.proxy.pool.spider.parser.ProxyListPageParserFactory;
import com.proxy.pool.spider.site.SitePool;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class CrawlerProducer implements Runnable {

    protected String url;
    private BlockingQueue<ProxyBD> queue;

    public CrawlerProducer(BlockingQueue<ProxyBD> q, String url){
        this.queue = q;
        this.url = url;
    }

    @Override
    public void run() {
        //获取IP及PORT，并将获取到的原始数据加入到队列中
        long requestStartTime = System.currentTimeMillis();
        HttpGet tempRequest = null;
        try {
            PageBD page = HttpManager.get().getWebPage(url);
            int status = page.getStatusCode();
            long requestEndTime = System.currentTimeMillis();
            String logStr = Thread.currentThread().getName() + " "  +
                    "  executing request " + page.getUrl()  + " response statusCode:" + status +
                    "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
//            logger.info(logStr);
            if(status == HttpStatus.SC_OK){
                handle(page);
            }
        } catch (IOException e) {

        } finally {
            if (tempRequest != null){
                tempRequest.releaseConnection();
            }
        }
    }

    private void handle(PageBD page){
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
