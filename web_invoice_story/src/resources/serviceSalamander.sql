-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 13, 2022 at 12:52 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cis2901c`
--
DROP DATABASE IF EXISTS `cis2901c`;
CREATE DATABASE IF NOT EXISTS `cis2901c` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `cis2901c`;

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customerId` int(11) NOT NULL,
  `firstName` varchar(45) NOT NULL DEFAULT '',
  `lastName` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL DEFAULT '',
  `city` varchar(45) NOT NULL DEFAULT '',
  `state` varchar(45) NOT NULL DEFAULT '',
  `zipcode` varchar(45) NOT NULL DEFAULT '',
  `homePhone` varchar(45) NOT NULL DEFAULT '',
  `workPhone` varchar(45) NOT NULL DEFAULT '',
  `cellPhone` varchar(45) NOT NULL DEFAULT '',
  `email` varchar(45) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customerId`, `firstName`, `lastName`, `address`, `city`, `state`, `zipcode`, `homePhone`, `workPhone`, `cellPhone`, `email`) VALUES
(1, 'Matthew', 'Glass', '75 N Salenger', 'Washington', 'D.C.', '99999', '1234567897', '987654321', '7', 'mcg@something.org'),
(2, 'Steven', 'Stephens', '255 South Schleshinger St', 'Adolay', 'OH', '45030', '7631567', '2371537', '5191357513', 'ss@stephens.com'),
(3, 'Mike', 'Michaelson', '275 Dubensville Dr', 'Dubensville', 'SC', '35845', '13751654561', '', '', ''),
(4, 'Adam', 'Adams', '77777 Abes Ave', 'Athens', 'GA', '32740', '9324567', '', '', ''),
(5, 'Heather', 'Lubens', '243 Delinger Dr', 'Cincinnati', 'OH', '43851', '5315167513', '', '', ''),
(6, 'Liam', 'Neeson', '', '', '', '', '', '', '', ''),
(7, 'Elizabeth', 'Olsen', '', '', '', '', '', '', '', ''),
(8, 'Paul', 'Bettany', '', '', '', '', '', '', '', ''),
(9, 'Lynn', 'Collins', '', 'Auburndale', '', '', '', '', '', ''),
(10, 'Sam', 'Worthington', '', '', '', '', '', '', '', ''),
(11, 'Cillian', 'Murphey', '', '', '', '', '', '', '', ''),
(12, 'Megan', 'Burns', '', '', '', '', '', '', '', ''),
(13, 'Julienne', 'Fererico', '', '', '', '', '', '', '', ''),
(14, 'Elissa', 'Alexis', '', '', '', '', '', '', '', ''),
(15, 'Amara', 'Maple', '576 Worthington St', 'Hamilton', 'SD', '', '', '', '', 'maps@maps.map'),
(16, 'Kelsey', 'Hayes', '', '', '', '', '', '', '', ''),
(17, 'Ashley', 'Matthews', '', '', '', '', '', '', '', ''),
(18, 'Carolyn', 'Paparozzi', '1600 Pennsylvania Ave NW', 'Washington', 'D.C.', '20500', '', '', '', ''),
(19, 'Torquato', 'Tasso', '', '', '', '', '', '', '', ''),
(20, 'Dada', 'Bevins', '654 Appleton Dr', 'Fithersom', 'FL', '32456', '7891516502', '', '7537456', ''),
(21, 'Gwendolyn', 'Brooks', '', '', '', '', '', '', '', ''),
(22, 'Anthony', 'Hecht', '', '', '', '', '', '', '', ''),
(23, 'Conrad', 'Aikin', '7511 Montana Dr', 'Mondana', 'MA', '15124', '75746481998', '', '7779875', 'fmlby@grrrr.ca'),
(24, 'Robert', 'Fronst', '9954 Tripoly St', 'Abernathy', 'WA', '99924', '6545151357', '', '', 'jj@jr.rm'),
(25, 'Charles', 'Bukowski', '75 Nowhere Ln', 'LA', 'CA', '95543', '7675461', '', '5151657', 'moded'),
(26, 'Michael', 'Ende', '77 Rhenton Rd', 'Absinth', 'NY', '12442', '7564515', '', '73519515341', 'rhetoric@rhetridly.rm'),
(27, 'Rob', 'Thomas', '34 Custome Ct', '', '', '', '', '', '', ''),
(28, 'Vladimir', 'Savchenko', '89 Varthern Wy', '', '', '', '', '', '', ''),
(29, 'Robert', 'Ludlum', '77 Warmington St', 'Samilby', 'CA', '94857', '', '', '', ''),
(30, 'Sloan', 'Wilson', '', '', '', '', '', '', '', ''),
(31, 'Paul', 'Deitel', '75 Newington Ave', 'Springfield', 'PA', '01101', '8765135', '', '', 'thisis@anemail.yes'),
(32, 'Steph', 'Subtle LLC', '1234 Worshington Dr', 'Orlando', 'WA', '98734', '757-615-4351', '99', '98', 'ss@hitmain.com'),
(33, 'Harvey', 'Deitel', '', '', '', '', '1-485-753-5361', '', '', ''),
(34, 'The', 'Dude', '', 'See-Me-Now', 'LA', '', '', '', '', ''),
(35, 'Quick', 'Dead', '', 'Seattle', '84651', '', '', '', '', ''),
(36, 'Absolunginger', 'Type', '', '', '', '', '', '', '', ''),
(37, 'Mister', 'Smithers', '', '', '', '', '', '', '', ''),
(38, 'Fisty', 'Fisel', '', '', '', '', '', '', '', ''),
(39, 'Fithersworth', 'Flemming', '', '', '', '', '', '', '', ''),
(40, 'Map', 'Mapple', '', '', '', '', '', '', '', ''),
(41, 'Samwise', 'Somlinger', '', '', '', '', '', '', '', ''),
(42, 'Fitz', 'Farthing', '', '', 'OH', '45927', '', '', '757-4951', ''),
(43, 'Newton', 'Farnsworth', '77 Fancy Lp', '', '', '', '', '', '', '999@12..c'),
(44, 'Grennish', 'Grennich', '38 Doodle Dr', '', '', '', '', '', '', 'it works well'),
(45, 'Refactored', 'TextBoxModifier', '99 S Hampton Dr West', 'Stubensville', 'VA', '', '18757844563', '', '', ''),
(47, 'Charles', 'Barkley', '', '', '', '', '', '', '', ''),
(48, 'Sampson', 'Simpson', '', '', '', '', '', '', '', 'SS@SAMSIM.ORG'),
(49, 'Bastian', 'Bux', '72 Fantastica Ln', 'Ivory Tower', 'FA', '99827', '735-548-5961', '', '', '');

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--

CREATE TABLE `invoice` (
  `invoiceNum` int(11) NOT NULL,
  `customerid` int(11) NOT NULL,
  `customerdata` varchar(255) NOT NULL,
  `notes` varchar(1027) NOT NULL DEFAULT '',
  `tax` decimal(13,2) NOT NULL,
  `cashiereddate` datetime NOT NULL,
  `cashiered` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `invoice`
--

INSERT INTO `invoice` (`invoiceNum`, `customerid`, `customerdata`, `notes`, `tax`, `cashiereddate`, `cashiered`) VALUES
(50000, 1, 'Glass, Matthew', 'This is a  text Invoice, the first in fact', '0.39', '2022-02-03 17:34:00', 1),
(50001, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', '', '6.24', '2022-02-03 00:00:00', 1),
(50014, 6, 'gdfg, asdf', '5', '0.33', '2022-02-04 00:11:54', 1),
(50015, 6, 'gdfg, asdf\r\nnull\r\nnull, null null\r\nnull\r\nnull\r\nnull', '5', '0.33', '2022-02-04 00:13:24', 1),
(50016, 14, 'hjsdrt;;;;;;\r\nnull\r\nnull, null null\r\nnull\r\nnull\r\nnull', 'This will happen sometimes', '5.85', '2022-02-04 00:18:27', 1),
(50017, 2, 'Dude, The\r\nDuddette\r\nnull, null null\r\nnull\r\nnull\r\nnull', 'This will happen sometimes', '5.85', '2022-02-04 00:18:53', 1),
(50018, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', '5', '0.33', '2022-02-04 00:20:10', 1),
(50019, 7, 'fasdf, a QUICJ nw\r\nnull\r\nnull, null null\r\nnull\r\nnull\r\nnull', '5', '0.33', '2022-02-04 00:23:43', 1),
(50020, 6, 'gdfg, asdf\r\nnull\r\nnull, null null\r\nnull\r\nnull\r\nnull', '5', '0.33', '2022-02-04 00:24:33', 1),
(50021, 15, 'Productions, UPDATE\r\nTHIS QUICK\r\ncow, qwith null\r\nnull\r\nnull\r\nMAPPPPPSS', '', '6.24', '2022-02-04 00:37:49', 1),
(50022, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', '', '6.24', '2022-02-04 00:39:37', 1),
(50023, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', 'This will happen sometimes', '5.85', '2022-02-04 00:40:51', 1),
(50024, 2, 'Dude, The\r\nDuddette\r\nnull, null null\r\nnull\r\nnull\r\nnull', '5', '0.33', '2022-02-04 00:42:02', 1),
(50025, 2, 'Dude, The\r\nDuddette\r\nnull, null null\r\nnull\r\nnull\r\nnull', 'This will happen sometimes', '5.85', '2022-02-04 00:42:48', 1),
(50026, 13, 'fgggggg\r\nnull\r\nnull, null null\r\nnull\r\nnull\r\nnull', 'TESTS', '13.92', '2022-02-04 00:47:01', 1),
(50027, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', 'This will happen sometimes', '6.24', '2022-02-04 14:29:39', 1),
(50028, 2, 'Dude, The\r\nDuddette\r\nnull, null null\r\nnull\r\nnull\r\nnull', 'This will happen sometimes', '5.85', '2022-02-08 20:51:59', 1),
(50029, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nnull', 'TESTS', '6.50', '2022-02-09 16:28:18', 1),
(50030, 43, 'AnD, New\r\nFancy\r\nnull, null null\r\nnull\r\nnull\r\n999@12..c', '', '56.75', '2022-02-09 18:10:18', 1),
(50031, 43, 'AnD, New\r\nFancy\r\nnull, null null\r\nnull\r\nnull\r\n999@12..c', '', '56.75', '2022-02-09 18:11:48', 1),
(50032, 43, 'AnD, New\r\nFancy\r\nnull, null null\r\nnull\r\nnull\r\n999@12..c', '', '56.75', '2022-02-09 18:13:38', 1),
(50033, 23, 'FFFfff, FFFfff\r\nFFFfff\r\nFFFfff, JJJJJJJJJJJJ 89\r\n123-4567\r\n8\r\nFFFfff', '', '56.75', '2022-02-09 18:14:21', 1),
(50034, 14, 'Alexis, Elissa', '', '6.57', '2022-02-12 20:35:16', 1),
(50035, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 'This will happen sometimes', '5.85', '2022-02-12 20:36:16', 1),
(50036, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 'This will happen sometimes', '5.85', '2022-02-12 20:36:50', 1),
(50037, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 'This will happen sometimes', '5.85', '2022-02-12 20:37:17', 1),
(50038, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 'TESTS', '8.58', '2022-02-12 21:31:56', 1),
(50039, 14, 'Alexis, Elissa', '', '6.70', '2022-02-12 21:32:15', 1),
(50040, 47, 'Barkley, Charles', 'This will happen sometimes', '5.85', '2022-02-15 19:53:39', 1),
(50041, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', '', '0.72', '2022-02-16 23:32:06', 1),
(50042, 1, 'Glass, Matthew\r\n75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 'WITH BEARINGS', '22.75', '2022-03-01 16:47:59', 1),
(50043, 14, 'Alexis, Elissa', '', '6.57', '2022-03-13 15:30:44', 1),
(50044, 49, 'Bux, Bastian\r\n72 Fantastica Ln\r\nIvory Tower, FA 99827\r\n735-548-5961', '', '1.56', '2022-03-20 12:13:13', 1),
(50045, 14, 'Alexis, Elissa', 'FRONT WHEEL, VN900', '24.70', '2022-03-20 12:57:38', 1),
(50046, 49, 'Bux, Bastian\r\n72 Fantastica Ln\r\nIvory Tower, FA 99827\r\n735-548-5961', 'return unused part', '-24.69', '2022-03-20 13:32:19', 1),
(50047, 9, 'Collins, Lynn\r\n\r\nAuburndale', 'Invoice Notes...', '-22.74', '2022-03-20 13:34:48', 1),
(50048, 31, 'Deitel, Paul\r\n75 Newington Ave\r\nSpringfield, PA 01101\r\n8765135\r\n\r\nthisis@anemail.yes', 'Invoice Notes...', '-0.45', '2022-03-20 13:36:16', 1),
(50049, 13, 'Fererico, Julienne', 'Invoice Notes...', '-8.44', '2022-03-20 13:41:29', 1),
(50050, 44, 'Grennich, Grennish\r\n38 Doodle Dr\r\n  \r\n\r\n\r\nit works well', 'Invoice Notes...', '-1.94', '2022-03-21 09:47:37', 1),
(50051, 12, 'Burns, Megan', 'Invoice Notes...', '20.74', '2022-03-21 12:42:28', 1),
(50053, 23, 'Aikin, Conrad\r\n7511 Montana Dr\r\nMondana, MA 15124\r\n75746481998\r\n7779875\r\nfmlby@grrrr.ca', 'Invoice Notes...', '12.03', '2022-03-22 20:07:59', 1),
(50054, 39, 'Flemming, Fithersworth', 'Invoice Notes...', '6.24', '2022-03-22 20:35:09', 1),
(50055, 13, 'Fererico, Julienne', 'Invoice Notes...', '-1.62', '2022-03-23 00:40:05', 1),
(50056, 41, 'Somlinger, Samwise', 'Invoice Notes...', '-1.62', '2022-03-23 00:40:54', 1),
(50057, 15, 'Maple, Amara\r\n576 Worthington St\r\nHamilton, SD \r\n\r\n\r\nmaps@maps.map', 'Invoice Notes...', '-1.62', '2022-03-23 00:45:46', 1),
(50058, 35, 'Dead, Quick\r\n\r\nSeattle, 84651', 'Invoice Notes...', '-1.62', '2022-03-23 00:50:20', 1),
(50059, 19, 'Tasso, Torquato', 'Invoice Notes...', '6.18', '2022-03-23 11:22:48', 1),
(50060, 21, 'Brooks, Gwendolyn', 'Invoice Notes...', '-47.90', '2022-03-30 22:56:24', 1),
(50061, 15, 'Maple, Amara\r\n576 Worthington St\r\nHamilton, SD \r\n\r\n\r\nmaps@maps.map', 'Invoice Notes...', '-1.62', '2022-03-30 23:16:55', 1);

-- --------------------------------------------------------

--
-- Table structure for table `invoicepart`
--

CREATE TABLE `invoicepart` (
  `invoicenum` int(11) NOT NULL,
  `partid` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `quantity` int(11) NOT NULL,
  `soldprice` decimal(13,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `invoicepart`
--

INSERT INTO `invoicepart` (`invoicenum`, `partid`, `description`, `quantity`, `soldprice`) VALUES
(50000, 1, 'OIL FILTER', 1, '5.99'),
(50001, 1, 'OIL FILTER', 1, '5.99'),
(50022, 1, 'OIL FILTER', 1, '5.99'),
(50027, 1, 'OIL FILTER', 1, '5.99'),
(50034, 1, 'OIL FILTER', 1, '5.99'),
(50039, 1, 'OIL FILTER', 1, '5.99'),
(50041, 1, 'OIL FILTER', 1, '5.99'),
(50044, 1, 'OIL FILTER, HON', 1, '6.99'),
(50048, 1, 'OIL FILTER, HON', -1, '6.99'),
(50060, 1, 'OIL FILTER, HON', -1, '6.99'),
(50026, 2, 'SOMEHTING ELSE', 1, '97.00'),
(50030, 2, 'SOMEHTING ELSE', 9, '873.00'),
(50031, 2, 'SOMEHTING ELSE', 9, '873.00'),
(50032, 2, 'SOMEHTING ELSE', 9, '873.00'),
(50033, 2, 'SOMEHTING ELSE', 9, '873.00'),
(50034, 2, 'SOMEHTING ELSE', 1, '97.00'),
(50039, 2, 'SOMEHTING ELSE', 1, '97.00'),
(50022, 3, 'ANYTHING ELSDE', 1, '0.00'),
(50026, 3, 'ANYTHING ELSDE', 1, '23.00'),
(50027, 3, 'ANYTHING ELSDE', 1, '0.00'),
(50028, 3, 'ANYTHING ELSDE', 1, '0.00'),
(50029, 3, 'ANYTHING ELSDE', 5, '100.00'),
(50038, 3, 'ANYTHING ELSDE', 1, '42.00'),
(50040, 3, 'ANYTHING ELSDE', 1, '0.00'),
(50043, 3, 'BOLT', 1, '4.99'),
(50053, 3, 'BOLT', 1, '0.00'),
(50055, 3, 'BOLT', 1, '0.00'),
(50056, 3, 'BOLT', 1, '0.00'),
(50058, 3, 'BOLT', -5, '24.95'),
(50059, 3, 'BOLT', 1, '0.00'),
(50061, 3, 'BOLT', -5, '4.99'),
(50019, 4, 'That', 1, '5.00'),
(50020, 4, 'That', 1, '5.00'),
(50024, 4, 'That', 1, '5.00'),
(50026, 4, 'That', 1, '5.00'),
(50034, 4, 'That', 1, '5.00'),
(50041, 4, 'That', 1, '5.00'),
(50001, 5, 'SHIFT ROD', 1, '89.99'),
(50022, 5, 'SHIFT ROD', 1, '89.99'),
(50023, 5, 'SHIFT ROD', 1, '89.99'),
(50025, 5, 'SHIFT ROD', 1, '89.99'),
(50026, 5, 'SHIFT ROD', 1, '89.00'),
(50027, 5, 'SHIFT ROD', 1, '89.99'),
(50028, 5, 'SHIFT ROD', 1, '89.99'),
(50034, 5, 'SHIFT ROD', 1, '89.99'),
(50036, 5, 'SHIFT ROD', 1, '89.99'),
(50038, 5, 'SHIFT ROD', 1, '89.99'),
(50040, 5, 'SHIFT ROD', 1, '89.99'),
(50043, 5, 'SHIFT ROD', 1, '89.99'),
(50053, 5, 'SHIFT ROD', 1, '0.00'),
(50054, 5, 'SHIFT ROD', 1, '0.00'),
(50059, 5, 'SHIFT ROD', 1, '0.00'),
(50051, 8, 'SEAT, COMFORT', 1, '299.99'),
(50060, 8, 'SEAT, COMFORT', -2, '299.99'),
(50042, 10, 'WHEEL, F', 1, '349.99'),
(50047, 10, 'WHEEL, F', -1, '349.99'),
(50051, 11, 'TUBE, 250/300-15', 1, '18.99'),
(50049, 12, 'TAILLIGHT', -1, '129.99'),
(50060, 12, 'TAILLIGHT', -1, '129.99'),
(50043, 14, 'OIL FILTER, SUZ', 1, '5.99'),
(50044, 14, 'OIL FILTER, SUZ', 2, '11.98'),
(50054, 14, 'OIL FILTER, SUZ', 1, '0.00'),
(50044, 15, 'OIL FILTER, KAW', 1, '4.99'),
(50050, 15, 'OIL FILTER, KAW', -5, '29.95'),
(50045, 17, 'WHEEL, SPOKED, FR', 1, '379.99'),
(50046, 17, 'WHEEL, SPOKED, FR', -1, '379.99');

-- --------------------------------------------------------

--
-- Table structure for table `job`
--

CREATE TABLE `job` (
  `jobid` int(11) NOT NULL,
  `roid` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `complaint` varchar(255) NOT NULL,
  `resolution` varchar(255) NOT NULL,
  `recommendations` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `job`
--

INSERT INTO `job` (`jobid`, `roid`, `name`, `complaint`, `resolution`, `recommendations`) VALUES
(1, 50000, 'REPAIR SHIFT ROD', 'Complaints...', 'Resolution...', 'Reccomendations...'),
(2, 50000, 'SEAT', '', '', ''),
(5, 50002, 'fdsdf', 'Complaints...', 'Resolution...', 'Reccomendations...'),
(6, 50002, 'Another', 'Complaints...', 'Resolution...', 'Reccomendations...'),
(7, 50001, 'OIL CHANGE', 'Complaints...', 'Resolution...', 'Reccomendations...');

-- --------------------------------------------------------

--
-- Table structure for table `joblabor`
--

CREATE TABLE `joblabor` (
  `joblaborid` int(11) NOT NULL,
  `jobid` int(11) NOT NULL,
  `description` varchar(64) NOT NULL,
  `hours` decimal(13,2) NOT NULL,
  `hourrate` decimal(13,2) NOT NULL,
  `technician` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `joblabor`
--

INSERT INTO `joblabor` (`joblaborid`, `jobid`, `description`, `hours`, `hourrate`, `technician`) VALUES
(1, 1, 'is some test labor', '1.00', '100.00', 'This'),
(7, 7, 'OIL CHANGE', '0.50', '100.00', '');

-- --------------------------------------------------------

--
-- Table structure for table `jobpart`
--

CREATE TABLE `jobpart` (
  `jobpartid` int(11) NOT NULL,
  `jobid` int(11) NOT NULL,
  `partid` int(11) NOT NULL,
  `partnumber` varchar(45) NOT NULL,
  `description` varchar(64) NOT NULL,
  `quantity` int(11) NOT NULL,
  `soldprice` decimal(13,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `jobpart`
--

INSERT INTO `jobpart` (`jobpartid`, `jobid`, `partid`, `partnumber`, `description`, `quantity`, `soldprice`) VALUES
(1, 1, 5, 'DS-47286', 'SHIFT ROD', 1, '89.99'),
(2, 1, 3, '77', 'BOLT', 1, '4.99'),
(3, 2, 8, '756-HK-7595', 'SEAT, COMFORT', 1, '299.99'),
(19, 5, 3, '77', 'BOLT', 1, '4.99'),
(20, 6, 17, '15856751', 'WHEEL, SPOKED, FR', 1, '379.99'),
(26, 7, 1, '171640', 'OIL FILTER, HON', 1, '6.99'),
(27, 7, 30, '151671', '10W40 QUART', 2, '4.99');

-- --------------------------------------------------------

--
-- Table structure for table `part`
--

CREATE TABLE `part` (
  `partId` int(11) NOT NULL,
  `partNumber` varchar(45) NOT NULL,
  `supplier` varchar(45) NOT NULL,
  `category` varchar(45) NOT NULL DEFAULT '',
  `description` varchar(64) NOT NULL,
  `notes` varchar(256) NOT NULL DEFAULT '',
  `cost` decimal(13,2) NOT NULL DEFAULT 0.00,
  `retail` decimal(13,2) NOT NULL DEFAULT 0.00,
  `onHand` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `part`
--

INSERT INTO `part` (`partId`, `partNumber`, `supplier`, `category`, `description`, `notes`, `cost`, `retail`, `onHand`) VALUES
(1, '171640', 'TR', 'AMA', 'OIL FILTER, HON', '', '4.75', '6.99', 1),
(2, '15765', 'KAW', 'OEM', 'FORK, SLIDER, LH', '', '97.01', '154.99', 1),
(3, '77', 'TR', 'HWR', 'BOLT', '', '2.72', '4.99', 13),
(4, '76543', 'HL', 'HWR', 'NUT', '5', '2.73', '5.99', 3),
(5, 'DS-47286', 'DS', 'IHP', 'SHIFT ROD', 'This will happen sometimes', '45.73', '89.99', 0),
(7, '573495', 'TR', 'AMA', 'PV3, IND', '', '372.45', '499.99', 1),
(8, '756-HK-7595', 'YAM', 'YAC', 'SEAT, COMFORT', '', '179.42', '299.99', 1),
(9, '2884195', 'PO', 'OEM', 'FOOTPEG, RUBBER', '', '15.44', '20.99', 1),
(10, '15437-55-18815', 'HON', 'OEM', 'WHEEL, F', 'WITH BEARINGS', '275.33', '349.99', 1),
(11, '575346', 'TR', 'AMA', 'TUBE, 250/300-15', '', '12.55', '18.99', 4),
(12, '9481357-55', 'PO', 'VIC', 'TAILLIGHT', '', '74.73', '129.99', 2),
(14, '171655', 'TR', 'AMA', 'OIL FILTER, SUZ', '', '3.45', '5.99', 11),
(15, '171680', 'TR', 'AMA', 'OIL FILTER, KAW', '', '3.78', '5.99', 5),
(16, '875415', 'PO', 'IHP', 'BLINKER FLUID', '', '4.57', '7.00', 1),
(17, '15856751', 'KAW', 'KHP', 'WHEEL, SPOKED, FR', 'FRONT WHEEL, VN900', '287.64', '379.99', 0),
(30, '151671', 'TR', 'OIL', '10W40 QUART', 'CONVENTIONAL OIL', '2.35', '4.99', 13);

-- --------------------------------------------------------

--
-- Table structure for table `ro`
--

CREATE TABLE `ro` (
  `roId` int(11) NOT NULL,
  `customerId` int(11) DEFAULT NULL,
  `customerdata` varchar(255) NOT NULL,
  `unitId` int(11) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `closedTime` datetime DEFAULT NULL,
  `tax` decimal(13,2) NOT NULL,
  `total` decimal(13,2) NOT NULL,
  `cashiered` tinyint(1) NOT NULL DEFAULT 0,
  `canceled` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `ro`
--

INSERT INTO `ro` (`roId`, `customerId`, `customerdata`, `unitId`, `createdTime`, `closedTime`, `tax`, `total`, `cashiered`, `canceled`) VALUES
(50000, 1, '75 N Salenger\r\nWashington, D.C. 99999\r\n1234567897\r\n7\r\nmcg@something.org', 15, '2022-03-21 14:30:18', '2022-04-04 14:05:40', '32.18', '527.15', 0, 0),
(50001, 14, ',', 26, '2022-03-15 19:24:24', '2022-04-05 22:30:06', '4.36', '71.33', 0, 0),
(50002, 49, '72 Fantastica Ln\r\nIvory Tower, FA 99827\r\n735-548-5961', 27, '2022-03-22 21:12:36', NULL, '25.03', '410.01', 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `testtable`
--

CREATE TABLE `testtable` (
  `idtesttable` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `unit`
--

CREATE TABLE `unit` (
  `unitId` int(11) NOT NULL,
  `customerId` int(11) DEFAULT NULL,
  `make` varchar(45) DEFAULT NULL,
  `model` varchar(45) NOT NULL DEFAULT '',
  `modelName` varchar(45) NOT NULL DEFAULT '',
  `year` int(4) DEFAULT NULL,
  `mileage` int(11) NOT NULL DEFAULT 0,
  `color` varchar(45) NOT NULL DEFAULT '',
  `vin` varchar(17) NOT NULL DEFAULT '',
  `notes` varchar(255) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `unit`
--

INSERT INTO `unit` (`unitId`, `customerId`, `make`, `model`, `modelName`, `year`, `mileage`, `color`, `vin`, `notes`) VALUES
(15, 1, 'Kaw', 'KLT650', 'KLT', 2011, 17, 'Blk', '56FFFFF', 'This is obviously an imaginary motorcycle.'),
(16, 7, 'Indian', 'Pursuit', '', 0, 5468, '', '12345678901234567', 'sgsdgdfdf'),
(20, 16, 'Yamaha', 'YZF-R1', 'R1', 1999, 32951, 'Blue', '', ''),
(21, 6, 'KTM', 'Duke 890', '', 2021, 7002, 'Orange', '', ''),
(22, 1, 'Honda', 'RC116', '', 1966, 428, '', '', ''),
(23, 1, 'Honda', 'CBX1100', 'CBX', 1978, 11028, 'Silver', '', ''),
(24, 32, 'Triumph', '', 'Tiger', 0, 14728, '', '', ''),
(25, 1, 'Rickman', '', 'Metisse 650', 1971, 760, '', '', ''),
(26, 43, 'Honda', 'VFR750R', '', 1987, 17425, '', '', ''),
(27, 48, 'KAW', '', 'MEAN STREAK', 2014, 28028, 'GOLD/RED', '', ''),
(28, 12, 'BMW', 'R1200GS', '', 2016, 48357, 'BLK', '', ''),
(30, 15, 'MV Agusta', 'Brutale 1090RR', '', 2013, 4, 'Red/White', '', '');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerId`);

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoiceNum`),
  ADD KEY `fk_invoice_customer` (`customerid`);

--
-- Indexes for table `invoicepart`
--
ALTER TABLE `invoicepart`
  ADD PRIMARY KEY (`partid`,`invoicenum`),
  ADD KEY `fk_invoicepart_invoice_idx` (`invoicenum`);

--
-- Indexes for table `job`
--
ALTER TABLE `job`
  ADD PRIMARY KEY (`jobid`),
  ADD KEY `fk_job_ro` (`roid`);

--
-- Indexes for table `joblabor`
--
ALTER TABLE `joblabor`
  ADD PRIMARY KEY (`joblaborid`),
  ADD KEY `fk_joblabor_job` (`jobid`);

--
-- Indexes for table `jobpart`
--
ALTER TABLE `jobpart`
  ADD PRIMARY KEY (`jobpartid`),
  ADD KEY `fk_jobpart_part` (`partid`) USING BTREE,
  ADD KEY `fk_jobpart_job` (`jobid`);

--
-- Indexes for table `part`
--
ALTER TABLE `part`
  ADD PRIMARY KEY (`partId`);

--
-- Indexes for table `ro`
--
ALTER TABLE `ro`
  ADD PRIMARY KEY (`roId`),
  ADD KEY `ro_customerId_fk_idx` (`customerId`),
  ADD KEY `ro_unitId_fk_idx` (`unitId`);

--
-- Indexes for table `testtable`
--
ALTER TABLE `testtable`
  ADD PRIMARY KEY (`idtesttable`);

--
-- Indexes for table `unit`
--
ALTER TABLE `unit`
  ADD PRIMARY KEY (`unitId`),
  ADD KEY `unit_customerId_fk_idx` (`customerId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoiceNum` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50062;

--
-- AUTO_INCREMENT for table `job`
--
ALTER TABLE `job`
  MODIFY `jobid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `joblabor`
