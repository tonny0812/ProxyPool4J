package com.keepgulp.proxypool.proxypoolserver.server.util;

/**
 *
 **/
public enum SiteEnum {
    ERROR(Integer.valueOf(0), "错误"),

    RUNNING(Integer.valueOf(1), "任务运行中"),

    SUCCESS(Integer.valueOf(2), "任务运行成功"),

    FAILED(Integer.valueOf(3), "任务运行失败"),

    WAITING(Integer.valueOf(4), "任务等待中"),

    TIMTOUT(Integer.valueOf(5), "任务超时");

    private Integer val;

    private String msg;

    private SiteEnum(Integer val, String msg) {
        this.val = val;
        this.msg = msg;
    }

    public Integer getVal() {
        return this.val;
    }

    public String getMsg() {
        return this.msg;
    }

    public static SiteEnum get(Integer val) {
        if(val != null) {
            SiteEnum[] arr$ = values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                SiteEnum statusEnum = arr$[i$];
                if(val.equals(statusEnum.getVal())) {
                    return statusEnum;
                }
            }
        }

        return null;
    }

    public boolean equalsVal(Integer val) {
        return this.getVal().equals(val);
    }
}
