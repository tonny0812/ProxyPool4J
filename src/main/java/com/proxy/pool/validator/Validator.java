package com.proxy.pool.validator;

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

import java.io.IOException;

public class Validator {

    public String getMyIp() {
        return "";
    }

    public ProxyStatus checkHttpProxy(String selfIp, ProxyBD proxyBD, boolean isHttp) {
        return null;
    }

    public boolean checkProxy(HttpHost proxy) {

        if (proxy == null) return false;

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
        try {
            HttpGet request = new HttpGet("http://www.163.com/");
            response = client.execute(request, httpClientContext);

            int statusCode = response.getStatusLine().getStatusCode();// 连接代码
            if (statusCode == 200) {
                Header[] forwordArr = response.getHeaders("X-Forwarded-For" );
                Header[] realArr = response.getHeaders("'X-Real-Ip" );
                if(forwordArr.length > 0 ) {
                    for(Header h : forwordArr) {
                        System.out.println("------->>" + h.toString());
                    }
                }
                if(realArr.length > 0 ) {
                    for(Header h : realArr) {
                        System.out.println("-------@@" + h.toString());
                    }
                }

                return true;
            }
        } catch (IOException e) {
//            e.printStackTrace();
            return false;
        }

        return false;
    }
}
