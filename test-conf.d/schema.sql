CREATE DATABASE IF NOT EXISTS todo;

ALTER DATABASE todo
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

USE todo;

CREATE TABLE IF NOT EXISTS `task`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `description` VARCHAR(300) NOT NULL,
  `user_id` BIGINT(20) NOT NULL,
  `completed` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `user`(
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(200) NOT NULL,
  `password` VARCHAR(300) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`login`))
  ENGINE = InnoDB;


INSERT IGNORE INTO `user` VALUES (1, 'nick', '202cb962ac59075b964b07152d234b70');

INSERT IGNORE INTO `task` VALUES (1, 'buy food', 'buy some food', 1, true);
INSERT IGNORE INTO `task` VALUES (2, 'buy food2', 'buy some food2', 1, true);
INSERT IGNORE INTO `task` VALUES (3, 'buy food3', 'buy some food3', 1, false);
INSERT IGNORE INTO `task` VALUES (4, 'buy food4', 'buy some food4', 1, false);
INSERT IGNORE INTO `task` VALUES (5, 'buy food5', 'buy some food5', 1, false);