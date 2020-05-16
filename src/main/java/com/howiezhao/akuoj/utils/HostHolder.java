package com.howiezhao.akuoj.utils;

import com.howiezhao.akuoj.akuLogin.dao.User;
import org.springframework.stereotype.Component;

/**
 * @author: LiMing
 * @since: 2020/5/11 10:51
 *
 **/
@Component
public class HostHolder {
    //用户保存用户信息和session类似
    private ThreadLocal<User> users=new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }
}
