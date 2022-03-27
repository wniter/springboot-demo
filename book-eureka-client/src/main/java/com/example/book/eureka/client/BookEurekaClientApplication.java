package com.example.book.eureka.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
//@EnableConfigServer //开启配置服务器的支持
@EnableEurekaClient
@SpringBootApplication
public class BookEurekaClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(BookEurekaClientApplication.class, args);

        Environment env = applicationContext.getEnvironment();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        String ip = env.getProperty("eureka.instance.ip-address");

        System.out.println("\n----------------------------------------------------------\n\t" +
                "Eureka 注册中心 is running! Access URLs:\n\t" +
                "Local: \t\thttp://" + ip + ":" + port + "/\n\t" +
                "actuator: \thttp://" + ip + ":" + port + "/actuator/info\n\t" +
                "----------------------------------------------------------");
    }

}
