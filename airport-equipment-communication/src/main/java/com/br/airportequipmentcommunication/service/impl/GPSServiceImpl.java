package com.br.airportequipmentcommunication.service.impl;

import com.br.airportequipmentcommunication.entity.GPSInfo;
import com.br.airportequipmentcommunication.entity.TcpInfo;
import com.br.airportequipmentcommunication.service.GPSService;
import com.br.utils.TransitionUtil;

public class GPSServiceImpl implements GPSService {

    @Override
    public GPSInfo getGPSInfo(TcpInfo tcpInfo) {
        GPSInfo gpsInfo = new GPSInfo();
        //设置GPS的id信息
        gpsInfo.setId(tcpInfo.getUnixTime());
        //设置纬度信息
        String latitude = tcpInfo.getInfoContent().substring(6,8) + tcpInfo.getInfoContent().substring(4,6) + tcpInfo.getInfoContent().substring(2,4) + tcpInfo.getInfoContent().substring(0,2);
        double temp = Integer.parseInt(latitude,16) / 0.000001;
        gpsInfo.setLatitude(temp);
        //设置纬度半球N或S
        String latitudeDirection = TransitionUtil.hex2Str(tcpInfo.getInfoContent().substring(8,10));
        gpsInfo.setLatitudeDirection(latitudeDirection);
        //设置经度信息
        String longitude = tcpInfo.getInfoContent().substring(16,18) + tcpInfo.getInfoContent().substring(14,16) + tcpInfo.getInfoContent().substring(12,14) + tcpInfo.getInfoContent().substring(10,12);
        temp = Integer.parseInt(latitude,16) / 0.000001;
        gpsInfo.setLongitude(temp);
        //设置经度半球E或W
        String longitudeDirection = TransitionUtil.hex2Str(tcpInfo.getInfoContent().substring(18,20));
        gpsInfo.setLongitudeDirection(longitudeDirection);
        //设置航向角度
        String courseAngle = tcpInfo.getInfoContent().substring(22,24) + tcpInfo.getInfoContent().substring(20,22);
        temp = Integer.parseInt(courseAngle,16) / 0.1;
        gpsInfo.setCourseAngle(temp);
        //设置地速
        String speed = tcpInfo.getInfoContent().substring(24,26);
        temp = Integer.parseInt(speed,16);
        gpsInfo.setSpeed(temp);

        return gpsInfo;
    }
}
