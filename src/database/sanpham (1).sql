-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 04, 2026 at 07:26 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cafedb`
--

-- --------------------------------------------------------

--
-- Table structure for table `sanpham`
--

CREATE TABLE `sanpham` (
  `MaSP` int(11) NOT NULL,
  `TenSP` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `LoaiSP` varchar(50) NOT NULL,
  `GiaBan` double NOT NULL,
  `TrangThai` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'DangBan',
  `HinhAnh` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sanpham`
--

INSERT INTO `sanpham` (`MaSP`, `TenSP`, `LoaiSP`, `GiaBan`, `TrangThai`, `HinhAnh`) VALUES
(1, 'Cafe Đen', 'Cà phê', 20000, 'Đang bán', 'cafe_den_34670358c35d4a21ad899a1f6c383dec.jpg'),
(2, 'Cafe Sữa', 'Cà phê', 25000, 'Đang bán', 'cach-pha-cafe-sua-3-tang-1.jpg'),
(3, 'Trà sữa trân châu đường đen', 'Trà', 35000, 'Đang bán', 'huong-dan-cach-lam-tra-sua-tran-chau-duong-den-202201211522033706.jpg'),
(4, 'Trà sữa trân châu đường đen', 'Trà', 35000, 'Đang bán', 'huong-dan-cach-lam-tra-sua-tran-chau-duong-den-202201211522033706.jpg'),
(5, 'Trà Ô-Long Tea Plus', 'Nước ngọt', 12000, 'Đang bán', 'OIP.jpg'),
(6, 'Trà đào cam sả', 'Trà', 25000, 'Đang bán', 'th.jpg'),
(7, 'Cà phê đen', 'Cà phê', 20000, 'Đang bán', ''),
(8, 'Sting', 'Nước ngọt', 10000, 'Đang bán', 'sting-do.png'),
(9, 'Cà phê cacao', 'Cà phê', 30000, 'Đang bán', ''),
(10, 'Cafe Epresso', 'Cà phê', 40000, 'Đang bán', ''),
(11, 'Cafe Cherry', 'Cà phê', 35000, 'Đang bán', '1.3-1024x682.jpg'),
(12, 'Cafe Latte', 'Cà phê', 50000, 'Đang bán', ''),
(13, 'Capuchino', 'Cà phê', 45000, 'Đang bán', ''),
(14, 'Cafe Phin sữa đá', 'Cà phê', 30000, 'Đang bán', ''),
(15, 'Trà Sữa Chocolate', 'Trà', 35000, 'Đang bán', 'tra-sua-socola-boba-600x661.jpg'),
(16, 'Trà sữa dâu', 'Trà', 35000, 'Đang bán', 'dau4_4e9b8ba789c64395979862a9e526d20e_grande.jpg'),
(17, 'Trà Trái Cây', 'Trà', 40000, 'Đang bán', 'maxresdefault.jpg'),
(18, 'Trà Sữa Matcha', 'Trà', 40000, 'Đang bán', '01fa36387c60e0493f8549b0f2ce8c70.jpg'),
(19, 'Trà Sữa Khoai Môn', 'Trà', 30000, 'Đang bán', 'cong-thuc-pha-tra-sua-khoai-mon.jpg'),
(20, 'Trà Sữa CaCao', 'Trà', 30000, 'Đang bán', 'hinh-anh-ly-tra-sua-socola.jpg'),
(21, 'Trà Chanh', 'Trà', 25000, 'Đang bán', 's617352704893313315_p69_i1_w1080.jpeg'),
(22, 'Trà Gừng', 'Cà phê', 20000, 'Đang bán', 'R.jpg'),
(23, 'Trà Sữa Truyền Thống', 'Cà phê', 25000, 'Đang bán', 'hoc-cach-pha-tra-sua-o-long-dai-loan-thom-ngon-chuan-vi-ai-cung-me-202108100039248020.jpg'),
(24, 'Trà sữa trân châu đường đen', 'Trà', 35000, 'Đang bán', 'huong-dan-cach-lam-tra-sua-tran-chau-duong-den-202201211522033706.jpg');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `sanpham`
--
ALTER TABLE `sanpham`
  ADD PRIMARY KEY (`MaSP`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `sanpham`
--
ALTER TABLE `sanpham`
  MODIFY `MaSP` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
