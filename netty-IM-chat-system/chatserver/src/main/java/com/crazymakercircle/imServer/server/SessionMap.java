package com.crazymakercircle.imServer.server;

import com.crazymakercircle.im.common.bean.User;
import com.crazymakercircle.util.Print;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 一台服务器需要接受几万/几十万的客户端连接，每一条连接都对应到一个
 * ServerSession实例，服务器需要对大量的ServerSession实例进行管理。这里使用一个会话容
 * 器SessionMap，负责管理服务器端所有的ServerSession，其内部使用一个线程安全的
 * ConcurrentHashMap类型的映射成员，保持sessionId到服务器端ServerSession的映射。
 */
@Slf4j
@Data
public final class SessionMap {
    private SessionMap() {
    }

    private static SessionMap singleInstance = new SessionMap();

    //会话集合
    private ConcurrentHashMap<String, ServerSession> map =
            new ConcurrentHashMap<String, ServerSession>();

    public static SessionMap inst() {
        return singleInstance;
    }

    /**
     * 增加session对象
     */
    public void addSession(
            String sessionId, ServerSession s) {
        map.put(sessionId, s);
        log.info("用户登录:id= " + s.getUser().getUid()
                + "   在线总数: " + map.size());

    }

    /**
     * 获取session对象
     */
    public ServerSession getSession(String sessionId) {
        if (map.containsKey(sessionId)) {
            return map.get(sessionId);
        } else {
            return null;
        }
    }

    /**
     * 根据用户id，获取session对象
     */
    public List<ServerSession> getSessionsBy(String userId) {

        List<ServerSession> list = map.values()
                .stream()
                .filter(s -> s.getUser().getUid().equals(userId))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 删除session
     */
    public void removeSession(String sessionId) {
        if (!map.containsKey(sessionId)) {
            return;
        }
        ServerSession s = map.get(sessionId);
        map.remove(sessionId);
        Print.tcfo("用户下线:id= " + s.getUser().getUid()
                + "   在线总数: " + map.size());
    }


    public boolean hasLogin(User user) {
        Iterator<Map.Entry<String, ServerSession>> it =
                map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ServerSession> next = it.next();
            User u = next.getValue().getUser();
            if (u.getUid().equals(user.getUid())
                    && u.getPlatform().equals(user.getPlatform())) {
                return true;
            }
        }

        return false;
    }
}
