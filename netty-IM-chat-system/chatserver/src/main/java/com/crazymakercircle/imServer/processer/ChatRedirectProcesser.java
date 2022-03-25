package com.crazymakercircle.imServer.processer;

import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import com.crazymakercircle.util.Print;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 服务器端的ChatRedirectProcesser异步转发
 *  ChatRedirectProcesser异步消息转发类负责将消息发送到目标用户，这个一个异步执行
 * 的任务，其大致功能如下：
 * （1）根据目标用户ID，找出所有的服务器端的会话列表。
 * （2）然后为每一个会话转发一份消息。
 */
@Slf4j
@Service("ChatRedirectProcesser")
public class ChatRedirectProcesser extends AbstractServerProcesser {
    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.MESSAGE_REQUEST;
    }

    @Override
    public boolean action(ServerSession fromSession, ProtoMsg.Message proto) {
        // 聊天处理
        ProtoMsg.MessageRequest msg = proto.getMessageRequest();
        Print.tcfo("chatMsg | from="
                + msg.getFrom()
                + " , to=" + msg.getTo()
                + " , content=" + msg.getContent());
        // 获取接收方的chatID
        String to = msg.getTo();
        // int platform = msg.getPlatform();
        List<ServerSession> toSessions = SessionMap.inst().getSessionsBy(to);
        if (toSessions == null) {
            //接收方离线
            Print.tcfo("[" + to + "] 不在线，发送失败!");
        } else {

            toSessions.forEach((session) -> {
                // 将IM消息发送到接收方
                session.writeAndFlush(proto);
            });
        }
        return true;
    }

}
