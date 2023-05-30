-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: university_timetabling
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `university_timetabling`
--

--CREATE DATABASE /*!32312 IF NOT EXISTS*/ `university_timetabling` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `university_timetabling`;


-- Table structure for table `faculties`
--

--DROP TABLE IF EXISTS `faculties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `faculties` (
  `faculty_id` int NOT NULL AUTO_INCREMENT,
  `faculty_code` varchar(10) DEFAULT NULL,
  `faculty_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`faculty_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `faculties`
--

LOCK TABLES `faculties` WRITE;
/*!40000 ALTER TABLE `faculties` DISABLE KEYS */;
INSERT INTO `faculties` VALUES (1,'FHS','Faculty of Health Sciences'),(2,'FST','Faculty of Science And Technology'),(3,'FVM','Faculty of Veterinary Medicine'),(4,'FBMS','Faculty of Business and Management Sciences');
/*!40000 ALTER TABLE `faculties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

--DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `dept_id` int NOT NULL AUTO_INCREMENT,
  `dept_code` varchar(10) DEFAULT NULL,
  `dept_name` varchar(255) DEFAULT NULL,
  `faculty_id` int DEFAULT NULL,
  PRIMARY KEY (`dept_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `departments_ibfk_1` FOREIGN KEY (`faculty_id`) REFERENCES `faculties` (`faculty_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'I18','computer science',2),(2,'I20','mathematics',2),(3,'B17','commerce',4);
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `rooms`
--

DROP TABLE IF EXISTS `rooms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `room_name` varchar(255) DEFAULT NULL,
  `room_capacity` int DEFAULT NULL,
  `room_type` varchar(255) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  PRIMARY KEY (`room_id`),
  KEY `dept_id` (`dept_id`),
  CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `departments` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms`
--

