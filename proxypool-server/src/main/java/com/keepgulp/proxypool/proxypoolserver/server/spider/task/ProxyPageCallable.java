package com.keepgulp.proxypool.proxypoolserver.server.spider.task;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * 获取代理IP入口
 */
@Slf4j
public class ProxyPageCallable implements Callable<List<ProxyBD>>{

    protected String url;

    public ProxyPageCallable(String url){
        this.url = url;
    }

    @Override
    public List<ProxyBD> call() throws Exception {

        long requestStartTime = System.currentTimeMillis();
        HttpGet tempRequest = null;
        try {
            PageBD page = HttpManager.get().getWebPage(url);
            int status = page.getStatusCode();
            long requestEndTime = System.currentTimeMillis();
            String logStr = Thread.currentThread().getName() + " "  +
                    "  executing request " + page.getUrl()  + " response statusCode:" + status +
                    "  request cost time:" + (requestEndTime - requestStartTime) + "ms";
            log.info(logStr);
            if(status == HttpStatus.SC_OK){
                return handle(page);
            }

        } catch (IOException e) {

        } finally {
            if (tempRequest != null){
                tempRequest.releaseConnection();
            }
        }

        return null;
    }

    /**
     * 将下载的proxy放入代理池
     * @param page
     */
    private List<ProxyBD> handle(PageBD page){

        if (page == null || StringUtils.isEmpty(page.getHtml())){
            return null;
        }
        List<ProxyBD> result = new ArrayList<ProxyBD>();

        ProxyListPageParser parser = ProxyListPageParserFactory.getProxyListPageParser(SitePool.siteMap.get(url));
        if (parser!=null) {
            List<ProxyBD> proxyList = parser.parse(page.getHtml());
            if(!CollectionUtils.isEmpty(proxyList)) {
                for(ProxyBD p : proxyList){
                    if (!SitePool.proxyList.contains(p)) {
                        p.setArea("222");
                        p.setCountry("111");
                        p.setTypes(0);
                        p.setSpeed(0);
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }
}
