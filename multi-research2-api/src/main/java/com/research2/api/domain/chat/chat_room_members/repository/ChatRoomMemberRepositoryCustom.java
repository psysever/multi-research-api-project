package com.research2.api.domain.chat.chat_room_members.repository;

import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;


import java.util.List;

public interface ChatRoomMemberRepositoryCustom {

    List<String> findChatRoomIdsByMbIdPaged(String mbId, int pageNo, int pageSize);

    List<String> findMemberIdsByChatRoomId(String chatRoomId);

    List<ChatRoomMemberResDto> findMembersByChatRoomIdWithMbId(String chatRoomId, String memberId);


}
