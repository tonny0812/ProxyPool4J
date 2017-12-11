package com.proxy.pool.start;

import com.proxy.pool.dao.ProxyMapper;
import com.proxy.pool.manager.ProxyManager;
import com.proxy.pool.model.Proxy;
import com.proxy.pool.spider.BD.ProxyBD;
import com.proxy.pool.spider.site.SitePool;
import com.proxy.pool.utils.ParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by tony on 2017/11/22.
 */
@Component
public class ScheduleTasks implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    ProxyMapper proxyMapper;

    @Autowired
    ProxyManager proxyManager;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

      if(contextRefreshedEvent.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")) {
            System.out.println("----------------------------------任务初始化 Start...----------------------------------");
            proxyManager.start();
            CopyOnWriteArrayList<ProxyBD> list = SitePool.proxyList;

            // 先删除旧的数据

            // 然后再进行插入新的proxy
            if (!CollectionUtils.isEmpty(list)) {
                for (ProxyBD p:list) {
                    Proxy proxy = ParserUtil.parseToProxy(p);
                    proxyMapper.insert(proxy);
                }
            }
            System.out.println("----------------------------------初始化任务 End...----------------------------------");
        }
    }

    /**
     * 间隔2小时执行
     */
    @Scheduled(cron = "0 0 */2 * * ? ") //
    public void cronJob() {
        System.out.println("----------------------------------Job Start...----------------------------------");
        proxyManager.start();
        CopyOnWriteArrayList<ProxyBD> list = SitePool.proxyList;

        // 先删除旧的数据

        // 然后再进行插入新的proxy
        if (!CollectionUtils.isEmpty(list)) {
            for (ProxyBD p:list) {
                Proxy proxy = ParserUtil.parseToProxy(p);
                proxyMapper.insert(proxy);
            }
        }
        System.out.println("----------------------------------Job End...----------------------------------");
    }
}
