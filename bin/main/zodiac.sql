DROP DATABASE IF EXISTS Delivery;
CREATE DATABASE Delivery;

USE Delivery;

CREATE TABLE `user` (
    `no` INT NOT NULL AUTO_INCREMENT,
    `id` CHAR(24) NOT NULL,
    `pw` CHAR(64) NOT NULL,
    `name` VARCHAR(16) NOT NULL,
    `phone` CHAR(11) NOT NULL,
    `email` CHAR(32) DEFAULT NULL,
    `address` VARCHAR(64) NOT NULL,
    UNIQUE KEY (id),
    PRIMARY KEY (no)
);

/*
 * status 0 : checking
 * status 1 : done
 * status 2 : canceled
 */
CREATE TABLE `delivery` (
    `no` INT NOT NULL AUTO_INCREMENT,
    `sender` INT NOT NULL,
    `recipient` INT DEFAULT NULL,
	`name` CHAR(16) NOT NULL,
	`phone` CHAR(11) NOT NULL,
	`email` CHAR(32) DEFAULT NULL,
	`address` VARCHAR(64) NOT NULL,
    `status` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (no),
    FOREIGN KEY (sender) REFERENCES user(no)
);

CREATE TABLE `post` (
	`no` INT NOT NULL AUTO_INCREMENT,
	`title` VARCHAR(64) NOT NULL,
	`author` INT NOT NULL,
	`body` TEXT,
	`date` DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY (no),
	FOREIGN KEY (author) REFERENCES user(no)
);

INSERT INTO user VALUES (NULL, 'test', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'test', '00000000000', 'test@test.com', 'Seoul');
INSERT INTO delivery VALUES (NULL, 1, 1, 'test', '01012345678', 'test@test.com', 'Goyang', 0);
INSERT INTO post (title, author, body) VALUES ('test', 1, 'test');

SELECT delivery.no, S.name, S.phone, delivery.name, delivery.phone, delivery.address, delivery.status
    FROM delivery
    LEFT JOIN user S ON delivery.sender = S.no;

SELECT post.no, post.title, user.name, post.body, post.date
	FROM post
	LEFT JOIN user ON post.author = user.no;
