package com.research2.api.domain.chat.chat_rooms.service;


import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomResDto;
import com.research2.api.domain.chat.chat_rooms.dto.CreateChatRoomReqDto;
import com.research2.api.domain.chat.chat_rooms.dto.GetChatRoomReqDto;
import com.research2.api.domain.chat.chat_rooms.model.ChatRoom;


public interface ChatRoomService {
    ChatRoom createRoom(CreateChatRoomReqDto dto);

    ChatRoomResDto findRoomsByMember(GetChatRoomReqDto dto);

    int deleteRoom(String roomId);


}
