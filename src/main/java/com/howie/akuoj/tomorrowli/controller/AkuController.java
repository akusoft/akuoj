package com.howie.akuoj.tomorrowli.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LiMing
 * @Date: 2020/4/28 15:20
 * @Description:
 **/
@Controller
@RequestMapping(value = "/lm")
public class AkuController {

    @RequestMapping(value = "/hello")
    public String hello() {
        return "/index";
    }
}
