-- phpMyAdmin SQL Dump
-- version 3.2.4
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 24, 2010 at 04:04 ¤U¤È
-- Server version: 5.1.41
-- PHP Version: 5.3.1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hospital_rec`
--

-- --------------------------------------------------------

--
-- Table structure for table `allergy`
--

CREATE TABLE IF NOT EXISTS `allergy` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=11731 ;

--
-- Dumping data for table `allergy`
--

INSERT INTO `allergy` (`id`, `name`, `description`) VALUES
(1, 'Asthma', 'Cannot eat food A'),
(2, 'House Dust Mite Allergy', 'Cannot eat food B'),
(3, 'Food Allergy', 'cannot eat Allergy C'),
(4, 'Pet Allergy', 'cannot eat food D'),
(5, 'Pollen Allergies', 'cannot eat food E'),
(6, 'Insect Sting Allergy', 'cannot eat food F'),
(7, 'Dog allergy', 'cannot eat food G'),
(8, 'Cat allergy', 'cannot eat food H'),
(9, 'Lemon allergy', 'cannot eat food I'),
(10, 'Coke allergy', 'cannot eat food J');

-- --------------------------------------------------------

--
-- Table structure for table `dia-allergy_rec`
--

CREATE TABLE IF NOT EXISTS `dia-allergy_rec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pat_id` int(11) NOT NULL,
  `allergy_id` int(11) NOT NULL,
  `valid` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=71 ;

--
-- Dumping data for table `dia-allergy_rec`
--

INSERT INTO `dia-allergy_rec` (`id`, `pat_id`, `allergy_id`, `valid`) VALUES
(56, 1, 7, 0),
(57, 1, 8, 0),
(53, 1, 2, 0),
(52, 1, 1, 0),
(51, 5, 10, 1),
(50, 3, 6, 0),
(49, 3, 8, 0),
(48, 2, 10, 1),
(47, 1, 5, 0),
(46, 1, 4, 0),
(58, 1, 6, 0),
(59, 1, 5, 0),
(60, 1, 1, 0),
(61, 1, 6, 0),
(62, 1, 8, 0),
(63, 1, 7, 1),
(64, 14, 5, 1),
(65, 4, 6, 1),
(66, 4, 8, 0),
(67, 18, 5, 1),
(68, 18, 6, 1),
(69, 4, 5, 1),
(70, 4, 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `dia-disease_rec`
--

CREATE TABLE IF NOT EXISTS `dia-disease_rec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pat_id` int(11) NOT NULL,
  `dis_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Dumping data for table `dia-disease_rec`
--


-- --------------------------------------------------------

--
-- Table structure for table `disease`
--

CREATE TABLE IF NOT EXISTS `disease` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Dumping data for table `disease`
--


-- --------------------------------------------------------

--
-- Table structure for table `log`
--

