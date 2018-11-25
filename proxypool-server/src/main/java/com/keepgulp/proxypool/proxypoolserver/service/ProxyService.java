package com.keepgulp.proxypool.proxypoolserver.service;

import com.keepgulp.proxypool.proxypoolserver.entity.Proxy;
import com.keepgulp.proxypool.proxypoolserver.repository.ProxyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProxyService {

    @Autowired
    private ProxyRepository proxyPepository;

    public Proxy save(Proxy proxy) {
        return proxyPepository.save(proxy);
    }

    public int saveAll(List<Proxy> proxyList) {
        List<Proxy> list = proxyPepository.saveAll(proxyList);
        return list.size();
    }

    public List<Proxy> getAllProxy() {
        return proxyPepository.findAll();
    }

    public Page<Proxy> getProxies(Pageable pageable) {
        return proxyPepository.findAll(pageable);
    }
}
