package com.research2.api.domain.chat.chat_messages.repository;


import com.research2.api.domain.chat.chat_messages.model.ChatMessageRead;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageReadRepository extends MongoRepository<ChatMessageRead, String>, ChatMessageReadRepositoryCustom {
    ;

    void deleteAllByChatRoomIdAndMbId(String chatRoomId, String mbId);
}
