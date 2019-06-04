package com.br.log;

import com.br.entity.core.User;
import com.br.entity.log.InterfaceLog;
import com.br.log.annotation.BizOperation;
import com.br.service.log.LogService;
import com.br.utils.DateUtils;
import com.br.utils.IpAddrUtils;
import com.br.utils.SubjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 接口日志切面
 *
 * @Author Zero
 * @Date 2019 06 03
 */

@Aspect
public class InterfaceLogAspect {


    // IP地址工具类
    @Autowired
    private IpAddrUtils ipAddrUtils;

    // 时间工具类
    @Autowired
    private DateUtils dateUtils;

    // Subject 工具类
    @Autowired
    private SubjectUtils subjectUtils;

    // 日志服务
    @Autowired
    private LogService logService;


    /**
     * controller 切入点
     */
    @Pointcut("execution(* com.br.controller.controller.*.*.*(..))")
    public void controllerPointcut() {
    }


    /**
     * 前置方法
     */
    @Before("controllerPointcut()")
    public void beforeExecute(JoinPoint joinPoint) {
        /*--------------日志记录-----------------*/
        logRecord(joinPoint, "controller");
        System.out.println("-> before ... ... ... ... ...");
    }

    /**
     * 日志记录
     */
    public void logRecord(JoinPoint joinPoint, String logTier) {
        /*--------------获取切入点方法------------------*/
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        /*--------------获取业务操作注解类------------------*/
        BizOperation bizOperation = method.getAnnotation(BizOperation.class);
        /*--------------获取业务操作------------------*/
        String requestBizOperation = null;
        if (!ObjectUtils.isEmpty(bizOperation)) {
            requestBizOperation = bizOperation.value();
        }
        /*--------------获取请求类名------------------*/
        String requestClassName = joinPoint.getTarget().getClass().getName();
        /*--------------获取请求方法名------------------*/
        String requestMethodName = method.getName();
        /*--------------获取请求参数------------------*/
        Object[] requestArgs = joinPoint.getArgs();
        String requestArgsString = "";
        for(Object o : requestArgs){
            requestArgsString += o.toString() + ",";
        }
        /*--------------获取HTTP请求属性------------------*/
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        /*--------------获取HTTP请求对象------------------*/
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        /*--------------获取HTTP请求IP地址------------------*/
        String requestIp = this.ipAddrUtils.getIpAddress(request);
        /*--------------获取HTTP请求地址------------------*/
        String requestUrl = request.getRequestURI();
        /*--------------获取HTTP请求时间------------------*/
        String requestTime = request.getParameter("_");
        /*--------------判断原始请求时间是否为空------------------*/
        Long requestTimestamp = 0L;
        if (StringUtils.isEmpty(requestTime)) {
            requestTimestamp = System.currentTimeMillis();
        }else{
            requestTimestamp = Long.parseLong(requestTime);
        }
        /*--------------判断原始请求时间转日期类型-----------------*/
        Date requestDateTime = this.dateUtils.longToDate(requestTimestamp);
        /*--------------获取当前操作用户-----------------*/
        User user = this.subjectUtils.currentUser();
        /*--------------判断用户是否为空-----------------*/
        String requestUserName = null;
        if (!ObjectUtils.isEmpty(user)) {
            requestUserName = user.getUserName();
        } else {
            requestUserName = "unknown";
        }
        /*--------------保存日志信息-----------------*/
        InterfaceLog interfaceLog = new InterfaceLog();
        interfaceLog.setInterfaceLogTier(logTier);
        interfaceLog.setInterfaceLogBizOperation(requestBizOperation);
        interfaceLog.setInterfaceLogClassName(requestClassName);
        interfaceLog.setInterfaceLogMethodName(requestMethodName);
        /*--------------判断参数是否为空-----------------*/
        if(requestArgsString.equals("{},")){
            requestArgsString = "";
        }
        if(!StringUtils.isEmpty(requestArgsString) && !requestArgsString.equals("{},")){
            requestArgsString = requestArgsString.substring(0, requestArgsString.length() - 1);
        }
        interfaceLog.setInterfaceLogArgs(requestArgsString);
        interfaceLog.setInterfaceLogRequestIp(requestIp);
        interfaceLog.setInterfaceLogRequestUrl(requestUrl);
        interfaceLog.setInterfaceLogTime(requestDateTime);
        interfaceLog.setInterfaceLogTimestamp(requestTimestamp);
        interfaceLog.setInterfaceLogUserName(requestUserName);

        /*--------------存储日志到数据库-----------------*/
        this.logService.add(interfaceLog);
    }
}
