/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50547
Source Host           : localhost:3306
Source Database       : orm

Target Server Type    : MYSQL
Target Server Version : 50547
File Encoding         : 65001

Date: 2018-08-11 19:16:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_task
-- ----------------------------
DROP TABLE IF EXISTS `t_task`;
CREATE TABLE `t_task` (
  `id` int(11) DEFAULT NULL,
  `branch_no` varchar(255) DEFAULT NULL,
  `branch_name` varchar(255) DEFAULT NULL,
  `task_no` varchar(255) DEFAULT NULL,
  `task_name` varchar(255) DEFAULT NULL,
  `branch_total` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_task
-- ----------------------------
INSERT INTO `t_task` VALUES ('8', '111', '成都分部', '222', 'test', '1000');
