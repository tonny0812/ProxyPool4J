package com.proxy.pool.spider.parser;

import com.proxy.pool.spider.BD.ProxyBD;

import java.util.List;

public interface ProxyListPageParser {
    List<ProxyBD> parse(String content);
}
