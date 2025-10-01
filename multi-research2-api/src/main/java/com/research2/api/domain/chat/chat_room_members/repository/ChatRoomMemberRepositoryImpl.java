package com.research2.api.domain.chat.chat_room_members.repository;


import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomMemberResDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class ChatRoomMemberRepositoryImpl implements ChatRoomMemberRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<String> findChatRoomIdsByMbIdPaged(String mbId, int pageNo, int pageSize) {

        Criteria matchCriteria = Criteria.where("mbId").is(mbId);

     
        Aggregation aggregation = newAggregation(
                match(matchCriteria),
                sort(Sort.by(Sort.Direction.DESC, "createdAt")),
                project("chatRoomId"),
                skip(pageNo),
                limit(pageSize)
        );

        AggregationResults<ChatRoomIdOnly> results = mongoTemplate.aggregate(
                aggregation,
                "chat_room_members",
                ChatRoomIdOnly.class
        );

        return results.getMappedResults().stream()
                .map(ChatRoomIdOnly::getChatRoomId)
                .collect(Collectors.toList());
    }

    @Data
    //Internal DTO class (for MongoDB projection result mapping)
    private static class ChatRoomIdOnly {
        private String chatRoomId;
    }

    @Override
    public List<String> findMemberIdsByChatRoomId(String chatRoomId) {

        Aggregation aggregation = newAggregation(
                match(Criteria.where("chatRoomId").is(chatRoomId).and("mbId").ne("").and("nickName").ne(null)),
                project("mbId")
        );

        AggregationResults<MemberIdOnly> results = mongoTemplate.aggregate(
                aggregation,
                "chat_room_members",
                MemberIdOnly.class
        );

        return results.getMappedResults().stream()
                .map(MemberIdOnly::getMbId)
                .collect(Collectors.toList());
    }

    @Data
    private static class MemberIdOnly {
        private String mbId;
    }


    @Override
    public List<ChatRoomMemberResDto> findMembersByChatRoomIdWithMbId(String chatRoomId, String mbId) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("chatRoomId").is(chatRoomId).and("mbId").ne("").ne(mbId).and("nickName").ne(null).and("delFl").is(0)),
                project("mbId", "nickName")
        );

        AggregationResults<MemberDtoOnly> results = mongoTemplate.aggregate(
                aggregation,
                "chat_room_members",
                MemberDtoOnly.class
        );

        return results.getMappedResults().stream()
                .map(m -> ChatRoomMemberResDto.builder()
                        .mbId(m.getMbId())
                        .nickName(m.getNickName())
                        .build())
                .collect(Collectors.toList());
    }

    @Data
    private static class MemberDtoOnly {
        private String mbId;
        private String nickName;
        private int delFl;
    }


}

