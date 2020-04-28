package com.howie.akuoj.tomorrowli.controller;

import com.howie.akuoj.tomorrowli.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: LiMing
 * @Date: 2020/4/28 15:20
 * @Description:
 **/
@Controller
@RequestMapping(value = "/lm")
public class AkuController {

    @RequestMapping(value = "/hello")
    public String hello(Model model) {
        List<User> userlist = new ArrayList<>();
        User user = new User();
        user.setName("张三");
        User user1 = new User();
        user1.setName("李四");
        userlist.add(user);
        userlist.add(user1);
        model.addAttribute("list",userlist);
        return "/index";
    }
}
