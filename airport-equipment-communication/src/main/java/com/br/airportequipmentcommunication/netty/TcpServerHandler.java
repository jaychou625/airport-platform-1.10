package com.br.airportequipmentcommunication.netty;

import com.alibaba.fastjson.JSON;
import com.br.airportequipmentcommunication.entity.TcpInfo;
import com.br.airportequipmentcommunication.service.TcpService;
import com.br.airportequipmentcommunication.service.impl.TcpServiceImpl;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.codec.binary.Hex;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TcpServerHandler extends ChannelInboundHandlerAdapter {
    private static long oldTime = new Date().getTime();
    private  static  boolean flag = false;
    TcpService tcpService = new TcpServiceImpl();
    Map<String, String> map = new HashMap<String,String>();
    Object obj = null;
    String messageId = "";
    ByteBuf in = null;
    //收到数据时调用
    @Override
    public  void channelRead(ChannelHandlerContext ctx, Object  msg) throws Exception {
        try {
            map = new HashMap<String,String>();
            //读取消息字节
            in = (ByteBuf)msg;
            int readableBytes = in.readableBytes();
            byte[] bytes =new byte[readableBytes];
            in.readBytes(bytes);
            //转换16进制码
            String info = Hex.encodeHexString(bytes).toUpperCase();
            System.out.println("服务端接受的消息 : " + info);
            Integer len = 0;
            if(info.indexOf("55AA") != -1 && info.length() >= 42){
                info = info.substring(info.indexOf("55AA"));
                len = Integer.parseInt(info.substring(40,42),16);
            }else{
                return;
            }
            //确认可用字符长度
            len = len * 2 + 46;
            if(info.length() >= len){
                todoConnection(info);
            }else{
                System.out.println("数据包异常");
            }
//            todoTest(new String(bytes));
        }finally {
            // 抛弃收到的数据
            ReferenceCountUtil.release(msg);
            // 清空buf字节容器
//            if(in != null){
//                in.release();
//                in.clear();
//            }

        }
    }
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    /*
     * 建立连接时，返回消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
//        System.out.println("连接的客户端地址:" + ctx.channel().remoteAddress());
//        System.out.println("连接的客户端ID:" + ctx.channel().id());
//        ctx.writeAndFlush("client"+ InetAddress.getLocalHost().getHostName() + "success connected！ \n");
//        System.out.println("connection");
        //StaticVar.ctxList.add(ctx);
        //StaticVar.chc = ctx;
        super.channelActive(ctx);
    }
    //实时存储redis
    public void todoConnection(String entireInfo){
        //根据有效信息生成tcp数据对象，后续解析要用
        String infometion = entireInfo;
        TcpInfo tcpInfo = tcpService.getTcpInfo(infometion);
//            System.out.println(tcpInfo.toString());
        //判断消息类型，做不同处理，雷达协议，以及mobileEye协议，通过对象的infoType来判断，00：mobileEye，01：雷达
        if(tcpInfo.getInfoType().equals("00")){
//            //处理mobileEye协议
//            MobileEyeProtocalService mobileEyeProtocalService = new MobileEyeProtocalServiceImpl();
//            obj = mobileEyeProtocalService.getMobileEyeInfo(tcpInfo);
//            messageId = tcpInfo.getInfoContent().substring(2,4) + tcpInfo.getInfoContent().substring(0,2);
        }else if(tcpInfo.getInfoType().equals("01")){
//            //处理雷达协议
//            RadarProtocalService radarProtocalService = new RadarProtocalServiceImpl();
//            ultrasonicRadarProtocal = radarProtocalService.getRadarInfo(tcpInfo);
        }else{
            //协议有误处理
        }

        //存储数据库操作
//        if(ultrasonicRadarProtocal != null){
//            map.put(tcpInfo.getUnixTime(), JSON.toJSONString(ultrasonicRadarProtocal));
//            Jedis jedis = new Jedis("localhost",6379);
//            jedis.select(0);
//            jedis.hmset("Radar-" + tcpInfo.getEquipmentNo(),map);
//            jedis.close();
//            ultrasonicRadarProtocal = null;
//            map = null;
//        }
//        if(obj != null){
//            map.put(tcpInfo.getUnixTime(),JSON.toJSONString(obj));
//            Jedis jedis = new Jedis("localhost",6379);
//            jedis.hmset("MobileEye-" + messageId + "-" + tcpInfo.getEquipmentNo(),map);
//            jedis.close();
//            obj = null;
//            map = null;
//        }

    }

}