/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50554
Source Host           : localhost:3306
Source Database       : proxy

Target Server Type    : MYSQL
Target Server Version : 50554
File Encoding         : 65001

Date: 2017-11-28 21:48:18
*/

/*
Name	Type	Description
types	int	0: 高匿,1:匿名,2 透明
protocol	int	0: http, 1 https, 2 http/https
country	str	取值为 国内, 国外
area	str	地区
*/


SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for proxys
-- ----------------------------
DROP TABLE IF EXISTS `proxies`;
CREATE TABLE `proxies` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(16) NOT NULL,
  `port` int(11) NOT NULL,
  `types` int(11) NOT NULL,
  `protocol` int(11) NOT NULL,
  `country` varchar(100) NOT NULL,
  `area` varchar(100) NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  `speed` decimal(5,2) NOT NULL,
  `score` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12944 DEFAULT CHARSET=utf8;
