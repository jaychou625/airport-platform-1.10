package com.br.airportequipmentcommunication.service;

import com.br.airportequipmentcommunication.entity.GPSInfo;
import com.br.airportequipmentcommunication.entity.TcpInfo;

public interface GPSService {
    public GPSInfo getGPSInfo(TcpInfo TcpInfo);
}
