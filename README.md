# bdkqpr0x
Batch templates consisting of springboot, mybatis, lombok, and jsch.<br>
<div align=center> 
  <img src="https://img.shields.io/badge/oracle-F80000?style=flat-square&logo=oracle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/sonarlint-CB2029?style=flat-square&logo=sonarlint&logoColor=white">
  <img src="https://img.shields.io/badge/github-181717?style=flat-square&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/jenkins-D24939?style=flat-square&logo=jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=flat-square&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/oraclecloud-F80000?style=flat-square&logo=icloud&logoColor=white">
  <img src="https://img.shields.io/badge/rocky linux-10B981?style=flat-square&logo=rocky linux&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white">
  <br>
</div>

* * *

## Introduction
The batch job is divided into 4 parts and each role is as follows.
+ job b01: Batch program to transfer us-500.csv format.<br>
  MySQL to Oracle, MySQL to File to Oracle
+ job b02: Batch program to transfer us-500.csv format.<br>
  Oracle to MySQL, Oracle to File to MySQL
+ job b03: Batch program to transfer music_metadata.csv (CLOB) format.<br>
  Oracle to Oracle, Oracle to File to Oracle
+ job b04: rsyslog Syslog.SystemEvents. (10M rows)<br>
  MySQL to Oracle, MySQL to File to Oracle

* * *

## How to use
The batch program is distributed along with the container of auzj6fml, which is in charge of the screen, and is called from the host's shell as needed.
```sh
sudo cd /data/auzj6fml && sudo docker-compose rm -f -s && sudo docker-compose up -d && sudo docker exec -it auzj6fml date
sudo docker exec -it auzj6fml java -jar /batch.jar --job.name=b01 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /batch.jar --job.name=b02 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /batch.jar --job.name=b03 chunkSize=1000 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /batch.jar --job.name=b04 chunkSize=10000 requestDate=$(date "+%Y-%m-%d")
```

Called from cron to notify the execution result to telegram.
```sh
/root/sh/bdkqpr0x.sh example
#!/bin/bash
log_file=/tmp/bdkqpr0x.log
log_file_1=/tmp/bdkqpr0x_telegram_1.log
{
  systemctl stop rsyslog
  docker exec -it auzj6fml java -jar /batch.jar --job.name=b04 chunkSize=10000 requestDate=$(date "+%Y-%m-%d")
  systemctl start rsyslog
} > $log_file
{
  echo "$0"
  grep 'count=' $log_file
} > $log_file_1
log=$(< $log_file_1 tail -c 4096)
if [ -z "$log" ]; then
  exit 1
fi
curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```
> Note: For crontab root permission.

* * *

## Docker
It can be used as docker build, but since it is a development environment, it is simply put in an openjdk container for speed.
```yml
docker-compose.yml example
version: "3.8"
services:
  auzj6fml:
    image: openjdk:17-jdk
    container_name: auzj6fml
    user: 1000:1000
    networks:
      gvp6nx1a:
        ipv4_address: 172.18.0.102
    expose:
      - 80/tcp
    environment:
      - TZ=Asia/Seoul
      - JAVA_OPTS=-Xmx1g
    volumes:
      - /data/auzj6fml/dockerfile/build/libs/auzj6fml-0.0.1-SNAPSHOT.jar:/app.jar:rw
      - /data/auzj6fml/dockerfile/build/libs/bdkqpr0x-0.0.1-SNAPSHOT.jar:/batch.jar:rw
      - /data/auzj6fml/config:/config:rw
      - /data/auzj6fml/data:/data:rw
      - /data/auzj6fml/log:/log:rw
    command: java -jar /app.jar
    restart: always
networks:
  gvp6nx1a:
    external: true
```
> Note: Configure to store oracle wallet and private key for ssh tunneling in `config` directory. And keep the in and out files in the `data` directory.

## Jenkins
Shell script for pre build. Runs in a jenkins docker container.
```sh
/data/jenkins/data/sh/pre_build.sh example
#!/bin/bash
log_file=/tmp/pre_build.log
{
  echo "$0"
  echo "$1 #$2"
  mkdir -p var/jenkins_home/workspace/bdkqpr0x/src/main/resources
  cp -f -v /var/jenkins_home/config/profiles/application-security.properties /var/jenkins_home/workspace/bdkqpr0x/src/main/resources
} > $log_file
log=$(< $log_file tail -c 4096)
if [ -z "$log" ]; then
  exit 1
fi
/usr/bin/curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```

Shell script for post build. Runs in the target host to be deployed.
```sh
/data/jenkins/data/sh/post_build.sh example
#!/bin/bash
log_file=/tmp/post_build.log
{
  echo "$0"
  echo "$1 #$2"
  ls -alh /data/auzj6fml/dockerfile/build/libs/*.jar
  cd /data/auzj6fml || exit 1
  sudo docker-compose rm -f -s
  sudo docker-compose up -d
} > $log_file
log=$(< $log_file tail -c 4096)
if [ -z "$log" ]; then
  exit 1
fi
curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```

* * *

## Contact and license
<a href="mailto:xqbty8po-dntco43u@yahoo.com" target="_blank"><img src="https://img.shields.io/badge/yahoo!-6001D2?style=flat-square&logo=yahoo!&logoColor=white"/></a><br>
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
