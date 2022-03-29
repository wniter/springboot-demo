package com.example.springboot.graylog;

//public class statement {
//}
/**
 环境准备
 编写 graylog 的 docker-compose 启动文件
 version: '2'
 services:
 # MongoDB: https://hub.docker.com/_/mongo/
 mongodb:
 image: mongo:3
 # Elasticsearch: https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docker.html
 elasticsearch:
 image: docker.elastic.co/elasticsearch/elasticsearch-oss:6.6.1
 environment:
 - http.host=0.0.0.0
 - transport.host=localhost
 - network.host=0.0.0.0
 - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
 ulimits:
 memlock:
 soft: -1
 hard: -1
 mem_limit: 1g
 # Graylog: https://hub.docker.com/r/graylog/graylog/
 graylog:
 image: graylog/graylog:3.0
 environment:
 # 加密盐值，不设置，graylog会启动失败
 # 该字段最少需要16个字符
 - GRAYLOG_PASSWORD_SECRET=somepasswordpepper
 # 设置用户名
 - GRAYLOG_ROOT_USERNAME=admin
 # 设置密码，此为密码进过SHA256加密后的字符串
 # 加密方式，执行 echo -n "Enter Password: " && head -1 </dev/stdin | tr -d '\n' | sha256sum | cut -d" " -f1
 - GRAYLOG_ROOT_PASSWORD_SHA2=8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
 - GRAYLOG_HTTP_EXTERNAL_URI=http://127.0.0.1:9000/
 # 设置时区
 - GRAYLOG_ROOT_TIMEZONE=Asia/Shanghai
 links:
 - mongodb:mongo
 - elasticsearch
 depends_on:
 - mongodb
 - elasticsearch
 ports:
 # Graylog web interface and REST API
 - 9000:9000
 # Syslog TCP
 - 1514:1514
 # Syslog UDP
 - 1514:1514/udp
 # GELF TCP
 - 12201:12201
 # GELF UDP
 - 12201:12201/udp























 */