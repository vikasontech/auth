use security;

CREATE TABLE `users` (
                         `username` varchar(50) NOT NULL,
                         `password` varchar(500) NOT NULL,
                         `enabled` tinyint(1) NOT NULL,
                         PRIMARY KEY (`username`)
) ;

CREATE TABLE `authorities` (
                               `username` varchar(50) NOT NULL,
                               `authority` varchar(50) NOT NULL,
                               UNIQUE KEY `ix_auth_username` (`username`,`authority`),
                               CONSTRAINT `fk_authorities_users` FOREIGN KEY (`username`) REFERENCES `users` (`username`)
) ;

