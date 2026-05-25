-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
<<<<<<< HEAD
-- Generation Time: May 24, 2026 at 08:26 PM
=======
-- Generation Time: May 25, 2026 at 12:39 PM
>>>>>>> 51a000b (tambah database)
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ps_rental`
--

-- --------------------------------------------------------

--
<<<<<<< HEAD
=======
-- Table structure for table `bookings`
--

CREATE TABLE `bookings` (
  `id` int(11) NOT NULL,
  `device_id` varchar(10) NOT NULL,
  `customer_name` varchar(100) NOT NULL,
  `booking_date` date NOT NULL,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  `status` enum('pending','active','done','cancelled') DEFAULT 'pending',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bookings`
--

INSERT INTO `bookings` (`id`, `device_id`, `customer_name`, `booking_date`, `start_time`, `end_time`, `status`, `created_at`) VALUES
(1, 'PS3-04', 'Wahyono', '2026-05-25', '16:00:00', '18:00:00', 'done', '2026-05-25 07:13:55'),
(2, 'PS3-01', 'heri', '2026-05-25', '15:00:00', '17:00:00', 'done', '2026-05-25 07:37:35'),
(3, 'PS5-06', 'yudi', '2026-05-25', '15:00:00', '18:00:00', 'done', '2026-05-25 07:39:09'),
(4, 'PS5-06', 'rivo', '2026-05-25', '17:30:00', '19:00:00', 'pending', '2026-05-25 09:22:44'),
(5, 'PS5-07', 'fff', '2026-05-25', '16:30:00', '18:00:00', 'pending', '2026-05-25 09:28:38');

-- --------------------------------------------------------

--
>>>>>>> 51a000b (tambah database)
-- Table structure for table `devices`
--

CREATE TABLE `devices` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `devices`
--

INSERT INTO `devices` (`id`, `name`, `is_available`) VALUES
('PS3-01', 'PlayStation 3', 1),
('PS3-02', 'PlayStation 3', 1),
('PS3-03', 'PlayStation 3', 1),
('PS3-04', 'PlayStation 3', 1),
('PS3-05', 'PlayStation 3', 1),
('PS4-01', 'PlayStation 4', 1),
('PS4-02', 'PlayStation 4', 1),
('PS4-03', 'PlayStation 4', 1),
('PS4-04', 'PlayStation 4', 1),
('PS4-05', 'PlayStation 4', 1),
('PS4-06', 'PlayStation 4', 1),
('PS4-07', 'PlayStation 4', 1),
('PS4-08', 'PlayStation 4', 1),
('PS4-09', 'PlayStation 4', 1),
<<<<<<< HEAD
('PS4-10', 'PlayStation 4', 1),
=======
('PS4-10', 'PlayStation 4', 0),
>>>>>>> 51a000b (tambah database)
('PS5-01', 'PlayStation 5', 1),
('PS5-02', 'PlayStation 5', 1),
('PS5-03', 'PlayStation 5', 1),
('PS5-04', 'PlayStation 5', 1),
('PS5-05', 'PlayStation 5', 1),
('PS5-06', 'PlayStation 5', 1),
<<<<<<< HEAD
('PS5-07', 'PlayStation 5', 1);
=======
('PS5-07', 'PlayStation 5', 0);
>>>>>>> 51a000b (tambah database)

-- --------------------------------------------------------

--
-- Table structure for table `rentals`
--

CREATE TABLE `rentals` (
  `id` int(11) NOT NULL,
  `device_id` varchar(10) DEFAULT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `duration_minutes` int(11) DEFAULT NULL,
  `total_cost` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
<<<<<<< HEAD
=======
-- Dumping data for table `rentals`
--

INSERT INTO `rentals` (`id`, `device_id`, `customer_name`, `start_time`, `end_time`, `duration_minutes`, `total_cost`) VALUES
(24, 'PS3-01', 'Heri', '2026-05-25 13:03:56', '2026-05-25 13:04:17', 1, 3000),
(25, 'PS3-01', 'heri', '2026-05-25 15:00:00', '2026-05-25 17:00:00', 120, 12000),
(26, 'PS5-06', 'yudi', '2026-05-25 15:00:00', '2026-05-25 18:00:00', 180, 48000),
(27, 'PS4-04', 'jono', '2026-05-25 14:41:01', '2026-05-25 14:41:07', 1, 5000),
(28, 'PS3-02', 'bbb', '2026-05-25 16:22:58', '2026-05-25 16:27:53', 4, 3000),
(29, 'PS4-09', 'jono', '2026-05-25 14:39:57', '2026-05-25 16:55:33', 135, 25000);

--
>>>>>>> 51a000b (tambah database)
-- Indexes for dumped tables
--

--
<<<<<<< HEAD
=======
-- Indexes for table `bookings`
--
ALTER TABLE `bookings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_bookings_device_date` (`device_id`,`booking_date`,`status`);

--
>>>>>>> 51a000b (tambah database)
-- Indexes for table `devices`
--
ALTER TABLE `devices`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rentals`
--
ALTER TABLE `rentals`
  ADD PRIMARY KEY (`id`),
  ADD KEY `device_id` (`device_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
<<<<<<< HEAD
-- AUTO_INCREMENT for table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
=======
-- AUTO_INCREMENT for table `bookings`
--
ALTER TABLE `bookings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `rentals`
--
ALTER TABLE `rentals`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
>>>>>>> 51a000b (tambah database)

--
-- Constraints for dumped tables
--

--
<<<<<<< HEAD
=======
-- Constraints for table `bookings`
--
ALTER TABLE `bookings`
  ADD CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `devices` (`id`);

--
>>>>>>> 51a000b (tambah database)
-- Constraints for table `rentals`
--
ALTER TABLE `rentals`
  ADD CONSTRAINT `rentals_ibfk_1` FOREIGN KEY (`device_id`) REFERENCES `devices` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
