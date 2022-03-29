package com.example.springboot.graylog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *
 * 参考
 *
 * graylog 官方下载地址：https://www.graylog.org/downloads#open-source
 *
 * graylog 官方docker镜像：https://hub.docker.com/r/graylog/graylog/
 *
 * graylog 镜像启动方式：http://docs.graylog.org/en/stable/pages/installation/docker.html
 *
 * graylog 启动参数配置：http://docs.graylog.org/en/stable/pages/configuration/server.conf.html
 *
 * 注意，启动参数需要加 GRAYLOG_ 前缀
 *
 * 日志收集依赖：https://github.com/osiegmar/logback-gelf
 */

@SpringBootApplication
public class SpringbootGraylogApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootGraylogApplication.class, args);
    }

}
