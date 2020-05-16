package com.howiezhao.akuoj.akuLogin.controller;

import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.akuLogin.services.UserServices;
import com.howiezhao.akuoj.akuLogin.services.impl.UserServicesImpl;
import com.howiezhao.akuoj.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: LiMing
 * @since: 2020/5/11 10:28
 **/
@Controller
public class IndexController {

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserServicesImpl userServices;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getDiscussPost(Model model){

        User user = hostHolder.getUser();
        if(user!=null){
            model.addAttribute("user",userServices.selectUserById(user.getId()));
        }
        return "index";
    }

}
