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
INSERT INTO `current` VALUES (122,'17','done','done','active','inactive',6317,0),(123,'1','stop','done','active','done',15,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `extraConf`
--

LOCK TABLES `extraConf` WRITE;
/*!40000 ALTER TABLE `extraConf` DISABLE KEYS */;
INSERT INTO `extraConf` VALUES (8,122,'','','','','','',5,3000,'UTF-8',0),(9,123,'txtUserName','txtPassword','431200000000','aaaaaa','http://ai.inspur.com/login','btnLogin',5,3000,'UTF-8',13);
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
) ENGINE=InnoDB AUTO_INCREMENT=6814 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status`
--

LOCK TABLES `status` WRITE;
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` VALUES (123,6772,'0','info',0,13),(123,6773,'0','query',0,0),(123,6774,'1','info',0,0),(123,6775,'1','query',0,0),(123,6776,'2','info',0,0),(123,6777,'2','query',0,0),(123,6778,'1','info',0,0),(123,6779,'1','query',0,0),(122,6780,'1','info',224,3281),(122,6781,'1','query',0,357),(122,6782,'2','info',63,202),(122,6783,'2','query',0,26),(122,6784,'3','info',0,0),(122,6785,'3','query',0,0),(122,6786,'4','info',106,768),(122,6787,'4','query',0,94),(122,6788,'5','info',9,16),(122,6789,'5','query',0,4),(122,6790,'6','info',3,35),(122,6791,'6','query',0,3),(122,6792,'7','info',3,36),(122,6793,'7','query',0,4),(122,6794,'8','info',10,80),(122,6795,'8','query',0,9),(122,6796,'9','info',2,72),(122,6797,'9','query',0,11),(122,6798,'10','info',0,0),(122,6799,'10','query',0,0),(122,6800,'11','info',0,53),(122,6801,'11','query',0,5),(122,6802,'12','info',0,0),(122,6803,'12','query',0,0),(122,6804,'13','info',366,1448),(122,6805,'13','query',0,246),(122,6806,'14','info',0,0),(122,6807,'14','query',0,0),(122,6808,'15','info',39,276),(122,6809,'15','query',0,33),(122,6810,'16','info',1,50),(122,6811,'16','query',0,8),(122,6812,'17','info',0,0),(122,6813,'17','query',0,0);
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
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `website`
--

LOCK TABLES `website` WRITE;
/*!40000 ALTER TABLE `website` DISABLE KEYS */;
INSERT INTO `website` VALUES (122,'诏安县政府官网','http://www.zhaoan.gov.cn/cms/html/zaxrmzf/index.html','/Users/cwc/Desktop/tencent/data-crawling/zhaoan','unstructed',0,1,'2019-03-16 19:25:56','',0),(123,'扶贫','http://ai.inspur.com/Main/Archive','/Users/cwc/Desktop/tencent/data-crawling/provty','structed',1,1,'2019-03-19 14:15:51','',1);
/*!40000 ALTER TABLE `website` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `扶贫`
--

DROP TABLE IF EXISTS `扶贫`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `扶贫` (
  `dataId` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `Num` text,
  `unchecked` text,
  `省` text,
  `市` text,
  `县` text,
  `县属性` text,
  `乡（镇）数` text,
  `国有林场数` text,
  `国有贫困林场数` text,
  `行政村数` text,
  `贫困村数` text,
  `自然村数` text,
  `年末总户数` text,
  `乡村户数` text,
  `贫困户数` text,
  `贫困人数` text,
  `审核状态` text,
  `录入人` text,
  `录入时间` text,
  `修改人` text,
  `修改时间` text,
  PRIMARY KEY (`dataId`)
) ENGINE=MyISAM AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `扶贫`
--

LOCK TABLES `扶贫` WRITE;
/*!40000 ALTER TABLE `扶贫` DISABLE KEYS */;
INSERT INTO `扶贫` VALUES (6,'1','unchecked','山东省','济南市','市中区','03','8','1','0','91','31','819','157000','39000','16164','5176','未审核','吴勇','0001-01-01',' ',' '),(7,'2','unchecked','山东省','济南市','章丘区','02','25','0','0','345','100','3147','115940','104733','76274','21371','未审核','何艳','0001-01-01',' ',' '),(8,'3','unchecked','山东省','济南市','济阳区','01銆�02','23','2','2','499','170','1238','217700','157000','134374','40174','未审核','湖南省沅陵县','0001-01-01',' ',' '),(9,'4','unchecked','山东省','济南市','历城区','02','11','1','0','186','69','1706','86476','49226','47300','13692','未审核','43122905','0001-01-01',' ',' '),(10,'5','unchecked','山东省','济南市','历下区','03','3','1','1','22','8','148','28727','7500','4185','1738','未审核','43129101','0001-01-01',' ',' '),(11,'6','unchecked','山东省','济南市','长清区','02','43','5','2','653','177','6210','277109','210900','165316','55476','未审核','刘少宾','0001-01-01',' ',' '),(12,'7','unchecked','山东省','济南市','平阴县','02','23','1','1','315','106','1859','110700','93300','86421','24295','未审核','谭献民','0001-01-01',' ',' '),(13,'8','unchecked','山东省','济南市','槐荫区','03','25','2','1','316','95','3175','148945','102136','54064','18037','未审核','邓宗城','0001-01-01',' ',' '),(14,'9','unchecked','山东省','济南市','天桥区','02','23','1','1','422','118','2011','160700','126000','89907','22709','未审核','43122301','0001-01-01',' ',' '),(15,'10','unchecked','山东省','济南市','历城区','01銆�02','21','1','1','242','89','1667','64626','50732','54429','13024','未审核','欧丙岁','0001-01-01',' ',' '),(16,'11','unchecked','山东省','济南市','商河县','02','11','1','0','137','83','891','88557','72100','54728','18478','未审核','43122701','0001-01-01',' ',' '),(17,'12','unchecked','山东省','济南市','商河县','02','18','1','0','206','84','206','127000','88000','70691','21742','未审核','43122811','0001-01-01',' ',' '),(18,'13','unchecked','山东省','济南市','历城区','02','12','0','0','130','50','2167','93511','87925','19163','5890','未审核','43122101','0001-01-01',' ',' ');
/*!40000 ALTER TABLE `扶贫` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-23 11:21:13
