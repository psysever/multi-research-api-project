package com.research2.api.domain.chat.chat_messages.repository;


import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<ChatMessage> findChatRoomIdsByPaged(String chatRoomId, int pageNo, int pageSize) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("chatRoomId").is(chatRoomId).and("delFl").is(0)),
                Aggregation.sort(Direction.DESC, "createdAt"),
                Aggregation.skip(pageNo),
                Aggregation.limit(pageSize)

        );

        AggregationResults<ChatMessage> results = mongoTemplate.aggregate(
                aggregation,
                "chat_messages",
                ChatMessage.class
        );

        return results.getMappedResults();
    }


    public void updateMessagesDelFlByChatRoomId(String chatRoomId) {
        Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().set("delFl", 1);
        mongoTemplate.updateMulti(query, update, ChatMessage.class);
    }
}

