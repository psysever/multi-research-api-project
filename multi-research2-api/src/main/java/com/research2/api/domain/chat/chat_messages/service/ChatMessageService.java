package com.research2.api.domain.chat.chat_messages.service;


import com.research2.api.domain.chat.chat_messages.dto.ChatMessageReqDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatMessageResDto;
import com.research2.api.domain.chat.chat_messages.dto.ChatReadReqDto;
import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChatMessageService {


    void sendMessage(ChatMessage chat);

    String sendImageMessage(MultipartFile file);

    String sendBase64Image(String base64, String fileName) throws IOException;

    int markMessagesAsRead(ChatReadReqDto dto);

    ChatMessageResDto getChatMessageList(ChatMessageReqDto dto);

    int countAllUnreadMessages(String mbId);
}
