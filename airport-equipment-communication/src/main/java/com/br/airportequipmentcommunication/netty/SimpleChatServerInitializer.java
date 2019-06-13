package com.br.airportequipmentcommunication.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @创建人：
 * @时间：2018/9/20 0020
 * @描述：
 */
public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 自定义处理类
        ch.pipeline().addLast(new TcpServerHandler());
    }
}