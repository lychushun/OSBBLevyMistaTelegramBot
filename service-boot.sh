#!/bin/sh
     CUSTOM_SERVICE_NAME=OSBBLevyMista
     JAR_FILE_PATH=/home/ec2-user/OSBBLevyMista45/app/OSBBLevyMista.jar
     PID_NAME_PATH=/tmp/OSBBLevyMista-pid
     case $1 in
     start)
        echo "Starting $CUSTOM_SERVICE_NAME ..."
        echo "Starting $PID_NAME_PATH ..."
        if [ ! -f $PID_NAME_PATH ]; then
           nohup java -Dfile.encoding=UTF8 -jar $JAR_FILE_PATH /tmp 2>> /dev/null >>/dev/null & echo $! > $PID_NAME_PATH
           echo "$CUSTOM_SERVICE_NAME started ..."
        else
           echo "$CUSTOM_SERVICE_NAME is already running ..."
        fi
        ;;
     stop)

     if [ -f $PID_NAME_PATH ]; then
        PID=$(cat $PID_NAME_PATH);
           echo "$CUSTOM_SERVICE_NAME stopping ..."
        kill $PID;
        echo "$CUSTOM_SERVICE_NAME stopped ..."
        rm $PID_NAME_PATH
     else
        echo "$CUSTOM_SERVICE_NAME is not running ..."
     fi
     ;;

     restart)
     if [ -f $PID_NAME_PATH ]; then
        PID=$(cat $PID_NAME_PATH);
        echo "$CUSTOM_SERVICE_NAME stopping ...";
        kill $PID;
        echo "$CUSTOM_SERVICE_NAME stopped ...";
        rm $PID_NAME_PATH
        echo "$CUSTOM_SERVICE_NAME starting ..."
        nohup java -jar -Dfile.encoding=UTF8 $JAR_FILE_PATH /tmp 2>> /dev/null >> /dev/null & echo $! > $PID_NAME_PATH
        echo "$CUSTOM_SERVICE_NAME started ..."
     else
        echo "$CUSTOM_SERVICE_NAME is not running ..."
     fi
     ;;
     esac
