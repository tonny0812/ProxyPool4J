package com.keepgulp.proxypool.proxypoolserver.server.spider.BD;

public class PageBD {
    private String url;
    private int statusCode;//响应状态码
    private String html;//response content
    private ProxyBD proxyBD;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public ProxyBD getProxyBD() {
        return proxyBD;
    }

    public void setProxyBD(ProxyBD proxyBD) {
        this.proxyBD = proxyBD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageBD pageBD = (PageBD) o;

        return url.equals(pageBD.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
