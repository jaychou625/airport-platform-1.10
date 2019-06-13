package com.br.airportequipmentcommunication.entity;

/**
 * TCP传送信息类
 */
public class TcpInfo {
    //uuid 唯一标示
    private String Id;
    //有效报文体
    private String infometion;
    //报文起始符
    private String startMark;
    //协议版本号
    private String protocal;
    //设备型号
    private String equipmentNo;
    //时间戳信息
    private String unixTime;
    //报文类型
    private String infoType;
    //报文长度
    private String infoLen;
    //报文体
    private String infoContent;
    //校验和
    private String checkSum;
    //结束符
    private String endMark;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getInfometion() {
        return infometion;
    }

    public void setInfometion(String infometion) {
        this.infometion = infometion;
    }

    public String getStartMark() {
        return startMark;
    }

    public void setStartMark(String startMark) {
        this.startMark = startMark;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoLen() {
        return infoLen;
    }

    public void setInfoLen(String infoLen) {
        this.infoLen = infoLen;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public String getEndMark() {
        return endMark;
    }

    public void setEndMark(String endMark) {
        this.endMark = endMark;
    }

    @Override
    public String toString() {
        return "TcpInfo{" +
                "Id='" + Id + '\'' +
                ", infometion='" + infometion + '\'' +
                ", startMark='" + startMark + '\'' +
                ", protocal='" + protocal + '\'' +
                ", equipmentNo='" + equipmentNo + '\'' +
                ", unixTime='" + unixTime + '\'' +
                ", infoType='" + infoType + '\'' +
                ", infoLen='" + infoLen + '\'' +
                ", infoContent='" + infoContent + '\'' +
                ", checkSum='" + checkSum + '\'' +
                ", endMark='" + endMark + '\'' +
                '}';
    }

    public TcpInfo(String id, String infometion, String startMark, String protocal, String equipmentNo, String unixTime, String infoType, String infoLen, String infoContent, String checkSum, String endMark) {
        Id = id;
        this.infometion = infometion;
        this.startMark = startMark;
        this.protocal = protocal;
        this.equipmentNo = equipmentNo;
        this.unixTime = unixTime;
        this.infoType = infoType;
        this.infoLen = infoLen;
        this.infoContent = infoContent;
        this.checkSum = checkSum;
        this.endMark = endMark;
    }
}
