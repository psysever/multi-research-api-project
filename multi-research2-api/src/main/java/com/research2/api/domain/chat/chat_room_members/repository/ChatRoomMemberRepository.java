package com.research2.api.domain.chat.chat_room_members.repository;


import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends MongoRepository<ChatRoomMember, String>, ChatRoomMemberRepositoryCustom {

    int countByMbIdAndDelFl(String mbId, int delFl);


    int deleteByChatRoomId(String chatRoomId);

    int deleteByChatRoomIdAndMbId(String chatRoomId, String mbId);

    boolean existsByChatRoomIdAndMbId(String chatRoomId, String mbId, int delFl);

    int countByChatRoomId(String chatRoomId, int delFl);


    ChatRoomMember findFirstByChatRoomIdAndMbId(String chatRoomId, String mbId, int delFl);


}