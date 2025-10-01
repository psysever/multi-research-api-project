package com.research2.api.domain.chat.chat_messages.repository;

import com.research2.api.domain.chat.chat_messages.model.ChatMessage;


import java.util.List;

public interface ChatMessageReadRepositoryCustom {
    int countUnreadMessages(String chatRoomId, String mbId);

    int countAllUnreadMessages(String mbId);

    List<ChatMessage> countUnreadMessagesList(String chatRoomId, String mbId);

    void updateReadMessagesDelFlByChatRoomId(String chatRoomId);
}
