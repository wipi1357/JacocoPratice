DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS book; 

CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);
CREATE TABLE book (
  id INT NOT NULL AUTO_INCREMENT,
  title VARCHAR(50) NOT NULL,
  content VARCHAR(50) NOT NULL,
  PRIMARY KEY(id)
);