package com.research1.api.domain.social.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.research1.api.domain.social.dto.res.SocialInfoResDto;
import com.research1.api.global.exception.CustomException;
import com.research1.api.global.exception.error.ErrorCodes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Collections;


@Service("SocialPlatFormServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class SocialPlatFormServiceImpl implements SocialPlatFormService {

    @Value("${spring.security.oauth2.client.registration.tiktok.client-id}")
    private String tiktokClientId;

    @Value("${spring.security.oauth2.client.registration.tiktok.client-secret}")
    private String tiktokClientSecret;

    @Value("${spring.security.oauth2.client.registration.tiktok.redirect-uri}")
    private String tiktokRedirectUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String facebookRedirectUri;


    @Value("${spring.security.oauth2.client.registration.twitch.client-id}")
    private String twitchClientId;

    @Value("${spring.security.oauth2.client.registration.twitch.client-secret}")
    private String twitchClientSecret;

    @Value("${spring.security.oauth2.client.registration.twitch.redirect-uri}")
    private String twitchRedirectUri;


    private final OkHttpClient httpClient = new OkHttpClient();
    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_SUBSCRIBER_CHECK_URL = "https://www.googleapis.com/youtube/v3/channels";
    private static final String FACEBOOK_TOKEN_URL = "https://graph.facebook.com/v20.0/oauth/access_token";
    private static final String FACEBOOK_USER_ID_URL = "https://graph.facebook.com/v20.0/me";
    private static final String FACEBOOK_GET_PROFILE = "https://graph.facebook.com/v20.0/";
    private static final String TIKTOK_SUBSCRIBER_CHECK_URL = "https://open.tiktokapis.com/v2/user/info/";
    private static final String TWITCH_TOKEN_URL = "https://id.twitch.tv/oauth2/token";
    private static final String TWITCH_SUBSCRIBER_CHECK_URL = "https://api.twitch.tv/helix/users";


    public SocialInfoResDto getYoutubeSubscriberInfo(String code) {
        try {

            if (code == null) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_NEED_CODE);
            }
            // Get Google access token
            String accessToken = getGoogleToken(code);
            if (accessToken == null || accessToken.isEmpty()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_TOKEN_CAN_NOT_FIND);
            }

            // Build YouTube API URL
            UriComponentsBuilder youtubeBuilder = UriComponentsBuilder.fromHttpUrl(
                            GOOGLE_SUBSCRIBER_CHECK_URL)
                    .queryParam("part", "snippet,statistics")
                    .queryParam("mine", true);

            String youtubeUrl = youtubeBuilder.toUriString();

            // Set headers for YouTube API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make YouTube API request
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(youtubeUrl, HttpMethod.GET, entity,
                    String.class);
            log.info("google: code: {},  response: {}, accessToken: {}",
                    code,
                    response,
                    accessToken
            );
            // Process YouTube API response
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseBody);

                // Extract subscriber count from response
                int subscriberCount = responseJson.path("items").get(0).path("statistics")
                        .path("subscriberCount").asInt();
                String channelId = responseJson.path("items").get(0).path("id").asText();
                String channelUrl = "https://www.youtube.com/channel/" + channelId;
                String channelTitle = responseJson.path("items").get(0).path("snippet").path("title")
                        .asText();

                // Determine if subscriber count is greater than or equal to 100
                boolean isSubscriberCountGreaterThan100 = subscriberCount >= 100;
                log.info("google: code: {}, subscriberCount: {}, response: {}, accessToken: {}",
                        code,
                        subscriberCount,
                        response,
                        accessToken
                );
                // Create subscriberInfo object
                return SocialInfoResDto.builder().
                        subscriberValid(isSubscriberCountGreaterThan100)
                        .channelUrl(channelUrl)
                        .channelTitle(channelTitle)
                        .subscriberCount(subscriberCount).build();
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public String getGoogleToken(String code) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_URL)
                .queryParam("client_id", googleClientId)
                .queryParam("client_secret", googleClientSecret)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("code", code);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new CustomException(ErrorCodes.SnsErrorCode.SNS_CODE_UNEXPECTED);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseBody = mapper.readTree(response.getBody());
        return responseBody.path("access_token").asText();
    }

    public SocialInfoResDto getFacebookSubscriberInfo(String code) {
        try {
            // Get Facebook access token
            log.debug("FACK_BOOK_CODE", code);
            if (code == null) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_NEED_CODE);
            }
            String accessToken = getFacebookToken(code);
            if (accessToken == null || accessToken.isEmpty()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_TOKEN_CAN_NOT_FIND);
            }
            JsonNode userIdAndUserLink = getUserIdAndLink(accessToken);
            String userId = userIdAndUserLink.path("id").asText();
            String userLink = userIdAndUserLink.path("link").asText();
            String userName = userIdAndUserLink.path("name").asText();

            String profileInfoUrl = FACEBOOK_GET_PROFILE + userId + "/friends";


            // Set headers for TikTok API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make TikTok API request
            ResponseEntity<String> profileInfo = makeApiRequest(profileInfoUrl, entity);
            // Process Facebook API response
            if (profileInfo.getStatusCode() == HttpStatus.OK) {
                String responseBody = profileInfo.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode profileInfoJson = objectMapper.readTree(responseBody);


                int subscriberCount = profileInfoJson.path("summary").path("total_count").asInt();
                log.debug("FACEBOOK_SUB", subscriberCount);


                boolean isSubscriberCountGreaterThan100 = subscriberCount >= 100;
                return SocialInfoResDto.builder().
                        subscriberValid(isSubscriberCountGreaterThan100)
                        .channelUrl(userLink)
                        .channelTitle(userName)
                        .subscriberCount(subscriberCount).build();
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String getFacebookToken(String code) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", facebookClientId)
                .add("client_secret", facebookClientSecret)
                .add("code", code)
                .add("redirect_uri", facebookRedirectUri)
                .build();

        Request request = new Request.Builder()
                .url(FACEBOOK_TOKEN_URL)
                .post(formBody)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_CODE_UNEXPECTED);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseBody = mapper.readTree(response.body().string());
            return responseBody.path("access_token").asText();
        }
    }

    public JsonNode getUserIdAndLink(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            String facebookUrl = FACEBOOK_USER_ID_URL + "?fields=link,name";
            ResponseEntity<String> response = makeApiRequest(facebookUrl, entity);
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(responseBody);
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public SocialInfoResDto getTwitchSubscriberInfo(String code) {
        try {
            // Get Twitch access token
            log.debug("TWITCH_CODE", code);
            if (code == null) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_NEED_CODE);
            }
            String accessToken = getTwitchToken(code);
            if (accessToken == null || accessToken.isEmpty()) {
                throw new RuntimeException("Failed to obtain Twitch access token");
            }

            // Build Twitch API URL
            UriComponentsBuilder twitchBuilder = UriComponentsBuilder.fromHttpUrl(
                    TWITCH_SUBSCRIBER_CHECK_URL);

            String twitchUrl = twitchBuilder.toUriString();

            // Set headers for Twitch API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            headers.set("Client-ID", twitchClientId); // Client-ID header is required by Twitch
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make Twitch API request
            ResponseEntity<String> response = makeApiRequest(twitchUrl, entity);

            // Process Twitch API response
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseBody);

                // Extract Twitch user ID (needed to construct channel URL, Twitch does not provide direct subscriber count)
                String userId = responseJson.path("data").get(0).path("login").asText();
                String userLoginId = responseJson.path("data").get(0).path("id").asText();
                String displayName = responseJson.path("data").get(0).path("display_name")
                        .asText(); // Channel title
                String channelUrl = "https://www.twitch.tv/" + userId;

                UriComponentsBuilder twitchBuilderSub = UriComponentsBuilder.fromHttpUrl(
                                "https://api.twitch.tv/helix/channels/followed")
                        .queryParam("user_id", userLoginId); // Add user_id parameter

                String twitchSubUrl = twitchBuilderSub.toUriString();

                // Set headers for Twitch API request
                HttpHeaders headersSub = new HttpHeaders();
                headersSub.set("Authorization", "Bearer " + accessToken);
                headersSub.set("Client-ID", twitchClientId); // Client-ID header is required by Twitch
                HttpEntity<String> entitySub = new HttpEntity<>(headersSub);

                // Step 3: Make Twitch API request for followed channels
                ResponseEntity<String> responseSub = makeApiRequest(twitchSubUrl, entitySub);
                String responseSubBody = responseSub.getBody();
                ObjectMapper objectMapperSub = new ObjectMapper();
                JsonNode responseJsonSub = objectMapperSub.readTree(responseSubBody);

                int totalFollowers = responseJsonSub.path("total").asInt();
                boolean isViewCountGreaterThan100 = totalFollowers >= 100;
                return SocialInfoResDto.builder().
                        subscriberValid(isViewCountGreaterThan100)
                        .channelUrl(channelUrl)
                        .channelTitle(displayName)
                        .subscriberCount(totalFollowers).build();
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String getTwitchToken(String code) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("client_id", twitchClientId)
                .add("client_secret", twitchClientSecret)
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", twitchRedirectUri)
                .build();

        Request request = new Request.Builder()
                .url(TWITCH_TOKEN_URL)
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_CODE_UNEXPECTED);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseBody = mapper.readTree(response.body().string());
            return responseBody.path("access_token").asText();
        }
    }

    @Override
    public SocialInfoResDto getTiktokSubscriberInfo(String code) {
        try {
            log.debug("TIKTOK_CODE", code);
            if (code == null) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_NEED_CODE);
            }
            // Get TikTok access token
            String accessToken = getTiktokToken(code);
            if (accessToken == null || accessToken.isEmpty()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_TOKEN_CAN_NOT_FIND);
            }

            // Build TikTok API URL
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(TIKTOK_SUBSCRIBER_CHECK_URL)
                    .queryParam("fields", "follower_count,username");

            String apiUrl = builder.toUriString();

            // Set headers for TikTok API request
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make TikTok API request
            ResponseEntity<String> response = makeApiRequest(apiUrl, entity);

            // Process TikTok API response
            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode responseJson = objectMapper.readTree(responseBody);

                // Extract follower count from response
                int followerCount = responseJson.path("data").path("user").path("follower_count").asInt();
                log.debug("TIKTOK_SUB", followerCount);
                // Extract username from response
                String username = responseJson.path("data").path("user").path("username").asText();
                String channelUrl = "https://www.tiktok.com/@" + username;

                // Create subscriberInfo object
                boolean isFollowerCountGreaterThan100 = followerCount >= 100;
                return SocialInfoResDto.builder().
                        subscriberValid(isFollowerCountGreaterThan100)
                        .channelUrl(channelUrl)
                        .channelTitle(username)
                        .subscriberCount(followerCount).build();
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    public String getTiktokToken(String code) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("client_key", tiktokClientId)
                .add("client_secret", tiktokClientSecret)
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("redirect_uri", tiktokRedirectUri)
                .build();

        Request request = new Request.Builder()
                .url("https://open.tiktokapis.com/v2/oauth/token/")
                .post(formBody)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CustomException(ErrorCodes.SnsErrorCode.SNS_CODE_UNEXPECTED);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseBody = mapper.readTree(response.body().string());
            return responseBody.path("access_token").asText();
        }
    }

    private ResponseEntity<String> makeApiRequest(String url, HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

}

