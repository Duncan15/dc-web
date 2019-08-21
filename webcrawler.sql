
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apiBaseConf
-- ----------------------------
DROP TABLE IF EXISTS `apiBaseConf`;
CREATE TABLE `apiBaseConf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `inputXpath` varchar(256) NOT NULL DEFAULT '',
  `submitXpath` varchar(256) NOT NULL DEFAULT '',
  `infoLinkXpath` varchar(256) DEFAULT '' COMMENT '用于指定返回查询页面上数据链接的位置，用于帮助链接收集器收集链接\n当此值为空时，运行收集器的默认行为',
  `payloadXpath` varchar(256) NOT NULL DEFAULT '' COMMENT 'format:\nxpath,name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

-- ----------------------------
-- Table structure for extraConf
-- ----------------------------
DROP TABLE IF EXISTS `extraConf`;
CREATE TABLE `extraConf` (
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

-- ----------------------------
-- Table structure for formsbymd5
-- ----------------------------
DROP TABLE IF EXISTS `formsbymd5`;
CREATE TABLE `formsbymd5` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `formMd5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC;

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
  `pageSize` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT '',
  `totalAddress` varchar(256) DEFAULT '' COMMENT '总页数在json response中的位置',
  `contentAddress` varchar(256) NOT NULL DEFAULT '' COMMENT 'if value is an empty string, the root is cotent address',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;
-- ----------------------------
-- Table structure for jsonBaseConf
-- ----------------------------
DROP TABLE IF EXISTS `jsonBaseConf`;
CREATE TABLE `jsonBaseConf` (
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

-- ----------------------------
-- Table structure for sense
-- ----------------------------
DROP TABLE IF EXISTS `sense`;
CREATE TABLE `sense` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homeUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `targetUrl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for sensestate
-- ----------------------------
DROP TABLE IF EXISTS `sensestate`;
CREATE TABLE `sensestate` (
  `id` int(20) NOT NULL,
  `allLinks` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `trueLinks` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC;


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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

-- ----------------------------
-- Table structure for urlBaseConf
-- ----------------------------
DROP TABLE IF EXISTS `urlBaseConf`;
CREATE TABLE `urlBaseConf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `paramQuery` varchar(256) NOT NULL DEFAULT '',
  `paramPage` varchar(256) NOT NULL DEFAULT '',
  `startPageNum` varchar(256) NOT NULL DEFAULT '',
  `paramList` varchar(1024) NOT NULL DEFAULT '',
  `paramValueList` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ;

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
