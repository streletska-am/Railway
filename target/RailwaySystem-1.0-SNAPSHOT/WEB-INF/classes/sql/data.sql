INSERT INTO `railway_system`.`price`(`id`, `berth_factor`, `compartment_factor`, `deluxe_factor`) VALUE ('1','0.2', '0.75', '1.0');
INSERT INTO `railway_system`.`price`(`id`, `berth_factor`, `compartment_factor`, `deluxe_factor`) VALUE ('2','0.5', '1.00', '1.5');
INSERT INTO `railway_system`.`price`(`id`, `berth_factor`, `compartment_factor`, `deluxe_factor`) VALUE ('3','1.0', '2.00', '3.0');

INSERT INTO `railway_system`.`user`(`id`, `email`, `password`, `name`, `surname`, `phone`, `admin`) VALUE ('1','root', '63a9f0ea7bb98050796b649e85481845', 'Admin', 'Admin', '0', 1);
INSERT INTO `railway_system`.`user`(`id`, `email`, `password`, `name`, `surname`, `phone`, `admin`) VALUE ('2','andy97@ukr.net', '63a9f0ea7bb98050796b649e85481845', 'Andrii', 'Dubiletskiy', '0663533848', 0);
INSERT INTO `railway_system`.`user`(`id`, `email`, `password`, `name`, `surname`, `phone`, `admin`) VALUE ('3','vital@a.a', '63a9f0ea7bb98050796b649e85481845', 'Vitalya', 'Cherry', '0660033848', 0);
INSERT INTO `railway_system`.`user`(`id`, `email`, `password`, `name`, `surname`, `phone`, `admin`) VALUE ('4','a@a.a', '63a9f0ea7bb98050796b649e85481845', 'Anastasiia', 'Streletska', '0660033848', 1);
INSERT INTO `railway_system`.`station`(`id`, `name`) VALUE ('1','Kyiv');
INSERT INTO `railway_system`.`station`(`id`, `name`) VALUE ('2','Kharkiv');
INSERT INTO `railway_system`.`station`(`id`, `name`) VALUE ('3','Lviv');
INSERT INTO `railway_system`.`station`(`id`, `name`) VALUE ('4','Odessa');

INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('1','2019-07-01 12:45:00', '2019-07-01 23:00:00','1', '1','2',300);
INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('2','2019-07-01 14:30:00', '2019-07-01 22:00:00','1', '2','1',300);
INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('3','2019-07-01 23:45:00', '2019-07-02 05:00:00','2', '1','4',500);
INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('4','2019-07-01 11:12:00', '2019-07-01 17:00:00','2', '4','1',500);
INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('5','2019-07-01 06:12:00', '2019-07-01 14:00:00','3', '2','3',800);
INSERT INTO `railway_system`.`route`(`id`, `from_time`, `to_time`, `price_id`, `from_id`, `to_id`, `distance`) VALUE ('6','2019-07-01 05:12:00', '2019-07-01 16:00:00','2', '3','2',800);

INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('1','1',49,50,50);
INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('2','2',60,70,20);
INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('3','3',100,100,100);
INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('4','4',100,100,100);
INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('5','5',50,50,50);
INSERT INTO `railway_system`.`train`(`id`, `route_id`, `compartment_free`, `deluxe_free`, `berth_free`) VALUE ('6','6',30,20,50);

INSERT INTO `railway_system`.`request`(`id`, `user_id`, `train_id`, `type`, `price`) VALUE ('1','2','1','C',60);
