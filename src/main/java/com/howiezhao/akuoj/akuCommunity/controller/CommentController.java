package com.howiezhao.akuoj.akuCommunity.controller;

import com.howiezhao.akuoj.akuCommunity.dao.Comment;
import com.howiezhao.akuoj.akuCommunity.services.impl.CommentServicesImpl;
import com.howiezhao.akuoj.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @Author: LiMing
 * @Date: 2020/4/4 17:51
 * @Description:
 **/
@Controller
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentServicesImpl commentServices;

    @Autowired
    private HostHolder hostHolder;


    @RequestMapping(value = "/add/{discussId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussId") int discussId, Comment comment){

        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());

        commentServices.insertComment(comment);
        return "redirect:/discuss/detail/"+discussId;
    }
}
