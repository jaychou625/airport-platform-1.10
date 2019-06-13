package com.br.airportequipmentcommunication.service.impl;

import com.br.airportequipmentcommunication.entity.TcpInfo;
import com.br.airportequipmentcommunication.service.TcpService;
import com.br.utils.TransitionUtil;

import java.util.UUID;

public class TcpServiceImpl implements TcpService {
    TcpInfo tcpInfo;
    @Override
    public TcpInfo getTcpInfo(String infometion) {
//        System.out.println("有效信息：" + infometion);
        //生成唯一UUID标识
        String id = UUID.randomUUID().toString().replaceAll("-","");
        //获取起始符
        String startMark = infometion.substring(0,4);
//        System.out.println("起始符：" + startMark);
        //获取协议版本号
        String protocal = infometion.substring(4,6);
//        System.out.println("协议版本号：" + protocal);
        //获取设备编号
        String equipmentNo = infometion.substring(6,6+16);
        equipmentNo = TransitionUtil.hex2Str(equipmentNo);
//        System.out.println("设备编号：" + equipmentNo);
        //获取时间戳
        String unixTime = infometion.substring(6+16,6+16+16);
        unixTime = TransitionUtil.toLittle(unixTime);
        unixTime = TransitionUtil.hexString2TimeStamp(unixTime);
//        System.out.println("时间戳：" + unixTime);
        //获取报文类型
        String infoType = infometion.substring(6+16+16,6+16+16+2);
//        System.out.println("报文类型：" + infoType);
        //获取报文长度
        String infoLen = infometion.substring(6+16+16+2,6+16+16+2+2);
        Long infoLenhex = Long.parseLong(infoLen,16);
        infoLen = String.valueOf(infoLenhex);
//        System.out.println("报文长度为十进制：" + infoLenhex);
        //获取报文体内容
        String infoContent = infometion.substring(6+16+16+2+2,6+16+16+2+2 + infoLenhex.intValue() * 2);
//        System.out.println("报文体内容：" + infoContent);
        //获取校验和
        String infoCheckSum = infometion.substring(6+16+16+2+2 + infoLenhex.intValue() * 2,6+16+16+2+2 + infoLenhex.intValue() * 2 + 2);
//        System.out.println("校验和：" + infoCheckSum);
        //获取结束符
        String infoEndMark = infometion.substring(6+16+16+2+2 + infoLenhex.intValue() * 2 + 2);
//        System.out.println("结束符为：" + infoEndMark);
        tcpInfo = new TcpInfo(id,infometion,startMark,protocal,equipmentNo,unixTime,infoType,infoLen,infoContent,infoCheckSum,infoEndMark);
        return tcpInfo;
    }
}
