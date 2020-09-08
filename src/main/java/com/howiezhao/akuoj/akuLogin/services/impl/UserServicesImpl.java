package com.howiezhao.akuoj.akuLogin.services.impl;

import com.howiezhao.akuoj.akuLogin.dao.LoginTicket;
import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.akuLogin.mapper.LoginTicketMapper;
import com.howiezhao.akuoj.akuLogin.mapper.UserMapper;
import com.howiezhao.akuoj.akuLogin.services.UserServices;
import com.howiezhao.akuoj.annoation.LoginRequired;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiMing
 * @since 2020/5/11 10:32
 **/
@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Override
    public User selectUserById(int userId) {
        return  userMapper.selectUserById(userId);
    }

    @Override
    public Map<String, Object> login(String username, String password, int expiredTime) {
        Map<String, Object> map = new HashMap<>();

        //判断用户名
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","用户名为空！");
            return map;
        }
        //判断密码
        if(StringUtils.isBlank(password)){
            map.put("passwordMsg","密码为空！");
            return map;
        }

        User user = userMapper.selectUserByUserName(username);

        if(user==null){
            map.put("usernameMsg","用户不存在！");
            return map;
        }
        if(user.getStatus()==0){
            map.put("usernameMsg","用户未激活！");
            return map;
        }

        //校验密码
        String s = AkuOjUtils.md5(password+user.getSalt());
        if(!(s.equals(user.getPassword()))){
            map.put("passwordMsg","密码错误！！");
            return map;
        }

        //生成凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setExpired(new Date(System.currentTimeMillis()+ expiredTime*1000));
        loginTicket.setTicket(AkuOjUtils.generateUUID());
        loginTicket.setUserId(user.getId());
        loginTicket.setStatus(0);

        loginTicketMapper.insertTicket(loginTicket);

        map.put("ticket",loginTicket.getTicket());
        return map;
    }

    @Override
    public LoginTicket findLoginTicket(String ticket) {
        return loginTicketMapper.findLoginTicket(ticket);
    }

    @Override
    public void logout(String ticket) {
        loginTicketMapper.updateStatusByUserId(ticket,1);
    }

    @Override
    public void uploadHeaderUrl(int userId, String headerUrl) {
        userMapper.updateHeaderUrl(userId,headerUrl);
    }

    @Override
    public void updatepassword(int userId, String password) {
        User user = userMapper.selectUserById(userId);
        password=AkuOjUtils.md5(password+user.getSalt());
        userMapper.updatePassword(userId,password);
    }

    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectUserByUserName(username);
    }
}
