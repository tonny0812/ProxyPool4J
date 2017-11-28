package com.proxy.pool.spider.site;


import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.parser.Ip181ProxyListPageParser;

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
        int pages = 2;
        for(int i = 1; i <= pages; i++){
            siteMap.put("http://www.ip181.com/daili/" + i + ".html", Ip181ProxyListPageParser.class);
        }
    }
}
