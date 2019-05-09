package com.br.controller.controller.user;

import com.br.entity.task.TaskObject;
import com.br.service.service.task.handler.ApronDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 用户控制器
 * @Author zyx
 * @Date 2019 03 01
 */
@Controller
public class UserController {

    @Autowired
    private ApronDataHandler apronDataHandler;

    @RequestMapping(value = "/testTaskStatus", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskObject> testTaskStatus(){
        return apronDataHandler.getApronData();
    }
}
