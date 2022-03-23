package com.netty.springboot.protobuf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class NettySpringbootProtobufApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(NettySpringbootProtobufApplication.class, args);
//    }
//
//}
//Protocol Buffer是Google的语言中立的，平台中立的，可扩展机制的，用于序列化结构化数据
//对比XML，但更小，更快，更简单。您可以定义数据的结构化，然后可以使用特殊生成的源代码轻松地在各种数据流中使用各种语言编写和读取结构化数据。
//官网地址: https://developers.google.com/protocol-buffers/
//protocol指南
//https://www.cnblogs.com/sanshengshui/p/9739521.html
//执行mvn clean compile,输入完之后，回车即可在target文件夹中看到已经生成好的Java文件，然后直接在工程中使用此protobuf文件就可以了


//https://blog.csdn.net/tanksyg/article/details/84806508
//protoc.exe ./info.proto --java_out=./