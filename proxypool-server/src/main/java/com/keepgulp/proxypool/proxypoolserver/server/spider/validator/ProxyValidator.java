package com.keepgulp.proxypool.proxypoolserver.server.spider.validator;

import com.alibaba.fastjson.JSONObject;
import com.keepgulp.proxypool.proxypoolserver.server.Config;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyBD;
import com.keepgulp.proxypool.proxypoolserver.server.spider.BD.ProxyStatus;
import com.keepgulp.proxypool.proxypoolserver.server.spider.crawl.HttpManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Slf4j
public class ProxyValidator {

    public static ProxyStatus checkHttpProxy(String selfIp, ProxyBD proxyBD, boolean isHttp) {
        ProxyStatus proxyStatus = new ProxyStatus();
        String protocol = isHttp ? "http" : "https";
        HttpHost proxy = new HttpHost(proxyBD.getIp(), proxyBD.getPort(), protocol);
        // 创建Http请求配置参数
        RequestConfig.Builder builder = RequestConfig.custom()
                // 获取连接超时时间
                .setConnectionRequestTimeout(2000)
                // 请求超时时间
                .setConnectTimeout(2000)
                // 响应超时时间
                .setSocketTimeout(2000)
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
        String testUrl = proxy.getSchemeName().equalsIgnoreCase("http") ? Config.TEST_HTTP_HEADER  : Config.TEST_HTTPS_HEADER;
//        String testUrl = ScheduleManager.TEST_URL;
        try {
            HttpGet request = new HttpGet(testUrl);
            request.setHeaders(HttpManager.getHeaders());
            response = client.execute(request, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();// 连接代码

            log.info(proxyBD.getIp() + ":" + proxyBD.getPort() + "," + " 测试代理返回:"+ statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String str = EntityUtils.toString(response.getEntity(),"UTF-8");
                log.info("返回内容：{}", str);
                if(!StringUtils.isEmpty(str)) {
                    JSONObject json = null;
                    try {
                        json = JSONObject.parseObject(str);
                    } catch (Throwable r) {
                        return proxyStatus;
                    }
                    String origin = json.getString("origin");

                    log.info("{}:{}:{}", proxyBD.getIp(), proxyBD.getPort(), origin);

                    Header[] forwordArr = response.getHeaders("X-Forwarded-For" );
                    Header[] realArr = response.getHeaders("X-Real-Ip");
                    if(origin.indexOf(HttpManager.selfIP) > -1 || origin.indexOf(",") > -1) {
                        proxyStatus.setUseable(false);
                        return proxyStatus;
                    } else if(forwordArr.length == 0 && realArr.length == 0) {
                        proxyStatus.setUseable(true);
                        proxyStatus.setTypes(0);
                    } else {
                        proxyStatus.setUseable(true);
                        proxyStatus.setTypes(2);
                    }
                }
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return proxyStatus;
    }
}
