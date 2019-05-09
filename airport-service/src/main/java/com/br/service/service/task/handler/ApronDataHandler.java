package com.br.service.service.task.handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.br.entity.task.TaskObject;
import com.br.service.utils.CrypUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 站坪数据处理类
 * @Author Zero
 * @Date 2019 05 09
 */
public class ApronDataHandler {

    // 密码工具类
    @Autowired
    private CrypUtils crypUtils;

    /**
     * 获取站坪数据
     *
     * @return List<TaskObject>
     */
    public List<TaskObject> getApronData() {
        JSONArray jsonTaskObjects = null;
        List<TaskObject> taskObjectList = new ArrayList<>();
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
                taskObjectList.add(taskObject);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return taskObjectList;
    }
}
