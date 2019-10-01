DELETE FROM user_roles;
DELETE FROM dishes;
DELETE FROM votes;
DELETE FROM restaurants;
DELETE FROM users;

ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, last_name, email, password) VALUES
  ('User', 'Userovich','user@yandex.ru', '{noop}password'),
  ('Admin', 'Adminov','admin@gmail.com', '{noop}admin'),
  ('User2', 'Userovich2','user2@yandex.ru', '{noop}password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100),
  ('ROLE_ADMIN', 101),
  ('ROLE_USER', 101),
  ('ROLE_USER', 102);

INSERT INTO restaurants (name) VALUES
    ('Belkin'),
    ('Karl Fridrich');
--     ('Moscow');

INSERT INTO dishes (dish_name, price, rest_id) VALUES
    ('Sopa De Pollo', 50.0, 103),
    ('Garden salad', 20.0, 103),
    ('Chiken', 35.5, 103),
    ('Sopa De Res', 55.0, 104),
    ('Taco salad', 22.0, 104),
    ('Super Nachos', 36.0, 104);
--     ('Sopa Marinera', 48.0, 105),
--     ('Chicken', 23.0, 105),
--     ('Pastelitos', 38.5, 105);

INSERT INTO votes (user_id, rest_id) VALUES
(100, 103),
(101, 104),
(102, 103);



