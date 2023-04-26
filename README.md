# bdkqpr0x
<div align=left> 
  <img src="https://img.shields.io/badge/oracle-F80000?style=flat-square&logo=oracle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/sonarlint-CB2029?style=flat-square&logo=sonarlint&logoColor=white">
  <img src="https://img.shields.io/badge/jenkins-D24939?style=flat-square&logo=jenkins&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=flat-square&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/docker-2496ED?style=flat-square&logo=docker&logoColor=white">
  <img src="https://img.shields.io/badge/RHEL-EE0000?style=flat-square&logo=red hat&logoColor=white">
  <br>
  <br>
</div>

Batch templates consisting of springboot, mybatis, lombok, and jsch.<br>
The batch job is divided into 4 parts and each role is as follows.

|Jobs|Contents|Marks|
|:---|:---|:---|
|b01|Transfer us-500.csv format|MySQL to Oracle, MySQL to File to Oracle|
|b02|Transfer us-500.csv format|Oracle to MySQL, Oracle to File to MySQL|
|b03|Transfer music_metadata.csv format|CLOB, Oracle to Oracle, Oracle to File to Oracle|
|b04|Transfer rsyslog Syslog.SystemEvents|600M+ Rows, MySQL to Oracle, MySQL to File to Oracle|

* * *

### Configuration
It can be used as docker build, but since it is a development environment, it is simply put in an openjdk container for speed.

`docker-compose.yml example`
```yml
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
      - /data/auzj6fml/dockerfile/build/libs:/usr/share/java:rw
      - /data/auzj6fml/config:/config:rw
      - /data/auzj6fml/data:/data:rw
      - /data/auzj6fml/log:/log:rw
    command: java -jar /usr/share/java/auzj6fml-0.0.1-SNAPSHOT.jar
    restart: always
networks:
  gvp6nx1a:
    external: true
```
> Note: Configure to store oracle wallet and private key for ssh tunneling in config directory. And keep the in and out files in the data directory.

Shell script for Jenkins pre build. Runs in a jenkins docker container.
```sh
#!/bin/bash
log_file=/tmp/pre_build.log
{
  echo "$0"
  echo "$1 #$2"
  mkdir -p var/jenkins_home/workspace/auzj6fml/src/main/resources
  mkdir -p var/jenkins_home/workspace/bdkqpr0x/src/main/resources
  cp -f -v /var/jenkins_home/config/profiles/auzj6fml/application-security.properties /var/jenkins_home/workspace/auzj6fml/src/main/resources
  cp -f -v /var/jenkins_home/config/profiles/bdkqpr0x/application-security.properties /var/jenkins_home/workspace/bdkqpr0x/src/main/resources
} > $log_file
log=$(< $log_file tail -c 4096)
if [ -z "$log" ]; then
  exit 1
fi
/usr/bin/curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```

Shell script for Jenkins post build. Runs in the target host to be deployed.
```sh
#!/bin/bash
log_file=/tmp/post_build.log
{
  hostname -f
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
/usr/bin/curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```

* * *

### How to use
The batch program is distributed along with the container of auzj6fml, which is in charge of the UI, and is called from the host's shell as needed.
```sh
sudo cd /data/auzj6fml && sudo docker-compose rm -f -s && sudo docker-compose up -d && sudo docker exec -it auzj6fml date
sudo docker exec -it auzj6fml java -jar /bdkqpr0x-0.0.1-SNAPSHOT.jar --job.name=b01 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /bdkqpr0x-0.0.1-SNAPSHOT.jar --job.name=b02 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /bdkqpr0x-0.0.1-SNAPSHOT.jar --job.name=b03 chunkSize=1000 requestDate=$(date "+%Y-%m-%d")
sudo docker exec -it auzj6fml java -jar /bdkqpr0x-0.0.1-SNAPSHOT.jar --job.name=b04 chunkSize=10000 requestDate=$(date "+%Y-%m-%d")
```

Called from cron to notify the execution result to telegram.
```sh
#!/bin/bash
log_file=/tmp/bdkqpr0x.log
log_file_1=/tmp/bdkqpr0x_telegram_1.log
{
  systemctl stop rsyslog
  docker exec -i auzj6fml java -jar /usr/share/java/bdkqpr0x.jar --job.name=b04 chunkSize=10000 requestDate=$(date "+%Y-%m-%d")
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
/usr/bin/curl --data-urlencode text="$log" https://api.telegram.org/bot**********************************************/sendMessage?chat_id=**********
```

* * *

### Contact and license

<a href="mailto:xqbty8po-dntco43u@yahoo.com" target="_blank"><img src="https://img.shields.io/badge/yahoo!-6001D2?style=flat-square&logo=yahoo!&logoColor=white"/></a>
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

