-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 30, 2025 at 10:23 AM
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
-- Database: `warrantymaintenance`
--

-- --------------------------------------------------------

--
-- Table structure for table `issues`
--

CREATE TABLE `issues` (
  `id` int(11) NOT NULL,
  `appliance` varchar(50) DEFAULT NULL,
  `model_number` varchar(100) DEFAULT NULL,
  `reported_by` int(11) DEFAULT NULL,
  `issue_description` text DEFAULT NULL,
  `status` enum('Pending','In Progress','Resolved','Closed') DEFAULT 'Pending',
  `assigned_technician_id` int(11) DEFAULT NULL,
  `supervisor_comment` text DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `issues`
--

INSERT INTO `issues` (`id`, `appliance`, `model_number`, `reported_by`, `issue_description`, `status`, `assigned_technician_id`, `supervisor_comment`, `created_at`, `updated_at`) VALUES
(1, 'Air Conditioner', NULL, NULL, 'Not cooling', 'In Progress', 1, 'Technician assigned and work started', '2025-12-15 12:45:13', '2025-12-15 13:12:26'),
(2, 'Television', NULL, NULL, 'Screen flickering', 'Pending', NULL, NULL, '2025-12-15 12:46:24', '2025-12-15 12:46:24');

-- --------------------------------------------------------

--
-- Table structure for table `service_reports`
--

CREATE TABLE `service_reports` (
  `id` int(11) NOT NULL,
  `issue_id` int(11) NOT NULL,
  `work_done` text NOT NULL,
  `parts_replaced` varchar(255) DEFAULT NULL,
  `service_cost` decimal(10,2) NOT NULL,
  `before_photo` varchar(255) DEFAULT NULL,
  `after_photo` varchar(255) DEFAULT NULL,
  `additional_photos` text DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `service_reports`
--

INSERT INTO `service_reports` (`id`, `issue_id`, `work_done`, `parts_replaced`, `service_cost`, `before_photo`, `after_photo`, `additional_photos`, `notes`, `created_at`) VALUES
(1, 1, 'gas refilled, cooling restored', 'gas valve', 1500.00, 'uploads/service_reports/1765596112_before_photo_Screenshot 2025-02-18 145159.png', 'uploads/service_reports/1765596112_after_photo_Screenshot 2025-02-13 143902.png', '[]', 'recommended yearly service', '2025-12-13 03:21:52'),
(2, 1, 'gas refilled, cooling restored', 'gas valve', 1500.00, 'uploads/service_reports/1765783011_before_photo_Screenshot 2025-02-18 145159.png', 'uploads/service_reports/1765783011_after_photo_Screenshot 2025-02-13 143902.png', '[]', 'recommended yearly service', '2025-12-15 07:16:51');

-- --------------------------------------------------------

--
-- Table structure for table `technicians`
--

CREATE TABLE `technicians` (
  `id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(150) NOT NULL,
  `experience_years` int(11) NOT NULL DEFAULT 0,
  `specialization` varchar(50) NOT NULL,
  `address` text NOT NULL,
  `id_proof_path` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `technicians`
--

INSERT INTO `technicians` (`id`, `name`, `phone`, `email`, `experience_years`, `specialization`, `address`, `id_proof_path`, `created_at`) VALUES
(1, 'naveen', '8967456547', '0', 0, 'AC Technician', 'saveetha, 600125', 'uploads/technician_ids/id_1765428848_cb853daf42b2.png', '2025-12-11 10:24:08'),
(2, 'naveen', '8967456547', '0', 0, 'AC Technician', 'saveetha, 600125', 'uploads/technician_ids/id_1765435417_715bab5dc819.png', '2025-12-11 12:13:37'),
(4, 'kalyan', '8688435348', '0', 2, 'AC Technician', '801-chennai', 'uploads/technician_ids/id_1766982658_3eb43dbef4a0.jpg', '2025-12-29 10:00:58'),
(5, 'kalyan', '8688435348', '0', 2, 'AC Technician', '801- chennai', 'uploads/technician_ids/id_1766983032_1db63b43ec53.jpg', '2025-12-29 10:07:12'),
(6, 'Keerthipati Kalyan', '8688435348', '0', 3, 'Fan Technician', '80-6chennai', 'uploads/technician_ids/id_1766994301_969c1eb3daa1.jpg', '2025-12-29 13:15:01');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `address` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `reset_token_hash` varchar(64) DEFAULT NULL,
  `reset_expires` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `full_name`, `email`, `phone`, `password`, `created_at`, `reset_token_hash`, `reset_expires`) VALUES
(1, 'Kalyan Varma', 'test@gmail.com', '9876543210', '$2y$10$8pxBeXd2FZSo3/ZmqZkzce5iumDPciOAxWQszdp3vttuyy0snm19O', '2025-12-11 03:25:20', '6aaea304476b2893eb2051d28b7bb746a178bd14c48e2ca92b0349359b961412', '2025-12-27 13:46:48'),
(2, 'umar', 'umar@gmail.com', '8973456290', '$2y$10$E/ExpiZhRN6NcZl.Tx6xQOyN8Ipkidz72/vcZDzWMux31N5e/G6Dy', '2025-12-11 03:44:34', NULL, NULL),
(3, 'umar', 'kalyan@gmail.com', '8973456290', '$2y$10$mT57Jb5LJpCzLRJzMUGpS.Oo5nUB2zY1uHxqUNx7nyL8ZMPues4Yi', '2025-12-15 07:06:03', NULL, NULL),
(4, 'umar', 'kalyan12@gmail.com', '8973456290', '$2y$10$y6QgEMz4jI5hGkYAwzUKYO3zVJO.CFWUcawS4Ml6wEVC2cZZg0i2G', '2025-12-30 07:18:17', NULL, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `warranty_records`
--

CREATE TABLE `warranty_records` (
  `id` int(11) NOT NULL,
  `appliance` varchar(50) NOT NULL,
  `model_number` varchar(100) NOT NULL,
  `purchase_date` date NOT NULL,
  `expiry_date` date NOT NULL,
  `maintenance_frequency` varchar(100) NOT NULL,
  `notes` text DEFAULT NULL,
  `document_path` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `warranty_records`
--

INSERT INTO `warranty_records` (`id`, `appliance`, `model_number`, `purchase_date`, `expiry_date`, `maintenance_frequency`, `notes`, `document_path`, `created_at`) VALUES
(1, 'Air Conditioner', '&quot;lg1234&quot;', '2025-01-10', '2025-06-10', 'every 3 months', 'jhgvffuihdbuyigh', 'uploads/warranty_docs/doc_1765429986_2c2b54c78ea3.png', '2025-12-11 10:43:06'),
(3, 'Fan', '&quot;lg1234&quot;', '2025-01-10', '2025-06-10', 'every 6 months', 'dafaggrfv', 'uploads/warranty_docs/doc_1765782550_3925ee77868c.png', '2025-12-15 12:39:10'),
(4, 'Air Conditioner', 'lg005', '2025-10-08', '2025-12-29', '2 months', 'dhjskjjsjs', 'uploads/warranty_docs/doc_1766984200_cc2ee47b7e38.jpg', '2025-12-29 10:26:40');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `issues`
--
ALTER TABLE `issues`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `service_reports`
--
ALTER TABLE `service_reports`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `technicians`
--
ALTER TABLE `technicians`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `warranty_records`
--
ALTER TABLE `warranty_records`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `issues`
--
ALTER TABLE `issues`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `service_reports`
--
ALTER TABLE `service_reports`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `technicians`
--
ALTER TABLE `technicians`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `warranty_records`
--
ALTER TABLE `warranty_records`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
