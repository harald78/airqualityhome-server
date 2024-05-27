USE airqualityhome;

INSERT INTO role(id, name, created, updated) values (1, 'APP_READ', now(), now());

INSERT INTO role(id, name, created, updated) values (2, 'APP_WRITE', now(), now());

INSERT INTO user_user_role (user_id, role_id) values (1, 1);
INSERT INTO user_user_role (user_id, role_id) values (1, 2);