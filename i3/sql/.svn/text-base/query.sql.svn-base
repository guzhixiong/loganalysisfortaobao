SELECT t.TB_UserID, t.TB_Nick, t.TB_Alipay_Account, t.TB_Alipay_No, t.TB_Seller_Level, s.CreateDate
FROM TB_USER t, SYS_USER s 
WHERE t.TB_UserID=s.UserID AND s.CreateDate BETWEEN '20110223' AND '20110224'
ORDER BY s.CreateDate DESC

SELECT u.Nick, t.* FROM A_TEMPLATE_DAY t, SYS_USER u WHERE t.USERID=u.UserID ORDER BY t.DATE DESC LIMIT 100
