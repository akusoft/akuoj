package com.howiezhao.akuoj.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author LiMing
 * @since 2020/3/10 18:09
 **/
public class CookieUtil {

    //获取对应的cookie值
    public  static String getValue(HttpServletRequest request, String cookieName){

        if(request==null || StringUtils.isBlank(cookieName)){
            new IllegalArgumentException("参数不能为空！");
        }else {
            Cookie[] cookies = request.getCookies();
            if(cookies!=null){
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals(cookieName)){
                        return cookie.getValue();
                    }
                }
            }

        }
        return null;
    }
}
