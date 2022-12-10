# bdkqpr0x
Batch templates consisting of springboot, mybatis, lombok, and jsch.<br>
<div align=center> 
  <img src="https://img.shields.io/badge/oracle-F80000?style=flat-square&logo=oracle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=flat-square&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/java-007396?style=flat-square&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&logoColor=white">
  <br>
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

## Docker
It can be used as docker build, but since it is a development environment, it is simply put in an openjdk container for speed.
```yml
docker-compose.yml example
version: "3.8"
services:
  auzj6fml:
    image: openjdk:17-jdk
    container_name: auzj6fml
    user: 0:0
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

* * *

## How to use
The batch program is distributed along with the container of auzj6fml, which is in charge of the screen, and is called from the host's shell as needed.
```sh
cd /data/auzj6fml && sudo docker-compose rm -f -s && sudo docker-compose up -d && sudo docker exec -it auzj6fml date
docker exec -it auzj6fml java -jar /batch.jar --job.name=b01 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
docker exec -it auzj6fml java -jar /batch.jar --job.name=b02 chunkSize=500 requestDate=$(date "+%Y-%m-%d")
docker exec -it auzj6fml java -jar /batch.jar --job.name=b03 chunkSize=1000 requestDate=$(date "+%Y-%m-%d")
docker exec -it auzj6fml java -jar /batch.jar --job.name=b04 chunkSize=10000 requestDate=$(date "+%Y-%m-%d")
```

* * *

## Contact and license
<a href="mailto:xqbty8po-dntco43u@yahoo.com" target="_blank"><img src="https://img.shields.io/badge/yahoo!-6001D2?style=flat-square&logo=yahoo!&logoColor=white"/></a><br>
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
