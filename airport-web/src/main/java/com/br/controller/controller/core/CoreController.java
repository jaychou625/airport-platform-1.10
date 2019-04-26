package com.br.controller.controller.core;

import com.br.controller.controller.BaseController;
import com.br.entity.access.Menu;
import com.br.entity.access.Role;
import com.br.entity.user.User;
import com.br.entity.utils.BreadCrumb;
import com.br.entity.utils.Result;
import com.br.service.constant.RequestRouteConstant;
import com.br.service.constant.ViewConstant;
import com.br.service.enumeration.CommonEnumeration;
import com.br.service.enumeration.ShiroEnumeration;
import com.br.service.service.traffic.AewService;
import com.br.service.service.user.MenuService;
import com.br.service.service.user.RoleService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 核心控制器
 *
 * @Author Zero
 * @Date 2019 02 21
 */
@Controller
public class CoreController extends BaseController {

    // 角色服务
    @Autowired
    private RoleService roleService;

    // 菜单服务
    @Autowired
    private MenuService menuService;

    // 预警信息服务
    @Autowired
    private AewService aewService;

    /**
     * 登录视图
     *
     * @return 视图
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_LOGIN)
    public String login(HttpServletRequest request, Model model) {
        Result result = new Result();
        String exception = (String) request.getAttribute("shiroLoginFailure");
        if (exception != null) {
            if (IncorrectCredentialsException.class.getName().equals(exception)) {
                result.setCode(ShiroEnumeration.USERNAME_OR_PWD_ERROR.getCode());
                result.setStatus(ShiroEnumeration.USERNAME_OR_PWD_ERROR.getStatus());
                result.getData().put("error", ShiroEnumeration.USERNAME_OR_PWD_ERROR.getMessage());
            } else if (UnknownAccountException.class.getName().equals(exception)) {
                result.setCode(ShiroEnumeration.UNKNOWN_USER.getCode());
                result.setStatus(ShiroEnumeration.UNKNOWN_USER.getStatus());
                result.getData().put("error", ShiroEnumeration.UNKNOWN_USER.getMessage());
            }
        }
        model.addAttribute("result", result);
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_LOGIN;
    }


    /**
     * 主页
     *
     * @return 视图
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_MAIN, method = RequestMethod.GET)
    public String mainPage(Model model) {
        Result result = new Result();
        User user = this.currentUser();
        Role role = this.roleService.findRolesByUserSeq(user.getUserSeq()).get(0);
        List<Menu> menus = this.menuService.findAll();
        result.getData().put("user", user);
        result.getData().put("role", role);
        result.getData().put("menus", menus);
        model.addAttribute("result", result);
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_MAIN;
    }

    /**
     * 首页
     *
     * @return 视图
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_HOME, method = RequestMethod.GET)
    public String homePage(Model model) {
        Result result = new Result();
        result.setStatus(CommonEnumeration.SUCCESS.getStatus());
        result.setCode(CommonEnumeration.SUCCESS.getCode());
        result.getData().put("breadcrumb", new BreadCrumb[]{new BreadCrumb("首页", RequestRouteConstant.REQUEST_ROUTE_HOME)});
        result.getData().put("zoneId", "4602000001");
        result.getData().put("aewInfos", this.aewService.findAll());
        model.addAttribute("result", result);
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_HOME;
    }

}
