#!/bin/bash
sudo systemctl start springserver
nohup java -jar /home/ubuntu/pcy-car-reservation-site/build/libs/reservationcar-0.0.1-SNAPSHOT-plain.war 1>/dev/null 2>&1 &