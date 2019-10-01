DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS dishes;
DROP TABLE IF EXISTS votes;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100;

CREATE TABLE users
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name             VARCHAR                 NOT NULL,
  last_name        VARCHAR                 NOT NULL,
  email            VARCHAR                 NOT NULL,
  password         VARCHAR                 NOT NULL,
  registered       TIMESTAMP DEFAULT now() NOT NULL,
  enabled          BOOL DEFAULT TRUE       NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR,
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
  id               INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name             VARCHAR                NOT NULL
);

CREATE UNIQUE INDEX restaurants_unique_name ON restaurants (name);

CREATE TABLE votes
(
  id                INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  date              DATE DEFAULT now() NOT NULL,
  user_id           INTEGER NOT NULL,
  rest_id           INTEGER NOT NULL,
  CONSTRAINT user_votes_idx UNIQUE (date, user_id),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
  CONSTRAINT rest_votes_idx
  FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

CREATE TABLE dishes
(
  id           INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  date              DATE DEFAULT now() NOT NULL,
  dish_name         VARCHAR                NOT NULL,
  price             DECIMAL NOT NULL,
  rest_id           INTEGER NOT NULL,
  CONSTRAINT dishes_rest_idx
  FOREIGN KEY (rest_id) REFERENCES restaurants (id) ON DELETE CASCADE
);

