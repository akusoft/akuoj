package com.howiezhao.akuoj.akuCommunity.controller;

import com.howiezhao.akuoj.akuCommunity.services.impl.FollowServicesImpl;
import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.config.RedisTemplateConfig;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import com.howiezhao.akuoj.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: LiMing
 * @since: 2020/7/12 9:15
 **/
@Controller
public class FollowController {


    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private FollowServicesImpl followServices;

    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String  follow(int entityType,int entityId){

        User user = hostHolder.getUser();

        if(user!=null){
            followServices.follow(user.getId(),entityType,entityId);
        }
        return AkuOjUtils.getJSONObject(0,"已关注");
    }

    @RequestMapping(value = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String  unfollow(int entityType,int entityId){

        User user = hostHolder.getUser();

        if(user!=null){
            followServices.unfollow(user.getId(),entityType,entityId);
        }
        return AkuOjUtils.getJSONObject(0,"已取关");
    }

}
