package com.proxy.pool.spider.BD;

public class ProxyStatus {
    private boolean useable = false;
    private Integer types = -1;
    private Integer speed = -1;

    public boolean isUseable() {
        return useable;
    }

    public void setUseable(boolean useable) {
        this.useable = useable;
    }

    public Integer getTypes() {
        return types;
    }

    public void setTypes(Integer types) {
        this.types = types;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }
}
