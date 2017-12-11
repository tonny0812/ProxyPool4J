package com.proxy.pool.manager;

import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.crawl.HttpManager;
import com.proxy.pool.spider.site.SitePool;
import com.proxy.pool.spider.task.ProxyPageCallable;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import org.apache.http.HttpHost;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Created by tony on 2017/10/25.
 */
@Component
public class ProxyManager {

    protected Logger logger = Logger.getLogger(ProxyManager.class.getName());
    /**
     * 抓取代理，成功的代理存放到SitePool中
     */
    public void start() {

        Flowable.fromIterable(SitePool.siteMap.keySet())
                .parallel()
                .map(new Function<String, List<ProxyBD>>() {
                    @Override
                    public List<ProxyBD> apply(String s) throws Exception {
                        try {
                            return new ProxyPageCallable(s).call();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .flatMap(new Function<List<ProxyBD>, Publisher<ProxyBD>>() {
                    @Override
                    public Publisher<ProxyBD> apply(List<ProxyBD> proxies) throws Exception {

                        if (proxies == null) return null;

                        List<ProxyBD> result = proxies
                                .stream()
                                .parallel()
                                .filter(new Predicate<ProxyBD>() {
                            @Override
                            public boolean test(ProxyBD proxy) {
                                String protocol = proxy.getProtocol() == 1 ? "http":"https";
                                HttpHost httpHost = new HttpHost(proxy.getIp(), proxy.getPort(), protocol);
                                boolean flag = HttpManager.get().checkProxy(httpHost);
//                                logger.info("--------------------------->>>>>> Proxy = "+ proxy.toString() + " : " + flag);
                                return flag;
                            }
                        }).collect(Collectors.toList());

                        return Flowable.fromIterable(result);
                    }
                })
                .sequential()
                .subscribe(new Consumer<ProxyBD>() {
                    @Override
                    public void accept(ProxyBD proxy) throws Exception {
                        logger.info("<<<<<@@@@@------------- Proxy = "+ proxy.toString());
                        SitePool.proxyList.add(proxy);
                    }
                });
    }

    public static void main(String[] args) {
        ProxyManager proxyManager =  new ProxyManager();
        proxyManager.start();
    }
}
