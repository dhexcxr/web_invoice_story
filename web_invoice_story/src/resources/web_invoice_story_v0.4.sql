-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 29, 2023 at 08:10 AM
-- Server version: 10.4.21-MariaDB
-- PHP Version: 8.1.1

SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `web_invoice_story`
--
CREATE DATABASE IF NOT EXISTS `web_invoice_story` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `web_invoice_story`;

-- --------------------------------------------------------

--
-- Table structure for table `account`
--
-- Creation: Oct 29, 2023 at 06:20 AM
-- Last update: Oct 29, 2023 at 06:56 AM
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `accountId` int(11) NOT NULL,
  `customerId` int(11) NOT NULL,
  `accountNumber` varchar(45) NOT NULL,
  `accountName` varchar(45) NOT NULL,
  `balance` decimal(10,2) NOT NULL DEFAULT 0.00
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `account`:
--   `customerId`
--       `customer` -> `customerId`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountBalanceHx`
--
-- Creation: Oct 29, 2023 at 06:25 AM
-- Last update: Oct 29, 2023 at 06:59 AM
--

DROP TABLE IF EXISTS `accountBalanceHx`;
CREATE TABLE `accountBalanceHx` (
  `balanceHxId` int(11) NOT NULL,
  `accountId` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `balance` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `accountBalanceHx`:
--   `accountId`
--       `account` -> `accountId`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountTransaction`
--
-- Creation: Oct 29, 2023 at 06:23 AM
--

