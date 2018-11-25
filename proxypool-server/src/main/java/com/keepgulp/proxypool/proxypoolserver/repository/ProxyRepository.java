package com.keepgulp.proxypool.proxypoolserver.repository;

import com.keepgulp.proxypool.proxypoolserver.entity.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyRepository extends JpaRepository<Proxy,Integer>, JpaSpecificationExecutor<Proxy> {
}