CREATE TABLE IF NOT EXISTS `log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `datetime` varchar(20) NOT NULL,
  `user` varchar(20) NOT NULL,
  `content` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `log`
--


-- --------------------------------------------------------

--
-- Table structure for table `medicine`
--

CREATE TABLE IF NOT EXISTS `medicine` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text COLLATE utf8_unicode_ci NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Dumping data for table `medicine`
--


-- --------------------------------------------------------

--
-- Table structure for table `patient_personal`
--

CREATE TABLE IF NOT EXISTS `patient_personal` (
  `pid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `gender` varchar(1) COLLATE utf8_unicode_ci NOT NULL,
  `address` text COLLATE utf8_unicode_ci NOT NULL,
  `contact_no` varchar(8) COLLATE utf8_unicode_ci NOT NULL,
  `birthday` date NOT NULL,
  `pic` int(11) NOT NULL,
  `description` text CHARACTER SET latin1,
  PRIMARY KEY (`pid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=28 ;

--
-- Dumping data for table `patient_personal`
--

INSERT INTO `patient_personal` (`pid`, `name`, `gender`, `address`, `contact_no`, `birthday`, `pic`, `description`) VALUES
(1, 'Tang Siu Chun Chris', 'M', 'Hong Kong', '12345678', '2010-01-21', 1, 'testcase'),
(2, 'Doll Kwan heyhey', 'F', 'Kowloon', '12343534', '2010-01-05', 1, 'hihih'),
(3, 'Billy', 'M', 'NT', '53452353', '2010-01-03', 2, 'byebye!'),
(4, 'Chung Man ho pizza', 'F', 'tin shiu wai', '22342344', '2010-01-01', 1, '45345345'),
(5, 'Yeung Tai Man', 'M', 'Rm 1008, ABC Building, Pokfulam Road, hk ', '21345678', '2010-01-13', 3, NULL),
(6, 'Bob 9 head', 'M', 'rm123, abc building, pokfulam road, hk', '26543183', '2010-01-11', 2, 'hi'),
(7, 'wilis', 'M', 'rm456, abc building, pokfulam road, hk', '29845678', '2010-01-21', 1, NULL),
(8, 'Bob', 'M', 'rm123, abc building, pokfulam road, hk', '26543183', '2010-01-11', 2, NULL),
(9, 'WallE', 'M', 'rm456, abc building, pokfulam road, hk', '29845678', '2010-01-21', 1, ''),
(10, 'Peter', 'M', 'rm678, abc building, pokfulam road, hk', '32556656', '2010-01-19', 1, NULL),
(11, 'MaryMary', 'F', 'rm566, abc building, pokfulam road, hk', '25435345', '2010-01-22', 1, ''),
(13, 'Gilbert', 'M', 'ma on shan', '91234353', '2010-01-03', 2, 'i am gilbert'),
(14, 'Tang chunchun Chris', 'M', 'sai yin pun', '60103043', '2010-01-03', 1, 'this is chris tang'),
(15, 'denon', 'M', 'rich land', '35425412', '2010-01-03', 3, 'haha'),
(16, 'Eagle', 'F', 'hw312', '88459871', '2010-01-01', 2, 'this person is fat'),
(17, 'ellie', 'F', 'use', '12345690', '1990-05-08', 2, 'testing ellie'),
(18, 'Barry Ip', 'M', 'Estate', '12340192', '1987-06-12', 1, 'undertaken by 14'),
(19, 'katrina', 'F', 'kowloon', '12343532', '1975-11-04', 14, 'arrwerew'),
(20, 'Albert Yiu', 'M', 'Second Street, Hong Kong', '92038541', '2001-10-10', 1, 'he is albery yiu'),
(21, 'Emily Lo', 'F', 'HK', '92840325', '1989-06-07', 14, 'she is undertaken by PIC14'),
(22, 'Daisy', 'F', 'KW', '92028371', '1978-06-25', 1, 'undertaken by PIC1'),
(23, 'David Chan', 'M', 'TW', '67390123', '1996-11-27', 1, 'abeabe'),
(24, 'Evis', 'M', 'Hong Kong', '99081234', '2007-03-08', 1, 'baby'),
(25, 'Ivy Hiu', 'F', 'Causeway Bay', '96734521', '1987-04-07', 1, 'born in HKSAR	'),
(26, 'Zoo Yan', 'M', 'Wan Chai', '90873451', '2007-08-19', 1, 'Zoo yan'),
(27, 'Yuki Chan', 'F', 'hk', '72584910', '1900-01-01', 1, '');

-- --------------------------------------------------------

--
-- Table structure for table `privilege`
--

CREATE TABLE IF NOT EXISTS `privilege` (
  `Role` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `Read` tinyint(1) NOT NULL,
  `Write` tinyint(1) NOT NULL,
  `Add` tinyint(1) NOT NULL,
  PRIMARY KEY (`Role`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `privilege`
--

INSERT INTO `privilege` (`Role`, `Read`, `Write`, `Add`) VALUES
('doctor', 1, 1, 1),
('nurse', 1, 0, 0);

-- --------------------------------------------------------

--
-- Table structure for table `swapped_rec`
--

CREATE TABLE IF NOT EXISTS `swapped_rec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `pub_key` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mod` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `swapped_rec`
--


-- --------------------------------------------------------

--
-- Table structure for table `t-m_rec`
--

CREATE TABLE IF NOT EXISTS `t-m_rec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tid` int(11) NOT NULL,
  `mid` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=1 ;

--
-- Dumping data for table `t-m_rec`
--


-- --------------------------------------------------------

--
-- Table structure for table `temp_card`
--

CREATE TABLE IF NOT EXISTS `temp_card` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `pub_key` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mod` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `temp_card`
--


-- --------------------------------------------------------

--
-- Table structure for table `treatment`
--

CREATE TABLE IF NOT EXISTS `treatment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) NOT NULL,
  `pic` int(11) NOT NULL,
  `description` text COLLATE utf8_unicode_ci,
  `date_of_issue` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=17 ;

--
-- Dumping data for table `treatment`
--

INSERT INTO `treatment` (`id`, `pid`, `pic`, `description`, `date_of_issue`) VALUES
(1, 1, 1, 't1', '2010-01-08'),
(2, 2, 1, 'tqagsd', '2010-01-08'),
(3, 1, 1, 't2', '2010-01-08'),
(4, 3, 4, 'asqg', '2010-01-08'),
(5, 4, 1, 'ghfjg', '2010-01-08'),
(6, 5, 2, 'fdk', '2010-01-08'),
(7, 6, 4, 'dasgag', '2010-01-08'),
(8, 7, 3, '33123123a232131', '2010-01-08'),
(9, 8, 4, 'asdgdfh', '2010-01-08'),
(10, 9, 1, 'afdh', '2010-01-08'),
(11, 10, 1, 'T1', '2010-01-08'),
(12, 11, 2, 'fshd', '2010-01-08'),
(13, 4, 1, 'hihi', '2010-03-19'),
(14, 4, 1, 'he is gay', '2010-03-19'),
(15, 4, 1, 'he is gay	', '2010-03-22'),
(16, 4, 1, '', '2010-03-22');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `Role` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `pub_key` text COLLATE utf8_unicode_ci NOT NULL,
  `mod` text COLLATE utf8_unicode_ci NOT NULL,
  `pwd` text COLLATE utf8_unicode_ci NOT NULL,
  `RegDate` date NOT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=23 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`uid`, `Role`, `pub_key`, `mod`, `pwd`, `RegDate`, `username`) VALUES
(1, 'doctor', '10001', '9f02b441c4c3b3cab09ba38d43434c365ee9b2bb5293626c03c56b1f1d6212774a7c945d0716207488ff882636768d5463fe783d87421841640fa24b81abdaf9cef5bd16b83ccf5d2854de29164783eecb5ea77a83ef5dca8aa53c38926b44135b5bc7786e8491531481dbef20f49ef53786e1177d3157e5bd7e7ba43202e477', '098f6bcd4621d373cade4e832627b4f6', '2010-03-22', 'test'),
(13, 'doctor', '10001', 'a62321414826158119361e3d0cf769704de5509be334ba769ffd60678689d2bb82e1a6066bf3ff1971469c3e69352ac7643b8581471191764f9f746d88fa6688408a9455b40d73d5e05240dc1d036799488f37bee5a10a56b07d4af8fbc594ece9248911dd8a69a258108c97fb3cc5c2ceb4862ac6e566310dc54f701a9143ad', '778e63c8a1765befc06c88051114cbe4', '2010-03-01', 'barry'),
(21, 'nurse', '10001', '9a993e9cf5517e51144528a1976056d9aaad8b36c787125397227babd74e88e01ba59070946404ae7625b8a34521466277a7d09e82d5fe5c812002f3662f76790d1b7338afff5ee580f61ca1845492e9a97342297d55aac1e15a79f12e9ae3841d5ad681edb2f51a2a1b24883a74ab89d468fa930a173e5dbed505bbb661468d', '098f6bcd4621d373cade4e832627b4f6', '2010-03-22', 'test1'),
(14, 'doctor', '10001', '8249dbdc3430ca740191bb934b68e51144d01665c50bfbc577e028072d2d32c6122c4e3062c5126fa4481eed769912bad914ff6bf5f25d5564889aa73f954d71f9b44bf224699b2461bdf312dbc7c54f8679d15f5bc5992ee240a5ec8aee0096490e17eea1853ad1cdaf715f8dbe5f4b270139d5f755149504c36bac31ac97e7', '5bc6dbc0f9d713f383167abb8e7109a2', '2010-03-01', 'chun');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
