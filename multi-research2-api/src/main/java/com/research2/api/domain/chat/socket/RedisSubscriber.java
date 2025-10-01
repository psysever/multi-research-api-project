package com.research2.api.domain.chat.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.research2.api.domain.chat.chat_messages.dto.ChatRoomSummaryMsgDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber implements MessageListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        String body = new String(message.getBody(), StandardCharsets.UTF_8);

        try {
            if (channel.startsWith("/topic/chat/room/members/")) {
                ChatRoomMemberResDto[] members = objectMapper.readValue(body, ChatRoomMemberResDto[].class);
                messagingTemplate.convertAndSend(channel, members);
            } else if (channel.startsWith("/topic/chat/room/summary/")) {
                ChatRoomSummaryMsgDto summary = objectMapper.readValue(body, ChatRoomSummaryMsgDto.class);
                messagingTemplate.convertAndSend(channel, summary);
            } else if (channel.startsWith("/topic/chat/room/")) {
                ChatMessage chatMessage = objectMapper.readValue(body, ChatMessage.class);
                messagingTemplate.convertAndSend(channel, chatMessage);
            } else {
                log.warn("Unknown channel: {}", channel);
            }
        } catch (Exception e) {
            log.error("RedisSubscriber 오류", e);
        }
    }
}