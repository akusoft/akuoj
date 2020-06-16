package com.howiezhao.akuoj.advice;

import com.howiezhao.akuoj.utils.AkuOjUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: LiMing
 * @since: 2020/6/14 9:27
 **/
@ControllerAdvice(annotations = Controller.class)
public class ExpectionAdvices {

    private static final Logger logger= LoggerFactory.getLogger(ExpectionAdvices.class);

    @ExceptionHandler(Exception.class)
    public void handleExpetion(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生错误！"+e.getMessage());

        //遍历所有的错误信息

        StackTraceElement[] stackTrace = e.getStackTrace();

        for (StackTraceElement stackTraceElement : stackTrace) {

            logger.error(stackTraceElement.toString());
        }

        //判断是否为异步请求
        String requestType = request.getHeader("x-requested-with");

        //异步
        if("XMLHttpRequest".equals(requestType)){
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(AkuOjUtils.getJSONObject(1,"服务器发生异常！！"));
        }else {
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }
}
