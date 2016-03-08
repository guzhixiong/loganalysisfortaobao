cd bin
java -server  -XX:CompileThreshold=10 -XX:+UseParallelGC -XX:ParallelGCThreads=20 -XX:+AggressiveOpts -XX:+UseFastAccessorMethods -Dcom.sun.management.jmxremote=netty -Dcom.sun.management.jmxremote.port=8903 -Dcom.sun.management.jmxremote.ssl=false  -Dcom.sun.management.jmxremote.authenticate=false  -Djava.ext.dirs=../lib com/wangcheng/http/NettyServer
cd ..

