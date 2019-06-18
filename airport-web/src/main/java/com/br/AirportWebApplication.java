package com.br;

import com.br.airporttaskserver.handler.TaskApronDataHandler;
import com.br.airporttaskserver.service.TrafficTaskStateService;
import com.br.entity.map.Car;
import com.br.entity.map.CarInfo;
import com.br.entity.task.TaskStateInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.text.SimpleDateFormat;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan("com.br.mapper")
public class AirportWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirportWebApplication.class, args);
    }


    @Autowired
    TrafficTaskStateService trafficTaskStateService;
//
    @Scheduled(fixedRate = 2000)
    public void todo(){
        Car car = new Car();
        car.setCarType("摆渡车");
        car.setCarNo("摆渡车No1");
        car.setCarSeq(1);
        CarInfo carInfo = new CarInfo();
        carInfo.setCar(car);
        TaskStateInfo taskState = trafficTaskStateService.getTaskState(carInfo,false,"681C715909005758");
        if(taskState.getState() != -1){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String endTime = taskState.getState() == 2 ?  sdf.format(taskState.getEndTime()) : "无结束时间";
            System.out.println(taskState.getId() + "---" + taskState.getCarType() + "---" + taskState.getFltNo() + "---" + taskState.getCarNo() + "---" + taskState.getDriverName() + "---" + taskState.getStartTime() + "---" + endTime + "---" + taskState.getState());
        }
    }
//
//
//
//    @Scheduled(fixedRate = 2000)
//    public void todo2(){
//        Car car = new Car();
//        car.setCarType("清水车");
//        car.setCarNo("清水车No");
//        car.setCarSeq(2);
//        CarInfo carInfo = new CarInfo();
//        carInfo.setCar(car);
//        TaskStateInfo taskState = trafficTaskStateService.getTaskState(carInfo,false);
//        if(taskState.getState() != -1){
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//            String endTime = taskState.getState() == 2 ?  sdf.format(taskState.getEndTime()) : "无结束时间";
//            System.out.println(taskState.getId() + "---" + taskState.getCarType() + "---" + taskState.getFltNo() + "---" + taskState.getCarNo() + "---" + taskState.getDriverName() + "---" + taskState.getStartTime() + "---" + endTime + "---" + taskState.getState());
//        }
//    }
}

