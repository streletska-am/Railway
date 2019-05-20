INSERT INTO price(id, berthFactor, compartmentFactor, deluxeFactor) VALUE (1,'0.2', '0.75', '1');
INSERT INTO price(id, berthFactor, compartmentFactor, deluxeFactor) VALUE (2,'0.5', '1', '1.5');
INSERT INTO price(id, berthFactor, compartmentFactor, deluxeFactor) VALUE (3,'1', '2', '3');

INSERT INTO user(id, email, password, name, surname, phone, admin) VALUE (1,"root", "root", "Admin", "Admin", "0", 1);
INSERT INTO user(id, email, password, name, surname, phone, admin) VALUE (2,"andy97@ukr.net", "root", "Andrii", "Yashin", "0663533848", 0);

INSERT INTO station(id, name) VALUE (1,'Kyiv');
INSERT INTO station(id, name) VALUE (2,'Kharkiv');
INSERT INTO station(id, name) VALUE (3,'Lviv');
INSERT INTO station(id, name) VALUE (4,'Odessa');

INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (1,'2017-07-01 12:45:00', '2017-07-01 23:00:00',1, 1,2,300);
INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (2,'2017-07-01 14:30:00', '2017-07-01 22:00:00',1, 2,1,300);
INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (3,'2017-07-01 23:45:00', '2017-07-02 05:00:00',2, 1,4,500);
INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (4,'2017-07-01 11:12:00', '2017-07-01 17:00:00',2, 4,1,500);
INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (5,'2017-07-01 06:12:00', '2017-07-01 14:00:00',3, 2,3,800);
INSERT INTO route(id, from_time, to_time, price_id, from_id, to_id, distance) VALUE (6,'2017-07-01 05:12:00', '2017-07-01 16:00:00',2, 3,2,800);

INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (1,1,49,50,50);
INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (2,2,60,70,20);
INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (3,3,100,100,100);
INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (4,4,100,100,100);
INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (5,5,50,50,50);
INSERT INTO train(id, route_id, compartment_free, deluxe_free, berth_free) VALUE (6,6,30,20,50);

INSERT INTO request(id, user_id, train_id, type, price) VALUE (1,2,1,'C',60);

