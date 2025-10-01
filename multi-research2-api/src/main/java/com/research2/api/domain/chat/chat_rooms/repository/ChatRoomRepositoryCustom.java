package com.research2.api.domain.chat.chat_rooms.repository;

import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomInfoResDto;
import com.research2.api.domain.chat.chat_rooms.dto.GetChatRoomReqDto;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomInfoResDto> findChatRoomsByIdsWithProjection(List<String> roomIds, GetChatRoomReqDto dto);


}
