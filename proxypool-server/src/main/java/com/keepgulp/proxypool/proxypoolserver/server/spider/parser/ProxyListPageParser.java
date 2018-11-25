package com.keepgulp.proxypool.proxypoolserver.server.spider.parser;

import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;

import java.util.List;

public interface ProxyListPageParser {
    List<ProxyBD> parse(String content);
}
