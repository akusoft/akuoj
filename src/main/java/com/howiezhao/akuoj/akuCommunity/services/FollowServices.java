package com.howiezhao.akuoj.akuCommunity.services;

/**
 * @author: LiMing
 * @since: 2020/7/12 9:19
 **/
public interface FollowServices {

    //关注用户
    public void follow(int userId,int entityType,int entityId);

    //取消关注
    public void unfollow(int userId,int entityType,int entityId);


    //获取关注用户的数量
    long getFolloweeCount(int userId,int entityType);

    //获取粉丝的数量
    long getFollowerCount(int entityType,int entityId);

    //判断用户是否是已关注
    boolean hasFollowee(int userId,int entityType,int entityId);
}
