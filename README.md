# bdkqpr0x

## Introduction
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <br>
</div>

Batch templates consisting of springboot, mybatis, lombok, and jsch.
The batch job is divided into 4 parts and each role is as follows.
1. job b01: Batch program to transfer us-500.csv format.
    MySQL to Oracle, MySQL to File to Oracle
2. job b02: Batch program to transfer us-500.csv format
    Oracle to MySQL, Oracle to File to MySQL
3. job b03: Batch program to transfer music_metadata.csv (CLOB) format
    Oracle to Oracle, Oracle to File to Oracle
4. job b04: rsyslog Syslog.SystemEvents (10M rows)
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

## License
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
