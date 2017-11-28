package com.proxy.pool.dao;

import com.proxy.pool.model.Proxy;

public interface ProxyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Proxy record);

    int insertSelective(Proxy record);

    Proxy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Proxy record);

    int updateByPrimaryKey(Proxy record);
}