package com.research2.api.domain.fcm_push_message.service;


import com.google.firebase.messaging.*;

import com.research2.api.domain.fcm_push_message.dto.req.FcmSendMessageDto;
import com.research2.api.domain.fcm_push_message.entity.DeviceInfo;
import com.research2.api.domain.fcm_push_message.repository.FcmPushMessageRepository;
import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("FcmPushMessageServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class FcmPushMessageServiceImpl implements FcmPushMessageService {

    private final FcmPushMessageRepository fcmPushMessageRepository;
    private final FirebaseMessaging firebaseMessaging;


    //단일 발송
    public void sendSinglePushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException {
        DeviceInfo fcmToken = fcmPushMessageRepository.findOneDevice(fcmSendMessageDto.getUserId());
        if (fcmToken == null) {
            throw new CustomException(ErrorCodes.UserErrorCode.CAN_NOT_FIND_DEVICE_INFO);
        } else {
            firebaseMessaging.send(makeMessage("fcmToken.getDeviceToken()", fcmSendMessageDto.getTitle(),
                    fcmSendMessageDto.getBody(), fcmSendMessageDto.getImage()));

        }
    }

    public Message makeMessage(String targetToken, String title, String body, String image) {

        Map<String, String> androidData = new HashMap<>();
        androidData.put("title", title);
        androidData.put("body", body);
        androidData.put("image", image);

        // Create the AndroidConfig object to set priority
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH) // Set high priority for Android
                .putAllData(androidData)
                .build();

        Map<String, Object> apnData = new HashMap<>();
        apnData.put("title", title);
        apnData.put("body", body);
        apnData.put("image", image);

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setContentAvailable(true)
                        .build())
                .putAllCustomData(apnData)
                .build();

        return Message
                .builder()
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .setToken(targetToken)
                .build();
    }

    //단체발송
    public void sendMultiPushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException {

        List<DeviceInfo> fcmTokens = fcmPushMessageRepository.findAllFcmTokens();
        List<String> targetTokens = fcmTokens.stream()
                .map(DeviceInfo::getDeviceToken)
                .collect(Collectors.toList());

        FirebaseMessaging.getInstance().sendEachForMulticast(
                makeMessages(fcmSendMessageDto.getTitle(), fcmSendMessageDto.getBody(), fcmSendMessageDto.getImage(),
                        targetTokens)
        );

    }

    public MulticastMessage makeMessages(String title, String body, String image,
                                         List<String> targetTokens) {

        Map<String, String> androidData = new HashMap<>();
        androidData.put("title", title);
        androidData.put("body", body);
        androidData.put("image", image);

        // Create the AndroidConfig object to set priority
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH) // Set high priority for Android
                .putAllData(androidData)
                .build();

        Map<String, Object> apnData = new HashMap<>();
        apnData.put("title", title);
        apnData.put("body", body);
        apnData.put("image", image);

        ApnsConfig apnsConfig = ApnsConfig.builder()
                .setAps(Aps.builder()
                        .setContentAvailable(true)
                        .build())
                .putAllCustomData(apnData)
                .build();

        return MulticastMessage
                .builder()
                .setAndroidConfig(androidConfig)
                .setApnsConfig(apnsConfig)
                .addAllTokens(targetTokens)
                .build();
    }


}

