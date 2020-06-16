package com.howiezhao.akuoj.aspect;

import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;

import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: LiMing
 * @since: 2020/6/14 10:43
 **/
@Component
@Aspect
public class ServiceLogAspect {


    private static final Logger logger= LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.howiezhao.akuoj.*.services.*.*(..))")
    public void cutPoint(){

    }


    @Before("cutPoint()")
    public void before(JoinPoint joinPoint){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String ip = requestAttributes.getRequest().getRemoteHost();

        String date = new SimpleDateFormat("yyyy-MM-dd: HH:ss:mm").format(new Date());

        String target=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();

        logger.info(String.format("用户[%s],在[%s],访问[%s],",ip,date,target));
    }
}
