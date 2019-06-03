package com.br.controller.controller.core;

import com.br.constant.RequestRouteConstant;
import com.br.constant.ViewConstant;
import com.br.constant.enumeration.CommonEnumeration;
import com.br.controller.controller.BaseController;
import com.br.entity.access.Menu;
import com.br.entity.access.Role;
import com.br.entity.core.User;
import com.br.entity.utils.BreadCrumb;
import com.br.entity.utils.Result;
import com.br.service.aew.AewService;
import com.br.service.user.MenuService;
import com.br.service.user.RoleService;
import com.br.utils.CrypUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    // 密码工具类
    @Autowired
    private CrypUtils crypUtils;

    /**
     * 登录视图
     *
     * @return 视图
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_LOGIN)
    public String login() {
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
        result.getData().put("aewInfos", this.aewService.findAll());
        model.addAttribute("result", result);
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_HOME;
    }

    /**
     * 未授权页面
     *
     * @return 视图
     */
    @RequestMapping(value = RequestRouteConstant.REQUEST_ROUTE_UNAUTHED, method = RequestMethod.GET)
    public String unAuthPage() {
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_UNAUTHED;
    }

}

