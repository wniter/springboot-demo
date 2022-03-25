package com.crazymakercircle.imClient.command;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Scanner;

/**
 * 从上面的输出可以看出，ClientCommandMenu菜单展示类打印了4个选项：
 * （1）登录（2）聊天（3）退出（4）查看全部命令
 * 每一个菜单选项都对应到一个信息的收集类：
 * （1）聊天命令的信息收集类：ChatConsoleCommand
 * （2）登录命令的信息收集类：LoginConsoleCommand
 * （3）退出命令的信息收集类：LogoutConsoleCommand
 * （4）命令的类型收集类：ClientCommandMenu
 */
@Data
@Service("LoginConsoleCommand")
public class LoginConsoleCommand implements BaseCommand {
    public static final String KEY = "1";

    private String userName;
    private String password;

    @Override
    public void exec(Scanner scanner) {

        System.out.println("请输入用户信息(id:password)  ");
        String[] info = null;
        while (true) {
            String input = scanner.next();
            info = input.split(":");
            if (info.length != 2) {
                System.out.println("请按照格式输入(id:password):");
            } else {
                break;
            }
        }
        userName = info[0];
        password = info[1];
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public String getTip() {
        return "登录";
    }

}
