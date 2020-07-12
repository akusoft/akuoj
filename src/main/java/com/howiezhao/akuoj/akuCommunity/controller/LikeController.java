package com.howiezhao.akuoj.akuCommunity.controller;

import com.howiezhao.akuoj.akuCommunity.services.impl.LikeServicesImpl;
import com.howiezhao.akuoj.utils.AkuOjConstant;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: LiMing
 * @since: 2020/6/20 18:11
 **/
@Controller
public class LikeController {

    @Autowired
    private LikeServicesImpl services;

    @RequestMapping(value = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String getLike(int entityType,int entityId,int entityUserId){

        Map<String,Object> map = new HashMap();

        services.setLikeCount(entityType,entityId,entityUserId);

        long likeCount = services.getLikeCount(entityType, entityId);

        int likeStatus = services.getLikeStatus(entityType, entityId);

        map.put("likeCount",likeCount);

        map.put("likeStatus",likeStatus);

        return AkuOjUtils.getJSONObject(0,null,map);

    }
}
