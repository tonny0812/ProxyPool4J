package com.proxy.pool.spider.task;

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
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * 获取代理IP入口
 */
public class ProxyPageCallable implements Callable<List<ProxyBD>>{

    protected Logger logger = Logger.getLogger(ProxyPageCallable.class.getName());

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
            logger.info(logStr);
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
                        result.add(p);
                    }
                }
            }
        }
        return result;
    }
}
