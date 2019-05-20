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
  `id` INT(11) NOT NULL AUTO_INCREMENT,
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
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `compartmentFactor` FLOAT NOT NULL,
  `deluxeFactor` FLOAT NOT NULL,
  `berthFactor` FLOAT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `railway_system`.`station`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`station` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`station` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `railway_system`.`route`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `railway_system`.`route` ;

CREATE TABLE IF NOT EXISTS `railway_system`.`route` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `fromTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `toTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `priceId` INT(11) NOT NULL,
  `fromId` INT NOT NULL,
  `toId` INT NOT NULL,
  `distance` DOUBLE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_route_price1_idx` (`priceId` ASC),
  INDEX `fk_route_station1_idx` (`fromId` ASC),
  INDEX `fk_route_station2_idx` (`toId` ASC),
  CONSTRAINT `fk_route_price1`
    FOREIGN KEY (`priceId`)
    REFERENCES `railway_system`.`price` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_station1`
    FOREIGN KEY (`fromId`)
    REFERENCES `railway_system`.`station` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_route_station2`
    FOREIGN KEY (`toId`)
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
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `routeId` INT NOT NULL,
  `compartmentFree` INT(11) NOT NULL,
  `deluxeFree` INT(11) NOT NULL,
  `berthFree` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `train_route_idx` (`routeId` ASC),
  CONSTRAINT `train_route`
    FOREIGN KEY (`routeId`)
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
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userId` INT(11) NOT NULL,
  `trainId` INT(11) NOT NULL,
  `type` ENUM('C', 'L', 'B') NOT NULL,
  `price` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `passenger_idx` (`userId` ASC),
  INDEX `invoice_train_idx` (`trainId` ASC),
  CONSTRAINT `passenger`
    FOREIGN KEY (`userId`)
    REFERENCES `railway_system`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `invoice_train`
    FOREIGN KEY (`trainId`)
    REFERENCES `railway_system`.`train` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '\n';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
