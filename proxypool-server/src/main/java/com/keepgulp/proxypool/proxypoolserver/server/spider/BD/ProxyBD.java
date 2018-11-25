package com.keepgulp.proxypool.proxypoolserver.server.spider.BD;

import lombok.Data;

/*
*
Name	Type	Description
types	int	0: 高匿,1:匿名,2 透明
protocol	int	0: http, 1 https, 2 http/https
country	str	取值为 国内, 国外
area	str	地区
* */
@Data
public class ProxyBD implements Comparable{

    private String ip;
    private Integer port;
    private Integer types;
    private Integer protocol;
    private Integer speed;
    private String country;
    private String area;

    public ProxyBD(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ProxyBD{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", types=" + types +
                ", protocol=" + protocol +
                ", speed=" + speed +
                ", country='" + country + '\'' +
                ", area='" + area + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        ProxyBD p = (ProxyBD) obj;
        if(this.getIp().equals(p.getIp()) && this.getPort() == p.getPort()) {
            return true;
        }
        return false;
    }
}

