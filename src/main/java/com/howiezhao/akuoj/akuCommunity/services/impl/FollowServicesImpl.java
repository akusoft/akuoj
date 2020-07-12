package com.howiezhao.akuoj.akuCommunity.services.impl;

import com.howiezhao.akuoj.akuCommunity.services.FollowServices;
import com.howiezhao.akuoj.config.RedisTemplateConfig;
import com.howiezhao.akuoj.utils.RedisSetKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author: LiMing
 * @since: 2020/7/12 9:19
 **/
@Service
public class FollowServicesImpl implements FollowServices {


    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void follow(int userId, int entityType, int entityId) {

        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {


                String followeeKey = RedisSetKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisSetKeyUtil.getFollowerKey(entityType, entityId);

                redisOperations.multi();

                redisOperations.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                redisOperations.opsForZSet().add(followerKey,userId,System.currentTimeMillis());


                return redisOperations.exec();
            }
        });
    }

    @Override
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {


                String followeeKey = RedisSetKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisSetKeyUtil.getFollowerKey(entityType, entityId);

                redisOperations.multi();

                redisOperations.opsForZSet().remove(followeeKey,entityId);
                redisOperations.opsForZSet().remove(followerKey,userId);

                return redisOperations.exec();
            }
        });
    }

    @Override
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisSetKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    @Override
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisSetKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    @Override
    public boolean hasFollowee(int userId, int entityType, int entityId) {
        String followeeKey = RedisSetKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey,entityId)!=null;
    }
}
