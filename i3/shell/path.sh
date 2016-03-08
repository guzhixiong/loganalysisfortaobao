#!/bin/sh

hadoop_home='/opt/hadoop/hadoop-0.20.2'
hadoop=${hadoop_home}/bin/hadoop

date=`date '+%Y%m%d' -d '1 days ago'`
argc=$#
if [ $argc -eq 1 ]
then
  date=$1
fi
#echo "$date $h"

netty=2
in=''
for hour in `seq 0 23`
do
  leftpad=$((2 - `expr length $hour`))
  while [ $leftpad -gt 0 ]
  do
    hour='0'$hour
    leftpad=$(($leftpad - 1))
  done
  #echo $hour

  in=$in'data/'$date$hour'_a'
  if [ $hour -lt 23 ]
  then
    in=$in,
  fi
done
#echo $in

logs=`${hadoop} fs -ls data/ | grep ${date}.._a | wc -l`
if [ $logs -lt 24 ]
then 
  echo 'log not ready'
  exit -1
fi

jar=${hadoop_home}/i3-0.1.jar
cp=${hadoop_home}/lib
cp=${hadoop_home}/log4j.properties:$cp/commons-lang-2.1.jar:$cp/commons-logging-1.0.4.jar:$cp/commons-io-1.4.jar:$cp/log4j-1.2.15.jar:$cp/commons-codec-1.3.jar:$cp/je-4.0.103.jar:$cp/mysql-connector-java-5.1.13.jar:$cp/xml-apis-1.3.04.jar:$cp/xercesImpl-2.9.1.jar:${jar}

echo ${date}' start'

hadoop_jar="${hadoop} jar ${jar}"
out=data/${date}

#echo ${in}

${hadoop_jar} i.path2.PathPrev ${in} ${out} \
 && ${hadoop_jar} i.path2.PathFrom ${out} \
 && ${hadoop_jar} i.path2.PathFromTop ${out} \
 && ${hadoop_jar} i.path2.Path1 ${out} \
 && ${hadoop_jar} i.path2.Path1Top ${out} \
 && ${hadoop_jar} i.path2.Path2 ${out} \
 && ${hadoop_jar} i.path2.Path2Top ${out} \
 && echo ' successful'

# data/${date}23_a
#
#

