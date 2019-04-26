package com.br.service.service.traffic;

import com.br.entity.map.Car;
import com.br.mapper.CarMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 车辆服务
 *
 * @Author Zero
 * @Date 2019 03 21
 */

@Service("carService")
public class CarService {

    // 车辆DAO服务
    @Resource
    private CarMapper carMapper;

    /**
     * 查找全部车辆
     *
     * @return List<Car>
     */
    public List<Car> findAll() {
        return carMapper.findAll();
    }

    /**
     * 查找单个车辆
     *
     * @param carSeq 车辆序号
     * @return Car
     */
    public Car find(Integer carSeq) {
        return this.carMapper.find(carSeq);
    }


}
