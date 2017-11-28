package com.proxy.pool.dao;

import com.proxy.pool.BaseTest;
import com.proxy.pool.model.Proxy;
import org.junit.Test;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

public class ProxyMapperTest extends BaseTest {

    @Resource
    private ProxyMapper proxyMapper;

    @Test
    public void deleteByPrimaryKey() throws Exception {
        proxyMapper.deleteByPrimaryKey(12944);
    }

    @Test
    public void insert() throws Exception {
        proxyMapper.insert(new Proxy(null, "11.10.10.10", 111, 1, 2, "测试", "测试", new Date(), new BigDecimal(110), 10));
    }

    @Test
    public void selectByPrimaryKey() throws Exception {
        Proxy proxy = proxyMapper.selectByPrimaryKey(12945);
        logger.info(proxy.toString());
    }

    @Test
    public void updateByPrimaryKey() throws Exception {
        Proxy proxy = proxyMapper.selectByPrimaryKey(12944);
        proxy.setScore(2);
        proxy.setUpdatetime(new Date());
        proxyMapper.updateByPrimaryKey(proxy);
    }

}