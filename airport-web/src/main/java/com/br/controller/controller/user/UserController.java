package com.br.controller.controller.user;

import com.br.service.service.user.UserService;
import com.br.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户控制器
 * @Author zyx
 * @Date 2019 03 01
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/testUser", method = RequestMethod.GET)
    @ResponseBody
    public User testUser(){
        return this.userService.find(1, null);
    }
}
