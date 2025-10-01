package com.research2.api.domain.chat.socket;


import com.research2.api.domain.chat.chat_messages.dto.ChatRoomSummaryMsgDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    //채팅 메시지 전송
    public void publish(ChatMessage message) {
        redisTemplate.convertAndSend("/topic/chat/room/" + message.getChatRoomId(), message);
    }

    // 참여자 리스트 전송
    public void publishMembers(String roomId, List<ChatRoomMemberResDto> updatedMembers) {
        redisTemplate.convertAndSend("/topic/chat/room/members/" + roomId, updatedMembers);

    }

    // 채팅방 요약 메시지 전송
    public void publishSummary(String memberId, ChatRoomSummaryMsgDto summary) {
        redisTemplate.convertAndSend("/topic/chat/room/summary/" + memberId, summary);
    }

}