package com.research2.api.domain.chat.chat_messages.service;


import com.research2.api.domain.chat.chat_messages.dto.ChatMessageReqDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatMessageResDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatReadReqDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatRoomSummaryMsgDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_messages.model.ChatMessageRead;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageReadRepository;
import com.research2.api.domain.chat.chat_messages.repository.ChatMessageRepository;
import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import com.research2.api.domain.chat.chat_room_members.repository.ChatRoomMemberRepository;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;
import com.research2.api.domain.chat.socket.RedisPublisher;
import com.research2.api.domain.chat.config.S3Uploader;
//import com.petnuri.api.domain.fcm_push_message.service.FcmPushMessageService;
import com.research2.api.domain.chat.utils.MultipartFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final RedisPublisher redisPublisher;
    //    private final FcmPushMessageService pushMessageService;
    private final ChatMessageReadRepository chatMessageReadRepository;
    private final S3Uploader s3Uploader;


    @Override
    public void sendMessage(ChatMessage chat) {
        String mbId = chat.getSenderMbId(); // MBID
        String receiverMbId = chat.getReceiverMbId();
        String chatRoomId = chat.getChatRoomId();
        String nickName = chat.getNickName();


        // [1] Check if this is your first time entering the site
        boolean isFirstEnter = chatRoomMemberRepository.existsByChatRoomIdAndMbId(chatRoomId, mbId, 0);

        if (!isFirstEnter) {
            // [2] ChatRoomMember Registration
            ChatRoomMember newMember = ChatRoomMember.builder()
                    .chatRoomId(chatRoomId)
                    .mbId(mbId)
                    .nickName(nickName)
                    .isTextNotificationEnabled(true)
                    .delFl(0)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            chatRoomMemberRepository.save(newMember);

            // [3]Create and send an entry message
            ChatMessage enterMsg = ChatMessage.builder()
                    .chatRoomId(chatRoomId)
                    .senderMbId(mbId)
                    .receiverMbId(receiverMbId)
                    .nickName(nickName)
                    .type(ChatMessage.MessageType.SYSTEM)
                    .message(chat.getNickName() + " have entered.")
                    .delFl(0)
                    .createId(chat.getSenderMbId())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            chatMessageRepository.save(enterMsg);
            redisPublisher.publish(enterMsg);
        }

        // [4]General message processing
        chat.setType(ChatMessage.MessageType.MESSAGE);
        chat.setCreateId(chat.getSenderMbId());
        chat.setCreatedAt(LocalDateTime.now());
        chat.setUpdatedAt(LocalDateTime.now());


        chatMessageRepository.save(chat);
        redisPublisher.publish(chat);

        // Send real-time member list updates
        List<ChatRoomMemberResDto> updatedMembers =
                chatRoomMemberRepository.findMembersByChatRoomIdWithMbId(chatRoomId, mbId);

        if (!updatedMembers.isEmpty()) {
            redisPublisher.publishMembers(chatRoomId, updatedMembers);
        }

        // mbId
        List<String> mbIdList = updatedMembers.stream()
                .map(ChatRoomMemberResDto::getMbId)
                .collect(Collectors.toList());

        Map<String, String> data = new HashMap<>();
        data.put("menuTypeId", chatRoomId);
        data.put("senderMbId", mbId);
        data.put("menuType", "PET_CHAT");

// FCM PUSH MESSAGE
//        PushMessageReqDto pushMessageReqDto = PushMessageReqDto.builder()
//                .mbId(mbIdList)
//                .title("CHAT TITLE")
//                .body(
//                        chat.getType() == ChatMessage.MessageType.IMAGE
//                                ? chat.getNickName() + " sent a photo."
//                                : chat.getNickName() + " sent a message."
//                )
//                .data(new ObjectMapper().writeValueAsString(data))
//                .build();
//          Separate service implementation
//      pushMessageService.sendSinglePushMessage(pushMessageReqDto);

        ChatRoomSummaryMsgDto summary = ChatRoomSummaryMsgDto.builder()
                .chatRoomId(chatRoomId)
                .latestMessage(chat.getMessage())
                .latestSendTime(chat.getCreatedAt())
                .build();


        // 5.Sender reads immediately
        for (ChatRoomMemberResDto member : updatedMembers) {

            if (member.getMbId().equals(mbId)) {
                // The sender reads it immediately
                ChatMessageRead read = ChatMessageRead.builder()
                        .chatMessageId(chat.getChatRoomId())
                        .chatRoomId(chatRoomId)
                        .mbId(mbId)
                        .delFl(0)
                        .createdAt(LocalDateTime.now())
                        .build();
                chatMessageReadRepository.save(read);
            }
        }

        for (ChatRoomMemberResDto memberId : updatedMembers) {
            int unreadCount = chatMessageReadRepository.countUnreadMessages(chatRoomId, memberId.getMbId());
            summary.setUnreadCount(unreadCount);
            int unreadAllCount = chatMessageReadRepository.countAllUnreadMessages(memberId.getMbId());
            summary.setUnreadTotalCount(unreadAllCount);
        }
    }

    public int countAllUnreadMessages(String mbId) {
        return chatMessageReadRepository.countAllUnreadMessages(mbId);
    }

    public String sendImageMessage(MultipartFile file) {
        return s3Uploader.upload(file, "chat-images");
    }


    public String sendBase64Image(String base64, String fileName) throws IOException {
        MultipartFile file = MultipartFileUtil.createFromBase64(base64, fileName, "image/jpeg");
        return s3Uploader.upload(file, "chat-images");

    }


    public int markMessagesAsRead(ChatReadReqDto dto) {
        List<ChatMessage> messages = chatMessageReadRepository.countUnreadMessagesList(dto.getChatRoomId(), dto.getMbId());

        for (ChatMessage message : messages) {
            ChatMessageRead read = ChatMessageRead.builder()
                    .chatMessageId(message.getId())
                    .chatRoomId(dto.getChatRoomId())
                    .mbId(dto.getMbId())
                    .createdAt(LocalDateTime.now())
                    .build();
            chatMessageReadRepository.save(read);
        }
        return 1;
    }

    @Override
    public ChatMessageResDto getChatMessageList(ChatMessageReqDto dto) {
        List<ChatMessage> messagesList = chatMessageRepository.findChatRoomIdsByPaged(dto.getChatRoomId(), dto.getPageNo(), dto.getPageSize());
        int messageCount = chatMessageRepository.countByChatRoomId(dto.getChatRoomId(), 0);
        return ChatMessageResDto.builder()
                .chatMessageList(messagesList)
                .totalCnt(messageCount)
                .build();
    }

}