LOCK TABLES `rooms` WRITE;
/*!40000 ALTER TABLE `rooms` DISABLE KEYS */;
INSERT INTO `rooms` VALUES (1,'1st Year Computer Lab',120,'LAB',1,1),(2,'2nd Year Computer Lab',120,'LAB',1,1),(3,'3rd Year Computer Lab',120,'LAB',1,1),(4,'4th Year Computer Lab',120,'LAB',1,1),(5,'SCI Room 100',120,'SLT',1,1),(6,'SCI Room 200',120,'SLT',1,1),(7,'SCI Room 300',120,'SLT',1,1),(8,'SCI Room 400',120,'SLT',1,1),(9,'DS Room 100',279,'SLT',1,2),(10,'DS Room 200',135,'LT',1,2),(11,'DS Room 300',374,'LT',1,2),(12,'DS Room 400',94,'LT',1,2),(13,'Bcom Room 100',230,'LT',1,3),(14,'Bcom Room 200',104,'LT',1,3),(15,'Bcom Room 300',495,'LT',1,3),(16,'Bcom Room 400',193,'SLT',1,3),(17,'Millenium Hall 1234',800,'HALL',1,3);
/*!40000 ALTER TABLE `rooms` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `room_department`
--

DROP TABLE IF EXISTS `room_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room_department` (
  `room_id` int NOT NULL,
  `dept_id` int NOT NULL,
  PRIMARY KEY (`room_id`,`dept_id`),
  KEY `dept_id` (`dept_id`),
  CONSTRAINT `room_department_ibfk_1` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`room_id`),
  CONSTRAINT `room_department_ibfk_2` FOREIGN KEY (`dept_id`) REFERENCES `departments` (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room_department`
--

LOCK TABLES `room_department` WRITE;
/*!40000 ALTER TABLE `room_department` DISABLE KEYS */;
INSERT INTO `room_department` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(17,1),(9,2),(10,2),(11,2),(12,2),(17,2),(13,3),(14,3),(15,3),(16,3),(17,3);
/*!40000 ALTER TABLE `room_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programmes`
--

DROP TABLE IF EXISTS `programmes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programmes` (
  `programme_id` int NOT NULL AUTO_INCREMENT,
  `programme_code` varchar(10) DEFAULT NULL,
  `programme_name` varchar(255) DEFAULT NULL,
  `faculty_id` int DEFAULT NULL,
  PRIMARY KEY (`programme_id`),
  KEY `faculty_id` (`faculty_id`),
  CONSTRAINT `programmes_ibfk_1` FOREIGN KEY (`faculty_id`) REFERENCES `faculties` (`faculty_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programmes`
--

LOCK TABLES `programmes` WRITE;
/*!40000 ALTER TABLE `programmes` DISABLE KEYS */;
INSERT INTO `programmes` VALUES (1,'P15','Bachelor of Science Computer Science',2),(2,'I17','Bachelor of Science In Data Science',2),(3,'B22','Bachelor of Commerce(Human Resource)',4);
/*!40000 ALTER TABLE `programmes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `program_enrollments`
--

DROP TABLE IF EXISTS `program_enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `program_enrollments` (
  `enrollment_id` int NOT NULL AUTO_INCREMENT,
  `program_id` int NOT NULL,
  `enrollment_year` int NOT NULL,
  `enrollment_number` int NOT NULL,
  PRIMARY KEY (`enrollment_id`),
  KEY `program_id` (`program_id`),
  CONSTRAINT `program_enrollments_ibfk_1` FOREIGN KEY (`program_id`) REFERENCES `programmes` (`programme_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `program_enrollments`
--

LOCK TABLES `program_enrollments` WRITE;
/*!40000 ALTER TABLE `program_enrollments` DISABLE KEYS */;
INSERT INTO `program_enrollments` VALUES (1,1,1,100),(2,1,2,100),(3,1,3,100),(4,1,4,100),(5,2,1,100),(6,2,2,100),(7,2,3,100),(8,3,1,100),(9,3,2,100),(10,3,3,100),(11,3,4,100);
/*!40000 ALTER TABLE `program_enrollments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `user_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Justina','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(2,'Hildegaard','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(3,'Errol','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(4,'Denny','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(5,'Robby','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(6,'Priscilla','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(7,'Yuri','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(8,'Hermia','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(9,'Cesare','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(10,'Coleen','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(11,'Terese','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(12,'Sharona','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(13,'Juliane','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(14,'Romy','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(15,'Joan','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(16,'Vernen','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(17,'Terencio','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(18,'Haslett','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(19,'Hersch','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(20,'Lexine','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(21,'Nancey','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(22,'Merlina','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(23,'Betsey','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(24,'Andy','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(25,'Beverie','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(26,'Neel','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(27,'Vinita','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(28,'Rana','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(29,'Bondie','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(30,'Avram','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(31,'Susie','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(32,'Ferdinande','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(33,'Brander','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(34,'Myrilla','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(35,'Jacinda','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(36,'Avis','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(37,'Umberto','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(38,'Jesselyn','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(39,'Yettie','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(40,'Ruttger','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(41,'Lucienne','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(42,'Issi','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(43,'Garvy','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(44,'Sergent','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(45,'Fannie','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','INSTRUCTOR','Instructor'),(46,'Kyalo','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(47,'Wanyoro','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(48,'Kaseve','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(49,'Yvette','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(50,'Maurice','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(51,'Mativo','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(52,'Jeff','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(53,'Kimingi','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(54,'Mercy','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(55,'Brendah','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(56,'Sandrah','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','STUDENT','Student'),(57,'Admin','$2a$10$ynmv1/ETM9jvnXy5JWzUTO9R9r6ASAW8DWbuXqEsH2gBsS5IpgViG','ADMIN','');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `students` (
  `id` int NOT NULL AUTO_INCREMENT,
  `year` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `program_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `program_id` (`program_id`),
  CONSTRAINT `students_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `students_ibfk_2` FOREIGN KEY (`program_id`) REFERENCES `programmes` (`programme_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,1,46,1),(2,2,47,1),(3,3,48,1),(4,4,49,1),(5,1,50,2),(6,2,51,2),(7,3,52,2),(8,1,53,3),(9,2,54,3),(10,3,55,3),(11,4,56,3);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructors`
--

DROP TABLE IF EXISTS `instructors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instructors` (
  `instructor_id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `dept_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`instructor_id`),
  KEY `dept_id` (`dept_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `instructors_ibfk_1` FOREIGN KEY (`dept_id`) REFERENCES `departments` (`dept_id`),
  CONSTRAINT `instructors_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructors`
--

LOCK TABLES `instructors` WRITE;
/*!40000 ALTER TABLE `instructors` DISABLE KEYS */;
INSERT INTO `instructors` VALUES (1,'Justina','Rattray',1,1),(2,'Hildegaard','Daintrey',1,2),(3,'Errol','Belfelt',1,3),(4,'Denny','Huyton',1,4),(5,'Robby','Jacobovitch',1,5),(6,'Priscilla','Bulley',1,6),(7,'Yuri','Heisler',1,7),(8,'Hermia','Raynard',1,8),(9,'Cesare','Booth-Jarvis',1,9),(10,'Coleen','McClifferty',1,10),(11,'Terese','Murtagh',1,11),(12,'Sharona','Kemwall',1,12),(13,'Juliane','Waddilow',1,13),(14,'Romy','Lowdham',1,14),(15,'Joan','Baseley',1,15),(16,'Vernen','Prandini',1,16),(17,'Terencio','Skewes',1,17),(18,'Haslett','Borrows',2,18),(19,'Hersch','Feragh',2,19),(20,'Lexine','Shutle',2,20),(21,'Nancey','Fierro',2,21),(22,'Merlina','Threadgall',2,22),(23,'Betsey','Gatley',2,23),(24,'Andy','Grigs',2,24),(25,'Beverie','Guitt',2,25),(26,'Neel','Romaine',2,26),(27,'Vinita','Thulborn',2,27),(28,'Rana','Viccary',2,28),(29,'Bondie','Pryell',2,29),(30,'Avram','Lademann',2,30),(31,'Susie','Eastgate',3,31),(32,'Ferdinande','Piele',3,32),(33,'Brander','Daltrey',3,33),(34,'Myrilla','Croxon',3,34),(35,'Jacinda','Edgin',3,35),(36,'Avis','Dosedale',3,36),(37,'Umberto','Fourcade',3,37),(38,'Jesselyn','Haggart',3,38),(39,'Yettie','Eglese',3,39),(40,'Ruttger','Castellini',3,40),(41,'Lucienne','Howels',3,41),(42,'Issi','Bracer',3,42),(43,'Garvy','Livezley',3,43),(44,'Sergent','Dixon',3,44),(45,'Fannie','Ayers',3,45);
/*!40000 ALTER TABLE `instructors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `courses` (
  `course_id` int NOT NULL AUTO_INCREMENT,
  `course_code` varchar(10) DEFAULT NULL,
  `course_name` varchar(255) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `semester` int DEFAULT NULL,
  `room_spec` varchar(255) DEFAULT NULL,
  `programme_id` int DEFAULT NULL,
  `dept_id` int DEFAULT NULL,
  `instructor_id` int DEFAULT NULL,
  `common_id` int DEFAULT NULL,
  PRIMARY KEY (`course_id`),
  KEY `programme_id` (`programme_id`),
  KEY `dept_id` (`dept_id`),
  KEY `instructor_id` (`instructor_id`),
  CONSTRAINT `courses_ibfk_1` FOREIGN KEY (`programme_id`) REFERENCES `programmes` (`programme_id`),
  CONSTRAINT `courses_ibfk_2` FOREIGN KEY (`dept_id`) REFERENCES `departments` (`dept_id`),
  CONSTRAINT `courses_ibfk_3` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`instructor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (1,'CSC111','Introduction to Computer Systems',1,1,NULL,1,1,1,0),(2,'CSC112','Introduction to Programming',1,1,NULL,1,1,2,0),(3,'CSC113','Discrete Mathematics',1,1,'SLT',1,1,3,0),(4,'CSC115','Programming Lab �',1,1,'LAB',1,1,4,0),(5,'CSC126','Physics for Computing Systems',1,1,NULL,1,1,5,0),(6,'CCS001','Communication Skills',1,1,'HALL',1,1,6,1),(7,'CCS009','Elements of Economics�',1,1,NULL,1,1,7,0),(8,'CSC122�','Database Systems',1,2,NULL,1,1,8,0),(9,'CSC123','Data Communications',1,2,NULL,1,1,9,0),(10,'�CSC125','Linear Algebra�',1,2,NULL,1,1,10,0),(11,'CSC127','Object Oriented Programming',1,2,NULL,1,1,11,0),(12,'CSC211','�Data Structures and Algorithms',1,2,NULL,1,1,12,0),(13,'CSC214','Digital Electronics �',1,2,NULL,1,1,13,0),(14,'CCS010','HIV/AIDS �',1,2,NULL,1,1,14,0),(15,'CSC114','Differential and Integral Calculus',2,1,NULL,1,1,15,0),(16,'CSC212�','Systems Analysis and Design',2,1,NULL,1,1,16,0),(17,'CSC213�','Computer Architecture',2,1,NULL,1,1,17,0),(18,'CSC217�','Knowledge-based Systems & Programming �',2,1,NULL,1,1,1,0),(19,'CSC223�','Operating Systems �',2,1,NULL,1,1,2,0),(20,'CSC224�','Software Engineering',2,1,NULL,1,1,3,0),(21,'CSC225�','�Computer Networks �',2,1,NULL,1,1,4,0),(22,'CSC124','Probability and Statistics',2,2,NULL,1,1,5,0),(23,'CSC216�','Assembly Language Programming',2,2,NULL,1,1,6,0),(24,'CSC222�','Automata Theory',2,2,NULL,1,1,7,0),(25,'CSC227�','Programming Project�',2,2,NULL,1,1,8,0),(26,'CSC228�','Web and Services Programming �',2,2,NULL,1,1,9,0),(27,'CSC229�','Machine Learning Algorithms & Programming�',2,2,NULL,1,1,10,0),(28,'CSC313�','Foundations of Human Computer Interaction',2,2,NULL,1,1,11,0),(29,'CSC311�','Analysis and Design of Algorithms',3,1,NULL,1,1,12,0),(30,'CSC314�','Computer Graphics �',3,1,NULL,1,1,13,0),(31,'CSC315�','Distributed Systems',3,1,NULL,1,1,14,0),(32,'CSC316�','Intro to Organizations and Management �',3,1,NULL,1,1,15,0),(33,'CSC317�','Artificial Intelligence Applications',3,1,NULL,1,1,16,0),(34,'CSC318�','Network Design Implementation and Management',3,1,NULL,1,1,17,0),(35,'CSC319�','Innovation & Entrepreneurship',3,1,NULL,1,1,1,0),(36,'CSC321�','ICT Project Management',3,2,NULL,1,1,2,0),(37,'CSC322�','Network and Distributed Programming',3,2,NULL,1,1,3,0),(38,'CSC326�','Compiler Construction',3,2,NULL,1,1,4,0),(39,'CSC327�','Embedded Systems & Mobile Programming',3,2,NULL,1,1,5,0),(40,'CSC328�','Business Intelligence & Analytics',3,2,NULL,1,1,6,0),(41,'CSC411�','Computer Network Security',3,2,NULL,1,1,7,0),(42,'CSC414','ICTs and Society�',4,1,NULL,1,1,8,0),(43,'CSC417�','Information Systems and Organizations',4,1,NULL,1,1,9,0),(44,'CSC418�','Emerging Technologies Bootcamps',4,1,NULL,1,1,10,0),(45,'CSC451�','Distributed Databases�',4,1,NULL,1,1,11,0),(46,'CSC481�','Computer Games Programming',4,1,NULL,1,1,12,0),(47,'CSC416�','Computer Systems Project �',4,1,NULL,1,1,13,0),(48,'CSC416','Computer Systems Project',4,2,NULL,1,1,14,0),(49,'CSC434�','Cloud Computing and Services',4,2,NULL,1,1,15,0),(50,'CSC452�','Information Systems Control Audit �',4,2,NULL,1,1,16,0),(51,'CSC455�','Information for Emerging Online Solutions�',4,2,NULL,1,1,17,0),(52,'SMA3101','Basic Mathematics',1,1,NULL,2,2,18,0),(53,'SMA3103','Calculus I',1,1,NULL,2,2,19,0),(54,'SMA3105','Geometry I',1,1,NULL,2,2,20,0),(55,'STA3101','Introduction to Probability and Statistics',1,1,NULL,2,2,21,0),(56,'SCS3101','Introduction to Computer Systems',1,1,NULL,2,2,22,0),(57,'SCS3103','Introduction to Programming',1,1,NULL,2,2,23,0),(58,'SMA3104','Calculus II',1,2,NULL,2,2,24,0),(59,'SMA3116','Geometry II',1,2,NULL,2,2,25,0),(60,'SMA3108','Discrete Mathematics I',1,2,NULL,2,2,26,0),(61,'SDS3102','Foundations of Data Science',1,2,NULL,2,2,27,0),(62,'CCS010','HIV/AIDS �',1,2,NULL,2,2,28,0),(63,'SMA3201','Advanced Calculus',2,1,NULL,2,2,29,0),(64,'SMA3203','Linear Algebra I',2,1,NULL,2,2,30,0),(65,'SMA3251','Discrete Mathematics II',2,1,NULL,2,2,18,0),(66,'STA3201','Probability and Statistics I',2,1,NULL,2,2,19,0),(67,'SDS3201','Database Systems',2,1,NULL,2,2,20,0),(68,'SDS3203','Data Structures and Algorithms',2,2,NULL,2,2,21,0),(69,'SMA3204','Linear Algebra II',2,2,NULL,2,2,22,0),(70,'SMA3206','Introduction to Analysis',2,2,NULL,2,2,23,0),(71,'SMA3208','Ordinary Differential Equations I',2,2,NULL,2,2,24,0),(72,'STA3202','Statistical Inference I',2,2,NULL,2,2,25,0),(73,'STA3222','Time Series Analysis',2,2,NULL,2,2,26,0),(74,'SDS3204','Data Mining and Visualization',2,1,NULL,2,2,27,0),(75,'STA3301','Probability and Statistics II',3,1,NULL,2,2,28,0),(76,'SMA3351','Optimization Methods',3,1,NULL,2,2,29,0),(77,'SDS3301','Artificial Intelligence',3,1,NULL,2,2,30,0),(78,'SDS3303','Systems Analysis and Design',3,1,NULL,2,2,18,0),(79,'SDS3311','Numerical Methods and Convex Optimisation',3,2,NULL,2,2,19,0),(80,'STA3302','Linear Modeling',3,2,NULL,2,2,20,0),(81,'STA3308','Sample Survey Methods',3,2,NULL,2,2,21,0),(82,'STA3318','Statistical Inference II',3,2,NULL,2,2,22,0),(83,'SDS3304','Software Engineering',3,2,NULL,2,2,23,0),(84,'SDS3306','Machine Learning',3,2,NULL,2,2,24,0),(85,'CCS001','Communication Skills',1,1,NULL,3,3,31,1),(86,'ICT1101','Introduction to Information Systems',1,1,NULL,3,3,32,0),(87,'ACC1101','Introduction to Accounting I',1,1,NULL,3,3,33,0),(88,'BAM1101','Business Studies',1,1,NULL,3,3,34,0),(89,'BAM1102','Principles of Management',1,1,NULL,3,3,35,0),(90,'MAT1102','Management Mathematics I',1,1,NULL,3,3,36,0),(91,'CUU002','Information Literacy',1,1,NULL,3,3,37,0),(92,'CUU0001','Health Awareness & Life Skills',1,2,NULL,3,3,38,0),(93,'BAM1204','Business Law I',1,2,NULL,3,3,39,0),(94,'BAM1205','Development Sudies and Ethics',1,2,NULL,3,3,40,0),(95,'ACC1202','Introduction to Accounting II',1,2,NULL,3,3,41,0),(96,'ECO11201','Introduction to Macroeconomics',1,2,NULL,3,3,42,0),(97,'MKT1101','Principles of Marketing',1,2,NULL,3,3,43,0),(98,'MAT1203','Management Mathematics II',1,2,NULL,3,3,44,0),(99,'ACC1303','Introduction to Taxation',2,1,NULL,3,3,45,0),(100,'ACC1304','Intermediate Accounting I',2,1,NULL,3,3,31,0),(101,'FIN1301','Business Finance',2,1,NULL,3,3,32,0),(102,'STA1305','Business Statistics I',2,1,NULL,3,3,33,0),(103,'ECO1302','Introduction to Macroeconomics',2,1,NULL,3,3,34,0),(104,'HRM1302','Introduction to Human Resource Management',2,1,NULL,3,3,35,0),(105,'ICT2104','E-Commerce',2,2,NULL,3,3,36,0),(106,'ACC2101','Introduction to Cost Accounting',2,2,NULL,3,3,37,0),(107,'FIN2101','Insurance and Risk Management',2,2,NULL,3,3,38,0),(108,'STA2101','Business Statistics II',2,2,NULL,3,3,39,0),(109,'HRM2102','Organization Behavior',2,2,NULL,3,3,40,0),(110,'FIN 2102','Financial Management',2,2,NULL,3,3,41,0),(111,'BAM2201','Total Quality Management',3,1,NULL,3,3,42,0),(112,'BAM2202','Company Law',3,1,NULL,3,3,43,0),(113,'BAM2204','Strategic Management',3,1,NULL,3,3,44,0),(114,'BAM2205','Project Management',3,1,NULL,3,3,45,0),(115,'ICT2203','Computer Application Software',3,1,NULL,3,3,31,0),(116,'BAM2206','Career Management',3,1,NULL,3,3,32,0),(117,'ENT2306','Entreprenuership',3,2,NULL,3,3,33,0),(118,'BAM2313','International Relations',3,2,NULL,3,3,34,0),(119,'BAM2314','International Business Strategy',3,2,NULL,3,3,35,0),(120,'HRM2309','Compenstation and Reward Management',3,2,NULL,3,3,36,0),(121,'HRM2310','Industrial Safety and Health',3,2,NULL,3,3,37,0),(122,'HRM2311','Training and Development',3,2,NULL,3,3,38,0),(123,'HRM3118','Labour Law & Economics',4,1,NULL,3,3,39,0),(124,'HRM3108','Human Resource Planning',4,1,NULL,3,3,40,0),(125,'HRM3123','Strategic Human Resource Management',4,1,NULL,3,3,41,0),(126,'CFU3100','Research Methodology',4,1,NULL,3,3,42,0),(127,'HRM3119','Human Resource Information Systems',4,1,NULL,3,3,43,0),(128,'HRM3221','Global HRM',4,2,NULL,3,3,44,0),(129,'HRM3215','Perfomance-Based Management',4,2,NULL,3,3,45,0),(130,'CFU3204','Research Project',4,2,NULL,3,3,31,0),(131,'HRM3211','Employee relations',4,2,NULL,3,3,32,0);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `time_slots`
--

DROP TABLE IF EXISTS `time_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `time_slots` (
  `id` int NOT NULL AUTO_INCREMENT,
  `day` varchar(255) NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `time_slots`
--

LOCK TABLES `time_slots` WRITE;
/*!40000 ALTER TABLE `time_slots` DISABLE KEYS */;
INSERT INTO `time_slots` VALUES (1,'Monday','09:00:00','10:45:00'),(2,'Tuesday','09:00:00','10:45:00'),(3,'Wednesday','09:00:00','10:45:00'),(4,'Thursday','09:00:00','10:45:00'),(5,'Friday','09:00:00','10:45:00'),(6,'Monday','11:15:00','13:00:00'),(7,'Tuesday','11:15:00','13:00:00'),(8,'Wednesday','11:15:00','13:00:00'),(9,'Thursday','11:15:00','13:00:00'),(10,'Friday','11:15:00','13:00:00'),(11,'Monday','14:00:00','15:45:00'),(12,'Tuesday','14:00:00','15:45:00'),(13,'Wednesday','14:00:00','15:45:00'),(14,'Thursday','14:00:00','15:45:00'),(15,'Friday','14:00:00','15:45:00'),(16,'Monday','15:45:00','17:30:00'),(17,'Tuesday','15:45:00','17:30:00'),(18,'Wednesday','15:45:00','17:30:00'),(19,'Thursday','15:45:00','17:30:00'),(20,'Friday','15:45:00','17:30:00');
/*!40000 ALTER TABLE `time_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sections`
--

DROP TABLE IF EXISTS `sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sections` (
  `section_id` int NOT NULL AUTO_INCREMENT,
  `number_of_classes` int NOT NULL,
  `course_id` int NOT NULL,
  PRIMARY KEY (`section_id`),
  KEY `course_id` (`course_id`),
  CONSTRAINT `sections_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=132 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sections`
--

LOCK TABLES `sections` WRITE;
/*!40000 ALTER TABLE `sections` DISABLE KEYS */;
INSERT INTO `sections` VALUES (1,2,1),(2,2,2),(3,2,3),(4,2,4),(5,2,5),(6,2,6),(7,2,7),(8,2,8),(9,2,9),(10,2,10),(11,2,11),(12,2,12),(13,2,13),(14,1,14),(15,2,15),(16,2,16),(17,2,17),(18,2,18),(19,2,19),(20,2,20),(21,2,21),(22,2,22),(23,2,23),(24,2,24),(25,2,25),(26,2,26),(27,2,27),(28,2,28),(29,2,29),(30,2,30),(31,2,31),(32,2,32),(33,2,33),(34,2,34),(35,2,35),(36,2,36),(37,2,37),(38,2,38),(39,2,39),(40,2,40),(41,2,41),(42,2,42),(43,2,43),(44,2,44),(45,2,45),(46,4,46),(47,4,47),(48,2,48),(49,2,49),(50,2,50),(51,2,51),(52,2,52),(53,2,53),(54,2,54),(55,2,55),(56,2,56),(57,2,57),(58,2,58),(59,2,59),(60,2,60),(61,2,61),(62,1,62),(63,2,63),(64,2,64),(65,2,65),(66,2,66),(67,2,67),(68,2,68),(69,2,69),(70,2,70),(71,2,71),(72,2,72),(73,2,73),(74,2,74),(75,2,75),(76,2,76),(77,2,77),(78,2,78),(79,2,79),(80,2,80),(81,2,81),(82,2,82),(83,2,83),(84,1,84),(85,2,85),(86,2,86),(87,2,87),(88,2,88),(89,2,89),(90,2,90),(91,2,91),(92,2,92),(93,2,93),(94,2,94),(95,2,95),(96,2,96),(97,2,97),(98,2,98),(99,2,99),(100,2,100),(101,2,101),(102,2,102),(103,2,103),(104,2,104),(105,2,105),(106,2,106),(107,2,107),(108,2,108),(109,2,109),(110,2,110),(111,2,111),(112,2,112),(113,2,113),(114,2,114),(115,2,115),(116,2,116),(117,2,117),(118,2,118),(119,2,119),(120,2,120),(121,2,121),(122,2,122),(123,2,123),(124,2,124),(125,2,125),(126,2,126),(127,2,127),(128,2,128),(129,2,129),(130,4,130),(131,2,131);
/*!40000 ALTER TABLE `sections` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `instructor_preferences`
--

DROP TABLE IF EXISTS `instructor_preferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `instructor_preferences` (
  `instructor_id` int NOT NULL,
  `timeslot_id` int NOT NULL,
  PRIMARY KEY (`instructor_id`,`timeslot_id`),
  KEY `timeslot_id` (`timeslot_id`),
  CONSTRAINT `instructor_preferences_ibfk_1` FOREIGN KEY (`instructor_id`) REFERENCES `instructors` (`instructor_id`),
  CONSTRAINT `instructor_preferences_ibfk_2` FOREIGN KEY (`timeslot_id`) REFERENCES `time_slots` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructor_preferences`
--

LOCK TABLES `instructor_preferences` WRITE;
/*!40000 ALTER TABLE `instructor_preferences` DISABLE KEYS */;
INSERT INTO `instructor_preferences` VALUES (8,7),(8,8);
/*!40000 ALTER TABLE `instructor_preferences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint DEFAULT NULL,
  `room_id` bigint DEFAULT NULL,
  `section_id` bigint DEFAULT NULL,
  `time_slot_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_status`
--

DROP TABLE IF EXISTS `schedule_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `semester` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_status`
--

LOCK TABLES `schedule_status` WRITE;
/*!40000 ALTER TABLE `schedule_status` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_status` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `rooms_occupied_time_slots`
--

DROP TABLE IF EXISTS `rooms_occupied_time_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rooms_occupied_time_slots` (
  `room_room_id` bigint NOT NULL,
  `occupied_time_slots_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rooms_occupied_time_slots`
--

LOCK TABLES `rooms_occupied_time_slots` WRITE;
/*!40000 ALTER TABLE `rooms_occupied_time_slots` DISABLE KEYS */;
/*!40000 ALTER TABLE `rooms_occupied_time_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_results`
--

DROP TABLE IF EXISTS `schedule_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_results` (
  `id` bigint NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_results`
--

LOCK TABLES `schedule_results` WRITE;
/*!40000 ALTER TABLE `schedule_results` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_results` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_result_course_codes`
--

DROP TABLE IF EXISTS `schedule_result_course_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_result_course_codes` (
  `schedule_result_id` bigint NOT NULL,
  `course_codes` varchar(255) DEFAULT NULL,
  KEY `FKklltntgdt9pb2i5s3cmd8b0tq` (`schedule_result_id`),
  CONSTRAINT `FKklltntgdt9pb2i5s3cmd8b0tq` FOREIGN KEY (`schedule_result_id`) REFERENCES `schedule_results` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_result_course_codes`
--

LOCK TABLES `schedule_result_course_codes` WRITE;
/*!40000 ALTER TABLE `schedule_result_course_codes` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_result_course_codes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_result_instructor_names`
--

DROP TABLE IF EXISTS `schedule_result_instructor_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_result_instructor_names` (
  `schedule_result_id` bigint NOT NULL,
  `instructor_names` varchar(255) DEFAULT NULL,
  KEY `FKjwbv8exa4mgun03k15od9iuba` (`schedule_result_id`),
  CONSTRAINT `FKjwbv8exa4mgun03k15od9iuba` FOREIGN KEY (`schedule_result_id`) REFERENCES `schedule_results` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_result_instructor_names`
--

LOCK TABLES `schedule_result_instructor_names` WRITE;
/*!40000 ALTER TABLE `schedule_result_instructor_names` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_result_instructor_names` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_result_room_names`
--

DROP TABLE IF EXISTS `schedule_result_room_names`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_result_room_names` (
  `schedule_result_id` bigint NOT NULL,
  `room_names` varchar(255) DEFAULT NULL,
  KEY `FKfleegajlhqgxfr6o4ty0r2gn` (`schedule_result_id`),
  CONSTRAINT `FKfleegajlhqgxfr6o4ty0r2gn` FOREIGN KEY (`schedule_result_id`) REFERENCES `schedule_results` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_result_room_names`
--

LOCK TABLES `schedule_result_room_names` WRITE;
/*!40000 ALTER TABLE `schedule_result_room_names` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_result_room_names` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_result_time_slots`
--

DROP TABLE IF EXISTS `schedule_result_time_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_result_time_slots` (
  `schedule_result_id` bigint NOT NULL,
  `time_slots` varchar(255) DEFAULT NULL,
  KEY `FK771x4mbm9byeq1v7ffgcfru2h` (`schedule_result_id`),
  CONSTRAINT `FK771x4mbm9byeq1v7ffgcfru2h` FOREIGN KEY (`schedule_result_id`) REFERENCES `schedule_results` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_result_time_slots`
--

LOCK TABLES `schedule_result_time_slots` WRITE;
/*!40000 ALTER TABLE `schedule_result_time_slots` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_result_time_slots` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `schedule_results_seq`
--

DROP TABLE IF EXISTS `schedule_results_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule_results_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_results_seq`
--

LOCK TABLES `schedule_results_seq` WRITE;
/*!40000 ALTER TABLE `schedule_results_seq` DISABLE KEYS */;
INSERT INTO `schedule_results_seq` VALUES (1);
/*!40000 ALTER TABLE `schedule_results_seq` ENABLE KEYS */;
UNLOCK TABLES;








/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-30 23:25:43
