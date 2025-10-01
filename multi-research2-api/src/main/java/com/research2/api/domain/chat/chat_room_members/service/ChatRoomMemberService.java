package com.research2.api.domain.chat.chat_room_members.service;


import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import com.research2.api.domain.chat.chat_rooms.dto.LeaveChatRoomResDto;


public interface ChatRoomMemberService {

    void join(ChatRoomMember member);
    
    void removeMembersByRoomId(String roomId);

    int leaveMembersByRoomId(LeaveChatRoomResDto dto);

    int findByIsTextNotificationEnabled(String chatRoomId, String mbId);

}