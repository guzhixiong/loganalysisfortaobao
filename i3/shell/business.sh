#!/bin/sh

hadoop_home='/opt/hadoop-0.20.2'
hadoop=${hadoop_home}/bin/hadoop

jar=${hadoop_home}/i-0.1.jar
cp=${hadoop_home}/lib
cp=/root:${hadoop_home}/hadoop-0.20.2-core.jar:$cp/commons-lang-2.1.jar:$cp/commons-logging-1.0.4.jar:$cp/commons-io-1.4.jar:$cp/log4j-1.2.15.jar:$cp/commons-codec-1.3.jar:$cp/je-4.0.103.jar:$cp/mysql-connector-java-5.1.13.jar:$cp/jdom-1.0.jar:$cp/xalan-2.6.0.jar:$cp/jaxen-1.1.1.jar:$cp/xml-apis-1.3.04.jar:$cp/xercesImpl-2.9.1.jar:${jar}

date=`date '+%Y%m%d' -d '1 days ago'`
argc=$#
if [ $argc -eq 1 ]
then
  date=$1
fi

local=/opt/i/stati/${date}/
out=data/${date}

(rm -rf ${out}_page_pv_uv; ${hadoop} fs -get ${out}_page_pv_uv ${local}${date}_page_pv_uv; java -cp $cp i3.business.UserItemUv ${date})
(rm -rf ${out}_shop_pv_uv; ${hadoop} fs -get ${out}_shop_pv_uv ${local}${date}_shop_pv_uv; java -cp $cp i3.business.UserUv ${date})
(rm -rf ${out}_shop_area_pv_uv; ${hadoop} fs -get ${out}_shop_area_pv_uv ${local}${date}_shop_area_pv_uv; java -cp $cp i3.business.UserProvinceUv ${date})

#time=`date '+%Y%m%d%H%M%S'`
#touch test.log \
# && echo "lock $time " >> test.log \
# && java -cp $cp App $time \
# && echo "unlock $time" >> test.log
