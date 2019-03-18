/*
Navicat MySQL Data Transfer

Source Server         : shell
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : webcrawler

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-03-18 08:49:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for estimate
-- ----------------------------
DROP TABLE IF EXISTS `estimate`;
CREATE TABLE `estimate` (
  `startWord` varchar(255) DEFAULT NULL,
  `estiId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `linksXpath` varchar(255) DEFAULT NULL,
  `pagesInfoId` varchar(255) DEFAULT NULL,
  `contentXpath` varchar(255) DEFAULT NULL,
  `result` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `rateBar` varchar(255) DEFAULT NULL,
  `walkTimes` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`estiId`),
  CONSTRAINT `estiId` FOREIGN KEY (`estiId`) REFERENCES `website` (`webId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of estimate
-- ----------------------------
INSERT INTO `estimate` VALUES ('的', '1', 'con03', 'rt_con01', 'list05', '0', 'stop', '00.00%', '100');
INSERT INTO `estimate` VALUES ('的', '3', 'wz-list', '', 'zw', '0', 'stop', '00.00%', '100');
