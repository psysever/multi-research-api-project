package com.research2.api.domain.chat.chat_messages.repository;


import com.research2.api.domain.chat.chat_messages.model.ChatMessage;


import java.util.List;

public interface ChatMessageRepositoryCustom {

    List<ChatMessage> findChatRoomIdsByPaged(String mbId, int pageNo, int pageSize);

    void updateMessagesDelFlByChatRoomId(String chatRoomId);


}
