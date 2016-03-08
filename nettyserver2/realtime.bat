cd bin
java  -Xms512m -Xmx1024m -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Dcom.sun.management.jmxremote=netty -Dcom.sun.management.jmxremote.port=9903 -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.authenticate=false  -Djava.ext.dirs=../lib com/wangcheng/udp/UDPServer
cd ..
pause