--
ALTER TABLE `joblabor`
  MODIFY `joblaborid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `jobpart`
--
ALTER TABLE `jobpart`
  MODIFY `jobpartid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `part`
--
ALTER TABLE `part`
  MODIFY `partId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `ro`
--
ALTER TABLE `ro`
  MODIFY `roId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50006;

--
-- AUTO_INCREMENT for table `unit`
--
ALTER TABLE `unit`
  MODIFY `unitId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `fk_invoice_customer` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerId`);

--
-- Constraints for table `invoicepart`
--
ALTER TABLE `invoicepart`
  ADD CONSTRAINT `fk_invoicepart_invoice` FOREIGN KEY (`invoicenum`) REFERENCES `invoice` (`invoiceNum`),
  ADD CONSTRAINT `fk_invoicepart_part` FOREIGN KEY (`partid`) REFERENCES `part` (`partId`);

--
-- Constraints for table `job`
--
ALTER TABLE `job`
  ADD CONSTRAINT `fk_job_ro` FOREIGN KEY (`roid`) REFERENCES `ro` (`roId`);

--
-- Constraints for table `joblabor`
--
ALTER TABLE `joblabor`
  ADD CONSTRAINT `fk_joblabor_job` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`);

--
-- Constraints for table `jobpart`
--
ALTER TABLE `jobpart`
  ADD CONSTRAINT `fk_jobopart_part` FOREIGN KEY (`partid`) REFERENCES `part` (`partId`),
  ADD CONSTRAINT `fk_jobpart_job` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`);

--
-- Constraints for table `ro`
--
ALTER TABLE `ro`
  ADD CONSTRAINT `ro_customerId_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  ADD CONSTRAINT `ro_unitId_fk` FOREIGN KEY (`unitId`) REFERENCES `unit` (`unitId`);

--
-- Constraints for table `unit`
--
ALTER TABLE `unit`
  ADD CONSTRAINT `unit_customerId_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;


GRANT USAGE ON *.* TO `TestUser`@`localhost` IDENTIFIED BY PASSWORD '*94BDCEBE19083CE2A1F959FD02F964C7AF4CFC29';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES, EXECUTE, CREATE VIEW, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `cop2805`.* TO `TestUser`@`localhost` WITH GRANT OPTION;

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, CREATE TEMPORARY TABLES, LOCK TABLES, EXECUTE, CREATE VIEW, SHOW VIEW, CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `cis2901c`.* TO `TestUser`@`localhost` WITH GRANT OPTION;