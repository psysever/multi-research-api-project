package com.research2.api.domain.chat.chat_room_members.service;


import com.research2.api.domain.chat.chat_messages.dto.ChatRoomSummaryMsgDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageReadRepository;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageRepository;
import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import com.research2.api.domain.chat.chat_room_members.repository.ChatRoomMemberRepository;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;
import com.research2.api.domain.chat.chat_rooms.dto.LeaveChatRoomResDto;
import com.research2.api.domain.chat.socket.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberServiceImpl implements ChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisPublisher redisPublisher;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageReadRepository chatMessageReadRepository;

    //User participation (entry) in chat room
    @Override
    public void join(ChatRoomMember member) {
        chatRoomMemberRepository.save(member);
    }


    // Delete all members of a room by chat room ID (call when deleting a room
    @Override
    public void removeMembersByRoomId(String roomId) {
        chatRoomMemberRepository.deleteByChatRoomId(roomId);
    }

    //Delete a member of the room using the chat room ID (call when deleting a room)
    @Override
    public int leaveMembersByRoomId(LeaveChatRoomResDto dto) {
        // [1]
        //Delete the departing member from the DB first
        int deleted = chatRoomMemberRepository.deleteByChatRoomIdAndMbId(dto.getChatRoomId(),
                dto.getMbId());

        // [2] SYSTEM MESSAGE
        ChatMessage leaveMsg = ChatMessage.builder()
                .type(ChatMessage.MessageType.SYSTEM)
                .chatRoomId(dto.getChatRoomId())
                .senderMbId(dto.getMbId())
                .nickName(dto.getNickName())
                .message(dto.getNickName() + " have left.")
                .createId(dto.getMbId())
                .createdAt(LocalDateTime.now())
                .build();

        chatMessageRepository.save(leaveMsg);
        redisPublisher.publish(leaveMsg);

        // [3] Send after checking the member list
        List<ChatRoomMemberResDto> updatedMembers =
                chatRoomMemberRepository.findMembersByChatRoomIdWithMbId(dto.getChatRoomId(), dto.getMbId());

        redisPublisher.publishMembers(dto.getChatRoomId(), updatedMembers);

        messagingTemplate.convertAndSend(
                "/topic/chat/room/members/" + dto.getChatRoomId(),
                updatedMembers
        );


        // [4] summary BoardCast
        ChatRoomSummaryMsgDto summary = ChatRoomSummaryMsgDto.builder()
                .chatRoomId(dto.getChatRoomId())
                .latestMessage(leaveMsg.getMessage())
                .latestSendTime(leaveMsg.getCreatedAt())
                .build();

        List<String> remainingMembers =
                chatRoomMemberRepository.findMemberIdsByChatRoomId(dto.getChatRoomId());

        for (String memberId : remainingMembers) {
            messagingTemplate.convertAndSend("/topic/chat/room/summary/" + memberId, summary);
        }
        int chatMemberCount = chatRoomMemberRepository.countByChatRoomId(dto.getChatRoomId(), 0);
        if (chatMemberCount < 1) {
            chatRoomMemberRepository.deleteByChatRoomId(dto.getChatRoomId());
        }
        chatMessageReadRepository.deleteAllByChatRoomIdAndMbId(dto.getChatRoomId(), dto.getMbId());
        return deleted;
    }


    @Override
    public int findByIsTextNotificationEnabled(String chatRoomId, String mbId) {
        int result = 0;
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findFirstByChatRoomIdAndMbId(chatRoomId, mbId, 0);
        if (chatRoomMember != null) {
            if (chatRoomMember.isTextNotificationEnabled()) {
                result = 1;
            }
        }

        return result;
    }
}

