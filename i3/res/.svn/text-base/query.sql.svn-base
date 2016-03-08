select S.Nick, A.* from NBDR_V3.A_SHOP_HOUR A, SYS_USER S
  where A.UserID = S.UserID and Time = '2010-11-03 00:00:00' order by A.id;
select S.Nick, A.* from NBDR_V3.A_INDEX_HOUR A, SYS_USER S 
  where A.UserID = S.UserID and Time = '2010-11-11 00:00:00' order by A.time;

select * from NBDR_V3.A_SHOP_HOUR A where Time = '2010-11-11 00:00:00';
select * from NBDR_V3.A_INDEX_HOUR A where Time = '2010-11-11 00:00:00';

SELECT * FROM NBDR_V3.SYS_USER S order by createdate desc;

select * 
  from (select userid from A_ITEM_DAY a group by userid) as u
  right join SYS_USER s on u.userid = s.userid
  where u.userid is null
  order by CreateDate;



SELECT * FROM `NBDR_V3`.`A_INDEX_DAY` where date = '2010-11-11 00:00:00' and userid = 10158036;
SELECT userid, count(*) as c FROM `NBDR_V3`.`A_INDEX_DAY` where date = '2010-11-11 00:00:00' group by userid having c = 1;

SELECT userid, count(*) as c FROM `NBDR_V3`.`A_INDEX_DAY` where date = '2010-11-11 00:00:00' group by userid having c > 1;
SELECT userid, province, count(*) as c FROM `NBDR_V3`.`A_INDEX_PROVINCE_DAY` where date = '2010-11-11 00:00:00' group by userid, province having c > 1;
SELECT userid, from_id, count(*) as c  FROM `NBDR_V3`.`A_INDEX_FROM_DAY` where date = '2010-11-11 00:00:00' group by userid, from_id having c > 1;
SELECT userid, categoryid, count(*) as c FROM `NBDR_V3`.`A_CATEGORY_DAY` where date = '2010-11-11 00:00:00' group by userid, categoryid having c > 1;
SELECT userid, num_iid, count(*) as c FROM `NBDR_V3`.`A_ITEM_DAY` where date = '2010-11-11 00:00:00' group by userid, num_iid having c > 1;
SELECT userid, num_iid, from_id, count(*) as c FROM `NBDR_V3`.`A_ITEM_FROM_DAY` where date = '2010-11-11 00:00:00' group by  userid, num_iid, from_id having c > 1;
SELECT userid, num_iid, keyword, count(*) as c FROM `NBDR_V3`.`A_ITEM_KEYWORD_DAY` where date = '2010-11-11 00:00:00' group by userid, num_iid, keyword having c > 1;
SELECT userid, category_url, count(*) as c FROM `NBDR_V3`.`A_USER_CATEGORY_DAY` where date = '2010-11-11 00:00:00' group by userid, category_url having c > 1;
SELECT userid, count(*) as c FROM `NBDR_V3`.`A_SHOP_DAY` where date = '2010-11-11 00:00:00' group by userid having c > 1;
SELECT userid, from_id, count(*) as c FROM `NBDR_V3`.`A_SHOP_FROM_DAY` where date = '2010-11-11 00:00:00' group by userid, from_id having c > 1;
SELECT userid, province, count(*) as c FROM `NBDR_V3`.`A_SHOP_PROVINCE_DAY` where date = '2010-11-11 00:00:00' group by userid, province having c > 1;
SELECT userid, keyword, count(*) as c FROM `NBDR_V3`.`A_TAOBAO_KEYWORD_DAY` where date = '2010-11-11 00:00:00' group by userid, keyword having c > 1;
SELECT userid, keyword, count(*) as c FROM `NBDR_V3`.`A_KEYWORD_DAY` where date = '2010-11-11 00:00:00' group by userid, keyword having c > 1;

