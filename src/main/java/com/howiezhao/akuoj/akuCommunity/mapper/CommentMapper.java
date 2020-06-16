package com.howiezhao.akuoj.akuCommunity.mapper;

import com.howiezhao.akuoj.akuCommunity.dao.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: LiMing
 * @Date: 2020/3/17 14:55
 * @Description:
 **/
@Mapper
public interface CommentMapper {


    List<Comment> slelctCommentList(@Param("entityType") int entityType, @Param("entityId") int entityId, @Param("offset") int offset, @Param("limit") int limit);

    int selectCommuntCount(@Param("entityType") int entityType, @Param("entityId") int entityId);


    int insertComment(Comment comment);
}
