insert into customer(id, name, contact_id, password) values (1,'Basil','1','spring');
insert into customer(id, name, contact_id, password) values (2,'Arun','2','spring');
insert into customer(id, name, contact_id, password) values (3,'Roj','3','spring');
insert into customer(id, name, contact_id, password) values (4,'basil','basil','basil');
 
insert into role(id, name) values (1,'ROLE_USER');
insert into role(id, name) values (2,'ROLE_ADMIN');
insert into role(id, name) values (3,'ROLE_GUEST'); 

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (1,2);
insert into user_role(user_id, role_id) values (2,1);
insert into user_role(user_id, role_id) values (3,1);
insert into user_role(user_id, role_id) values (4,1);
