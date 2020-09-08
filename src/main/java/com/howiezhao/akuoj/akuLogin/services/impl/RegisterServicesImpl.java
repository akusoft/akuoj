package com.howiezhao.akuoj.akuLogin.services.impl;

import com.howiezhao.akuoj.akuLogin.dao.EmailKaptcha;
import com.howiezhao.akuoj.akuLogin.dao.LoginTicket;
import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.akuLogin.mapper.LoginTicketMapper;
import com.howiezhao.akuoj.akuLogin.mapper.UserMapper;
import com.howiezhao.akuoj.akuLogin.services.RegisterServices;
import com.howiezhao.akuoj.utils.AkuOjConstant;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import com.howiezhao.akuoj.utils.SendMail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author LiMing
 * @since 2020/5/11 17:22
 **/
@Service
public class RegisterServicesImpl implements RegisterServices,AkuOjConstant{


    @Autowired
    private UserMapper userMapper;

    @Value("${aku.path}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private SendMail sendMail;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Transactional
    @Override
    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();
        if(user==null){
            new IllegalArgumentException("参数不能为空！！");
        }else {
            if(StringUtils.isBlank(user.getUsername())){
                map.put("usernameMsg","账号不能为空！！");
                return map;
            }
            if(StringUtils.isBlank(user.getPassword())){
                map.put("passwordMsg","密码不能为空！！");
                return map;
            }
            if(StringUtils.isBlank(user.getEmail())){
                map.put("emailMsg","邮箱不能为空！！");
                return map;
            }
        }
        //验证账户是否已经注册
        User u = userMapper.selectUserByUserName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","账户已经被注册！！");
            return map;
        }
        u = userMapper.selectUserByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","邮箱已经被注册！！");
            return map;
        }
        //注册用户
        user.setSalt(AkuOjUtils.generateUUID().substring(0,5));
        user.setPassword(AkuOjUtils.md5(user.getPassword()+user.getSalt()));
        user.setStatus(0);
        user.setType(0);
        user.setCreateTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        user.setActivationCode(AkuOjUtils.generateUUID());
        userMapper.insertUser(user);

        //发送邮件
        Context context = new Context();
        context.setVariable("email",user.getEmail());
        String url=domain+contextPath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        context.setVariable("url",url);

        String process = templateEngine.process("/mail/activation", context);
        sendMail.sendEmail(user.getEmail(),"激活邮件",process);
        return map;

    }

    @Override
    public int getActivationStatus(int userId, String code) {
        User user = userMapper.selectUserById(userId);
        if(user.getStatus()==1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId,1);
            return ACTIVATION_SUCCESS;
        }else{
            return ACTIVATION_FAILURE;
        }
    }

    @Transactional
    @Override
    public Map<String, Object> forget(User user) {
        Map<String, Object> map = new HashMap<>();
        if(user==null){
            new IllegalArgumentException("参数不能为空！！");
        }else {
            if(StringUtils.isBlank(user.getEmail())){
                map.put("emailMsg","邮箱不能为空！！");
                return map;
            }
            if(StringUtils.isBlank(user.getKaptcha())){
                map.put("kaptchaMsg","验证码不能为空！！");
                return map;
            }
        }
        //验证验证码是否有效
        EmailKaptcha emailKaptcha = loginTicketMapper.selectKaptch(user.getEmail());
        if(emailKaptcha!=null){

            if(emailKaptcha.getKaptcha()!=user.getKaptcha()){
                map.put("kaptchaMsg","验证码不正确！！");
                return map;
            }
            if(emailKaptcha.getExpired().after(new Date())){
                map.put("kaptchaMsg","验证码已过期！！");
                return map;
            }
        }

        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg","密码不能为空！！");
            return map;
        }

        User user1 = userMapper.selectUserByEmail(user.getEmail());
        String  password=AkuOjUtils.md5(user.getPassword()+user1.getSalt());
        userMapper.updatePassword(user1.getId(),password);
        return map;

    }

    @Transactional
    @Override
    public Map<String, Object> sendKaptcha(String email) {
        Map<String, Object> map = new HashMap<>();
        if(email==null){
            new IllegalArgumentException("参数不能为空！！");
        }
        if(StringUtils.isBlank(email)){
            map.put("emailMsg","邮箱不能为空！！");
            return map;
        }
        //验证邮箱是否已经注册
        User u = userMapper.selectUserByEmail(email);
        if(u==null){
            map.put("emailMsg","邮箱还没有注册，请先注册！！");
            return map;
        }
        //发送邮件
        Context context = new Context();
        context.setVariable("email",email);
        int kaptCha=(int)((Math.random()*9+1)*100000);
        context.setVariable("kaptCha",kaptCha);

        String process = templateEngine.process("/mail/forget", context);
        sendMail.sendEmail(email,"验证码邮件",process);

        //生成验证码
        EmailKaptcha emailKaptcha = new EmailKaptcha();
        emailKaptcha.setExpired(new Date(System.currentTimeMillis()+ 3600*5*1000));
        emailKaptcha.setKaptcha(String.valueOf(kaptCha));
        emailKaptcha.setEmail(email);
        emailKaptcha.setStatus(0);

        EmailKaptcha emailKaptcha1 = loginTicketMapper.selectKaptch(email);
        if(emailKaptcha1!=null){
            loginTicketMapper.updateEmailKapthch(emailKaptcha1.getId());
        }else{
            loginTicketMapper.intsertKaptcha(emailKaptcha);
        }
        return map;

    }
}
