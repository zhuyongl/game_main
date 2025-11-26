-- 用户表
CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `email` varchar(100) NOT NULL UNIQUE,
  `password` varchar(100) NOT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `userType` enum('local', 'wechat') NOT NULL DEFAULT 'local',
  `wechatOpenId` varchar(100) DEFAULT NULL UNIQUE,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_email` (`email`),
  INDEX `idx_wechat_openid` (`wechatOpenId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 留言表
CREATE TABLE `Messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `content` text NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`userId`) REFERENCES `Users`(`id`) ON DELETE CASCADE,
  INDEX `idx_user_id` (`userId`),
  INDEX `idx_created_at` (`createdAt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;