SELECT * FROM `NBDR_V3`.`A_INDEX_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_INDEX_PROVINCE_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_INDEX_FROM_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_CATEGORY_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_ITEM_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_ITEM_FROM_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_ITEM_KEYWORD_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_USER_CATEGORY_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_SHOP_DAY` where date = '2010-11-11 00:00:00' order by userid limit 100;
SELECT * FROM `NBDR_V3`.`A_SHOP_FROM_DAY` where date = '2010-11-11 00:00:00' limit 100;
SELECT * FROM `NBDR_V3`.`A_SHOP_PROVINCE_DAY` where date = '2010-11-11 00:00:00' order by userid, province limit 100;
SELECT * FROM `NBDR_V3`.`A_TAOBAO_KEYWORD_DAY` where date = '2010-11-11 00:00:00' order by userid, keyword limit 100;
SELECT * FROM `NBDR_V3`.`A_KEYWORD_DAY` where date = '2010-11-11 00:00:00' order by userid, keyword limit 100;

delete FROM `NBDR_V3`.`A_INDEX_DAY`where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_INDEX_PROVINCE_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_INDEX_FROM_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_CATEGORY_DAY`where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_ITEM_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_ITEM_FROM_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_ITEM_KEYWORD_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_USER_CATEGORY_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_SHOP_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_SHOP_FROM_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_SHOP_PROVINCE_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_TAOBAO_KEYWORD_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_KEYWORD_DAY` where date = '2010-11-11 00:00:00';



SELECT * FROM `NBDR_V3`.`A_PATH_FROM_DAY` where date = '2010-11-11 00:00:00' order by userid, from_id, pv desc;
SELECT * FROM `NBDR_V3`.`A_PATH_DAY_RELATION` where view_id in (
    SELECT view_id FROM `NBDR_V3`.`A_PATH_DAY` where date = '2010-11-11 00:00:00'
  );
SELECT * FROM `NBDR_V3`.`A_PATH_DAY` where date = '2010-11-11 00:00:00';


delete FROM `NBDR_V3`.`A_PATH_FROM_DAY` where date = '2010-11-11 00:00:00';
delete FROM `NBDR_V3`.`A_PATH_DAY_RELATION` where view_id in (
    SELECT view_id FROM `NBDR_V3`.`A_PATH_DAY` where date = '2010-11-11 00:00:00'
  );
delete FROM `NBDR_V3`.`A_PATH_DAY` where date = '2010-11-11 00:00:00';


SELECT * FROM A_PATH_DAY a right join A_PATH_DAY_RELATION b on a.view_id = b.view_id
  where a.userid = 20718739 order by date, b.from_id, b.pv desc;



SELECT * FROM A_INDEX_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_INDEX_FROM_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_INDEX_PROVINCE_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_ITEM_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_ITEM_FROM_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_ITEM_KEYWORD_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_CATEGORY_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_USER_CATEGORY_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_SHOP_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_SHOP_FROM_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_SHOP_PROVINCE_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_KEYWORD_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM A_TAOBAO_KEYWORD_DAY A where date = '2010-11-11 00:00:00';

delete FROM A_INDEX_DAY where date = '2010-11-11 00:00:00';
delete FROM A_INDEX_FROM_DAY where date = '2010-11-11 00:00:00';
delete FROM A_INDEX_PROVINCE_DAY where date = '2010-11-11 00:00:00';
delete FROM A_ITEM_DAY where date = '2010-11-11 00:00:00';
delete FROM A_ITEM_FROM_DAY where date = '2010-11-11 00:00:00';
delete FROM A_ITEM_KEYWORD_DAY where date = '2010-11-11 00:00:00';
delete FROM A_KEYWORD_DAY where date = '2010-11-11 00:00:00';
delete FROM A_SHOP_DAY where date = '2010-11-11 00:00:00';
delete FROM A_SHOP_FROM_DAY where date = '2010-11-11 00:00:00';
delete FROM A_SHOP_PROVINCE_DAY where date = '2010-11-11 00:00:00';
delete FROM A_USER_CATEGORY_DAY where date = '2010-11-11 00:00:00';
delete FROM A_TAOBAO_KEYWORD_DAY where date = '2010-11-11 00:00:00';
delete FROM A_CATEGORY_DAY where date = '2010-11-11 00:00:00';