DROP TABLE IF EXISTS `accountTransaction`;
CREATE TABLE `accountTransaction` (
  `transactionId` int(11) NOT NULL,
  `accountId` int(11) NOT NULL,
  `invoiceId` int(11) NOT NULL,
  `transactionDate` datetime NOT NULL,
  `description` varchar(45) NOT NULL,
  `amount` decimal(9,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `accountTransaction`:
--   `accountId`
--       `account` -> `accountId`
--   `invoiceId`
--       `invoice` -> `invoiceNum`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--
-- Creation: Oct 25, 2023 at 11:50 PM
-- Last update: Oct 26, 2023 at 12:49 AM
--

DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customerId` int(11) NOT NULL,
  `homeLocId` int(11) NOT NULL,
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
-- RELATIONSHIPS FOR TABLE `customer`:
--   `homeLocId`
--       `storeLocation` -> `locationId`
--

-- --------------------------------------------------------

--
-- Table structure for table `invoice`
--
-- Creation: Oct 26, 2023 at 12:02 AM
--

DROP TABLE IF EXISTS `invoice`;
CREATE TABLE `invoice` (
  `invoiceNum` int(11) NOT NULL,
  `customerid` int(11) NOT NULL,
  `storeLocId` int(11) NOT NULL,
  `customerdata` varchar(255) NOT NULL,
  `notes` varchar(1027) NOT NULL DEFAULT '',
  `tax` decimal(13,2) NOT NULL,
  `cashiereddate` datetime NOT NULL,
  `cashiered` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `invoice`:
--   `customerid`
--       `customer` -> `customerId`
--   `storeLocId`
--       `storeLocation` -> `locationId`
--

-- --------------------------------------------------------

--
-- Table structure for table `invoiceLineItem`
--
-- Creation: Oct 26, 2023 at 12:03 AM
--

DROP TABLE IF EXISTS `invoiceLineItem`;
CREATE TABLE `invoiceLineItem` (
  `invoicenum` int(11) NOT NULL,
  `partid` int(11) NOT NULL,
  `description` varchar(45) NOT NULL,
  `quantity` int(11) NOT NULL,
  `soldprice` decimal(13,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `invoiceLineItem`:
--   `invoicenum`
--       `invoice` -> `invoiceNum`
--   `partid`
--       `part` -> `partId`
--

-- --------------------------------------------------------

--
-- Table structure for table `job`
--
-- Creation: Oct 25, 2023 at 11:59 PM
--

DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `jobid` int(11) NOT NULL,
  `projectId` int(11) NOT NULL,
  `name` varchar(64) NOT NULL,
  `description` varchar(255) NOT NULL,
  `resolution` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `job`:
--   `projectId`
--       `project` -> `projectId`
--

-- --------------------------------------------------------

--
-- Table structure for table `joblabor`
--
-- Creation: Oct 26, 2023 at 12:06 AM
--

DROP TABLE IF EXISTS `joblabor`;
CREATE TABLE `joblabor` (
  `joblaborid` int(11) NOT NULL,
  `jobid` int(11) NOT NULL,
  `description` varchar(64) NOT NULL,
  `hours` decimal(13,2) NOT NULL,
  `hourrate` decimal(13,2) NOT NULL,
  `installer` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `joblabor`:
--   `jobid`
--       `job` -> `jobid`
--

-- --------------------------------------------------------

--
-- Table structure for table `jobpart`
--
-- Creation: Oct 26, 2023 at 12:01 AM
--

DROP TABLE IF EXISTS `jobpart`;
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
-- RELATIONSHIPS FOR TABLE `jobpart`:
--   `jobid`
--       `job` -> `jobid`
--   `partid`
--       `part` -> `partId`
--

-- --------------------------------------------------------

--
-- Table structure for table `part`
--
-- Creation: Oct 26, 2023 at 01:39 AM
-- Last update: Oct 26, 2023 at 01:43 AM
--

DROP TABLE IF EXISTS `part`;
CREATE TABLE `part` (
  `partId` int(11) NOT NULL,
  `storeLocId` int(11) NOT NULL,
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
-- RELATIONSHIPS FOR TABLE `part`:
--   `storeLocId`
--       `storeLocation` -> `locationId`
--

-- --------------------------------------------------------

--
-- Table structure for table `project`
--
-- Creation: Oct 25, 2023 at 11:53 PM
--

DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `projectId` int(11) NOT NULL,
  `customerId` int(11) DEFAULT NULL,
  `storeLocId` int(11) NOT NULL,
  `customerdata` varchar(255) NOT NULL,
  `projectSiteId` int(11) DEFAULT NULL,
  `createdTime` datetime DEFAULT NULL,
  `closedTime` datetime DEFAULT NULL,
  `tax` decimal(13,2) NOT NULL,
  `total` decimal(13,2) NOT NULL,
  `cashiered` tinyint(1) NOT NULL DEFAULT 0,
  `canceled` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `project`:
--   `customerId`
--       `customer` -> `customerId`
--   `projectSiteId`
--       `projectsite` -> `projectSiteId`
--   `storeLocId`
--       `storeLocation` -> `locationId`
--

-- --------------------------------------------------------

--
-- Table structure for table `projectSite`
--
-- Creation: Oct 26, 2023 at 01:21 AM
-- Last update: Oct 26, 2023 at 01:21 AM
--

DROP TABLE IF EXISTS `projectSite`;
CREATE TABLE `projectSite` (
  `projectSiteId` int(11) NOT NULL,
  `customerId` int(11) DEFAULT NULL,
  `address` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zipcode` varchar(45) NOT NULL,
  `notes` varchar(255) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `projectSite`:
--   `customerId`
--       `customer` -> `customerId`
--

-- --------------------------------------------------------

--
-- Table structure for table `storeLocation`
--
-- Creation: Oct 25, 2023 at 11:44 PM
-- Last update: Oct 26, 2023 at 12:35 AM
--

DROP TABLE IF EXISTS `storeLocation`;
CREATE TABLE `storeLocation` (
  `locationId` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `address` varchar(45) NOT NULL,
  `city` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `zipcode` varchar(45) NOT NULL,
  `phone` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- RELATIONSHIPS FOR TABLE `storeLocation`:
--

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`accountId`),
  ADD KEY `account_customer_fk` (`customerId`);

--
-- Indexes for table `accountBalanceHx`
--
ALTER TABLE `accountBalanceHx`
  ADD PRIMARY KEY (`balanceHxId`),
  ADD KEY `balanceHx_account_fk` (`accountId`);

--
-- Indexes for table `accountTransaction`
--
ALTER TABLE `accountTransaction`
  ADD PRIMARY KEY (`transactionId`),
  ADD KEY `transaction_account_fk` (`accountId`),
  ADD KEY `transaction_invoice_fk` (`invoiceId`);

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customerId`),
  ADD KEY `cust_storeLocId_fk_idx` (`homeLocId`) USING BTREE;

--
-- Indexes for table `invoice`
--
ALTER TABLE `invoice`
  ADD PRIMARY KEY (`invoiceNum`),
  ADD KEY `invoice_storeLoc_fk_idx` (`storeLocId`) USING BTREE,
  ADD KEY `invoice_customer_fk_idx` (`customerid`) USING BTREE;

--
-- Indexes for table `invoiceLineItem`
--
ALTER TABLE `invoiceLineItem`
  ADD PRIMARY KEY (`partid`,`invoicenum`),
  ADD UNIQUE KEY `invoiceLineItem_part_fk_idx` (`partid`),
  ADD KEY `invoiceLineItem_invoice_fk_idx` (`invoicenum`) USING BTREE;

--
-- Indexes for table `job`
--
ALTER TABLE `job`
  ADD PRIMARY KEY (`jobid`),
  ADD KEY `job_project_fk_idx` (`projectId`) USING BTREE;

--
-- Indexes for table `joblabor`
--
ALTER TABLE `joblabor`
  ADD PRIMARY KEY (`joblaborid`),
  ADD KEY `joblabor_job_fk_idx` (`jobid`) USING BTREE;

--
-- Indexes for table `jobpart`
--
ALTER TABLE `jobpart`
  ADD PRIMARY KEY (`jobpartid`),
  ADD KEY `jobLineItem_part_fk_idx` (`partid`) USING BTREE,
  ADD KEY `jobLineItem_job_fk_idx` (`jobid`) USING BTREE;

--
-- Indexes for table `part`
--
ALTER TABLE `part`
  ADD PRIMARY KEY (`partId`),
  ADD UNIQUE KEY `partNumber_storeLoc_uniq` (`partNumber`,`storeLocId`) USING BTREE,
  ADD KEY `part_storeLoc_fk_idx` (`storeLocId`) USING BTREE;

--
-- Indexes for table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`projectId`),
  ADD KEY `project_customerId_fk_idx` (`customerId`) USING BTREE,
  ADD KEY `project_projectSiteId_fk_idx` (`projectSiteId`) USING BTREE,
  ADD KEY `project_storeLocId_fk_idx` (`storeLocId`) USING BTREE;

--
-- Indexes for table `projectSite`
--
ALTER TABLE `projectSite`
  ADD PRIMARY KEY (`projectSiteId`),
  ADD KEY `projectSite_customerId_fk_idx` (`customerId`) USING BTREE;

--
-- Indexes for table `storeLocation`
--
ALTER TABLE `storeLocation`
  ADD PRIMARY KEY (`locationId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `accountId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `accountBalanceHx`
--
ALTER TABLE `accountBalanceHx`
  MODIFY `balanceHxId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `accountTransaction`
--
ALTER TABLE `accountTransaction`
  MODIFY `transactionId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customerId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `invoice`
--
ALTER TABLE `invoice`
  MODIFY `invoiceNum` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `job`
--
ALTER TABLE `job`
  MODIFY `jobid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `joblabor`
--
ALTER TABLE `joblabor`
  MODIFY `joblaborid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `jobpart`
--
ALTER TABLE `jobpart`
  MODIFY `jobpartid` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `part`
--
ALTER TABLE `part`
  MODIFY `partId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `project`
--
ALTER TABLE `project`
  MODIFY `projectId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `projectSite`
--
ALTER TABLE `projectSite`
  MODIFY `projectSiteId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `storeLocation`
--
ALTER TABLE `storeLocation`
  MODIFY `locationId` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_customer_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`);

--
-- Constraints for table `accountBalanceHx`
--
ALTER TABLE `accountBalanceHx`
  ADD CONSTRAINT `balanceHx_account_fk` FOREIGN KEY (`accountId`) REFERENCES `account` (`accountId`);

--
-- Constraints for table `accountTransaction`
--
ALTER TABLE `accountTransaction`
  ADD CONSTRAINT `transaction_account_fk` FOREIGN KEY (`accountId`) REFERENCES `account` (`accountId`),
  ADD CONSTRAINT `transaction_invoice_fk` FOREIGN KEY (`invoiceId`) REFERENCES `invoice` (`invoiceNum`);

--
-- Constraints for table `customer`
--
ALTER TABLE `customer`
  ADD CONSTRAINT `cust_storeLocId_fk` FOREIGN KEY (`homeLocId`) REFERENCES `storeLocation` (`locationId`);

--
-- Constraints for table `invoice`
--
ALTER TABLE `invoice`
  ADD CONSTRAINT `invoice_customer_fk` FOREIGN KEY (`customerid`) REFERENCES `customer` (`customerId`),
  ADD CONSTRAINT `invoice_storeLoc_fk` FOREIGN KEY (`storeLocId`) REFERENCES `storeLocation` (`locationId`);

--
-- Constraints for table `invoiceLineItem`
--
ALTER TABLE `invoiceLineItem`
  ADD CONSTRAINT `invoiceLineItem_invoice_fk` FOREIGN KEY (`invoicenum`) REFERENCES `invoice` (`invoiceNum`),
  ADD CONSTRAINT `invoiceLineItem_part_fk` FOREIGN KEY (`partid`) REFERENCES `part` (`partId`);

--
-- Constraints for table `job`
--
ALTER TABLE `job`
  ADD CONSTRAINT `job_project_fk` FOREIGN KEY (`projectId`) REFERENCES `project` (`projectId`);

--
-- Constraints for table `joblabor`
--
ALTER TABLE `joblabor`
  ADD CONSTRAINT `joblabor_job_fk` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`);

--
-- Constraints for table `jobpart`
--
ALTER TABLE `jobpart`
  ADD CONSTRAINT `jobLineItem_job_fk` FOREIGN KEY (`jobid`) REFERENCES `job` (`jobid`),
  ADD CONSTRAINT `jobLineItem_part_fk` FOREIGN KEY (`partid`) REFERENCES `part` (`partId`);

--
-- Constraints for table `part`
--
ALTER TABLE `part`
  ADD CONSTRAINT `part_storeLoc_fk` FOREIGN KEY (`storeLocId`) REFERENCES `storeLocation` (`locationId`);

--
-- Constraints for table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `project_customerId_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`),
  ADD CONSTRAINT `project_projectSiteId_fk` FOREIGN KEY (`projectSiteId`) REFERENCES `projectsite` (`projectSiteId`),
  ADD CONSTRAINT `project_storeLocId_fk` FOREIGN KEY (`storeLocId`) REFERENCES `storeLocation` (`locationId`);

--
-- Constraints for table `projectSite`
--
ALTER TABLE `projectSite`
  ADD CONSTRAINT `projectSite_customerId_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`customerId`);
SET FOREIGN_KEY_CHECKS=1;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
