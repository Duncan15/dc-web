-- MySQL dump 10.13  Distrib 8.0.13, for osx10.12 (x86_64)
--
-- Host: localhost    Database: webcrawler
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `apiBaseConf`
--

DROP TABLE IF EXISTS `apiBaseConf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `apiBaseConf` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `webId` bigint(20) NOT NULL DEFAULT '0',
  `prefix` varchar(1024) NOT NULL DEFAULT '',
  `inputXpath` varchar(256) NOT NULL DEFAULT '',
  `submitXpath` varchar(256) NOT NULL DEFAULT '',
  `infoLinkXpath` varchar(256) DEFAULT '' COMMENT '用于指定返回查询页面上数据链接的位置，用于帮助链接收集器收集链接\n当此值为空时，运行收集器的默认行为',
  `payloadXpath` varchar(256) NOT NULL DEFAULT '' COMMENT 'format:\nxpath,name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apiBaseConf`
--

LOCK TABLES `apiBaseConf` WRITE;
/*!40000 ALTER TABLE `apiBaseConf` DISABLE KEYS */;
/*!40000 ALTER TABLE `apiBaseConf` ENABLE KEYS */;
UNLOCK TABLES;


-- ----------------------------
-- Table structure for formsbymd5
-- ----------------------------
DROP TABLE IF EXISTS `formsbymd5`;
CREATE TABLE `formsbymd5`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `formMd5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sensestate
-- ----------------------------
DROP TABLE IF EXISTS `sensestate`;
CREATE TABLE `sensestate`  (
  `id` int(20) NOT NULL,
  `allLinks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `trueLinks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sense
-- ----------------------------
DROP TABLE IF EXISTS `sense`;
CREATE TABLE `sense`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `homeUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `targetUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 158 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


--
-- Table structure for table `current`
--

DROP TABLE IF EXISTS `current`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `current`
--

LOCK TABLES `current` WRITE;
/*!40000 ALTER TABLE `current` DISABLE KEYS */;
INSERT INTO `current` VALUES (122,'20','done','done','done','active',3426,0);
/*!40000 ALTER TABLE `current` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estimate`
--

DROP TABLE IF EXISTS `estimate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `estimate` (
  `startWord` varchar(255) NOT NULL DEFAULT '',
  `estiId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `linksXpath` varchar(255) NOT NULL DEFAULT '',
  `pagesInfoId` varchar(255) NOT NULL DEFAULT '',
  `contentXpath` varchar(255) NOT NULL DEFAULT '',
  `result` varchar(255) NOT NULL DEFAULT '',
  `status` varchar(255) NOT NULL DEFAULT '',
  `rateBar` varchar(255) NOT NULL DEFAULT '',
  `walkTimes` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`estiId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estimate`
--

