--
-- Datenbank: `finances`
--
CREATE DATABASE IF NOT EXISTS `finances` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `finances`;

--
-- Tabellenstruktur für Tabelle `Security`
--
DROP TABLE IF EXISTS `SEC`;
CREATE TABLE IF NOT EXISTS `SEC` (
  `id` varchar(36) NOT NULL,
  `inventory` bit DEFALT FALSE NOT NULL,
  `isin` varchar(12) NOT NULL,
  `name` varchar(255) NOT NULL,
  `symbol` varchar(6),
  `type` varchar(255) NOT NULL,
  `wkn` varchar(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `SEC` ADD CONSTRAINT `UK_EKHJ3LSPUP7ST9DCHEXCWU4Y8` UNIQUE (`isin`);
ALTER TABLE `SEC` ADD CONSTRAINT `UK_K4Y19WXR5JJ87TGHD9T44JTKJ` UNIQUE (`wkn`);

--
-- Tabellenstruktur für Tabelle `Entry`
--
DROP TABLE IF EXISTS `Entry`;
CREATE TABLE IF NOT EXISTS `Entry` (
  `id` varchar(36) NOT NULL,
  `amount` decimal(16,6) NOT NULL,
  `date` datetime NOT NULL,
  `number_of` decimal(16,4) NOT NULL,
  `price` decimal(16,6) NOT NULL,
  `type` varchar(255) NOT NULL,
  `security_id` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4001852C0BEF936` (`security_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `Entry` ADD CONSTRAINT FK6VWMQ6T0OI9CV29TXC2EMIKSP FOREIGN KEY (`security_id`) REFERENCES `SEC` (`id`);

--
-- Tabellenstruktur für Tabelle `Inflation`
--
DROP TABLE IF EXISTS `Inflation`;
CREATE TABLE IF NOT EXISTS `Inflation` (
  `year` int(11) NOT NULL,
  `rate` double NOT NULL,
  PRIMARY KEY (`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Tabellenstruktur für Tabelle `UserProperty`
--
DROP TABLE IF EXISTS `UserProperty`;
CREATE TABLE IF NOT EXISTS `UserProperty` (
  `key` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL,
  `value` varchar(255) CHARACTER SET latin2 NOT NULL,
  PRIMARY KEY (`key`,`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

