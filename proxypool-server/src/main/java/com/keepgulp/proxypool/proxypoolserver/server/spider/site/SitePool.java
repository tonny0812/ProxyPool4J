package com.keepgulp.proxypool.proxypoolserver.server.spider.site;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.parser.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 代理URL池配置
 */
public class SitePool {

    public static CopyOnWriteArrayList<ProxyBD> proxyList = new CopyOnWriteArrayList<ProxyBD>();
    public final static Map<String, Class> siteMap = new HashMap<String, Class>();

    static {
        int pages = 1;
        for(int i = 1; i <= pages; i++){
            siteMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListPageParser.class);
            siteMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
            siteMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListPageParser.class);
            siteMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListPageParser.class);
        }
    }
}
