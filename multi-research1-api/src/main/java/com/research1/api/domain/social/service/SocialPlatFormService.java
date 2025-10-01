package com.research1.api.domain.social.service;


import com.research1.api.domain.social.dto.res.SocialInfoResDto;

import java.io.IOException;

public interface SocialPlatFormService {


    SocialInfoResDto getYoutubeSubscriberInfo(String code);

    SocialInfoResDto getFacebookSubscriberInfo(String code) throws IOException;

    SocialInfoResDto getTwitchSubscriberInfo(String code) throws IOException;

    SocialInfoResDto getTiktokSubscriberInfo(String code) throws IOException;


}
