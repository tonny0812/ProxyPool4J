package com.keepgulp.proxypool.proxypoolserver.server.spider.parser;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * https://www.kuaidaili.com
 */
public class KuaidailiProxyListPageParser implements ProxyListPageParser {

    @Override
    public List<ProxyBD> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("#list table tbody tr");
        List<ProxyBD> proxyList = new ArrayList<ProxyBD>(elements.size());
        for (Element element : elements){
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String type = element.select("td:eq(3)").first().text();
            Integer protocol = ("http".equalsIgnoreCase(type)) ? 1:2;
            ProxyBD proxyBD = new ProxyBD(ip, Integer.valueOf(port));
            proxyBD.setProtocol(protocol);
            proxyList.add(proxyBD);
        }
        return proxyList;
    }
}
