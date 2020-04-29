-- -----------------------------------------------------
-- Table `tasks`
-- -----------------------------------------------------
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
