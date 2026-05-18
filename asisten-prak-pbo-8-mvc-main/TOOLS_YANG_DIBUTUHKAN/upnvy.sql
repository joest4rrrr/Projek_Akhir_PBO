-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 01, 2024 at 08:35 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `upnvy`
--

-- --------------------------------------------------------

--
-- Table structure for table `dosen`
--

CREATE TABLE `dosen` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `nidn` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `dosen`
--

INSERT INTO `dosen` (`id`, `nama`, `nidn`) VALUES
(1, 'Dr.Awang Hendrianto Pratomo, S.T., M.T', '0025077701'),
(2, 'Andiko Putro Suryotomo, S.Kom., M.Cs', '0030098504'),
(3, 'Dessyanto Boedi Prasetyo, S.T., M.T', '0505127501'),
(4, 'Rochmat Husaini, S.Kom., M.Kom.', '0026048804'),
(5, 'Frans Richard Kodong, S.T., M.Kom.', '0523026201'),
(6, 'Mangaras Yanu F.,S.T., M.T.', '0521018201'),
(7, 'Dyah Ayu Irawati, S.T., M.Cs.', '0008078401'),
(8, 'Dr. Heriyanto, A.Md., S.Kom., M.Cs.', '0508067703'),
(9, 'Shoffan Saifullah, S.Kom., M.Kom.', '0528019302'),
(10, 'Rifki Indra Perwira, S.Kom., M.Eng.', '0508078301'),
(11, 'Oliver Samuel Simanjuntak, S.Kom., M.Eng.', '0525058302'),
(12, 'Dr. Herlina Jayadianti, S.T., M.T.', '0527087701'),
(13, 'Nur Heri Cahyana, S.T, M.Kom.', '0522096001'),
(14, 'Hidayatulah Himawan, S.T., M.M., M.Eng.', '0024127601');

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `id` int(11) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `nim` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`id`, `nama`, `nim`) VALUES
(1, 'RIZKY APRILIA INEZTRI UTOMO', '123220012'),
(2, 'AQSHA JAUZAARAFA SETYA HADI', '123220016'),
(3, 'BAIQ AFIFAH HANUM', '123220037'),
(4, 'NURMA INTAN HARIANJA', '123220046'),
(5, 'FEBRIAN CHRISNA ARDIANTO', '123220051'),
(6, 'Shinta Nursobah Chairani', '123220074'),
(7, 'KRISTOPHER FREDRIK HUTAPEA', '123220075'),
(8, 'VRIDA PUSPARANI', '123220082'),
(9, 'Gita Poetri Dewi Siregar', '123220084'),
(10, 'Sakti Maulana Ibrahim', '123220101'),
(11, 'Alvino Abyan Rizaldi', '123220114'),
(12, 'HAFIZH AKBAR KARIMY', '123220116'),
(14, 'Resti Ramadhani', '123220147'),
(15, 'Jeslyn Vicky Hanjaya', '123220150'),
(16, 'FAIZA NUR RAFIDA', '123220159'),
(17, 'YEDHIT TRISAKTI TAMMA', '123220160'),
(18, 'RIFQI ARIEF NUR RASYID', '123220162'),
(19, 'HANAFIE BUDI PRATAMA', '123220166'),
(20, 'MALIK AFIF BHIRAWA', '123220170'),
(21, 'ROYAN ADITYA', '123220174'),
(22, 'BAGUS WIRA APRITIANTO', '123220180'),
(23, 'ARYAMUKTI SATRIA HENDRAYANA', '123220181'),
(24, 'SYIFA NUR RAMADHANI', '123220194'),
(25, 'AISYAH KUSUMAWATI', '123220195'),
(26, 'YOHANES FEBRYAN KANA NYOLA', '123220198'),
(27, 'MUHAMMAD NAUFAL FAUZI ALI', '123220207'),
(28, 'GUSTANSYAH DWI PUTRA SUJANTO', '123220210'),
(29, 'Veri Anggoro Wijayanto', '123220146');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `dosen`
--
ALTER TABLE `dosen`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `dosen`
--
ALTER TABLE `dosen`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
