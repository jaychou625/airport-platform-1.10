package com.br.controller.exception;

import com.br.service.constant.ViewConstant;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(AuthorizationException.class)
    public String unAuthed() {
        return ViewConstant.VIEW_DIR_CORE + ViewConstant.VIEW_FILE_CORE_UNAUTHED;
    }

}
