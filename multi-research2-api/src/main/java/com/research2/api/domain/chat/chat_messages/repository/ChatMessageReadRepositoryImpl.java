package com.research2.api.domain.chat.chat_messages.repository;


import com.research2.api.domain.chat.chat_messages.model.ChatMessage;
import com.research2.api.domain.chat.chat_messages.model.ChatMessageRead;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class ChatMessageReadRepositoryImpl implements ChatMessageReadRepositoryCustom {

    private final MongoTemplate mongoTemplate;


    @Override
    public int countUnreadMessages(String chatRoomId, String mbId) {
        // 1. Get the message ID that the user read
        List<String> readMessageIds = mongoTemplate.find(
                Query.query(Criteria.where("chatRoomId").is(chatRoomId)
                        .and("mbId").is(mbId).and("delFl").is(0)),
                ChatMessageRead.class
        ).stream().map(ChatMessageRead::getChatMessageId).toList();

        // 2. Counting unread messages in a message collection
        Query unreadQuery = new Query(
                Criteria.where("chatRoomId").is(chatRoomId)
                        .and("_id").nin(readMessageIds)
                        .and("senderMbId").ne(mbId).and("delFl").is(0) // 내 메시지는 제외
        );

        return Math.toIntExact(mongoTemplate.count(unreadQuery, ChatMessage.class));
    }

    @Override
    public int countAllUnreadMessages(String mbId) {
        // 1. Get the message ID that the user read
        List<String> readMessageIds = mongoTemplate.find(
                Query.query(Criteria.where("mbId").is(mbId)
                        .and("delFl").is(0)),
                ChatMessageRead.class
        ).stream().map(ChatMessageRead::getChatMessageId).toList();

        Aggregation aggregation = Aggregation.newAggregation(
                //2.Unread Message Filter
                match(Criteria.where("_id").nin(readMessageIds)
                        .and("receiverMbId").is(mbId)
                        .and("delFl").is(0)
                ),

                // 2. Join with chat_room_members
                lookup(
                        "chat_room_members",       // from Collection
                        "chatRoomId",              // localField in chat_messages
                        "chatRoomId",              // foreignField in chat_room_members
                        "memberInfo"               // Result field name
                ),

                //3.Filtering (leaving only the corresponding mbId in the memberInfo array)
                unwind("memberInfo"),
                match(Criteria.where("memberInfo.mbId").is(mbId)),

                // 4. Conditions can be added if needed (e.g. memberInfo.delFl == 0)
                match(Criteria.where("memberInfo.delFl").is(0)),

                // 5. total number count
                count().as("unreadCount")
        );

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "chat_messages", Document.class);
        Document result = results.getUniqueMappedResult();

        return result != null ? result.getInteger("unreadCount", 0) : 0;
    }

    @Override
    public List<ChatMessage> countUnreadMessagesList(String chatRoomId, String mbId) {
        // 1.Get the message ID that the user read
        List<String> readMessageIds = mongoTemplate.find(
                Query.query(Criteria.where("chatRoomId").is(chatRoomId)
                        .and("mbId").is(mbId).and("delFl").is(0)),
                ChatMessageRead.class
        ).stream().map(ChatMessageRead::getChatMessageId).toList();

        // 2. Counting unread messages in a message collection
        Query unreadQuery = new Query(
                Criteria.where("chatRoomId").is(chatRoomId)
                        .and("_id").nin(readMessageIds)
                        .and("senderMbId").ne(mbId).and("delFl").is(0) // exclude my messages
        );

        return mongoTemplate.find(unreadQuery, ChatMessage.class);
    }

    public void updateReadMessagesDelFlByChatRoomId(String chatRoomId) {
        Query query = new Query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().set("delFl", 1);
        mongoTemplate.updateMulti(query, update, ChatMessageRead.class);
    }
}