DROP DATABASE TapasTop;
CREATE DATABASE TapasTop;
USE TapasTop;

CREATE TABLE `usuario` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) UNIQUE NOT NULL,
  `email` VARCHAR(255) UNIQUE NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `enabled` BIT(1) NOT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `country` VARCHAR(255) DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `first_name` VARCHAR(255) DEFAULT NULL,
  `gender` VARCHAR(255) DEFAULT NULL,
  `last_name` VARCHAR(255) DEFAULT NULL,
  `photo_url` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `local` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) UNIQUE NOT NULL,
  `id_usuario` BIGINT NOT NULL,
  `address` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `longitude` DOUBLE DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
  ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `degustacion` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `plate_name` VARCHAR(255) NOT NULL,
  `id_local` BIGINT NOT NULL,
  `id_usuario` BIGINT NOT NULL,
  `created_at` DATETIME DEFAULT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `origin_country` VARCHAR(255) DEFAULT NULL,
  `photourl` VARCHAR(255) DEFAULT NULL,
  `taste_qualifier` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`plate_name`, `id_local`),
  FOREIGN KEY (`id_local`) REFERENCES `local` (`id`) 
  ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
  ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usuario_valora_degustacion` (
  `id_degustacion` BIGINT NOT NULL,
  `id_usuario` BIGINT NOT NULL,
  `rate` INT,
  PRIMARY KEY (`id_degustacion`,`id_usuario`),
  FOREIGN KEY (`id_degustacion`) REFERENCES `degustacion` (`id`)
  ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id`)
  ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
