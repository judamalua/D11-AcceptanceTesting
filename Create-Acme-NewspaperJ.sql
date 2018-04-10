start transaction;
-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Newspaper
-- ------------------------------------------------------
-- Server version	5.5.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


drop database if exists `acme-newspaper`;
create database `acme-newspaper`;

drop user 'acme-user'@'%';
drop user 'acme-manager'@'%';

create user 'acme-user'@'%' identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';
create user 'acme-manager'@'%' identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

use `acme-newspaper`;

grant select, insert, update, delete 
	on `acme-rendezvous`.* to 'acme-user'@'%';
grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `acme-rendezvous`.* to 'acme-manager'@'%';

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `birthDate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gfgqmtp2f9i5wsojt33xm0uth` (`userAccount_id`),
  CONSTRAINT `FK_gfgqmtp2f9i5wsojt33xm0uth` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (69,0,'1995-10-03','super@mail.com','Super',NULL,NULL,'Super',45),(70,0,'1987-04-11','administrator1@mail.com','Administrator 1',NULL,NULL,'Administrator',46);
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article`
--

DROP TABLE IF EXISTS `article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` longtext,
  `finalMode` bit(1) NOT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `taboo` bit(1) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_4lk2cp8abj9kf1g1qku5j959x` (`finalMode`,`taboo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article`
--

LOCK TABLES `article` WRITE;
/*!40000 ALTER TABLE `article` DISABLE KEYS */;
INSERT INTO `article` VALUES (71,0,'Test Articulo 1','','Si has sido aceptado recientemente en nuestra escuela, este artculo te ayudar a pasar tus primeros das.','\0','[GUIA] Como sobrevivir en la ETSII'),(72,0,'Test Articulo 2','','El prximo 5 de mayo el premio Nobel John Doe dar una conferencia en nuestra escuela sobre gestion de proyectos informáticos.','\0','Charla del premio Nobel John Doe'),(73,0,'Test Articulo 3','','En este artculo encontrars informacin para tu primer da corriendo por la ciudad. Adelante!','\0','Primer da de runner'),(74,0,'Test Articulo 4','','Con estos cinco consejos y un poco de constancia conseguirs bajar de peso con nuestro hobby favorito.','\0','5 consejos para perder peso corriendo'),(75,0,'En la universidad de sevilla se va a organizar un concurso de mecanografía en el que TÚ estas obligado a venir, pon a prueba tus reflejos y tu capacidad!','','Ponte a prueba!','\0','Concurso mecanografia en la etsii'),(76,0,'Con este curso te dejaremos listo para que puedas empezar a usar tu imaginación en crear algo maravilloso para la comunidad. El cielo es el límite!!','','Crea tu propio juego!','\0','Cusrso de programación en UNITY'),(77,0,'Ven con nosotros, nosotros ponemos la comida y tú la boca.','','Judamalua te invita a pizza unete!','\0','Come hasta que no puedas mas!'),(78,0,'1 Ven. 2 Come pizza. 3. Disfruta.','','Con estos cinco consejos conseguiras la felicidad','\0','5 consejos para ser feliz'),(79,0,'Fui de viaje al lago ness estas vacaciones en bussca del monstruo y no te los vas a creer!,     no encontré nada!. La proxima vez será :D.','','Voy al lago ness y pasa esto!','\0','No te lo vas a creer'),(80,0,'Actualizar los antivirus!!','','Los principes e corchuelo atacan de nuevo.','\0','Nadie esta seguro'),(81,0,'Borrador','\0','Borrador','\0','Borrador de artículo');
/*!40000 ALTER TABLE `article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `article_followup`
--

DROP TABLE IF EXISTS `article_followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `article_followup` (
  `Article_id` int(11) NOT NULL,
  `followUps_id` int(11) NOT NULL,
  UNIQUE KEY `UK_dga3q6hwsns7nc4vas0d0imlo` (`followUps_id`),
  KEY `FK_d7j7ndqrqlxfglsyspsaag0f9` (`Article_id`),
  CONSTRAINT `FK_d7j7ndqrqlxfglsyspsaag0f9` FOREIGN KEY (`Article_id`) REFERENCES `article` (`id`),
  CONSTRAINT `FK_dga3q6hwsns7nc4vas0d0imlo` FOREIGN KEY (`followUps_id`) REFERENCES `followup` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `article_followup`
--

LOCK TABLES `article_followup` WRITE;
/*!40000 ALTER TABLE `article_followup` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_followup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chirp`
--

DROP TABLE IF EXISTS `chirp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chirp` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `moment` datetime DEFAULT NULL,
  `taboo` bit(1) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_22i101vlgegtcw2p27wmhj742` (`taboo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chirp`
--

LOCK TABLES `chirp` WRITE;
/*!40000 ALTER TABLE `chirp` DISABLE KEYS */;
INSERT INTO `chirp` VALUES (53,0,'Este es mi primer chirp en Acme-Newspaper :)','2018-04-08 00:00:00','\0','Hola mundo!'),(54,0,'Al parecer el hombre tiene 3 trabajos muy distintos entre sí. Está muy ocupado y apenas tiene tiempo libre. ','2018-04-08 00:00:00','\0','He quedado con mi amigo Pipo'),(55,0,'El 14 de abril empieza la Feria, estoy deseando que llegue el día!','2018-04-08 00:00:00','\0','Deseando que empiece la Feria de Abril'),(56,0,'¿Quién se echa un Fornite?','2018-04-08 00:00:00','\0','Estoy aburrido'),(57,0,'Griezmann ha celebrado su gol contra el Madrid bailando como un personaje del Fornite, ¡qué grande!','2018-04-08 00:00:00','\0','La celebración de Griezmann jaja'),(58,0,'Ese periódico runner está repleto de gente que me cae mal...','2018-04-08 00:00:00','\0','Odio a los runners'),(59,0,'Tengo dos gatos, y desearía que en este sistema hubiera un periódico sobre animales o gatos','2018-04-08 00:00:00','\0','Me encantan los animales'),(60,0,'¿Alguien sabe si es normal?','2018-04-08 00:00:00','\0','Mi gata no para de dormir'),(61,0,'Yo creo que será el Barça... Messi es el mejor!','2018-04-08 00:00:00','\0','¿Quien pensáis que ganará la Champions?'),(62,0,'¿Quién os gusta más? Creo que Messi es mejor, pero Cristiano es muy guapo!','2018-04-08 00:00:00','\0','Messi vs Cristiano'),(63,0,'Esta semana de Feria voy a estar aburrido y me gustaría leer un poco','2018-04-08 00:00:00','\0','¿Me recomendáis un libro?'),(64,0,'Hablar de sexo está muy mal visto en nuestra sociedad. ¿Por qué?','2018-04-08 00:00:00','\0','Palabras tabú'),(65,0,'Aunque en Twitter hay muchos más usuarios, Acme-Newspaper me gusta más!','2018-04-08 00:00:00','\0','Esto de los chirps... es muy parecido a Twitter');
/*!40000 ALTER TABLE `chirp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration`
--

DROP TABLE IF EXISTS `configuration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `businessNameFirst` varchar(255) DEFAULT NULL,
  `businessNameLast` varchar(255) DEFAULT NULL,
  `cookies_eng` varchar(255) DEFAULT NULL,
  `cookies_es` varchar(255) DEFAULT NULL,
  `pageSize` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration`
--

LOCK TABLES `configuration` WRITE;
/*!40000 ALTER TABLE `configuration` DISABLE KEYS */;
INSERT INTO `configuration` VALUES (88,0,'Acme','Newspaper','This page uses cookies for its internal operations, by continuing, we will assume that you accept these cookies.','Esta página usa cookies para realizar su correcto funcionamiento, si continúa navegando, asumiremos que acepta estas cookies.',5);
/*!40000 ALTER TABLE `configuration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuration_taboowords`
--

DROP TABLE IF EXISTS `configuration_taboowords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configuration_taboowords` (
  `Configuration_id` int(11) NOT NULL,
  `tabooWords` varchar(255) DEFAULT NULL,
  KEY `FK_jflyxemijdnhx9v7hc8aodm6g` (`Configuration_id`),
  CONSTRAINT `FK_jflyxemijdnhx9v7hc8aodm6g` FOREIGN KEY (`Configuration_id`) REFERENCES `configuration` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuration_taboowords`
--

LOCK TABLES `configuration_taboowords` WRITE;
/*!40000 ALTER TABLE `configuration_taboowords` DISABLE KEYS */;
INSERT INTO `configuration_taboowords` VALUES (88,'viagra'),(88,'cialis'),(88,'sex'),(88,'sexo');
/*!40000 ALTER TABLE `configuration_taboowords` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `creditcard`
--

DROP TABLE IF EXISTS `creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `creditcard` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `brandName` varchar(255) DEFAULT NULL,
  `cookieToken` varchar(255) DEFAULT NULL,
  `cvv` int(11) DEFAULT NULL,
  `expirationMonth` int(11) DEFAULT NULL,
  `expirationYear` int(11) DEFAULT NULL,
  `holderName` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `customer_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_hcl4y7dcrebek2iyjpoa7vt0w` (`cookieToken`),
  KEY `FK_ngdvidm52sk4p32c2tt4a9nq7` (`customer_id`),
  CONSTRAINT `FK_ngdvidm52sk4p32c2tt4a9nq7` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `creditcard`
--

LOCK TABLES `creditcard` WRITE;
/*!40000 ALTER TABLE `creditcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `birthDate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_pwmktpkay2yx7v00mrwmuscl8` (`userAccount_id`),
  CONSTRAINT `FK_pwmktpkay2yx7v00mrwmuscl8` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (66,0,'1987-04-11','user1@mail.com','Customer 1','635587789','San Antion Spurs Street','Customer',49);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followup`
--

DROP TABLE IF EXISTS `followup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `followup` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `publicationDate` date DEFAULT NULL,
  `summary` varchar(255) DEFAULT NULL,
  `taboo` bit(1) NOT NULL,
  `text` longtext,
  `title` varchar(255) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_38ij9oiisx23fdi0kaje2fw5m` (`publicationDate`,`taboo`),
  KEY `FK_pikn3prpshakeb3jseg908jf2` (`user_id`),
  CONSTRAINT `FK_pikn3prpshakeb3jseg908jf2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followup`
--

LOCK TABLES `followup` WRITE;
/*!40000 ALTER TABLE `followup` DISABLE KEYS */;
/*!40000 ALTER TABLE `followup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('DomainEntity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newspaper`
--

DROP TABLE IF EXISTS `newspaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newspaper` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `publicNewspaper` bit(1) NOT NULL,
  `publicationDate` date DEFAULT NULL,
  `taboo` bit(1) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_fvud9pk9cmcdjljg56xsqlnqc` (`publicNewspaper`,`taboo`,`publicationDate`,`title`,`description`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newspaper`
--

LOCK TABLES `newspaper` WRITE;
/*!40000 ALTER TABLE `newspaper` DISABLE KEYS */;
INSERT INTO `newspaper` VALUES (82,0,'El Informtico es el peridico oficial de la Escuela Tcnica Superior de Ingeniera Informtica de la Universidad de Sevilla.     Sguenos y mantente al corriente sobre las ltimas novedades de la escuela!','http://comunicacion.us.es/sites/default/files/ingenieria_informatica.jpg','','2018-04-09','\0','El Informatico'),(83,0,'The Runner Journal es el diario donde runners de todo el mundo comparten sus experiencias sobre el deporte ms practicado hoy en da. Ponte las botas, que nos vamos!','https://blogicalimentaria.files.wordpress.com/2016/03/runners.jpg','',NULL,'\0','The Runner Journal'),(84,0,'The biggest pizza in the world in Spain! Some people think that is better than sex.','https://i0.wp.com/conectica.com/wp-content/uploads/2017/02/pizza-más-grande-del-mundo.jpg','\0','2018-04-04','','The big pizza'),(85,0,'The biggest hamburger in the world in Spain!','https://ep00.epimg.net/elpais/imagenes/2016/07/04/tentaciones/1467621449_945556_1467633477_noticia_normal.jpg','\0','2018-01-09','\0','The big hamburger'),(86,0,'Nessie sighted again. ','https://www.pullmantur.travel/media/images/b2bbrasil/continentes/europa/escocia/lago-ness/608-240px/lago-ness-panoramica-foto-general.jpg','\0','2012-11-03','\0','Nessie'),(87,0,'The company Judamalua S.A was attacked the last day by a group of hackers autodenominated the princes of corchuelo.','https://cdn.tn.com.ar/sites/default/files/styles/1366x765/public/2017/10/27/hacker.jpg','','2018-04-08','\0','New hacker attacks!');
/*!40000 ALTER TABLE `newspaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newspaper_article`
--

DROP TABLE IF EXISTS `newspaper_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newspaper_article` (
  `Newspaper_id` int(11) NOT NULL,
  `articles_id` int(11) NOT NULL,
  UNIQUE KEY `UK_b6yh4ur28qo9smnd995jo356g` (`articles_id`),
  KEY `FK_53pw01kjsjxn1ycjlwa1q6j13` (`Newspaper_id`),
  CONSTRAINT `FK_53pw01kjsjxn1ycjlwa1q6j13` FOREIGN KEY (`Newspaper_id`) REFERENCES `newspaper` (`id`),
  CONSTRAINT `FK_b6yh4ur28qo9smnd995jo356g` FOREIGN KEY (`articles_id`) REFERENCES `article` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newspaper_article`
--

LOCK TABLES `newspaper_article` WRITE;
/*!40000 ALTER TABLE `newspaper_article` DISABLE KEYS */;
INSERT INTO `newspaper_article` VALUES (82,71),(82,72),(82,73),(82,74),(82,75),(82,76),(83,81),(84,77),(85,78),(86,79),(87,80);
/*!40000 ALTER TABLE `newspaper_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newspaper_creditcard`
--

DROP TABLE IF EXISTS `newspaper_creditcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `newspaper_creditcard` (
  `Newspaper_id` int(11) NOT NULL,
  `creditCards_id` int(11) NOT NULL,
  KEY `FK_2wlij0yg02u5nmhsylh4w8bco` (`creditCards_id`),
  KEY `FK_3wf54dshwo4ltvhhg02selht0` (`Newspaper_id`),
  CONSTRAINT `FK_3wf54dshwo4ltvhhg02selht0` FOREIGN KEY (`Newspaper_id`) REFERENCES `newspaper` (`id`),
  CONSTRAINT `FK_2wlij0yg02u5nmhsylh4w8bco` FOREIGN KEY (`creditCards_id`) REFERENCES `creditcard` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newspaper_creditcard`
--

LOCK TABLES `newspaper_creditcard` WRITE;
/*!40000 ALTER TABLE `newspaper_creditcard` DISABLE KEYS */;
/*!40000 ALTER TABLE `newspaper_creditcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `slider`
--

DROP TABLE IF EXISTS `slider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `slider` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `align` varchar(255) DEFAULT NULL,
  `pictureUrl` varchar(255) DEFAULT NULL,
  `text_en` varchar(255) DEFAULT NULL,
  `text_es` varchar(255) DEFAULT NULL,
  `title_en` varchar(255) DEFAULT NULL,
  `title_es` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `slider`
--

LOCK TABLES `slider` WRITE;
/*!40000 ALTER TABLE `slider` DISABLE KEYS */;
INSERT INTO `slider` VALUES (50,0,'left','https://tinyurl.com/adventure-meetup','Example text 1','Texto de ejemplo 1','Welcome to Sample Project 1.14!','¡Bienvenid@ a Sample Project 1.14!'),(51,0,'right','https://i.imgur.com/SoiZR6j.jpg','Example text 2','Texto de ejemplo 2','Welcome to Sample Project 1.14!','¡Bienvenid@ a Sample Project 1.14!'),(52,0,'center','https://imgur.com/Wu0HYcs.jpg','Example text 3','Texto de ejemplo 3','Welcome to Sample Project 1.14!','áBienvenid@ a Sample Project 1.14!');
/*!40000 ALTER TABLE `slider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `birthDate` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `userAccount_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_o6s94d43co03sx067ili5760c` (`userAccount_id`),
  CONSTRAINT `FK_o6s94d43co03sx067ili5760c` FOREIGN KEY (`userAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (67,0,'1987-04-11','user1@mail.com','User 1','635587789','San Antion Spurs Street','User',47),(68,0,'1992-07-15','user2@mail.com','User 2','635582389','Okklahoma Thunder Street','User ',48);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_chirp`
--

DROP TABLE IF EXISTS `user_chirp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_chirp` (
  `User_id` int(11) NOT NULL,
  `chirps_id` int(11) NOT NULL,
  UNIQUE KEY `UK_ls9bn8hbpkktyfahbm4wujrps` (`chirps_id`),
  KEY `FK_8lvf5igmdhhmxc70p7ujd36ym` (`User_id`),
  CONSTRAINT `FK_8lvf5igmdhhmxc70p7ujd36ym` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_ls9bn8hbpkktyfahbm4wujrps` FOREIGN KEY (`chirps_id`) REFERENCES `chirp` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_chirp`
--

LOCK TABLES `user_chirp` WRITE;
/*!40000 ALTER TABLE `user_chirp` DISABLE KEYS */;
INSERT INTO `user_chirp` VALUES (67,53),(67,54),(67,55),(67,56),(67,57),(67,58),(67,59),(67,60),(68,61),(68,62),(68,63),(68,64),(68,65);
/*!40000 ALTER TABLE `user_chirp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_newspaper`
--

DROP TABLE IF EXISTS `user_newspaper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_newspaper` (
  `User_id` int(11) NOT NULL,
  `newspapers_id` int(11) NOT NULL,
  UNIQUE KEY `UK_oqhrpkgl440xehybmm713ru36` (`newspapers_id`),
  KEY `FK_24xv3fsqc505dhy1bv4ldnor9` (`User_id`),
  CONSTRAINT `FK_24xv3fsqc505dhy1bv4ldnor9` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_oqhrpkgl440xehybmm713ru36` FOREIGN KEY (`newspapers_id`) REFERENCES `newspaper` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_newspaper`
--

LOCK TABLES `user_newspaper` WRITE;
/*!40000 ALTER TABLE `user_newspaper` DISABLE KEYS */;
INSERT INTO `user_newspaper` VALUES (67,82),(67,83),(67,84),(67,85),(67,86),(67,87);
/*!40000 ALTER TABLE `user_newspaper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_user`
--

DROP TABLE IF EXISTS `user_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_user` (
  `User_id` int(11) NOT NULL,
  `users_id` int(11) NOT NULL,
  KEY `FK_skc3d47tu4nnp2kx12n5lsiur` (`users_id`),
  KEY `FK_nlnx78x3m38aq2r86t1d5eio1` (`User_id`),
  CONSTRAINT `FK_nlnx78x3m38aq2r86t1d5eio1` FOREIGN KEY (`User_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_skc3d47tu4nnp2kx12n5lsiur` FOREIGN KEY (`users_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_user`
--

LOCK TABLES `user_user` WRITE;
/*!40000 ALTER TABLE `user_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount`
--

DROP TABLE IF EXISTS `useraccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_csivo9yqa08nrbkog71ycilh5` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount`
--

LOCK TABLES `useraccount` WRITE;
/*!40000 ALTER TABLE `useraccount` DISABLE KEYS */;
INSERT INTO `useraccount` VALUES (45,0,'21232f297a57a5a743894a0e4a801fc3','admin'),(46,0,'e00cf25ad42683b3df678c61f42c6bda','admin1'),(47,0,'24c9e15e52afc47c225b757e7bee1f9d','user1'),(48,0,'7e58d63b60197ceb55a1c487989a3720','user2'),(49,0,'ffbc4675f864e0e9aab8bdf7a0437010','customer1');
/*!40000 ALTER TABLE `useraccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `useraccount_authorities`
--

DROP TABLE IF EXISTS `useraccount_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `useraccount_authorities` (
  `UserAccount_id` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_b63ua47r0u1m7ccc9lte2ui4r` (`UserAccount_id`),
  CONSTRAINT `FK_b63ua47r0u1m7ccc9lte2ui4r` FOREIGN KEY (`UserAccount_id`) REFERENCES `useraccount` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `useraccount_authorities`
--

LOCK TABLES `useraccount_authorities` WRITE;
/*!40000 ALTER TABLE `useraccount_authorities` DISABLE KEYS */;
INSERT INTO `useraccount_authorities` VALUES (45,'ADMIN'),(46,'ADMIN'),(47,'USER'),(48,'USER'),(49,'CUSTOMER');
/*!40000 ALTER TABLE `useraccount_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-10 14:38:52
commit;
