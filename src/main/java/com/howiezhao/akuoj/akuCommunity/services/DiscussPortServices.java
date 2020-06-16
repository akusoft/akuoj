package com.howiezhao.akuoj.akuCommunity.services;


import com.howiezhao.akuoj.akuCommunity.dao.DiscussPost;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: LiMing
 * @Date: 2020/2/29 18:11
 * @Description:
 **/
@Service
public interface DiscussPortServices {
    List<DiscussPost> selectAll(int userId, int offset, int limit);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussDetail(int discussId);

    int findDiscussPostRows(int userId);
}
