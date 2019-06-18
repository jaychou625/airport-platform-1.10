package com.br.airporttaskserver.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.entity.taskState.EquipmentInfo;
import com.br.mapper.EquipmentInfoMapper;
import com.br.utils.CrypUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskApronDataHandler {
    // 密码工具类
    @Autowired
    private CrypUtils crypUtils;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private EquipmentInfoMapper equipmentInfoMapper;

    private Jedis jedis;

    @Scheduled(fixedRate = 10000)
    public void getAndSave(){
        jedis = new Jedis("localhost", 6379);
        jedis.select(5);
        JSONArray jsonTaskEquipment = null;
        Date date = new Date();
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = sdf_date.format(new Date(date.getTime() - 7200000));
        SimpleDateFormat sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetimeString = sdf_datetime.format(date);
        String str = "DevOnlinesyx" + dateString  + datetimeString + datetimeString + "syx.call.2019wgss.webcall.2019";
        String sign = this.crypUtils.toMD5(str);
        String url = "http://10.2.135.122:8091/CallHandlers/WgssHandler.ashx?MethodName=DevOnline&CallUserName=syx&BeginTime="
                + dateString + "&EndTime=" + datetimeString + "&DateTimeToken=" + datetimeString + "&Sign=" + sign;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            jsonTaskEquipment = jsonObject.getJSONArray("Data");
            for(int i = 0; i < jsonTaskEquipment.size(); i++){
                EquipmentInfo equipmentInfo = ((JSONObject)jsonTaskEquipment.get(i)).toJavaObject(EquipmentInfo.class);


                //存放redis
                jedis.hset(equipmentInfo.getDev_No(),String.valueOf(equipmentInfo.getLogin_Time().getTime()),JSON.toJSONString(equipmentInfo));

                //存放mysql
                if(this.equipmentInfoMapper.selectById(equipmentInfo.getId()) == 0){
                    Timestamp ts = new Timestamp(new Date().getTime());
                    equipmentInfo.setUnixTime(ts);
                    this.equipmentInfoMapper.add(equipmentInfo);
                }
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }finally {

        }

    }
}
