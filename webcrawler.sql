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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `apiBaseConf`
--

LOCK TABLES `apiBaseConf` WRITE;
/*!40000 ALTER TABLE `apiBaseConf` DISABLE KEYS */;
INSERT INTO `apiBaseConf` VALUES (2,124,'http://10.24.13.223:8080/hbky/privateFileManager/grwpgl','//*[@id=\"searchtext\"]','//div[@class=\"searchIcon\"]','//div[@id=\"allwenjian\"]//a[@href]','//div[@id=\"allwenjian\"]//div[@class=\"filename\"],title');
/*!40000 ALTER TABLE `apiBaseConf` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extraConf`
--

LOCK TABLES `extraConf` WRITE;
/*!40000 ALTER TABLE `extraConf` DISABLE KEYS */;
INSERT INTO `extraConf` VALUES (8,122,'','','','','','',5,3000,'UTF-8',0),(9,123,'txtUserName','txtPassword','431200000000','aaaaaa','http://ai.inspur.com/login','btnLogin',5,3000,'UTF-8',13),(10,124,'//*[@id=\"j_username\"]','//*[@id=\"j_password\"]','zongyb','abing201!2','http://10.24.13.223:8080/hbky/index.jsp#','//*[@id=\"submit_btn\"]',5,3000,'UTF-8',0),(11,125,'//*[@id=\"j_username\"]','//*[@id=\"j_password\"]','zongyb','abing201!2','http://10.24.13.223:8080/hbky/index.jsp#','//*[@id=\"submit_btn\"]',20,30000,'UTF-8',0);
/*!40000 ALTER TABLE `extraConf` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jsonBaseConf`
--

DROP TABLE IF EXISTS `jsonBaseConf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jsonBaseConf`
--

LOCK TABLES `jsonBaseConf` WRITE;
/*!40000 ALTER TABLE `jsonBaseConf` DISABLE KEYS */;
INSERT INTO `jsonBaseConf` VALUES (1,125,'http://10.24.13.223:8080/hbky/search/getResult?','keyword','pageIndex','1,1','type=0&searchtime=0','/0/sum','','[http://10.24.13.223:8080/hbky/lucene/wjdownload?path=]+/path+[&filename=]+/filename+[&fileid=]+/fileid+[&category=]+/category','/content');
/*!40000 ALTER TABLE `jsonBaseConf` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pattern_structed`
--

LOCK TABLES `pattern_structed` WRITE;
/*!40000 ALTER TABLE `pattern_structed` DISABLE KEYS */;
INSERT INTO `pattern_structed` VALUES (123,'a','/html/body/div[3]/div/div[1]/div[1]/div[1]/div/table','(a+b)*(c+d)','formula','/',2),(123,'b','/html/body/div[3]/div/div[1]/div[2]/div[1]/div/table','(a+b)*(c+d)','formula','/',3),(123,'c','/html/body/div[3]/div/div[1]/div[1]/div[2]/div/table','(a+b)*(c+d)','formula','/',4),(123,'d','/html/body/div[3]/div/div[1]/div[2]/div[2]/table','(a+b)*(c+d)','formula','/',5),(123,'subpage_data','pcObj','pcObj','json','/subpage',6);
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
) ENGINE=InnoDB AUTO_INCREMENT=7130 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `structedparam`
--

LOCK TABLES `structedparam` WRITE;
/*!40000 ALTER TABLE `structedparam` DISABLE KEYS */;
INSERT INTO `structedparam` VALUES (2,123,'ifmNav','贫困县','ifmCon','btnSearch','Aaa003','/html/body/div[3]/div/div[2]/table/tbody/tr/td[8]/a','/html/body/div[3]/div/div[2]/table/tbody/tr/td[5]/input','Aaa003','combo-arrow','/','_easyui_combobox_i8_0,_easyui_combobox_i8_1,_easyui_combobox_i8_2,_easyui_combobox_i8_3,_easyui_combobox_i8_4,_easyui_combobox_i8_5');
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
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `website`
--

LOCK TABLES `website` WRITE;
/*!40000 ALTER TABLE `website` DISABLE KEYS */;
INSERT INTO `website` VALUES (122,'诏安县政府官网','http://www.zhaoan.gov.cn/cms/html/zaxrmzf/index.html','/Users/cwc/Desktop/tencent/data-crawling/zhaoan','unstructed',0,1,'2019-03-16 19:25:56','',0),(123,'扶贫','http://ai.inspur.com/Main/Archive','/Users/cwc/Desktop/tencent/data-crawling/provty','structed',1,1,'2019-03-19 14:15:51','',1),(124,'网盘爬取','http://10.24.13.223:8080/hbky/index.jsp#','/Users/cwc/Desktop/tencent/data-crawling/pan','unstructed',0,1,'2019-03-24 14:48:30','',1),(125,'网盘全文检索','http://10.24.13.223:8080/hbky/index.jsp#','/Users/cwc/Desktop/tencent/data-crawling/pan','unstructed',0,1,'','',2);
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

-- Dump completed on 2019-04-06 14:43:26
