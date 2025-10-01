package com.research2.api.domain.chat.chat_rooms.service;


import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageReadRepository;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageRepository;
import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import com.research2.api.domain.chat.chat_room_members.repository.ChatRoomMemberRepository;
import com.research2.api.domain.chat.chat_room_members.service.ChatRoomMemberService;
import com.petnuri.api.domain.chat.chat_rooms.dto.*;
import com.research2.api.domain.chat.chat_rooms.dto.*;
import com.research2.api.domain.chat.chat_rooms.model.ChatRoom;
import com.research2.api.domain.chat.chat_rooms.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReadRepository chatMessageReadRepository;
    private final ChatRoomMemberService chatRoomMemberService;


    @Override
    public ChatRoom createRoom(CreateChatRoomReqDto dto) {


        ChatRoom room = ChatRoom.builder()
                .type("CHAT_ROOM")
                .senderMbId(dto.getMbId())
                .receiverMbId(dto.getAttentionMbId())
                .createId(dto.getMbId())
                .delFl(0)
                .createdYmd(LocalDateTime.now())
                .build();


        ChatRoom newChatRoomResDto = chatRoomRepository.save(room);


        ChatRoomMember newMember = ChatRoomMember.builder()
                .chatRoomId(newChatRoomResDto.getChatRoomId())
                .mbId(dto.getMbId())
                .nickName("JACK")
                .isTextNotificationEnabled(true)
                .delFl(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        chatRoomMemberService.join(newMember);


        ChatRoomMember attentionMember = ChatRoomMember.builder()
                .chatRoomId(newChatRoomResDto.getChatRoomId())
                .mbId(dto.getAttentionMbId())
                .nickName("JACK")
                .isTextNotificationEnabled(true)
                .delFl(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        chatRoomMemberService.join(attentionMember);
        return newChatRoomResDto;
    }


    @Override
    public ChatRoomResDto findRoomsByMember(GetChatRoomReqDto dto) {
        List<String> roomIds = chatRoomMemberRepository.findChatRoomIdsByMbIdPaged(
                dto.getMbId(),
                dto.getPageNo(),
                dto.getPageSize()
        );

        List<ChatRoomInfoResDto> chatRoomInfoResDtoList = chatRoomRepository.findChatRoomsByIdsWithProjection(roomIds, dto);

        chatRoomInfoResDtoList.forEach(room -> {
            //RECENT MESSAGE
            ChatMessage lastMsg = chatMessageRepository.findTopByChatRoomIdAndDelFlOrderByCreatedAtDesc(room.getChatRoomId(), 0);
            if (lastMsg != null) {
                room.setLatestMessage(lastMsg.getMessage());
                room.setLatestMessageTime(lastMsg.getCreatedAt());
            } else {
                room.setLatestMessage("");
                room.setLatestMessageTime(null);
            }


            // UNREAD COUNT MESSAGE
            Integer unreadCount = (int) chatMessageReadRepository.countUnreadMessages(room.getChatRoomId(), dto.getMbId());
            room.setUnreadCount(unreadCount);

            // CHAT MEMBERS
            List<ChatRoomMemberResDto> members = chatRoomMemberRepository.findMembersByChatRoomIdWithMbId(room.getChatRoomId(), dto.getMbId())
                    .stream()
                    .map(member -> ChatRoomMemberResDto.builder()
                            .mbId(member.getMbId())
                            .nickName(member.getNickName())
                            .build())
                    .collect(Collectors.toList());
            room.setMembers(members);
            ChatRoomMember chatRoomMember = chatRoomMemberRepository.findFirstByChatRoomIdAndMbId(room.getChatRoomId(), dto.getMbId(), 0);
            if (chatRoomMember != null) {
                room.setTextNotificationEnabled(chatRoomMember.isTextNotificationEnabled());
            }

        });

        int totalCnt = 0;
        if (dto.getChatType() != null) {
            if (dto.getChatType().equals("PRIVATE")) {
                totalCnt = chatRoomRepository.countByReceiverMbIdAndDelFl(dto.getMbId(), 0);
            } else if (dto.getChatType().equals("ALL")) {
                totalCnt = chatRoomRepository.countBySenderMbIdAndDelFl(dto.getMbId(), 0);
            }
        } else {
            totalCnt = chatRoomMemberRepository.countByMbIdAndDelFl(dto.getMbId(), 0);
        }

        return ChatRoomResDto.builder()
                .chatRoomList(chatRoomInfoResDtoList)
                .totalCnt(totalCnt)
                .build();
    }


    @Override
    public int deleteRoom(String chatRoomId) {
        chatRoomRepository.deleteById(chatRoomId);
        chatMessageRepository.updateMessagesDelFlByChatRoomId(chatRoomId);
        chatMessageReadRepository.updateReadMessagesDelFlByChatRoomId(chatRoomId);
        return chatRoomMemberRepository.deleteByChatRoomId(chatRoomId);
    }


}
