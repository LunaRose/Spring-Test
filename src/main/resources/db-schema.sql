CREATE TABLE comments (
  id          INTEGER      NOT NULL AUTO_INCREMENT,
  author      CHAR(50)     NOT NULL,
  commentText VARCHAR(250) NOT NULL,
  PRIMARY KEY (id));

CREATE TABLE people (
  id   INTEGER      NOT NULL AUTO_INCREMENT,
  name VARCHAR(50)  NOT NULL,
  age  INT          NOT NULL,
  sex  VARCHAR(25)  NOT NULL,
  img  VARCHAR(250) NOT NULL,
  PRIMARY KEY (id));

INSERT INTO people (name, age, sex, img) VALUES ('Alexander Meerkat', 6, 'Male', 'alexander.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Luna Fox', 12, 'Female', 'luna.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Frank Peacock', 5, 'Male', 'frank.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Twitch Feathers', 2, 'Male', 'twitch.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Millie Mouser', 8, 'Female', 'millie.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Fluffy The Destroyer of Worlds', 900, 'Female', 'fluffy.jpg');
INSERT INTO people (name, age, sex, img) VALUES ('Smarts Wiseowl', 11, 'Male', 'smarty.jpg');

INSERT INTO comments (author, commentText) VALUES ('Alexander Meerkat', 'Simples!');
INSERT INTO comments (author, commentText) VALUES ('Luna Fox', 'Im going to eat that twitchy one');
INSERT INTO comments (author, commentText) VALUES ('Frank Peacock', 'Squark');
INSERT INTO comments (author, commentText) VALUES ('Twitch Feathers', 'ISTHATCOFFEEILOVECOFFEECANIHAVESOME!');
INSERT INTO comments (author, commentText) VALUES ('Millie Mouser', 'Go away im sleeping');
INSERT INTO comments (author, commentText) VALUES ('Fluffy the Destroyer of Worlds', 'I WILL DESTROY YOU!');
INSERT INTO comments (author, commentText) VALUES ('Smarts Wiseowl', 'Err..... I think that squirrel is quite unhinged...');