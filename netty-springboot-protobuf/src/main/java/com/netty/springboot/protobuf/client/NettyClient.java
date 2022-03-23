package com.netty.springboot.protobuf.client;

import com.netty.springboot.protobuf.server.NettyServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @create 2022-01-15 23:58
 */
@Service("NettyClient")

public class NettyClient {
    private static  final Logger LOGGER = LoggerFactory.getLogger(NettyClient.class);
    @Value("${server.bind_address}")
    private String host;

    @Value("${server.bind_port}")
    private Integer port;

    /**唯一标记 */
    private boolean initFalg=true;

    private EventLoopGroup group;
    private ChannelFuture f;



    /**
     * Netty创建全部都是实现自AbstractBootstrap。 客户端的是Bootstrap，服务端的则是 ServerBootstrap。
     **/
    @PostConstruct
    public void init() {
        group = new NioEventLoopGroup();
        doConnect(new Bootstrap(), group);
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        LOGGER.info("正在停止客户端");
        try {
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        LOGGER.info("客户端已停止!");
    }
    /**
     * 重连
     */
    public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {
        try {
            if (bootstrap != null) {
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new NettyClientInitializer());
                bootstrap.remoteAddress(host, port);
                f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        LOGGER.info("与服务端断开连接!在10s之后准备尝试重连!");
                        eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
                    }
                });
                if(initFalg){
                    LOGGER.info("Netty客户端启动成功!");
                    initFalg=false;
                }
            }
        } catch (Exception e) {
            LOGGER.info("客户端连接失败!"+e.getMessage());
        }

    }



}
