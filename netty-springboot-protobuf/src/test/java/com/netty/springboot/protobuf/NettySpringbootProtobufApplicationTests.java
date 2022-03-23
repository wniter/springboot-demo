package com.netty.springboot.protobuf;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@SpringBootTest
class NettySpringbootProtobufApplicationTests {

    @Test
    void contextLoads() {
        UserMsg.User.Builder userInfo = UserMsg.User.newBuilder();
        userInfo.setId(1);
        userInfo.setName("helloworld");
        userInfo.setName("24");
        UserMsg.User user = userInfo.build();
        // 将数据写到输出流
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            user.writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将数据序列化后发送
        byte[] byteArray = output.toByteArray();
        // 接收到流并读取
        ByteArrayInputStream input = new ByteArrayInputStream(byteArray);
        // 反序列化
        UserMsg.User userInfo2 = null;
        try {
            userInfo2 = UserMsg.User.parseFrom(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("id:" + userInfo2.getId());
        log.info("name:" + userInfo2.getName());
        log.info("age:" + userInfo2.getAge());
    }

}
