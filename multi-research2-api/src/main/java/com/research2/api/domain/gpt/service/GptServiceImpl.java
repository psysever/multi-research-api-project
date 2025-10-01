package com.research2.api.domain.gpt.service;

import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import com.research2.api.domain.gpt.dto.req.CreateAiReportDto;
import com.research2.api.domain.gpt.repository.GptRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


@Service("GptServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class GptServiceImpl implements GptService {

    private final GptRepository gptRepository;

    // Constants
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-4-turbo";
    private static final int MAX_TOKENS = 3000;
    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 30;

    // 검사 결과 상태
    private static final String STATUS1 = "STATUS1";
    private static final String STATUS2 = "STATUS2";

    // 섹션 헤더
    private static final String MANAGEMENT_SECTION = "[? MANAGEMENT_SECTION]";
    private static final String SUBJECT_SECTION = "[? SUBJECT_SECTION]";
    private static final String SUMMARY_SECTION = "[? SUMMARY_SECTION]";


    @Value("${spring.gpt_client_key}")
    private String gptClientKey;


    @Async("asyncExecutor")
    public CompletableFuture<Void> userReportWithGpt(int userId) {
        try {
            Optional<?> userReport = gptRepository.userReport(userId);
            String prompt = buildPrompt(userReport);
            return callChatGptApi(prompt, userReport);
        } catch (Exception e) {
            log.error("REPORT FAILED: {}", e.getMessage(), e);
            throw new CustomException(ErrorCodes.UserErrorCode.GPT_ERROR);
        }
    }


    private String buildPrompt(Optional<?> userReport) {
        Map<String, String> values = buildPromptValues(userReport);
        TestResultCategories categories = categorizeTestResults(userReport);

        StringBuilder template = buildPromptTemplate(values, categories);
        return replacePlaceholders(template.toString(), values);
    }

    private Map<String, String> buildPromptValues(Optional<?> userReport) {
        Map<String, String> values = new HashMap<>();
        values.put("userName", "JACK");
        values.put("userAge", "35");
        return values;
    }

    private TestResultCategories categorizeTestResults(Optional<?> userReport) {
        Map<String, String> testResults = Map.of(
                "Height", "178",
                "Weight", "87",
                "Personality", "beautiful"
        );

        List<String> interestItems = new ArrayList<>();
        List<String> careItems = new ArrayList<>();
        List<String> totalItems = new ArrayList<>();

        testResults.entrySet().stream()
                .filter(entry -> STATUS1.equals(entry.getValue()) || STATUS2.equals(entry.getValue()))
                .forEach(entry -> {
                    String itemName = entry.getKey();
                    totalItems.add(itemName);

                    if (STATUS1.equals(entry.getValue())) {
                        interestItems.add(itemName);
                    } else if (STATUS2.equals(entry.getValue())) {
                        careItems.add(itemName);
                    }
                });

        return new TestResultCategories(interestItems, careItems, totalItems);
    }

    private StringBuilder buildPromptTemplate(Map<String, String> values, TestResultCategories categories) {
        StringBuilder template = new StringBuilder();

        // 기본 프롬프트 헤더
        template.append(buildBasicPromptHeader());

        // 관심사항이 있는 경우 추가
        if (!categories.managementItems.isEmpty()) {
            values.put("managementList", String.join(",", categories.managementItems));
            template.append("{userName}'s interests are {interestLst}.");
        }

        // 주의사항이 있는 경우 추가
        if (!categories.subjectItems.isEmpty()) {
            values.put("subjectList", String.join(",", categories.subjectItems));
            template.append("The note is {subjectList}.");
        }

        // 식이 관리 섹션
        template.append(buildManagementSection());

        // 각 항목별 상세 설명
        categories.totalItems.forEach(item -> {
            template.append("reuslt").append(item)
                    .append(" following the direction mentioned above.");
        });

        // 종합 요약 섹션
        template.append(buildSummarySection());

        return template;
    }

    private String buildBasicPromptHeader() {
        return """
                You are a doctor. Based on the information below, please write a diet report using the following format.
                Output Format:
                - 3 paragraphs in English
                - Each paragraph contains part of a complete report, consisting of 14 sentences.
                ==INPUT==
                
                
                1. userName: {userName}
                2. userAge: {userAge}
                3. Height : {Height}
                4. Weight: {Weight}
                5. Personality: {Personality}
                
                
                """;
    }

    private String buildManagementSection() {
        return """
                [Management] CONTENT
                """;
    }

    private String buildSummarySection() {
        return """
                [Summary] CONTENT
                """;
    }

    @Async("asyncExecutor")
    protected CompletableFuture<Void> callChatGptApi(String prompt, Optional<?> report) {
        try {
            String content = sendGptRequest(prompt);
            if (content != null) {
                saveAiReport(content, report);
            } else {
                throw new CustomException(ErrorCodes.UserErrorCode.GPT_NOT_FOUND_CONTENT);
            }
        } catch (Exception e) {
            log.error("GPT API 호출 실패: {}", e.getMessage(), e);
            throw new CustomException(ErrorCodes.UserErrorCode.GPT_ERROR);
        }
        return CompletableFuture.completedFuture(null);
    }

    private String sendGptRequest(String prompt) throws Exception {
        OkHttpClient client = createHttpClient();
        JSONObject requestJson = createRequestJson(prompt);
        Request request = createHttpRequest(requestJson);

        try (Response response = client.newCall(request).execute()) {
            return handleGptResponse(response);
        }
    }

    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private JSONObject createRequestJson(String prompt) {
        JSONObject json = new JSONObject();
        json.put("model", MODEL);
        json.put("max_tokens", MAX_TOKENS);

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", "You are a helpful assistant."));
        messages.put(new JSONObject().put("role", "user").put("content", prompt));
        json.put("messages", messages);

        return json;
    }

    private Request createHttpRequest(JSONObject requestJson) {
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json"),
                requestJson.toString()
        );

        return new Request.Builder()
                .url(OPENAI_URL)
                .addHeader("Authorization", "Bearer " + gptClientKey)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build();
    }

    private String handleGptResponse(Response response) throws Exception {
        if (!response.isSuccessful()) {
            String error = response.body() != null ? response.body().string() : "Unknown error";
            log.error("GPT 응답 오류: {}", error);
            throw new CustomException(ErrorCodes.UserErrorCode.GPT_ERROR);
        }

        String responseBody = response.body().string();
        JSONObject jsonResponse = new JSONObject(responseBody);

        return jsonResponse.getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");
    }

    private void saveAiReport(String content, Optional<?> report) {
        String management = extractSection(content, MANAGEMENT_SECTION);
        String subject = extractSection(content, SUBJECT_SECTION);
        String summary = extractSection(content, SUMMARY_SECTION);


        CreateAiReportDto aiReport = CreateAiReportDto.builder()
                .userId(1)
                .managementCommend(management)
                .subjectCommend(subject)
                .entireCommend(summary)
                .build();

        gptRepository.createAiReport(aiReport);
    }

    // 검사 결과 분류를 위한 내부 클래스
    private static class TestResultCategories {
        final List<String> managementItems;
        final List<String> subjectItems;
        final List<String> totalItems;


        TestResultCategories(List<String> managementItems, List<String> subjectItems, List<String> totalItems) {
            this.managementItems = managementItems;
            this.subjectItems = subjectItems;
            this.totalItems = totalItems;
        }
    }


    private String replacePlaceholders(String template, Map<String, String> values) {
        // 플레이스홀더 치환 로직 구현 필요
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String extractSection(String content, String sectionHeader) {
        // 섹션 추출 로직 구현 필요
        int startIndex = content.indexOf(sectionHeader);
        if (startIndex == -1) return "";

        startIndex += sectionHeader.length();
        int endIndex = content.indexOf("[", startIndex);
        if (endIndex == -1) endIndex = content.length();

        return content.substring(startIndex, endIndex).trim();
    }
}