SELECT * FROM NBDR_MARKET.A_CHANNEL_TBK_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM NBDR_MARKET.A_CHANNEL_ZTC_DAY A where date = '2010-11-11 00:00:00';
SELECT * FROM NBDR_MARKET.A_CHANNEL_ZTC_CAT_DAY A where date = '2010-11-11 00:00:00';
 and (ad_order_amount > 0 or ad_order_quantity > 0 or order_by_ad_hits > 0 or order_number > 0 or pay_quantity > 0 or pay_count > 0)

delete FROM NBDR_MARKET.A_CHANNEL_TBK_DAY where date = '2010-11-11 00:00:00';
delete FROM NBDR_MARKET.A_CHANNEL_ZTC_DAY where date = '2010-11-11 00:00:00';
delete FROM NBDR_MARKET.A_CHANNEL_ZTC_CAT_DAY where date = '2010-11-11 00:00:00';

SELECT U.nick, A.* FROM NBDR_MARKET.A_CHANNEL_TBK_DAY A, NBDR_V3.SYS_USER U
 where date = '2010-11-11 00:00:00' and A.userid = U.userid
 and (ad_order_amount > 0 or ad_order_quantity > 0 or order_by_ad_hits > 0 or order_number > 0 or pay_quantity > 0 or pay_count > 0);
SELECT U.nick, A.* FROM NBDR_MARKET.A_CHANNEL_ZTC_DAY A, NBDR_V3.SYS_USER U
 where date = '2010-11-11 00:00:00' and A.userid = U.userid
 and (ad_order_amount > 0 or ad_order_quantity > 0 or order_by_ad_hits > 0 or order_number > 0 or pay_quantity > 0 or pay_count > 0);


select * FROM A_SHOP_HOUR where time < '2010-11-11 00:00:00' and  time > '2010-11-11 00:00:00';
select * FROM A_INDEX_HOUR where time < '2010-11-11 00:00:00' and  time > '2010-11-11 00:00:00';

delete FROM A_SHOP_HOUR where time < '2010-11-11 00:00:00' and  time > '2010-11-11 00:00:00';
delete FROM A_INDEX_HOUR where time < '2010-11-11 00:00:00' and  time > '2010-11-11 00:00:00';



SELECT a.*, b.nick FROM A_CHANNEL_DAY a, NBDR_V3.SYS_USER b where a.userid = b.userid;

insert into A_CHANNEL_DAY(date, userid, channel_id, pv, uv, avgtime, avgpage, ad_order_amount, ad_order_quantity, order_by_ad_hits, order_number, pay_quantity, pay_count)

SELECT date, userid, '1', sum(PV), sum(UV), round(sum(pv*avgtime)/sum(pv)), sum(pv)/sum(uv), sum(ad_order_amount), sum(ad_order_quantity), sum(order_by_ad_hits), sum(order_number), sum(pay_quantity), sum(pay_count)
  FROM (
    SELECT date, userid, pv, uv, avgtime, avgpage, ad_order_amount, ad_order_quantity, order_by_ad_hits, order_number, pay_quantity, pay_count 
      FROM A_CHANNEL_ZTC_DAY where date='2010-11-11 00:00:00' 
    union all
    SELECT date, userid, pv, uv, avgtime, avgpage, ad_order_amount, ad_order_quantity, order_by_ad_hits, order_number, pay_quantity, pay_count 
      FROM A_CHANNEL_ZTC_CAT_DAY where date='2010-11-11 00:00:00'
  ) A group by userid, date order by userid, date;

SELECT date, userid, '2', sum(PV), sum(UV), round(sum(pv*avgtime)/sum(pv)), sum(pv)/sum(uv), sum(ad_order_amount), sum(ad_order_quantity), sum(order_by_ad_hits), sum(order_number), sum(pay_quantity), sum(pay_count)
  FROM NBDR_MARKET.A_CHANNEL_TBK_DAY A where date='2010-11-11 00:00:00'
  group by userid, date order by userid, date;
