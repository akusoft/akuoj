package com.howiezhao.akuoj.akuCommunity.services.impl;


import com.howiezhao.akuoj.akuCommunity.dao.Comment;
import com.howiezhao.akuoj.akuCommunity.mapper.CommentMapper;
import com.howiezhao.akuoj.akuCommunity.mapper.DisCussPortMapper;
import com.howiezhao.akuoj.akuCommunity.services.CommentServices;
import com.howiezhao.akuoj.utils.AkuOjConstant;
import com.howiezhao.akuoj.utils.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: LiMing
 * @Date: 2020/3/17 14:53
 * @Description:
 **/
@Service
public class CommentServicesImpl implements CommentServices,AkuOjConstant {


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DisCussPortMapper disCussPortMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Comment> slelctCommentList(int entityType, int entityId, int offset, int limit) {
        return commentMapper.slelctCommentList(entityType,entityId,offset,limit);
    }

    @Override
    public int selectCommuntCount(int entityType, int entityId) {
        return commentMapper.selectCommuntCount(entityType,entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    @Override
    public int insertComment(Comment comment) {

        if(comment==null){
            new IllegalArgumentException("参数为空");
        }

        //添加评论
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));

        int rows = commentMapper.insertComment(comment);

        if(comment.getEntityType()==REPLY_POST){
            int count = commentMapper.selectCommuntCount(comment.getEntityType(), comment.getEntityId());
            disCussPortMapper.updateDiscussCount(comment.getEntityId(),count);
        }
        return rows;
    }
}
