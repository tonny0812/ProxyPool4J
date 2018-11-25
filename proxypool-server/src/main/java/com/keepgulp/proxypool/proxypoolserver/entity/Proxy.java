package com.keepgulp.proxypool.proxypoolserver.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "proxies")
@Data
public class Proxy {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",columnDefinition="int(11) COMMENT '主键'")
    private Integer id;

    @Column(name = "ip",columnDefinition="varchar(16) DEFAULT NULL COMMENT 'IP地址'")
    private String ip;

    @Column(name = "port",columnDefinition="int(11) DEFAULT NULL COMMENT '端口'")
    private Integer port;

    @Column(name = "types",columnDefinition="int(11) DEFAULT NULL COMMENT '代理类型 0: 高匿,1:匿名,2 透明'")
    private Integer types;

    @Column(name = "protocol",columnDefinition="int(11) DEFAULT NULL COMMENT '协议类型'")
    private Integer protocol;

    @Column(name = "country",columnDefinition="varchar(100) DEFAULT NULL COMMENT '代理所属国家'")
    private String country;

    @Column(name = "area",columnDefinition="varchar(100) DEFAULT NULL COMMENT '代理所属地区'")
    private String area;

    @LastModifiedDate
    @Column(name = "updatetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatetime;

    @Column(name = "speed",columnDefinition="decimal(5,2) DEFAULT NULL COMMENT '代理速度'")
    private BigDecimal speed;

    @Column(name = "score",columnDefinition="int(11) DEFAULT NULL COMMENT '代理评分'")
    private Integer score;

}
