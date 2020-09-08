package com.howiezhao.akuoj.utils;

/**
 * @author LiMing
 * @since 2020/3/4 11:47
 **/
public interface AkuOjConstant {


    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;


    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE=2;

    /**
     * 默认超时时间
     */
    int DEFAULT_EXPIRED_TIME=3600 * 12;

    /**
     *记住我的超时时间
     */

    int REMEMBER_EXPIRED_TIME=3600 * 24 *100;


    /**
     * 回复帖子
     */

    int REPLY_POST=1;


    /**
     * 回复评论
     */
    int REPLY_COMMNET=2;

    /**
     * 用户实体
     */
    int REPLY_USER=3;







}
