package com.research2.api.domain.fcm_push_message.service;


import com.google.firebase.messaging.FirebaseMessagingException;
import com.research2.api.domain.fcm_push_message.dto.req.FcmSendMessageDto;

public interface FcmPushMessageService {
    
    void sendSinglePushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException;

    void sendMultiPushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException;

}
