package com.keepgulp.proxypool.proxypoolserver.server.util;

import com.keepgulp.proxypool.proxypoolserver.entity.Proxy;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;

import java.math.BigDecimal;

public class ParserUtil {

    public static Proxy parseToProxy(ProxyBD proxyBD) {
        Proxy proxy = new Proxy();
        proxy.setIp(proxyBD.getIp());
        proxy.setPort(proxyBD.getPort());
        proxy.setProtocol(proxyBD.getProtocol());
        proxy.setTypes(proxyBD.getTypes());
        proxy.setCountry(proxyBD.getCountry());
        proxy.setArea(proxyBD.getArea());
        proxy.setSpeed(new BigDecimal(proxyBD.getSpeed()));
        proxy.setScore(0);
        return proxy;
    }
}
