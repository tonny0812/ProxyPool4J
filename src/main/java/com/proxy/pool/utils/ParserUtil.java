package com.proxy.pool.utils;

import com.proxy.pool.model.Proxy;
import com.proxy.pool.spider.BD.ProxyBD;

import java.math.BigDecimal;
import java.util.Date;

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
        proxy.setUpdatetime(new Date());
        proxy.setScore(0);
        return proxy;
    }
}
