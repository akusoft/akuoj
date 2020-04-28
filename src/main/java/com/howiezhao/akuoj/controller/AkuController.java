package com.howiezhao.akuoj.controller;

import com.howiezhao.akuoj.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiMing
 * @since 2020/4/28 15:20
 * this is a description
 * this is another description
 **/
@Controller
@RequestMapping(value = "/lm")
public class AkuController {

    @RequestMapping(value = "/hello")
    public String hello(Model model) {
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setName("张三");
        User user1 = new User();
        user1.setName("李四");
        userList.add(user);
        userList.add(user1);
        model.addAttribute("list", userList);
        return "/index";
    }
}
