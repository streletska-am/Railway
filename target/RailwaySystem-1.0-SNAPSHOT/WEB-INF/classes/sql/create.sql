SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema railway_system
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `railway_system` ;

-- -----------------------------------------------------
-- Schema railway_system
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `railway_system` DEFAULT CHARACTER SET utf8 ;
USE `railway_system` ;

-- -----------------------------------------------------
-- Table `railway_system`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`user` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`user` (
  `id` CHAR(36) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(35) NOT NULL,
  `name` VARCHAR(35) NOT NULL,
  `surname` VARCHAR(35) NOT NULL,
  `phone` VARCHAR(12) NOT NULL,
  `admin` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `railway_system`.`price`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`price` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`price` (
  `id` CHAR(36) NOT NULL,
  `compartment_factor` FLOAT NOT NULL,
  `deluxe_factor` FLOAT NOT NULL,
  `berth_factor` FLOAT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `railway_system`.`station`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`station` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`station` (
  `id` CHAR(36) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `railway_system`.`route`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`route` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`route` (
  `id` CHAR(36) NOT NULL,
  `from_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `to_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `price_id` CHAR(36) NOT NULL,
  `from_id` CHAR(36) NOT NULL,
  `to_id` CHAR(36) NOT NULL,
  `distance` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_route_price1_idx` (`price_id` ASC),
  INDEX `fk_route_station1_idx` (`from_id` ASC),
  INDEX `fk_route_station2_idx` (`to_id` ASC),
  CONSTRAINT `fk_route_price1`
    FOREIGN KEY (`price_id`)
    REFERENCES `railway_system`.`price` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_station1`
    FOREIGN KEY (`from_id`)
    REFERENCES `railway_system`.`station` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_station2`
    FOREIGN KEY (`to_id`)
    REFERENCES `railway_system`.`station` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `railway_system`.`train`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`train` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`train` (
  `id` CHAR(36) NOT NULL,
  `route_id` CHAR(36) NOT NULL,
  `compartment_free` INT(11) NOT NULL,
  `deluxe_free` INT(11) NOT NULL,
  `berth_free` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `train_route_idx` (`route_id` ASC),
  CONSTRAINT `train_route`
    FOREIGN KEY (`route_id`)
    REFERENCES `railway_system`.`route` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `railway_system`.`request`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`request` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`request` (
  `id` CHAR(36) NOT NULL,
  `user_id` CHAR(36) NOT NULL,
  `train_id` CHAR(36) NOT NULL,
  `type` ENUM('C', 'L', 'B') NOT NULL,
  `price` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `passenger_idx` (`user_id` ASC),
  INDEX `invoice_train_idx` (`train_id` ASC),
  CONSTRAINT `passenger`
    FOREIGN KEY (`user_id`)
    REFERENCES `railway_system`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `invoice_train`
    FOREIGN KEY (`train_id`)
    REFERENCES `railway_system`.`train` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '\n';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
