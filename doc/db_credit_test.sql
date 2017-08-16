# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: 127.0.0.1 (MySQL 5.7.16)
# Database: db_credit_test
# Generation Time: 2017-08-16 06:43:07 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table tab_penaly_wuhan_month
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tab_penaly_wuhan_month`;

CREATE TABLE `tab_penaly_wuhan_month` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `CF_WSH` varchar(256) DEFAULT NULL,
  `CF_CFMC` varchar(512) DEFAULT NULL,
  `CF_CFLB1` varchar(72) DEFAULT NULL,
  `CF_CFLB2` varchar(72) DEFAULT NULL,
  `CF_SY` varchar(4096) DEFAULT NULL,
  `CF_YJ` varchar(4096) DEFAULT NULL,
  `CF_XDR_MC` varchar(256) DEFAULT NULL,
  `CF_XDR_SHXYM` varchar(128) DEFAULT NULL,
  `CF_XDR_ZDM` varchar(128) DEFAULT NULL,
  `CF_XDR_GSDJ` varchar(128) DEFAULT NULL,
  `CF_XDR_SWDJ` varchar(128) DEFAULT NULL,
  `CF_XDR_SFZ` varchar(128) DEFAULT NULL,
  `CF_FR` varchar(512) DEFAULT NULL,
  `CF_JG` varchar(4096) DEFAULT NULL,
  `CF_JDRQ` date DEFAULT NULL,
  `CF_XZJG` varchar(128) DEFAULT NULL,
  `CF_ZT` varchar(2) DEFAULT NULL,
  `DFBM` varchar(12) DEFAULT NULL,
  `SJC` date DEFAULT NULL,
  `BZ` varchar(1024) DEFAULT NULL,
  `GSQX` varchar(128) DEFAULT NULL,
  `CF_AJMC` varchar(512) DEFAULT NULL,
  `CF_JZRQ` date DEFAULT NULL,
  `QT` varchar(1024) DEFAULT NULL,
  `SOURCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



# Dump of table tab_permisson_wuhan_month
# ------------------------------------------------------------

DROP TABLE IF EXISTS `tab_permisson_wuhan_month`;

CREATE TABLE `tab_permisson_wuhan_month` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `XK_WSH` varchar(256) DEFAULT NULL,
  `XK_XMMC` varchar(512) DEFAULT NULL,
  `XK_SPLB` varchar(32) DEFAULT NULL,
  `XK_NR` varchar(4096) DEFAULT NULL,
  `XK_XDR` varchar(512) DEFAULT NULL,
  `XK_XDR_SHXYM` varchar(128) DEFAULT NULL,
  `XK_XDR_ZDM` varchar(128) DEFAULT NULL,
  `XK_XDR_GSDJ` varchar(128) DEFAULT NULL,
  `XK_XDR_SWDJ` varchar(128) DEFAULT NULL,
  `XK_XDR_SFZ` varchar(128) DEFAULT NULL,
  `XK_FR` varchar(512) DEFAULT NULL,
  `XK_JDRQ` date DEFAULT NULL,
  `XK_JZQ` date DEFAULT NULL,
  `XK_XZJG` varchar(128) DEFAULT NULL,
  `XK_ZT` varchar(32) DEFAULT NULL,
  `DFBM` varchar(12) DEFAULT NULL,
  `SJC` date DEFAULT NULL,
  `BZ` varchar(1024) DEFAULT NULL,
  `QTXX` varchar(1024) DEFAULT NULL,
  `SJMC` varchar(512) DEFAULT NULL,
  `SOURCE` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
