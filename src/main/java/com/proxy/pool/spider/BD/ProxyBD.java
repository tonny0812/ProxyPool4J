package com.proxy.pool.spider.BD;

/*
*
Name	Type	Description
types	int	0: 高匿,1:匿名,2 透明
protocol	int	0: http, 1 https, 2 http/https
country	str	取值为 国内, 国外
area	str	地区
* */
public class ProxyBD implements Comparable{

    private String ip;
    private Integer port;
    private Integer types;
    private Integer protocol;
    private Integer speed;
    private String country;
    private String area;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

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

