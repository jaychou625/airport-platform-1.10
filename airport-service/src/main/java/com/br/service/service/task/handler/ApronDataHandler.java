package com.br.service.service.task.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.br.entity.task.TaskInfo;
import com.br.entity.task.TaskObject;
import com.br.mapper.TaskInfoMapper;
import com.br.mapper.TaskObjectMapper;
import com.br.service.utils.CrypUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 站坪数据处理类
 * @Author Zero
 * @Date 2019 05 09
 */
public class ApronDataHandler {

    // 任务对象Mapper
    @Resource
    private TaskObjectMapper taskObjectMapper;

    // 任务信息Mapper
    @Resource
    private TaskInfoMapper taskInfoMapper;

    // 密码工具类
    @Autowired
    private CrypUtils crypUtils;

    /**
     * 获取站坪数据
     *
     * */
    public void getAndSaveApronData() {
        JSONArray jsonTaskObjects = null;
        Date date = new Date();
        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyyMMdd");
        String dateString = sdf_date.format(date);
        SimpleDateFormat sdf_datetime = new SimpleDateFormat("yyyyMMddHHmmss");
        String datetimeString = sdf_datetime.format(date);
        String str = "Datasyx" + dateString + datetimeString + "syx.call.2019wgss.webcall.2019";
        String sign = this.crypUtils.toMD5(str);
        String url = "http://10.2.135.122:8091/CallHandlers/WgssHandler.ashx?MethodName=Data&CallUserName=syx&PlanDate=" + dateString + "&DateTimeToken=" + datetimeString + "&Sign=" + sign;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().get().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObject = JSONObject.parseObject(response.body().string());
            jsonTaskObjects = jsonObject.getJSONArray("Data");
            for (Object jsonTaskObject : jsonTaskObjects) {
                TaskObject taskObject = ((JSONObject) jsonTaskObject).toJavaObject(TaskObject.class);

                //redis
                Jedis jedis = new Jedis("localhost",6379);
                jedis.select(4);
                Map<String,String> mapTaskObject = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String planeDate = "";
                try{
                    planeDate = String.valueOf(sdf.parse(taskObject.getPlanDate()).getTime());
                }catch (Exception e){
                    e.printStackTrace();
                }
                mapTaskObject.put(planeDate, JSON.toJSONString(taskObject));
                jedis.hmset(String.valueOf(taskObject.getFid()), mapTaskObject);

                //mysql
                this.taskObjectMapper.add(taskObject);

                TaskInfo[] taskInfos = taskObject.getFeedBackData();
                if(taskInfos.length > 0){
                    jedis.select(3);
                    for(TaskInfo taskInfo : taskInfos){
                        if(taskInfo.getStartTime() != null){
                            //读取对应字段的数据，判断是否为第一次执行任务
                            TaskInfo temp = JSON.parseObject(jedis.hget(taskInfo.getPrcName(),String.valueOf(taskInfo.getStartTime().getTime())),new TypeReference<TaskInfo>() {});
                            if(temp != null){
                                taskInfo.setTaskNo(temp.getTaskNo());
                                taskInfo.setCarNo(temp.getCarNo());
                                if(temp.getCount() == null){
                                    Map<String,String> map = new HashMap<>();
                                    map.put(String.valueOf(taskInfo.getStartTime().getTime()),JSON.toJSONString(taskInfo));
                                    jedis.hmset(taskInfo.getPrcName(), map);
                                }else{
                                    taskInfo.setCount(1);
                                    Map<String,String> map = new HashMap<>();
                                    map.put(String.valueOf(taskInfo.getStartTime().getTime()),JSON.toJSONString(taskInfo));
                                    jedis.hmset(taskInfo.getPrcName(), map);
                                }
                            }else{
                                taskInfo.setTaskNo(UUID.randomUUID().toString().replaceAll("-",""));
                                Map<String,String> map = new HashMap<>();
                                map.put(String.valueOf(taskInfo.getStartTime().getTime()),JSON.toJSONString(taskInfo));
                                jedis.hmset(taskInfo.getPrcName(), map);
                            }
                        }
                    }
                    jedis.close();

                    for(TaskInfo taskInfo : taskInfos){
                        //mysql
                        this.taskInfoMapper.add(taskInfo);
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
