package com.obtk.utils;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

//@ControllerAdvice统一异常类，对所有控制器的异常处理,通过@ExceptionHandle的属性值，将异常分类处理。
@ControllerAdvice
public class ExceptionController {

    //跳转至视图页面
    //@ExceptionHandler只对某一个控制器里的异常进行处理
    @ExceptionHandler(value = IOException.class)
    public String error1(Exception e){
        System.out.println("Io异常错误："+e);
        return "error";
    }

    @ExceptionHandler(value = ArithmeticException.class)
    public String error2(Exception e){
        System.out.println("算术运行异常："+e.getMessage());
        return "error";
    }
}
