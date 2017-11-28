package com.proxy.pool.spider.parser;


import com.proxy.pool.spider.BD.ProxyBD;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Ip181ProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<ProxyBD> parse(String content) {
        Document document = Jsoup.parse(content);
        Elements elements = document.select("table tr:gt(0)");
        List<ProxyBD> proxyList = new ArrayList<ProxyBD>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String isAnonymous = element.select("td:eq(2)").first().text();
            String type = element.select("td:eq(3)").first().text();
            Integer protocol = ("http".equalsIgnoreCase(type)) ? 1:2;
            ProxyBD proxyBD = new ProxyBD(ip, Integer.valueOf(port));
            proxyBD.setProtocol(protocol);
            proxyList.add(proxyBD);
        }
        return proxyList;
    }
}
