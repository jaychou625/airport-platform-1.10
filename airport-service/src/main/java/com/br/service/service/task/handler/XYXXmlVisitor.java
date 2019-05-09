package com.br.service.service.task.handler;

import com.br.entity.task.FlightInfo;
import org.dom4j.Element;
import org.dom4j.VisitorSupport;

public class XYXXmlVisitor extends VisitorSupport {

    public static FlightInfo flightInfo = new FlightInfo();

    @Override
    public void visit(Element node) {
        try {
            String nodeName = node.getName();
            String nodeValue = node.getTextTrim();
            switch (nodeName) {
                case "FLIGHT":
                    flightInfo.setBizType(nodeName);
                    break;
                case "DELETE_FLIGHT":
                    flightInfo.setBizType(nodeName);
                    break;
                case "PAIR_FLIGHT":
                    flightInfo.setBizType(nodeName);
                    break;
                case "DATETIME":
                    flightInfo.setDataTime(nodeValue);
                    break;
                case "METHOD":
                    flightInfo.setMethod(nodeValue);
                    break;
                case "TYPE":
                    flightInfo.setType(nodeValue);
                    break;
                case "FLIGHTATTR":
                    flightInfo.setFlightAttrSeq(nodeValue);
                    break;
                case "FLIGHTTYPE":
                    flightInfo.setFlightTypeSeq(nodeValue);
                    break;
                case "AIRCRAFTTYPE":
                    flightInfo.setAircraftType(nodeValue);
                    break;
                case "REGISTRATION":
                    flightInfo.setRegistration(nodeValue);
                    break;
                case "FLIGHTNUMBER":
                    flightInfo.setFlightNumber(nodeValue);
                    break;
                case "PRIMARYFLT":
                    flightInfo.setPrimaryFlt(nodeValue);
                    break;
                case "RELATEDFLT":
                    flightInfo.setRelatedFlt(nodeValue);
                    break;
                case "FLIGHTSTATUS":
                    flightInfo.setFlightStatusSeq(nodeValue);
                    break;
                case "PORTNO":
                    flightInfo.setPortNo(nodeValue);
                    break;
                case "PLANDATE":
                    flightInfo.setPlanDate(nodeValue);
                    break;
                case "ADT":
                    flightInfo.setAdt(nodeValue);
                    break;
                case "EDT":
                    flightInfo.setEdt(nodeValue);
                    break;
                case "SDT":
                    flightInfo.setSdt(nodeValue);
                    break;
                case "ARRSDT":
                    flightInfo.setArrSdt(nodeValue);
                    break;
                case "COBT":
                    flightInfo.setCobt(nodeValue);
                    break;
                case "ORIGIN":
                    flightInfo.setOrigin(nodeValue);
                    break;
                case "DESTINATION":
                    flightInfo.setDestination(nodeValue);
                    break;
                case "VIA":
                    flightInfo.setVia(nodeValue);
                    break;
                case "COUNTEROPENDT":
                    flightInfo.setCounterOpenDT(nodeValue);
                    break;
                case "COUNTER":
                    flightInfo.setCounter(nodeValue);
                    break;
                case "COUNTER_INT":
                    flightInfo.setCounterInt(nodeValue);
                    break;
                case "DEPTERMINAL":
                    flightInfo.setDepTerminal(nodeValue);
                    break;
                case "GATE":
                    flightInfo.setGate(nodeValue);
                    break;
                case "GATE_INT":
                    flightInfo.setGateInt(nodeValue);
                    break;
                case "BELT":
                    flightInfo.setBelt(nodeValue);
                    break;
                case "BELT_INT":
                    flightInfo.setBeltInt(nodeValue);
                    break;
                case "PSG_TOTAL":
                    flightInfo.setPsgTotal(Integer.parseInt(nodeValue));
                    break;
                case "BAG_NUM":
                    flightInfo.setBagNum(Integer.parseInt(nodeValue));
                    break;
                case "BAG_WEIGHT":
                    flightInfo.setBagWeight(Integer.parseInt(nodeValue));
                    break;
                case "ARRFLIGHTNUMBER":
                    flightInfo.setArrFlightNumber(nodeValue);
                    break;
                case "ARRFLTTYPE":
                    flightInfo.setArrFltType(nodeValue);
                    break;
                case "DEPFLIGHTNUMBER":
                    flightInfo.setDepFlightNumber(nodeValue);
                    break;
                case "DEPFLTTYPE":
                    flightInfo.setDepFltType(nodeValue);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
