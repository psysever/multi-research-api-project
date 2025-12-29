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


    public void sendSinglePushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException {
        DeviceInfo fcmToken = fcmPushMessageRepository.findOneDevice(fcmSendMessageDto.getUserId());
        if (fcmToken == null) {
            throw new CustomException(ErrorCodes.UserErrorCode.CAN_NOT_FIND_DEVICE_INFO);
        } else {
            try {
                firebaseMessaging.send(makeMessage("fcmToken.getDeviceToken()", fcmSendMessageDto.getTitle(),
                        fcmSendMessageDto.getBody(), fcmSendMessageDto.getImage()));
            } catch (FirebaseMessagingException e) {
                log.error("FCM FAILED - deviceToken: {},deviceType: {}, error: {}",
                        fcmToken.getDeviceToken(),
                        fcmToken.getDeviceType(),
                        e.getMessage(),
                        e);
                throw new CustomException(ErrorCodes.CommonErrorCode.PUSH_MESSAGE_ERROR);
            }


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


    public void sendMultiPushMessage(FcmSendMessageDto fcmSendMessageDto) throws FirebaseMessagingException {
        int BATCH_SIZE = 500; // FCM MAX 500
        List<DeviceInfo> fcmTokens = fcmPushMessageRepository.findAllFcmTokens();
        List<String> targetTokens = fcmTokens.stream()
                .map(DeviceInfo::getDeviceToken)
                .collect(Collectors.toList());

        for (int i = 0; i < targetTokens.size(); i += BATCH_SIZE) {

            MulticastMessage msg = buildMulticastMessage(fcmSendMessageDto, targetTokens);
            try {
                BatchResponse res = firebaseMessaging.sendEachForMulticast(msg);
                for (int idx = 0; idx < targetTokens.size(); idx++) {
                    SendResponse r = res.getResponses().get(idx);
                    boolean success = r.isSuccessful();
                    if (success) {
                        log.info("FCM SUCCESS - targetTokens: {}, success: {}",
                                targetTokens,
                                true
                        );

                    }


                }
            } catch (FirebaseMessagingException e) {
                log.error("FCM FAILED - targetTokens: {}, error: {}",
                        targetTokens,
                        e.getMessage(),
                        e);
            }
        }
    }

    private MulticastMessage buildMulticastMessage(FcmSendMessageDto req, List<String> tokens) {
        return MulticastMessage.builder()
                .addAllTokens(tokens)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .putData("title", req.getTitle())
                        .putData("body", req.getBody())
                        .putData("data", req.getData() != null ? req.getData() : "")
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder().setContentAvailable(true).build())
                        .putCustomData("title", req.getTitle())
                        .putCustomData("body", req.getBody())
                        .putCustomData("data", req.getData())
                        .build())
                .build();
    }


}

