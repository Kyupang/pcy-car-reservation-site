#!/bin/bash
sudo systemctl stop springserver
PID=$(ps -ef | grep -v grep | grep 'java -jar /home/ubuntu/pcy-car-reservation-site/build/libs/reservationcar-0.0.1-SNAPSHOT-plain.war' | awk '{print $2}')
if [ -n "$PID" ]; then
    kill -9 $PID
    sleep 10
fi