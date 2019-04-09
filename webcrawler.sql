/*
Navicat MySQL Data Transfer

Source Server         : data
Source Server Version : 80015
Source Host           : localhost:3306
Source Database       : webcrawler2

Target Server Type    : MYSQL
Target Server Version : 80015
File Encoding         : 65001

Date: 2019-04-08 13:14:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apibaseconf
-- ----------------------------
DROP TABLE IF EXISTS `apibaseconf`;
CREATE TABLE `apibaseconf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `inputXpath` varchar(256) NOT NULL DEFAULT '',
  `submitXpath` varchar(256) NOT NULL DEFAULT '',
  `infoLinkXpath` varchar(256) DEFAULT '' COMMENT '用于指定返回查询页面上数据链接的位置，用于帮助链接收集器收集链接\n当此值为空时，运行收集器的默认行为',
  `payloadXpath` varchar(256) NOT NULL DEFAULT '' COMMENT 'format:\nxpath,name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of apibaseconf
-- ----------------------------
INSERT INTO `apibaseconf` VALUES ('2', '124', 'http://10.24.13.223:8080/hbky/privateFileManager/grwpgl', '//*[@id=\"searchtext\"]', '//div[@class=\"searchIcon\"]', '//div[@id=\"allwenjian\"]//a[@href]', '//div[@id=\"allwenjian\"]//div[@class=\"filename\"],title');

-- ----------------------------
-- Table structure for current
-- ----------------------------
DROP TABLE IF EXISTS `current`;
CREATE TABLE `current` (
  `webId` int(20) unsigned NOT NULL,
  `round` varchar(256) NOT NULL DEFAULT '0',
  `M1status` varchar(256) NOT NULL DEFAULT 'inactive',
  `M2status` varchar(256) NOT NULL DEFAULT 'inactive',
  `M3status` varchar(256) NOT NULL DEFAULT 'inactive',
  `M4status` varchar(256) NOT NULL DEFAULT 'inactive',
  `SampleData_sum` int(11) NOT NULL DEFAULT '0',
  `run` bigint(20) NOT NULL DEFAULT '0',
  KEY `round` (`round`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of current
-- ----------------------------
INSERT INTO `current` VALUES ('126', '1', 'stop', 'done', 'done', 'done', '245945', '0');

-- ----------------------------
-- Table structure for estimate
-- ----------------------------
DROP TABLE IF EXISTS `estimate`;
CREATE TABLE `estimate` (
 `contentLocation` varchar(255) DEFAULT '',
  `startWord` varchar(255) NOT NULL DEFAULT '',
  `estiId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `linksXpath` varchar(255) NOT NULL DEFAULT '',
  `pagesInfoId` varchar(255) NOT NULL DEFAULT '',
  `contentXpath` varchar(255) NOT NULL DEFAULT '',
  `result` varchar(255) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL DEFAULT '',
  `rateBar` varchar(255) NOT NULL DEFAULT '',
  `walkTimes` varchar(255) NOT NULL DEFAULT '',
  `querySend` varchar(255) NOT NULL DEFAULT '',
  `pid` varchar(255) DEFAULT '',
  PRIMARY KEY (`estiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of estimate
-- ----------------------------

-- ----------------------------
-- Table structure for extraconf
-- ----------------------------
DROP TABLE IF EXISTS `extraconf`;
CREATE TABLE `extraconf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `userNameXpath` varchar(1024) NOT NULL DEFAULT '',
  `passwordXpath` varchar(1024) DEFAULT '',
  `userName` varchar(1024) NOT NULL DEFAULT '',
  `password` varchar(1024) NOT NULL DEFAULT '',
  `loginUrl` varchar(1024) NOT NULL DEFAULT '',
  `submitXpath` varchar(1024) NOT NULL DEFAULT '',
  `threadNum` int(11) NOT NULL DEFAULT '5',
  `timeout` bigint(20) NOT NULL DEFAULT '3000',
  `charset` varchar(256) NOT NULL DEFAULT '',
  `databaseSize` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of extraconf
-- ----------------------------
INSERT INTO `extraconf` VALUES ('8', '122', '', '', '', '', '', '', '5', '3000', 'UTF-8', '0');
INSERT INTO `extraconf` VALUES ('9', '123', 'txtUserName', 'txtPassword', '431200000000', 'aaaaaa', 'http://ai.inspur.com/login', 'btnLogin', '5', '3000', 'UTF-8', '13');
INSERT INTO `extraconf` VALUES ('10', '124', '//*[@id=\"j_username\"]', '//*[@id=\"j_password\"]', 'zongyb', 'abing201!2', 'http://10.24.13.223:8080/hbky/index.jsp#', '//*[@id=\"submit_btn\"]', '5', '3000', 'UTF-8', '0');
INSERT INTO `extraconf` VALUES ('11', '125', '//*[@id=\"j_username\"]', '//*[@id=\"j_password\"]', 'zongyb', 'abing201!2', 'http://10.24.13.223:8080/hbky/index.jsp#', '//*[@id=\"submit_btn\"]', '20', '30000', 'UTF-8', '0');
INSERT INTO `extraconf` VALUES ('12', '126', 'txtUserName', 'txtPassword', '431200000000', 'aaaaaa', 'http://ai.inspur.com/login', 'btnLogin', '20', '8000', 'UTF-8', '249951');

-- ----------------------------
-- Table structure for formsbymd5
-- ----------------------------
DROP TABLE IF EXISTS `formsbymd5`;
CREATE TABLE `formsbymd5` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `formMd5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of formsbymd5
-- ----------------------------

-- ----------------------------
-- Table structure for jsonbase
-- ----------------------------
DROP TABLE IF EXISTS `jsonbase`;
CREATE TABLE `jsonbase` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `pageSize` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
  `totalAddress` varchar(256) DEFAULT '' COMMENT '总页数在json response中的位置',
  `contentAddress` varchar(256) NOT NULL DEFAULT '' COMMENT 'if value is an empty string, the root is cotent address',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of jsonbase
-- ----------------------------
INSERT INTO `jsonbase` VALUES ('2', '126', '1000', '/d/total', '/d/rows');

-- ----------------------------
-- Table structure for jsonbaseconf
-- ----------------------------
DROP TABLE IF EXISTS `jsonbaseconf`;
CREATE TABLE `jsonbaseconf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `paramQuery` varchar(256) NOT NULL DEFAULT '',
  `paramPage` varchar(256) NOT NULL DEFAULT '',
  `pageStrategy` varchar(256) NOT NULL DEFAULT '',
  `constString` varchar(1024) NOT NULL DEFAULT '',
  `totalAddress` varchar(256) DEFAULT '' COMMENT '总页数在json response中的位置',
  `contentAddress` varchar(256) NOT NULL DEFAULT '' COMMENT 'if value is an empty string, the root is cotent address',
  `linkRule` varchar(1024) DEFAULT '',
  `payloadRule` varchar(1024) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of jsonbaseconf
-- ----------------------------
INSERT INTO `jsonbaseconf` VALUES ('1', '125', 'http://10.24.13.223:8080/hbky/search/getResult?', 'keyword', 'pageIndex', '1,1', 'type=0&searchtime=0', '/0/sum', '', '[http://10.24.13.223:8080/hbky/lucene/wjdownload?path=]+/path+[&filename=]+/filename+[&fileid=]+/fileid+[&category=]+/category', '/content');

-- ----------------------------
-- Table structure for pattern
-- ----------------------------
DROP TABLE IF EXISTS `pattern`;
CREATE TABLE `pattern` (
  `webId` int(20) unsigned NOT NULL DEFAULT '0',
  `patternName` varchar(255) NOT NULL DEFAULT 'fulltext',
  `xpath` varchar(1024) NOT NULL DEFAULT '',
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pattern
-- ----------------------------

-- ----------------------------
-- Table structure for pattern_structed
-- ----------------------------
DROP TABLE IF EXISTS `pattern_structed`;
CREATE TABLE `pattern_structed` (
  `webId` int(11) unsigned NOT NULL,
  `patternName` varchar(255) NOT NULL DEFAULT '' COMMENT '主键，模板名称，每个网站都有两个默认模板fulltext和table',
  `xpath` varchar(255) NOT NULL DEFAULT '' COMMENT '该模板在该网站页面中的xpath，多个xpath可以用##分割',
  `formula` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(255) NOT NULL DEFAULT '',
  `headerXPath` varchar(1024) NOT NULL DEFAULT '',
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `webId` (`webId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of pattern_structed
-- ----------------------------
INSERT INTO `pattern_structed` VALUES ('123', 'a', '/html/body/div[3]/div/div[1]/div[1]/div[1]/div/table', '(a+b)*(c+d)', 'formula', '/', '2');
INSERT INTO `pattern_structed` VALUES ('123', 'b', '/html/body/div[3]/div/div[1]/div[2]/div[1]/div/table', '(a+b)*(c+d)', 'formula', '/', '3');
INSERT INTO `pattern_structed` VALUES ('123', 'c', '/html/body/div[3]/div/div[1]/div[1]/div[2]/div/table', '(a+b)*(c+d)', 'formula', '/', '4');
INSERT INTO `pattern_structed` VALUES ('123', 'd', '/html/body/div[3]/div/div[1]/div[2]/div[2]/table', '(a+b)*(c+d)', 'formula', '/', '5');
INSERT INTO `pattern_structed` VALUES ('123', 'subpage_data', 'pcObj', 'pcObj', 'json', '/subpage', '6');

-- ----------------------------
-- Table structure for queryparam
-- ----------------------------
DROP TABLE IF EXISTS `queryparam`;
CREATE TABLE `queryparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `webId` int(11) unsigned NOT NULL DEFAULT '0',
  `dataParamList` varchar(255) NOT NULL DEFAULT '',
  `N` varchar(255) NOT NULL DEFAULT '',
  `C` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `webId` (`webId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of queryparam
-- ----------------------------
INSERT INTO `queryparam` VALUES ('1', '126', '一般贫困户,低保户,五保户,低保贫困户;因病,因残,因学,因灾,缺土地,缺水', '24', '6');

-- ----------------------------
-- Table structure for requesttable
-- ----------------------------
DROP TABLE IF EXISTS `requesttable`;
CREATE TABLE `requesttable` (
  `requestID` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestName` varchar(1024) NOT NULL DEFAULT '',
  `requestDesc` varchar(2048) NOT NULL DEFAULT '',
  `createdTime` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`requestID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of requesttable
-- ----------------------------

-- ----------------------------
-- Table structure for sense
-- ----------------------------
DROP TABLE IF EXISTS `sense`;
CREATE TABLE `sense` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homeUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `targetUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sense
-- ----------------------------

-- ----------------------------
-- Table structure for sensestate
-- ----------------------------
DROP TABLE IF EXISTS `sensestate`;
CREATE TABLE `sensestate` (
  `id` int(20) NOT NULL,
  `allLinks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trueLinks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of sensestate
-- ----------------------------

-- ----------------------------
-- Table structure for status
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `webId` int(20) unsigned NOT NULL,
  `statusId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `round` varchar(256) NOT NULL DEFAULT '0',
  `type` varchar(24) NOT NULL DEFAULT 'query',
  `fLinkNum` int(11) unsigned NOT NULL DEFAULT '0',
  `sLinkNum` int(11) unsigned NOT NULL DEFAULT '0',
  KEY `round` (`round`),
  KEY `statusId` (`statusId`)
) ENGINE=InnoDB AUTO_INCREMENT=7134 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('126', '7130', '0', 'info', '0', '245945');
INSERT INTO `status` VALUES ('126', '7131', '0', 'query', '0', '250');
INSERT INTO `status` VALUES ('126', '7132', '1', 'info', '0', '0');
INSERT INTO `status` VALUES ('126', '7133', '1', 'query', '0', '0');

-- ----------------------------
-- Table structure for structedparam
-- ----------------------------
DROP TABLE IF EXISTS `structedparam`;
CREATE TABLE `structedparam` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `webId` int(11) unsigned NOT NULL DEFAULT '0',
  `iframeNav` varchar(255) NOT NULL DEFAULT '',
  `navValue` varchar(255) NOT NULL DEFAULT '',
  `iframeCon` varchar(255) NOT NULL DEFAULT '',
  `searchButton` varchar(255) NOT NULL DEFAULT '',
  `resultRow` varchar(255) NOT NULL DEFAULT '',
  `nextPageXPath` varchar(255) NOT NULL DEFAULT '',
  `pageNumXPath` varchar(255) NOT NULL DEFAULT '',
  `iframeSubParam` varchar(255) NOT NULL DEFAULT '',
  `arrow` varchar(255) NOT NULL DEFAULT '',
  `paramList` varchar(255) NOT NULL DEFAULT '',
  `paramValueList` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of structedparam
-- ----------------------------
INSERT INTO `structedparam` VALUES ('2', '123', 'ifmNav', '贫困县', 'ifmCon', 'btnSearch', 'Aaa003', '/html/body/div[3]/div/div[2]/table/tbody/tr/td[8]/a', '/html/body/div[3]/div/div[2]/table/tbody/tr/td[5]/input', 'Aaa003', 'combo-arrow', '/', '_easyui_combobox_i8_0,_easyui_combobox_i8_1,_easyui_combobox_i8_2,_easyui_combobox_i8_3,_easyui_combobox_i8_4,_easyui_combobox_i8_5');

-- ----------------------------
-- Table structure for urlbaseconf
-- ----------------------------
DROP TABLE IF EXISTS `urlbaseconf`;
CREATE TABLE `urlbaseconf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `paramQuery` varchar(256) NOT NULL DEFAULT '',
  `paramPage` varchar(256) NOT NULL DEFAULT '',
  `startPageNum` varchar(256) NOT NULL DEFAULT '',
  `paramList` varchar(1024) NOT NULL DEFAULT '',
  `paramValueList` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of urlbaseconf
-- ----------------------------
INSERT INTO `urlbaseconf` VALUES ('4', '122', 'http://www.zhaoan.gov.cn/cms/siteresource/search.shtml?', 'key', 'page', '1,1', 'searchSiteId,siteId,pageName', '60427348114130001,60427348114130001,quickSiteSearch');
INSERT INTO `urlbaseconf` VALUES ('5', '126', 'http://ai.inspur.com/Archive/PoorFamilyList-GetPoorFamilyData', 'poorproperty,poorcause,planOutPoor,realname,name6,basicArea,txtYear,Aad105,isHelp,isHelpPeople,isImmigrant,isPlan,AreaType,Aah006,Aad003,condition,membercondition,orders,sorts,poorFamilyType', 'pagenumber', '1,1', 'isNull,pagesize', '0,1000');

-- ----------------------------
-- Table structure for website
-- ----------------------------
DROP TABLE IF EXISTS `website`;
CREATE TABLE `website` (
  `webId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `webName` varchar(256) NOT NULL DEFAULT '',
  `indexUrl` varchar(1024) NOT NULL DEFAULT '',
  `workFile` varchar(1024) NOT NULL DEFAULT '',
  `runningMode` varchar(256) NOT NULL DEFAULT '',
  `driver` tinyint(4) NOT NULL DEFAULT '0',
  `usable` tinyint(4) NOT NULL DEFAULT '0',
  `createtime` varchar(256) NOT NULL DEFAULT '',
  `creator` varchar(256) NOT NULL DEFAULT '' COMMENT 'task creator',
  `base` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:url based\n1:api based',
  PRIMARY KEY (`webId`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of website
-- ----------------------------
INSERT INTO `website` VALUES ('122', '诏安县政府官网', 'http://www.zhaoan.gov.cn/cms/html/zaxrmzf/index.html', '/Users/cwc/Desktop/tencent/data-crawling/zhaoan', 'unstructed', '0', '1', '2019-03-16 19:25:56', '', '0');
INSERT INTO `website` VALUES ('123', '扶贫', 'http://ai.inspur.com/Main/Archive', '/Users/cwc/Desktop/tencent/data-crawling/provty', 'structed', '1', '1', '2019-03-19 14:15:51', '', '1');
INSERT INTO `website` VALUES ('124', '网盘爬取', 'http://10.24.13.223:8080/hbky/index.jsp#', '/Users/cwc/Desktop/tencent/data-crawling/pan', 'unstructed', '0', '1', '2019-03-24 14:48:30', '', '1');
INSERT INTO `website` VALUES ('125', '网盘全文检索', 'http://10.24.13.223:8080/hbky/index.jsp#', '/Users/cwc/Desktop/tencent/data-crawling/pan', 'unstructed', '0', '1', '', '', '2');
INSERT INTO `website` VALUES ('126', '扶贫测试', 'http://ai.inspur.com/Main/Archive', 'D:/table/provty', 'structed', '2', '1', '2019-03-19 14:15:51', '', '1');
