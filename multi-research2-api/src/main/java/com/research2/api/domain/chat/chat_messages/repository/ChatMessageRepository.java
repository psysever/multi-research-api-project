package com.research2.api.domain.chat.chat_messages.repository;

import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepositoryCustom {

    ChatMessage findTopByChatRoomIdAndDelFlOrderByCreatedAtDesc(String chatRoomId, int delFl);

    int countByChatRoomId(String chatRoomId, int delFl);


}