#!/bin/bash

echo "Starting of Server"
/home/jinh574/hive/bin/hive --service hiveserver2 > /dev/null 2>&1
/home/jinh574/hive/bin/hive --service metastore > /dev/null 2>&1
#java -jar /home/jinh574/CollectRssData-1.0-SNAPSHOT.jar > /dev/null 2>&1 &
echo "Finish script"
