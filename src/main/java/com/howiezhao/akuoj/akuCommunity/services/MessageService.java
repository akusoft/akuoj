package com.howiezhao.akuoj.akuCommunity.services;

import com.howiezhao.akuoj.akuCommunity.dao.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public interface MessageService {


     List<Message> findConversations(int userId, int offset, int limit);

     int findConversationCount(int userId);

     List<Message> findLetters(String conversationId, int offset, int limit);

     int findLetterCount(String conversationId);

     int findLetterUnreadCount(int userId, String conversationId) ;

     int addMessage(Message message) ;

     int readMessage(List<Integer> ids);

}
