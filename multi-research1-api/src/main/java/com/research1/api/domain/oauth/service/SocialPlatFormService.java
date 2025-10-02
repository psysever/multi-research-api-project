package com.research1.api.domain.oauth.service;


import com.research1.api.domain.oauth.dto.res.SocialUserInfoResDto;

import java.io.IOException;

public interface SocialPlatFormService {


    SocialUserInfoResDto getYoutubeSubscriberInfo(String code);

    SocialUserInfoResDto getFacebookSubscriberInfo(String code) throws IOException;

    SocialUserInfoResDto getTwitchSubscriberInfo(String code) throws IOException;

    SocialUserInfoResDto getTiktokSubscriberInfo(String code) throws IOException;


}
