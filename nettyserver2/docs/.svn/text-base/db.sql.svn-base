CREATE TABLE [dbo].[A_Track](
	[T_ID] [bigint] IDENTITY(1,1) NOT FOR REPLICATION NOT NULL,
	[T_UserID] [nvarchar](100) NOT NULL CONSTRAINT [DF_A_Track_T_UserID]  DEFAULT ((0)),
	[T_UV] [nvarchar](100) NOT NULL,
	[T_VisitUrl] [nvarchar](2000) NOT NULL,
	[T_ItemIID] [nvarchar](100) NULL,
	[T_VisitTime] [datetime] NOT NULL,
	[T_IP] [nvarchar](50) NULL,
	[T_Province] [nvarchar](100) NULL,
	[T_City] [nvarchar](100) NULL,
	[T_Address] [nvarchar](500) NULL,
	[T_Title] [nvarchar](50) NULL,
 CONSTRAINT [PK_A_Track] PRIMARY KEY CLUSTERED 
(

CREATE TABLE   `ip` (
  `id` bigint(20) primary key,
  `start_ip` bigint(20) DEFAULT NULL,
  `end_ip` bigint(20) DEFAULT NULL,
  `nation` varchar(50) ,
  `province` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `full_address` varchar(200) DEFAULT NULL,
  `isp` varchar(200) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL, 
)

UPDATE dc.SYS_IP, NBDR.SYS_PROVINCE SET dc.SYS_IP.PROVINCE_ID = NBDR.SYS_PROVINCE.ID  WHERE  dc.SYS_IP.province =NBDR.SYS_PROVINCE.Province;
Query OK, 20412 rows affected (1.25 sec)
Rows matched: 20412  Changed: 20412  Warnings: 0

mysql> UPDATE dc.SYS_IP, NBDR.SYS_CITY SET dc.SYS_IP.CITY_ID = NBDR.SYS_CITY.ID  WHERE  dc.SYS_IP.CITY =NBDR.SYS_CITY.CITY;


UPDATE dc.SYS_IP, NBDR.SYS_PROVINCE SET dc.SYS_IP.PROVINCE_ID = NBDR.SYS_PROVINCE.ID  WHERE  dc.SYS_IP.province =NBDR.SYS_PROVINCE.Province;
UPDATE dc.SYS_IP, NBDR.SYS_CITY SET dc.SYS_IP.CITY_ID = NBDR.SYS_CITY.ID  WHERE  dc.SYS_IP.CITY =NBDR.SYS_CITY.CITY;

select * from test_info into outfile '/tmp/test.csv' fields terminated by ',' optionally enclosed by '"' escaped by '"' lines terminated by '\r\n';  

load data infile 'ip-db.txt' into table SYS_IP character set utf8  fields terminated by ','  optionally enclosed by '"' escaped by '"';
UPDATE NBDR.SYS_IP, NBDR.SYS_PROVINCE SET NBDR.SYS_IP.PROVINCE_ID = NBDR.SYS_PROVINCE.ID  WHERE  NBDR.SYS_IP.province =NBDR.SYS_PROVINCE.Province;
UPDATE NBDR.SYS_IP, NBDR.SYS_CITY SET NBDR.SYS_IP.CITY_ID = NBDR.SYS_CITY.ID  WHERE  NBDR.SYS_IP.CITY =NBDR.SYS_CITY.CITY;