-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 29, 2025 at 10:30 AM
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
-- Database: `qltb_thpt`
--

-- --------------------------------------------------------

--
-- Table structure for table `ctphieumuon`
--

CREATE TABLE `ctphieumuon` (
  `mapm` int(11) NOT NULL,
  `matbi` varchar(30) NOT NULL,
  `trangthai_luc_muon` tinyint(4) DEFAULT NULL,
  `trangthai_luc_tra` tinyint(4) DEFAULT NULL,
  `soluong` int(11) DEFAULT 1,
  `ghichu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ctphieumuon`
--

INSERT INTO `ctphieumuon` (`mapm`, `matbi`, `trangthai_luc_muon`, `trangthai_luc_tra`, `soluong`, `ghichu`) VALUES
(1, 'TB001', 1, 3, 1, NULL),
(1, 'TB002', 1, 3, 1, NULL),
(2, 'TB003', 1, 3, 1, NULL),
(5, 'TB001', 1, 1, 1, NULL),
(5, 'TB002', 1, 1, 1, NULL),
(9, 'TB001', 1, 1, 1, 'Trả: 1 Tốt, 0 Hỏng. '),
(10, 'TB002', 1, 3, 1, 'Trả: 0 Tốt, 1 Hỏng. '),
(11, 'TB001', 1, 3, 2, 'Trả: 1 Tốt, 1 Hỏng. '),
(11, 'TB002', 1, 1, 1, 'Trả: 1 Tốt, 0 Hỏng. '),
(12, 'TB001', 1, 3, 1, 'Trả: 0 Tốt, 1 Hỏng. '),
(12, 'TB002', 1, 1, 1, 'Trả: 1 Tốt, 0 Hỏng. '),
(13, 'TB003', 1, 1, 2, 'Trả: 2 Tốt, 0 Hỏng. '),
(14, 'TB002', 1, 1, 1, 'Trả: 1 Tốt, 0 Hỏng. '),
(15, 'TB001', 1, 3, 4, 'Trả: 2 Tốt, 2 Hỏng. '),
(16, 'TB001', 1, 1, 4, 'Trả: 4 Tốt, 0 Hỏng. '),
(17, 'TB001', 1, 1, 4, 'Trả: 4 Tốt, 0 Hỏng. '),
(17, 'TB002', 1, 1, 3, 'Trả: 3 Tốt, 0 Hỏng. '),
(18, 'TB001', 1, 3, 9, 'Trả: 6 Tốt, 3 Hỏng. '),
(19, 'TB001', 1, 3, 7, 'Trả: 3 Tốt, 4 Hỏng. '),
(20, 'TB001', 1, NULL, 1, NULL),
(20, 'TB002', 1, NULL, 1, NULL),
(20, 'TB003', 1, NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `hocsinh`
--

CREATE TABLE `hocsinh` (
  `mahs` varchar(15) NOT NULL,
  `tenhs` varchar(50) NOT NULL,
  `malop` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hocsinh`
--

INSERT INTO `hocsinh` (`mahs`, `tenhs`, `malop`) VALUES
('HS001', 'Nguyễn Minh Anh', '10A1'),
('HS002', 'Trần Quốc Bảo', '10A1'),
('HS003', 'Lê Thị Hồng', '10A2');

-- --------------------------------------------------------

--
-- Table structure for table `khuvuc`
--

CREATE TABLE `khuvuc` (
  `makhuvuc` varchar(10) NOT NULL,
  `tenkhuvuc` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `khuvuc`
--

INSERT INTO `khuvuc` (`makhuvuc`, `tenkhuvuc`) VALUES
('KV005', 'Hội trường lớn'),
('KV001', 'Khu A'),
('KV002', 'Khu B'),
('KV003', 'Khu C'),
('KV004', 'Khu D'),
('KV006', 'Nhà đa năng');

-- --------------------------------------------------------

--
-- Table structure for table `loaitb`
--

CREATE TABLE `loaitb` (
  `maloai` varchar(20) NOT NULL,
  `tenloai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `loaitb`
--

INSERT INTO `loaitb` (`maloai`, `tenloai`) VALUES
('LOA', 'Loa'),
('LT', 'Laptop'),
('MC', 'Máy chiếu');

-- --------------------------------------------------------

--
-- Table structure for table `lop`
--

CREATE TABLE `lop` (
  `malop` varchar(15) NOT NULL,
  `tenlop` varchar(50) NOT NULL,
  `gv_chunhiem` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lop`
--

INSERT INTO `lop` (`malop`, `tenlop`, `gv_chunhiem`) VALUES
('10A1', 'Lớp 10A1', 'Nguyễn Văn Hùng'),
('10A2', 'Lớp 10A2', 'Trần Thị Lan');

-- --------------------------------------------------------

--
-- Table structure for table `phieumuon`
--

CREATE TABLE `phieumuon` (
  `mapm` int(11) NOT NULL,
  `mahs` varchar(15) DEFAULT NULL,
  `malop` varchar(15) DEFAULT NULL,
  `maphong` varchar(20) DEFAULT NULL,
  `ngaymuon` date DEFAULT curdate(),
  `hantra` date NOT NULL,
  `ngaytra` date DEFAULT NULL,
  `ghichu` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phieumuon`
--

INSERT INTO `phieumuon` (`mapm`, `mahs`, `malop`, `maphong`, `ngaymuon`, `hantra`, `ngaytra`, `ghichu`) VALUES
(1, 'HS001', '10A1', 'P101', '2025-12-24', '2025-12-20', '2025-12-24', 'Tiết Toán'),
(2, 'HS003', '10A2', 'PTN01', '2025-12-24', '2025-12-21', '2025-12-24', 'Thí nghiệm Lý'),
(5, 'HS001', '10A1', 'P101', '2025-12-24', '2026-01-12', '2025-12-24', 'Utt vip'),
(9, 'HS001', '10A1', 'P101', '2025-12-24', '2025-12-25', '2025-12-24', 'ok'),
(10, 'HS001', '10A1', 'PTN01', '2025-12-24', '2025-12-25', '2025-12-24', ''),
(11, 'HS001', '10A1', 'P101', '2025-12-24', '2025-12-24', '2025-12-24', 'Ok'),
(12, 'HS001', '10A1', 'P101', '2025-12-24', '2025-12-23', '2025-12-24', 'K OKE LẮM'),
(13, 'HS001', '10A1', 'PTN01', '2025-12-25', '2025-12-24', '2025-12-25', 'OK'),
(14, 'HS001', '10A1', 'P101', '2025-12-25', '2025-12-25', '2025-12-25', 'ok'),
(15, 'HS001', '10A1', 'P101', '2025-12-25', '2026-01-25', '2025-12-25', 'ok'),
(16, 'HS001', '10A1', 'P101', '2025-12-25', '2025-12-25', '2025-12-25', 'ok'),
(17, 'HS001', '10A1', 'PTN01', '2025-12-25', '2025-12-25', '2025-12-25', 'ok'),
(18, 'HS001', '10A1', 'PTN01', '2025-12-25', '2025-12-25', '2025-12-25', 'ok'),
(19, 'HS001', '10A1', 'P101', '2025-12-27', '2025-12-26', '2025-12-27', 'ok'),
(20, 'HS001', '10A1', 'PTN01', '2025-12-29', '2025-12-29', NULL, 'thằng mượn đẹp trai');

-- --------------------------------------------------------

--
-- Table structure for table `phong`
--

CREATE TABLE `phong` (
  `maphong` varchar(20) NOT NULL,
  `tenphong` varchar(50) NOT NULL,
  `trangthai` tinyint(4) DEFAULT NULL,
  `makhuvuc` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `phong`
--

INSERT INTO `phong` (`maphong`, `tenphong`, `trangthai`, `makhuvuc`) VALUES
('P009', 'Phòng 203', 2, 'KV005'),
('P101', 'Phòng học 101', 1, 'KV002'),
('PTN01', 'Phòng thí nghiệm Lý', 1, 'KV002');

-- --------------------------------------------------------

--
-- Table structure for table `thietbi`
--

CREATE TABLE `thietbi` (
  `matbi` varchar(30) NOT NULL,
  `tentbi` varchar(100) NOT NULL,
  `maloai` varchar(20) DEFAULT NULL,
  `trangthai` tinyint(4) DEFAULT NULL,
  `ghichu` varchar(200) DEFAULT NULL,
  `soluong_tot` int(11) DEFAULT 0,
  `soluong_hong` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `thietbi`
--

INSERT INTO `thietbi` (`matbi`, `tentbi`, `maloai`, `trangthai`, `ghichu`, `soluong_tot`, `soluong_hong`) VALUES
('TB001', 'Máy chiếu Epson EB-X05', 'MC', 1, '', 7, 0),
('TB002', 'Laptop Dell Latitude 5420', 'LT', 1, '', 6, 0),
('TB003', 'Loa kéo Acnos CS450', 'LOA', 1, '', 6, 0),
('TB004', 'JBL PartyBox 710', 'LOA', 1, '', 2, 0);

-- --------------------------------------------------------

--
-- Table structure for table `trangthai`
--

CREATE TABLE `trangthai` (
  `matinhtrang` tinyint(4) NOT NULL,
  `tentinhtrang` varchar(50) NOT NULL,
  `mota` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `trangthai`
--

INSERT INTO `trangthai` (`matinhtrang`, `tentinhtrang`, `mota`) VALUES
(1, 'Sẵn sàng', 'Thiết bị hoạt động bình thường'),
(2, 'Không Sẵn Sàng', 'Thiết Bị Chưa Được Cho Mượn'),
(3, 'Hỏng', 'Thiết bị bị hỏng');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `ctphieumuon`
--
ALTER TABLE `ctphieumuon`
  ADD PRIMARY KEY (`mapm`,`matbi`),
  ADD KEY `matbi` (`matbi`),
  ADD KEY `trangthai_luc_muon` (`trangthai_luc_muon`),
  ADD KEY `trangthai_luc_tra` (`trangthai_luc_tra`);

--
-- Indexes for table `hocsinh`
--
ALTER TABLE `hocsinh`
  ADD PRIMARY KEY (`mahs`),
  ADD KEY `malop` (`malop`);

--
-- Indexes for table `khuvuc`
--
ALTER TABLE `khuvuc`
  ADD PRIMARY KEY (`makhuvuc`),
  ADD UNIQUE KEY `tenkhuvuc` (`tenkhuvuc`);

--
-- Indexes for table `loaitb`
--
ALTER TABLE `loaitb`
  ADD PRIMARY KEY (`maloai`);

--
-- Indexes for table `lop`
--
ALTER TABLE `lop`
  ADD PRIMARY KEY (`malop`);

--
-- Indexes for table `phieumuon`
--
ALTER TABLE `phieumuon`
  ADD PRIMARY KEY (`mapm`),
  ADD KEY `mahs` (`mahs`),
  ADD KEY `malop` (`malop`),
  ADD KEY `maphong` (`maphong`);

--
-- Indexes for table `phong`
--
ALTER TABLE `phong`
  ADD PRIMARY KEY (`maphong`),
  ADD KEY `trangthai` (`trangthai`);

--
-- Indexes for table `thietbi`
--
ALTER TABLE `thietbi`
  ADD PRIMARY KEY (`matbi`),
  ADD KEY `maloai` (`maloai`),
  ADD KEY `trangthai` (`trangthai`);

--
-- Indexes for table `trangthai`
--
ALTER TABLE `trangthai`
  ADD PRIMARY KEY (`matinhtrang`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `phieumuon`
--
ALTER TABLE `phieumuon`
  MODIFY `mapm` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `ctphieumuon`
--
ALTER TABLE `ctphieumuon`
  ADD CONSTRAINT `ctphieumuon_ibfk_1` FOREIGN KEY (`mapm`) REFERENCES `phieumuon` (`mapm`) ON DELETE CASCADE,
  ADD CONSTRAINT `ctphieumuon_ibfk_2` FOREIGN KEY (`matbi`) REFERENCES `thietbi` (`matbi`),
  ADD CONSTRAINT `ctphieumuon_ibfk_3` FOREIGN KEY (`trangthai_luc_muon`) REFERENCES `trangthai` (`matinhtrang`),
  ADD CONSTRAINT `ctphieumuon_ibfk_4` FOREIGN KEY (`trangthai_luc_tra`) REFERENCES `trangthai` (`matinhtrang`);

--
-- Constraints for table `hocsinh`
--
ALTER TABLE `hocsinh`
  ADD CONSTRAINT `hocsinh_ibfk_1` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`) ON DELETE SET NULL;

--
-- Constraints for table `phieumuon`
--
ALTER TABLE `phieumuon`
  ADD CONSTRAINT `phieumuon_ibfk_1` FOREIGN KEY (`mahs`) REFERENCES `hocsinh` (`mahs`),
  ADD CONSTRAINT `phieumuon_ibfk_2` FOREIGN KEY (`malop`) REFERENCES `lop` (`malop`),
  ADD CONSTRAINT `phieumuon_ibfk_3` FOREIGN KEY (`maphong`) REFERENCES `phong` (`maphong`);

--
-- Constraints for table `phong`
--
ALTER TABLE `phong`
  ADD CONSTRAINT `phong_ibfk_1` FOREIGN KEY (`trangthai`) REFERENCES `trangthai` (`matinhtrang`);

--
-- Constraints for table `thietbi`
--
ALTER TABLE `thietbi`
  ADD CONSTRAINT `thietbi_ibfk_1` FOREIGN KEY (`maloai`) REFERENCES `loaitb` (`maloai`),
  ADD CONSTRAINT `thietbi_ibfk_2` FOREIGN KEY (`trangthai`) REFERENCES `trangthai` (`matinhtrang`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
