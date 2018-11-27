insert into t_user(username,nickname,password) values('user1','User 1','abc');
insert into t_user(username,nickname,password) values('user2','User 2','abc');
insert into t_user(username,nickname,password) values('user3','User 3','abc');
insert into t_user(username,nickname,password) values('user4','User 4','abc');

insert into t_user_relation(user_id,friend_id) values(1,2);
insert into t_user_relation(user_id,friend_id) values(1,3);
insert into t_user_relation(user_id,friend_id) values(2,3);

insert into t_message(message_content,sender_id,receiver_id) values('hello',1,2);
insert into t_message(message_content,sender_id,receiver_id) values('hi',2,1);
insert into t_message(message_content,sender_id,receiver_id) values('bye',1,2);