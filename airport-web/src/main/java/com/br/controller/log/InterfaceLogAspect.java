package com.br.controller.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 接口日志AOP
 *
 * @Author Zero
 * @Date 2019 06 02
 */
@Aspect
@Component
public class InterfaceLogAspect {

    /**
     * 控制层切入点
     */
    @Pointcut("execution(public * com.br.controller.controller.*.*(..))")
    public void controllerLogPointcut() {
    }


    @After("controllerLogPointcut()")
    public void logBeforeController(JoinPoint joinPoint) {
        System.out.println("beforeController...");
    }

}
