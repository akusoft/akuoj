package com.howiezhao.akuoj.akuCommunity.services.impl;

import com.howiezhao.akuoj.akuCommunity.services.LikeServices;
import com.howiezhao.akuoj.utils.HostHolder;
import com.howiezhao.akuoj.utils.RedisSetKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author: LiMing
 * @since: 2020/6/20 18:00
 **/
@Service
public class LikeServicesImpl implements LikeServices {


    @Autowired
    private RedisTemplate redisTemplate;


    @Autowired
    private HostHolder hostHolder;

    @Override
    public void setLikeCount(int entiyType, int entiyId,int entityUserId) {
        Integer userId = hostHolder.getUser().getId();
        /*Integer userId = hostHolder.getUser().getId();
        String rediskey = RedisSetKeyUtil.getEntityLikeKey(entiyType, entiyId);
        Boolean member = redisTemplate.opsForSet().isMember(rediskey, userId);
        if(member){
            redisTemplate.opsForSet().remove(rediskey,userId);
        }else {
            redisTemplate.opsForSet().add(rediskey,userId);
        }*/
        //redis支持事务
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {

                String rediskey = RedisSetKeyUtil.getEntityLikeKey(entiyType, entiyId);
                String userLikeKey = RedisSetKeyUtil.getUserLikeKey(entityUserId);
                Boolean member = redisOperations.opsForSet().isMember(rediskey, userId);

                redisOperations.multi();
                if(member){
                    redisOperations.opsForSet().remove(rediskey,userId);
                    redisOperations.opsForValue().decrement(userLikeKey);
                }else {
                    redisOperations.opsForSet().add(rediskey,userId);
                    redisOperations.opsForValue().increment(userLikeKey);
                }


                return redisOperations.exec();
            }
        });
    }

    @Override
    public long getLikeCount(int entiyType, int entiyId) {

        String rediskey = RedisSetKeyUtil.getEntityLikeKey(entiyType, entiyId);
        return redisTemplate.opsForSet().size(rediskey);
    }

    @Override
    public int getLikeStatus(int entiyType, int entiyId) {
        String rediskey = RedisSetKeyUtil.getEntityLikeKey(entiyType, entiyId);
        Integer userId = hostHolder.getUser().getId();
        Boolean member = redisTemplate.opsForSet().isMember(rediskey, userId);

        return member?1:0;
    }

    @Override
    public int getEntityUserCount(int userId){
        String userLikeKey = RedisSetKeyUtil.getUserLikeKey(userId);
        Integer userCount = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return userCount==null?0:userCount.intValue();
    }
}
