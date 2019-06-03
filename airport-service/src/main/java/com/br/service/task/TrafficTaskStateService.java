package com.br.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.br.entity.map.CarInfo;
import com.br.entity.task.TaskInfo;
import com.br.entity.task.TaskObject;
import com.br.entity.task.TaskStateInfo;
import com.br.utils.SortUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * 交通任务状态服务
 */
@Service("trafficTaskStateService")
public class TrafficTaskStateService {

    /**
     * 根据车辆信息查询任务状态，0：任务开始，1：任务进行中，2：任务结束
     *
     * @param carInfo，mark
     * @return Map:key--"state":任务状态（0：任务开始，1:任务进行中，2：任务结束，-1任务未开始），
     * "portNo":机位，"fltNo":航班号,"taskInfo":任务信息对象
     */
    public TaskStateInfo getTaskState1(CarInfo carInfo, Boolean mark) {
        //测试用，开始时间戳
        Long startTime = new Date().getTime();
        TaskStateInfo state = new TaskStateInfo();
        //获取车辆类型
        String carType = carInfo.getCar().getCarType();
        //查询redis数据库匹配车型最新的任务记录
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.select(3);
        //获取最新的任务信息
        Map<String, String> map = jedis.hgetAll(carType);
        if (map.size() == 0) {
            state.setState(-1);
            return state;
        }
        map = SortUtils.sortMapByKey(map);
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
        Map.Entry<String, String> entry = entries.next();
        TaskInfo taskInfo = JSON.parseObject(entry.getValue(), new TypeReference<TaskInfo>() {
        });
        //获取任务状态0：开始，1：任务中，2：任务结束
        if (taskInfo.getStartTime() != null && taskInfo.getEndTime() == null) {
            //判断是否为第一次接受到任务
            if (taskInfo.getCount() == null) {//任务开始
                state.setState(0);
                taskInfo.setCount(1);
                jedis.hset(taskInfo.getPrcName(), String.valueOf(taskInfo.getStartTime().getTime()), JSON.toJSONString(taskInfo));
            } else {//执行中
                //判断是否重规划，true为重规划
                if (mark) {
                    state.setState(0);
                } else {
                    state.setState(1);
                }
            }
        } else if (taskInfo.getStartTime() != null && taskInfo.getEndTime() != null) {//任务结束
            state.setState(2);
        } else if (taskInfo.getStartTime() == null) {//任务未开始
            state.setState(-1);
        }
        //获取任务对应航班号
        jedis.select(4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tempDate = "";
        try {
            tempDate = String.valueOf(sdf.parse(sdf.format(taskInfo.getSendTime())).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TaskObject taskObject = JSON.parseObject(jedis.hget(String.valueOf(taskInfo.getFid()), tempDate), new TypeReference<TaskObject>() {
        });
        if (taskObject != null) {
            state.setPortNo(taskObject.getPortNo());
            state.setFltNo(taskObject.getFltNo());
        }

        //设置相关信息
        state.setId(taskInfo.getTaskNo());
        state.setCarType(carType);
        state.setCarNo(carInfo.getCar().getCarNo());
        state.setDriverName(taskInfo.getFeedBackName());
        state.setStartTime(taskInfo.getStartTime());
        state.setEndTime(taskInfo.getEndTime());
        state.setCarSeq(carInfo.getCar().getCarSeq());
        Long endTime = new Date().getTime();
        System.out.println("耗时：" + (endTime - startTime) + "毫秒");


        jedis.close();
        return state;
    }

    public TaskStateInfo getTaskState(CarInfo carInfo, Boolean mark) {
        //测试用，开始时间戳
        Long startTime = new Date().getTime();
        TaskStateInfo state = new TaskStateInfo();
        //获取车辆类型
        String carType = carInfo.getCar().getCarType();
        //查询redis数据库匹配车型最新的任务记录
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.select(3);
        //获取最新的任务信息
        Map<String, String> map = jedis.hgetAll(carType);
        if (map.size() == 0) {
            state.setState(-1);
            return state;
        }
        map = SortUtils.sortMapByKey(map);
        Iterator<Map.Entry<String, String>> entries = map.entrySet().iterator();
//        Map.Entry<String,String> entry = entries.next();
//        TaskInfo taskInfo = JSON.parseObject(entry.getValue(),new TypeReference<TaskInfo>() {});

        //判断最新任务有无对应车牌号,如果吗，没有，则将最新的车牌号添加进去
        TaskInfo taskInfo = new TaskInfo();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            taskInfo = JSON.parseObject(entry.getValue(), new TypeReference<TaskInfo>() {
            });
            if (taskInfo.getCarNo() == null) {
                Iterator<Map.Entry<String, String>> entriesTemp = map.entrySet().iterator();
                Map.Entry<String, String> entryTempT = entriesTemp.next();
                Map.Entry<String, String> entryTemp = entriesTemp.next();
                TaskInfo taskInfoTemp = JSON.parseObject(entryTemp.getValue(), new TypeReference<TaskInfo>() {
                });
                if (!carInfo.getCar().getCarNo().equals(taskInfoTemp.getCarNo())) {
                    taskInfo.setCarNo(carInfo.getCar().getCarNo());
                }
                if (taskInfo.getStartTime() != null) {
                    jedis.hset(taskInfo.getPrcName(), String.valueOf(taskInfo.getStartTime().getTime()), JSON.toJSONString(taskInfo));
                }
            }
        }

        //更新完毕后再次读取,判断对应车牌下的最近一次任务信息
        map = jedis.hgetAll(carType);
        map = SortUtils.sortMapByKey(map);
        entries = map.entrySet().iterator();
        boolean flag = false;
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            taskInfo = JSON.parseObject(entry.getValue(), new TypeReference<TaskInfo>() {
            });
            if (taskInfo.getCarNo() != null) {
                if (taskInfo.getCarNo().equals(carInfo.getCar().getCarNo())) {
                    taskInfo = JSON.parseObject(entry.getValue(), new TypeReference<TaskInfo>() {
                    });
                    flag = true;
                    break;
                }
            }
        }
        //遍历完map还未找到匹配任务，返回状态-1
        if (!flag) {
            state.setState(-1);
            return state;
        }

        //获取任务状态0：开始，1：任务中，2：任务结束
        if (taskInfo.getStartTime() != null && taskInfo.getEndTime() == null) {
            //判断是否为第一次接受到任务
            if (taskInfo.getCount() == null) {//任务开始
                state.setState(0);
                taskInfo.setCount(1);
                jedis.hset(taskInfo.getPrcName(), String.valueOf(taskInfo.getStartTime().getTime()), JSON.toJSONString(taskInfo));
            } else {//执行中
                //判断是否重规划，true为重规划
                if (mark) {
                    state.setState(0);
                } else {
                    state.setState(1);
                }
            }
        } else if (taskInfo.getStartTime() != null && taskInfo.getEndTime() != null) {//任务结束
            state.setState(2);
        } else if (taskInfo.getStartTime() == null) {//任务未开始
            state.setState(-1);
        }
        //获取任务对应航班号
        jedis.select(4);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tempDate = "";
        try {
            tempDate = String.valueOf(sdf.parse(sdf.format(taskInfo.getSendTime())).getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        TaskObject taskObject = JSON.parseObject(jedis.hget(String.valueOf(taskInfo.getFid()), tempDate), new TypeReference<TaskObject>() {
        });
        if (taskObject != null) {
            state.setPortNo(taskObject.getPortNo());
            state.setFltNo(taskObject.getFltNo());
        }

        //设置相关信息
        state.setId(taskInfo.getTaskNo());
        state.setCarType(carType);
        state.setCarNo(carInfo.getCar().getCarNo());
        state.setDriverName(taskInfo.getFeedBackName());
        state.setStartTime(taskInfo.getStartTime());
        state.setEndTime(taskInfo.getEndTime());
        state.setCarSeq(carInfo.getCar().getCarSeq());
        Long endTime = new Date().getTime();
        System.out.println("耗时：" + (endTime - startTime) + "毫秒");
        jedis.close();
        return state;
    }

}
