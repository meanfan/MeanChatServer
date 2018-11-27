use meanchat;
create table if not exists t_user(
uid int auto_increment primary key,
username char(20) not null unique,
nickname char(20) not null,
password char(32) not null,
time_register timestamp,
time_last_login timestamp
);
create table if not exists t_user_relation(
 id int auto_increment primary key,
 user_id int not null,
 friend_id int not null,
 relation_name char(20),
 foreign key(user_id) references t_user(uid) on delete cascade on update cascade,
 foreign key(friend_id) references t_user(uid)on delete cascade on update cascade,
 check (user_id<friend_id)
 );
 create table if not exists t_message(
 id int auto_increment primary key,
 message_content varchar(255),
 message_time timestamp,
 sender_id int,
 receiver_id int,
 foreign key(sender_id) references t_user(uid)  on delete cascade on update cascade,
 foreign key(receiver_id) references t_user(uid)  on delete cascade on update cascade
 );
