package com.proxy.pool.spider.crawl;

import com.alibaba.fastjson.JSONObject;
import com.proxy.pool.manager.ScheduleManager;
import com.proxy.pool.spider.BD.PageBD;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by tony on 2017/10/19.
 */
public class HttpManager {

    /**
     * 全局连接池对象
     */
    private static PoolingHttpClientConnectionManager connManager = null;

    public static PoolingHttpClientConnectionManager getConnManager() {
        return connManager;
    }

    public static String selfIP;
    /**
     * 配置连接池信息，支持http/https
     */
    static {
        SSLContext sslcontext = null;
        try {
            //获取TLS安全协议上下文
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }}, null);

            SSLConnectionSocketFactory scsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
            RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT)
                    .setExpectContinueEnabled(true)
                    .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                    .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build();
            Registry<ConnectionSocketFactory> sfr = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", scsf).build();

            connManager = new PoolingHttpClientConnectionManager(sfr);

            // 设置最大连接数
            connManager.setMaxTotal(200);
            // 设置每个连接的路由数
            connManager.setDefaultMaxPerRoute(20);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private HttpManager() {
        // 创建Http请求配置参数
        RequestConfig.Builder builder = RequestConfig.custom()
                // 获取连接超时时间
                .setConnectionRequestTimeout(20000)
                // 请求超时时间
                .setConnectTimeout(20000)
                // 响应超时时间
                .setSocketTimeout(20000);

        RequestConfig requestConfig = builder.build();

        // 创建httpClient
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder
                // 把请求相关的超时信息设置到连接客户端
                .setDefaultRequestConfig(requestConfig)
                // 配置连接池管理对象
                .setConnectionManager(connManager);

        CloseableHttpClient client =  httpClientBuilder.build();
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = null;
        try {
            HttpGet request = new HttpGet(ScheduleManager.TEST_IP);
            request.setHeaders(getHeaders());
            response = client.execute(request, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();// 连接代码
            if(200 == statusCode) {
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                System.out.println(content);
                JSONObject json = JSONObject.parseObject(content);
                selfIP = json.getString("origin");
                System.out.println("SelfIP:"+selfIP);
            }
        } catch (IOException e) {

        }
    }

    public static HttpManager get() {
        return Holder.MANAGER;
    }

    private static class Holder {
        private static final HttpManager MANAGER = new HttpManager();
    }

    /**
     * 创建新的HttpClient
     * @return
     */
    public CloseableHttpClient createHttpClient() {

        return createHttpClient(20000,null,null);
    }

    /**
     * 获取Http客户端连接对象
     * @param timeOut 超时时间
     * @param proxy   代理
     * @param cookie  Cookie
     * @return Http客户端连接对象
     */
    public CloseableHttpClient createHttpClient(int timeOut,HttpHost proxy,BasicClientCookie cookie) {

        // 创建Http请求配置参数
        RequestConfig.Builder builder = RequestConfig.custom()
                // 获取连接超时时间
                .setConnectionRequestTimeout(timeOut)
                // 请求超时时间
                .setConnectTimeout(timeOut)
                // 响应超时时间
                .setSocketTimeout(timeOut)
                .setCookieSpec(CookieSpecs.STANDARD);

        if (proxy!=null) {
            builder.setProxy(proxy);
        }

        RequestConfig requestConfig = builder.build();

        // 创建httpClient
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder
                // 把请求相关的超时信息设置到连接客户端
                .setDefaultRequestConfig(requestConfig)
                // 把请求重试设置到连接客户端
                .setRetryHandler(new RetryHandler())
                // 配置连接池管理对象
                .setConnectionManager(connManager);

        if (cookie!=null) {
            CookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(cookie);
            httpClientBuilder.setDefaultCookieStore(cookieStore);
        }

        return httpClientBuilder.build();
    }

    public CloseableHttpResponse getResponse(String url) {

        HttpGet request = new HttpGet(url);
        return getResponse(request);
    }

    public CloseableHttpResponse getResponse(HttpRequestBase request) {

        request.setHeader("User-Agent", Constant.userAgentArray[new Random().nextInt(Constant.userAgentArray.length)]);
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = null;
        try {
            response = createHttpClient().execute(request, httpClientContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Header[] getHeaders() {
        Header[] headers = new BasicHeader[5];
        headers[0] = new BasicHeader("User-Agent", Constant.userAgentArray[new Random().nextInt(Constant.userAgentArray.length)]);
        headers[1] = new BasicHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers[2] = new BasicHeader("Accept-Language","en-US,en;q=0.5");
        headers[3] = new BasicHeader("Connection","keep-alive");
        headers[4] = new BasicHeader("Accept-Encoding","gzip, deflate");
        return headers;
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
                .setConnectionManager(connManager);

        CloseableHttpClient client =  httpClientBuilder.build();
        HttpClientContext httpClientContext = HttpClientContext.create();
        CloseableHttpResponse response = null;
        String testUrl = proxy.getSchemeName().equalsIgnoreCase("http") ? ScheduleManager.TEST_HTTP_HEADER  : ScheduleManager.TEST_HTTPS_HEADER;
        try {
//            System.out.println("Test URL:" + testUrl);
            HttpGet request = new HttpGet(testUrl);
            request.setHeaders(getHeaders());
            response = client.execute(request, httpClientContext);
            int statusCode = response.getStatusLine().getStatusCode();// 连接代码
            if (statusCode == 200) {
                JSONObject json = JSONObject.parseObject(EntityUtils.toString(response.getEntity(),"UTF-8"));
                String origin = json.getString("origin");
                if(origin.indexOf(selfIP) > 0 || origin.indexOf(",") > 0) {
                    return false;
                }
                Header[] forwordArr = response.getHeaders("X-Forwarded-For" );
                Header[] realArr = response.getHeaders("X-Real-Ip");
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


    public PageBD getWebPage(String url) throws IOException {
        return getWebPage(url, "UTF-8");
    }

    public PageBD getWebPage(String url, String charset) throws IOException {
        PageBD page = new PageBD();
        CloseableHttpResponse response = HttpManager.get().getResponse(url);
        if (response!=null) {
            page.setStatusCode(response.getStatusLine().getStatusCode());
            page.setUrl(url);
            try {
                if(page.getStatusCode() == 200){
                    page.setHtml(EntityUtils.toString(response.getEntity(), charset));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return page;
    }

    public static void main(String[] args) {
        HttpHost httpHost = new HttpHost("118.193.107.96", 80 , "http");
        HttpManager.get().checkProxy(httpHost);
    }
}
