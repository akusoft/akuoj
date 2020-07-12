package com.howiezhao.akuoj.akuCommunity.controller;


import com.howiezhao.akuoj.akuCommunity.dao.Comment;
import com.howiezhao.akuoj.akuCommunity.dao.DiscussPost;
import com.howiezhao.akuoj.akuCommunity.dao.Page;
import com.howiezhao.akuoj.akuCommunity.services.DiscussPortServices;
import com.howiezhao.akuoj.akuCommunity.services.impl.CommentServicesImpl;
import com.howiezhao.akuoj.akuCommunity.services.impl.LikeServicesImpl;
import com.howiezhao.akuoj.akuLogin.dao.User;
import com.howiezhao.akuoj.akuLogin.services.UserServices;
import com.howiezhao.akuoj.utils.AkuOjConstant;
import com.howiezhao.akuoj.utils.AkuOjUtils;
import com.howiezhao.akuoj.utils.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author: LiMing
 * @Date: 2020/2/29 18:10
 * @Description:
 **/

@Controller
@RequestMapping(value = "/community")
public class DiscussPortController implements AkuOjConstant {


    @Autowired
    private DiscussPortServices discussPortServices;

    @Autowired
    private UserServices userServices;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private CommentServicesImpl commentServices;

    @Autowired
    private LikeServicesImpl likeServices;



    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getDiscussPost(Model model, Page page){
        // 方法调用前,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.

        page.setRows(discussPortServices.findDiscussPostRows(0));
        page.setPath("/community/communityIndex");


        List<DiscussPost> discussPosts = discussPortServices.selectAll(0,page.getOffset(),page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        for (DiscussPost discussPost : discussPosts) {
            Map<String, Object> map = new HashMap<>();
            long likeCount = likeServices.getLikeCount(REPLY_POST, discussPost.getId());
            map.put("likeCount",likeCount);
            map.put("user",userServices.selectUserById(discussPost.getUserId()));
            map.put("post",discussPost);
            list.add(map);
        }
        model.addAttribute("disPost",list);
        return "/community/communityIndex";
    }


    @RequestMapping(value = "/discuss/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPort(String title,String content ){

        User user = hostHolder.getUser();
        if(user==null){
            return AkuOjUtils.getJSONObject(403,"你还没有登录！");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPost.setCommentCount(0);
        discussPost.setScore(0D);
        discussPost.setStatus(0);
        discussPost.setType(0);

        discussPortServices.insertDiscussPost(discussPost);
        return AkuOjUtils.getJSONObject(0,"发布成功");
    }

    @RequestMapping(value = "/discuss/detail/{discussId}")
    public String getDiscussPostDetail(@PathVariable("discussId") int discussId, Model model, Page page){
        DiscussPost post = discussPortServices.selectDiscussDetail(discussId);
        model.addAttribute("post",post);
        User user = userServices.selectUserById(post.getUserId());
        model.addAttribute("user",user);

        //分页
        page.setLimit(5);
        page.setPath("/community/discuss/detail/"+discussId);
        page.setRows(post.getCommentCount());

        model.addAttribute("likeCount",likeServices.getLikeCount(REPLY_POST,discussId));
        model.addAttribute("likeStatus",likeServices.getLikeStatus(REPLY_POST,discussId));

        List<Map<String, Object>> commentList = new ArrayList<>();
        //帖子回复
        List<Comment> comments = commentServices.slelctCommentList(REPLY_POST, post.getId(), page.getOffset(), page.getLimit());

        if(comments!=null){
            for (Comment comment : comments) {
                Map<String, Object> pcom = new HashMap<>();
                pcom.put("user",userServices.selectUserById(comment.getUserId()));
                pcom.put("comment",comment);
                pcom.put("likeCount",likeServices.getLikeCount(REPLY_POST,discussId));
                pcom.put("likeStatus",likeServices.getLikeStatus(REPLY_POST,discussId));
                //评论回复
                List<Map<String, Object>> replyList = new ArrayList<>();

                List<Comment> commentReply = commentServices.slelctCommentList(REPLY_COMMNET, comment.getId(), 0, Integer.MAX_VALUE);

                if(commentReply!=null){
                    for (Comment comment1 : commentReply) {
                        Map<String, Object> rcom = new HashMap<>();
                        rcom.put("user",userServices.selectUserById(comment1.getUserId()));
                        rcom.put("replyComment",comment1);
                        rcom.put("likeCount",likeServices.getLikeCount(REPLY_COMMNET,comment1.getId()));
                        rcom.put("likeStatus",likeServices.getLikeStatus(REPLY_COMMNET,comment1.getId()));
                        //回复目标
                        User target=  comment1.getTargetId()==0?null:userServices.selectUserById(comment1.getTargetId());
                        rcom.put("target",target);
                        replyList.add(rcom);

                    }
                }
                pcom.put("reply",replyList);
                //回复的数量
                pcom.put("replyCount", commentServices.selectCommuntCount(REPLY_COMMNET,comment.getId()));

                commentList.add(pcom);

            }
        }
        model.addAttribute("commentList",commentList);


        return "/community/discuss-detail";
    }


}
