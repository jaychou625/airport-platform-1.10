package com.br.airportequipmentcommunication.service.impl;

import com.br.airportequipmentcommunication.entity.GPSInfo;
import com.br.airportequipmentcommunication.entity.TcpInfo;
import com.br.airportequipmentcommunication.service.GPSService;

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
        //

        return gpsInfo;
    }
}