LOCK TABLES `estimate` WRITE;
/*!40000 ALTER TABLE `estimate` DISABLE KEYS */;
/*!40000 ALTER TABLE `estimate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `extraConf`
--

DROP TABLE IF EXISTS `extraConf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extraConf`
--

LOCK TABLES `extraConf` WRITE;
/*!40000 ALTER TABLE `extraConf` DISABLE KEYS */;
INSERT INTO `extraConf` VALUES (8,122,'','','','','','',5,3000,'UTF-8',0);
/*!40000 ALTER TABLE `extraConf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pattern`
--

DROP TABLE IF EXISTS `pattern`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `pattern` (
  `webId` int(20) unsigned NOT NULL DEFAULT '0',
  `patternName` varchar(255) NOT NULL DEFAULT 'fulltext',
  `xpath` varchar(1024) NOT NULL DEFAULT '',
  `id` int(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pattern`
--

LOCK TABLES `pattern` WRITE;
/*!40000 ALTER TABLE `pattern` DISABLE KEYS */;
/*!40000 ALTER TABLE `pattern` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pattern_structed`
--

DROP TABLE IF EXISTS `pattern_structed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pattern_structed`
--

LOCK TABLES `pattern_structed` WRITE;
/*!40000 ALTER TABLE `pattern_structed` DISABLE KEYS */;
/*!40000 ALTER TABLE `pattern_structed` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `queryparam`
--

DROP TABLE IF EXISTS `queryparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `queryparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `webId` int(11) unsigned NOT NULL DEFAULT '0',
  `dataParamList` varchar(255) NOT NULL DEFAULT '',
  `N` varchar(255) NOT NULL DEFAULT '',
  `C` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `webId` (`webId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `queryparam`
--

LOCK TABLES `queryparam` WRITE;
/*!40000 ALTER TABLE `queryparam` DISABLE KEYS */;
/*!40000 ALTER TABLE `queryparam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requesttable`
--

DROP TABLE IF EXISTS `requesttable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `requesttable` (
  `requestID` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestName` varchar(1024) NOT NULL DEFAULT '',
  `requestDesc` varchar(2048) NOT NULL DEFAULT '',
  `createdTime` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`requestID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requesttable`
--

LOCK TABLES `requesttable` WRITE;
/*!40000 ALTER TABLE `requesttable` DISABLE KEYS */;
/*!40000 ALTER TABLE `requesttable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status`
--

DROP TABLE IF EXISTS `status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `status` (
  `webId` int(20) unsigned NOT NULL,
  `statusId` int(20) unsigned NOT NULL AUTO_INCREMENT,
  `round` varchar(256) NOT NULL DEFAULT '0',
  `type` varchar(24) NOT NULL DEFAULT 'query',
  `fLinkNum` int(11) unsigned NOT NULL DEFAULT '0',
  `sLinkNum` int(11) unsigned NOT NULL DEFAULT '0',
  KEY `round` (`round`),
  KEY `statusId` (`statusId`)
) ENGINE=InnoDB AUTO_INCREMENT=6772 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (122,6732,'1','info',23,82),(122,6733,'1','query',0,8),(122,6734,'2','info',3,8),(122,6735,'2','query',0,1),(122,6736,'3','info',28,417),(122,6737,'3','query',0,34),(122,6738,'4','info',126,1318),(122,6739,'4','query',0,115),(122,6740,'5','info',6,210),(122,6741,'5','query',0,16),(122,6742,'6','info',34,117),(122,6743,'6','query',0,14),(122,6744,'7','info',0,0),(122,6745,'7','query',0,0),(122,6746,'8','info',0,0),(122,6747,'8','query',0,0),(122,6748,'9','info',9,47),(122,6749,'9','query',0,5),(122,6750,'10','info',25,56),(122,6751,'10','query',0,8),(122,6752,'11','info',0,0),(122,6753,'11','query',0,0),(122,6754,'12','info',0,1),(122,6755,'12','query',14,1),(122,6756,'13','info',0,0),(122,6757,'13','query',0,0),(122,6758,'14','info',10,51),(122,6759,'14','query',0,8),(122,6760,'15','info',8,3),(122,6761,'15','query',1,2),(122,6762,'16','info',13,3),(122,6763,'16','query',0,3),(122,6764,'17','info',5,12),(122,6765,'17','query',0,2),(122,6766,'18','info',0,163),(122,6767,'18','query',0,13),(122,6768,'19','info',15,332),(122,6769,'19','query',0,27),(122,6770,'20','info',23,606),(122,6771,'20','query',8,424);
/*!40000 ALTER TABLE `status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `structedparam`
--

DROP TABLE IF EXISTS `structedparam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `structedparam`
--

LOCK TABLES `structedparam` WRITE;
/*!40000 ALTER TABLE `structedparam` DISABLE KEYS */;
/*!40000 ALTER TABLE `structedparam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `urlBaseConf`
--

DROP TABLE IF EXISTS `urlBaseConf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `urlBaseConf`
--

LOCK TABLES `urlBaseConf` WRITE;
/*!40000 ALTER TABLE `urlBaseConf` DISABLE KEYS */;
INSERT INTO `urlBaseConf` VALUES (4,122,'http://www.zhaoan.gov.cn/cms/siteresource/search.shtml?','key','page','1,1','searchSiteId,siteId,pageName','60427348114130001,60427348114130001,quickSiteSearch');
/*!40000 ALTER TABLE `urlBaseConf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `website`
--

DROP TABLE IF EXISTS `website`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `website`
--

LOCK TABLES `website` WRITE;
/*!40000 ALTER TABLE `website` DISABLE KEYS */;
INSERT INTO `website` VALUES (122,'诏安县政府官网','http://www.zhaoan.gov.cn/cms/html/zaxrmzf/index.html','/Users/cwc/Desktop/tencent/data-crawling/zhaoan','unstructed',0,1,'2019-03-16 19:25:56','',0);
/*!40000 ALTER TABLE `website` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-19 12:19:29
