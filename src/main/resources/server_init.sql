CREATE SCHEMA if not exists `meanchat` ;
insert into mysql.user(Host,User,Password) values('localhost','meanchat_server','dev000');
grant all privileges on meanchat.* to meanchat_server@localhost identified by 'dev000';
