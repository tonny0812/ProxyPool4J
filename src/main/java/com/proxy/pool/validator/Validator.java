package com.proxy.pool.validator;

import com.alibaba.fastjson.JSONObject;
import com.proxy.pool.manager.ScheduleManager;
import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.BD.ProxyStatus;
import com.proxy.pool.spider.crawl.HttpManager;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Validator {

    public static ProxyStatus checkHttpProxy(String selfIp, ProxyBD proxyBD, boolean isHttp) {
        ProxyStatus proxyStatus = new ProxyStatus();
        String protocol = isHttp ? "http" : "https";
        HttpHost proxy = new HttpHost(proxyBD.getIp(), proxyBD.getPort(), protocol);
        // 创建Http请求配置参数
        RequestConfig.Builder builder = RequestConfig.custom()
                // 获取连接超时时间
                .setConnectionRequestTimeout(20000)
                // 请求超时时间
                .setConnectTimeout(20000)
                // 响应超时时间
                .setSocketTimeout(20000)
                .setProxy(proxy);

        RequestConfig requestConfig = builder.build();

        // 创建httpClient
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder
                // 把请求相关的超时信息设置到连接客户端
                .setDefaultRequestConfig(requestConfig)
                // 配置连接池管理对象
                .setConnectionManager(HttpManager.getConnManager());

        CloseableHttpClient client =  httpClientBuilder.build();
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = null;
        String testUrl = proxy.getSchemeName().equalsIgnoreCase("http") ? ScheduleManager.TEST_HTTP_HEADER  : ScheduleManager.TEST_HTTPS_HEADER;
        try {
//            System.out.println("Test URL:" + testUrl);
            HttpGet request = new HttpGet(testUrl);
            request.setHeaders(HttpManager.getHeaders());
            response = client.execute(request, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();// 连接代码
            if (statusCode == 200) {
                JSONObject json = JSONObject.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
                String origin = json.getString("origin");
                Header[] forwordArr = response.getHeaders("X-Forwarded-For" );
                Header[] realArr = response.getHeaders("X-Real-Ip");
                if(origin.indexOf(HttpManager.selfIP) > 0 || origin.indexOf(",") > 0) {
                    return proxyStatus;
                } else if(forwordArr.length == 0 && realArr.length == 0) {
                    proxyStatus.setTypes(0);
                } else {
                    proxyStatus.setTypes(2);
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }

        return proxyStatus;
    }
}
