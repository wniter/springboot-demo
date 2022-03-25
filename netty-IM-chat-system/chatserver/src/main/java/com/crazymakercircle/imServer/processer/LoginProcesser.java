package com.crazymakercircle.imServer.processer;

import com.crazymakercircle.im.common.ProtoInstant;
import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.protoBuilder.LoginResponceBuilder;
import com.crazymakercircle.imServer.server.ServerSession;
import com.crazymakercircle.imServer.server.SessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *LoginProcesser用户验证逻辑主要包括：密码验证、将验证的结果写入到通道。如果登
 * 录验证成功，还需要实现通道与服务器端会话的双向绑定，并且将服务器端会话加入到在
 * 线用户列表中。
 *
 * 用户密码验证的逻辑，在checkUser()方法中完成。在实际的生产场景中，
 * LoginProcesser进行用户登录验证的方式比较多：
 * ⚫ 通过RESTful接口验证用户
 * ⚫ 通过数据库去验证用户
 * ⚫ 通过认证（Auth）服务器去验证用户
 * 总之，验证用户涉及到RPC等耗时操作，为了尽量地简化流程，示例程序代码省去了
 * 通过账号和密码验证的过程，checkUser() 方法直接返回true，也就是默认所有的登录都是
 * 成功的。
 * 服务器端校验通过之后，可以完成服务器端会话（ServerSession）的绑定工作。服务
 * 器端的ServerSession会话与客户端的ClientSession会话类似，也是一个胶水类。每一个
 * ServerSession拥有一个Channel成员实例、一个User成员实例。Channel成员代表与客户端连
 * 接的子通道；User成员代表用户信息。稍后，会对ServerSession进行详细介绍。
 * 在用户校验成功后，服务端就需要向客户端发送登录响应。具体的方法是：调用登录
 * 响应的Protobuf消息构造器loginResponceBuilder，构造一个登录响应POJO实例，设置好校
 * 验成功的标志位，调用会话（Session）的writeAndFlush()方法写到客户端。
 */
@Slf4j
@Service("LoginProcesser")
public class LoginProcesser extends AbstractServerProcesser {
    @Autowired
    LoginResponceBuilder loginResponceBuilder;

    @Override
    public ProtoMsg.HeadType type() {
        return ProtoMsg.HeadType.LOGIN_REQUEST;
    }

    @Override
    public boolean action(ServerSession session,
                          ProtoMsg.Message proto) {
        // 取出token验证
        ProtoMsg.LoginRequest info = proto.getLoginRequest();
        long seqNo = proto.getSequence();

        User user = User.fromMsg(info);

        //检查用户
        boolean isValidUser = checkUser(user);
        if (!isValidUser) {
            ProtoInstant.ResultCodeEnum resultcode =
                    ProtoInstant.ResultCodeEnum.NO_TOKEN;
            //构造登录失败的报文
            ProtoMsg.Message response =
                    loginResponceBuilder.loginResponce(resultcode, seqNo, "-1");
            //发送登录失败的报文
            session.writeAndFlush(response);
            return false;
        }

        session.setUser(user);

        session.bind();

        //登录成功
        ProtoInstant.ResultCodeEnum resultcode =
                ProtoInstant.ResultCodeEnum.SUCCESS;
        //构造登录成功的报文
        ProtoMsg.Message response =
                loginResponceBuilder.loginResponce(
                        resultcode, seqNo, session.getSessionId());
        //发送登录成功的报文
        session.writeAndFlush(response);
        return true;
    }

    private boolean checkUser(User user) {

        if (SessionMap.inst().hasLogin(user)) {
            return false;
        }

        //校验用户,比较耗时的操作,需要100 ms以上的时间
        //方法1：调用远程用户restfull 校验服务
        //方法2：调用数据库接口校验

        return true;

    }

}
