package com.br.service.utils;

import com.br.entity.map.Plane;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ADSB 工具类
 *
 * @Author Zero
 * @Date 2019 03 02
 */
public class ADSBUtils {

    // 日期工具类
    @Autowired
    private DateUtils dateUtils;

    /**
     * ADSB 字符串转换为 PlaneBeanList
     *
     * @param planes 飞机信息
     * @return List<Plane>
     */
    public List<Plane> toPlaneList(String planes) {
        Long receiveTimeOfLong = System.currentTimeMillis();
        String planes_replace = planes.replaceAll(",,", ",");
        String planes_sub = planes_replace.substring(planes_replace.indexOf(",") + 1, planes_replace.lastIndexOf("End"));
        String[] aDSBInfos_split = planes_sub.split(",");
        List<String> planesStringList = new ArrayList<>();
        Integer index = 0;
        String planeString = "";
        for (Integer i = 0; i < aDSBInfos_split.length; i++) {
            index++;
            planeString += aDSBInfos_split[i] + ",";
            if (i != 0 && index == 11) {
                planesStringList.add(planeString.substring(0, planeString.length() - 1));
                planeString = "";
                index = 0;
            }
        }
        List<Plane> planeBeans = new ArrayList<>();
        for (String planeString_ : planesStringList) {
            Plane plane = new Plane();
            String[] planeInfos = planeString_.split(",");
            plane.setPlaneAddrCode(planeInfos[0]);
            plane.setDataSourceDept(planeInfos[1]);
            plane.setPlaneLongitude(new BigDecimal(planeInfos[2]));
            plane.setPlaneLatitude(new BigDecimal(planeInfos[3]));
            plane.setPlaneVerticalSpeed(Integer.parseInt(planeInfos[4]));
            plane.setPlaneGroundVelocity(Integer.parseInt(planeInfos[5]));
            plane.setPlaneHeight(Integer.parseInt(planeInfos[6]));
            plane.setReceivePlaneCode(planeInfos[7]);
            if (planeInfos[8].equals("")) {
                plane.setPlaneSeq(planeInfos[0]);
            } else {
                plane.setPlaneSeq(planeInfos[8]);
            }
            plane.setPlaneHeading(new BigDecimal(planeInfos[9]));
            plane.setAckPlaneCode(planeInfos[10]);
            plane.setReceiveTimeStamp(receiveTimeOfLong);
            plane.setReceiveTime(this.dateUtils.dateToString(new Date(receiveTimeOfLong)));
            planeBeans.add(plane);
        }
        return planeBeans;
    }
}
