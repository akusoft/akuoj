package com.howiezhao.akuoj.akuCommunity.services;

/**
 * @author: LiMing
 * @since: 2020/6/20 17:54
 **/
public interface LikeServices {

    //向redis写入点赞数量

    void setLikeCount(int entiyType, int entiyId,int entityUserId);


    //获取点赞数量
    long getLikeCount(int entiyType, int entiyId);


    //获取点赞的状态
    int getLikeStatus(int entiyType, int entiyId);

    //获取用户的点赞数量
    int getEntityUserCount(int userId);

}
