package com.proxy.pool.model;

import java.math.BigDecimal;
import java.util.Date;

public class Proxy {
    private Integer id;

    private String ip;

    private Integer port;

    private Integer types;

    private Integer protocol;

    private String country;

    private String area;

    private Date updatetime;

    private BigDecimal speed;

    private Integer score;

    public Proxy(Integer id, String ip, Integer port, Integer types, Integer protocol, String country, String area, Date updatetime, BigDecimal speed, Integer score) {
        this.id = id;
        this.ip = ip;
        this.port = port;
        this.types = types;
        this.protocol = protocol;
        this.country = country;
        this.area = area;
        this.updatetime = updatetime;
        this.speed = speed;
        this.score = score;
    }

    public Proxy() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getPort() {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "id=" + id +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", types=" + types +
                ", protocol=" + protocol +
                ", country='" + country + '\'' +
                ", area='" + area + '\'' +
                ", updatetime=" + updatetime +
                ", speed=" + speed +
                ", score=" + score +
                '}';
    }
}