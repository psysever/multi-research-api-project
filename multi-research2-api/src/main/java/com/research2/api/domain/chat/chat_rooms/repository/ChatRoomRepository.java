package com.research2.api.domain.chat.chat_rooms.repository;

import com.research2.api.domain.chat.chat_rooms.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>, ChatRoomRepositoryCustom {


    int countBySenderMbIdAndDelFl(String senderMbId, int delFl);

    int countByReceiverMbIdAndDelFl(String receiverMbId, int delFl);


}