package com.br.controller.exception;

import com.br.constant.ViewConstant;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理
 * @Author Zero
 * @Date 2019 05 25
 */
@ControllerAdvice
public class GlobalException {

    /**
     * 未授权
     * @return 未授权视图
     */
    @ExceptionHandler(AuthorizationException.class)
    public String unAuthed() {
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_UNAUTHED;
    }

}
