package com.crazymakercircle.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 *点对点单聊的实践案例
 *单聊的业务非常简单，就像微信的文字聊天功能，主要的业务流程大致如下：
 * （1）当用户A登录成功之后，按照单聊的消息格式，发送所要的消息。
 * 为了简单，这里的消息格式简化为——userId:content。其中的userId，就是消息接收方
 * 目标用户B的userId；其中的content表示聊天的内容。
 * （2）服务器端收到消息后，根据目标userID进行消息的转发，发送到用户B所在的客
 * 户端。
 * （3）客户端用户B收到用户A发来的消息，在自己的控制台显示出来。
 * 在这里有问题，为什么服务器端的路由转发不是根据sessionID，而是根据userID呢？
 * 前面讲到，用户B可能登录了多个会话（桌面会话、移动端会话、网页端会话），这时发
 * 给用户B的聊天消息必须转发到多个会话，所以需要根据userID进行转发。
 *
 * 单聊的端到端流程
 * （1）用户A发送单聊Protobuf数据包到服务端；
 * （2）服务器端接收到用户A的单聊数据包；
 * （3）服务器端转发单聊数据包到用户B；
 * （4）最终用户B接收到来自用户A的单聊数据包。
 *
 *  客户端的ChatConsoleCommand收集聊天内容
 *  聊天消息收集类ChatConsoleCommand负责从控制台Scanner实例收集用户输入的聊天的消息（格式为：id:message）
 *
 *
 *
 */
@Data
@Service("ChatConsoleCommand")
public class ChatConsoleCommand implements BaseCommand {

    private String toUserId;
    private String message;
    public static final String KEY = "2";

    @Override
    public void exec(Scanner scanner) {
        System.out.print("请输入聊天的消息(id:message)：");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请输入聊天的消息(id:message):");
            } else {
                break;
            }
        }
        toUserId = info[0];
        message = info[1];
    }


    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "聊天";
    }

}
