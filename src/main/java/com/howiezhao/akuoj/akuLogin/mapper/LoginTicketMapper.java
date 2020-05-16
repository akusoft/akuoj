package com.howiezhao.akuoj.akuLogin.mapper;

import com.howiezhao.akuoj.akuLogin.dao.EmailKaptcha;
import com.howiezhao.akuoj.akuLogin.dao.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.websocket.server.PathParam;

/**
 * @Author: LiMing
 * @Date: 2020/3/8 22:55
 * @Description:
 **/
@Mapper
public interface LoginTicketMapper {

    int updateStatusByUserId(@Param("ticket") String ticket, @Param("status") int status);

    int insertTicket(LoginTicket ticket);

    LoginTicket findLoginTicket(@Param("ticket") String ticket);

    int intsertKaptcha(EmailKaptcha kaptcha);

    EmailKaptcha selectKaptch(@Param("email") String email);

    int updateEmailKapthch(@Param("id") int id);

}
