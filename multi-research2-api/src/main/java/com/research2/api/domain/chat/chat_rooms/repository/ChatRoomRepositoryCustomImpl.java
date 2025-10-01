package com.research2.api.domain.chat.chat_rooms.repository;


import com.research2.api.domain.chat.chat_room_members.model.ChatRoomMember;
import com.research2.api.domain.chat.chat_room_members.repository.ChatRoomMemberRepository;
import com.research2.api.domain.chat.chat_rooms.dto.ChatRoomInfoResDto;
import com.research2.api.domain.chat.chat_rooms.dto.GetChatRoomReqDto;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryCustomImpl implements ChatRoomRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Override
    public List<ChatRoomInfoResDto> findChatRoomsByIdsWithProjection(List<String> roomIds, GetChatRoomReqDto dto) {
        Criteria baseCriteria = Criteria.where("_id").in(roomIds).and("delFl").is(0);

        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(baseCriteria);


        Criteria finalCriteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
        AddFieldsOperation addSortKey = Aggregation.addFields()
                .addFieldWithValue("sortKey",
                        ConditionalOperators.ifNull("latestMessageCreatedYmd")
                                .then(new Date(0))
                )
                .build();
        Aggregation aggregation = Aggregation.newAggregation(

                match(finalCriteria),


                context -> new Document("$addFields",
                        new Document("stringId", new Document("$toString", "$_id"))
                ),


                lookup("chat_messages", "stringId", "chatRoomId", "messages"),

                // 4. unwind messages
                //(Required: Remove false option to exclude documents without messages)
                unwind("messages", true),


                sort(Sort.by(Sort.Direction.DESC, "messages.createdAt")),

                //Extract the latest message by group
                group("_id")
                        .first("type").as("type")
                        .first("createId").as("createId")
                        .first("createdYmd").as("createdYmd")
                        .first("senderMbId").as("senderMbId")
                        .first("receiverMbId").as("receiverMbId")
                        .first("messages.createdAt").as("latestMessageCreatedYmd"),

                // 7. Add sortKey (null-safe)
                addSortKey,

                // 8. Sort by newest message
                sort(Sort.by(Sort.Direction.DESC, "sortKey")),

                // 9.final field project
                project()
                        .and(ConvertOperators.ToString.toString("_id")).as("chatRoomId")
                        .andInclude("type", "createId", "createdYmd", "senderMbId", "receiverMbId", "latestMessageCreatedYmd")
        );
        AggregationResults<ChatRoomInfoResDto> results = mongoTemplate.aggregate(
                aggregation,
                "chat_rooms",
                ChatRoomInfoResDto.class
        );

        results.forEach(room -> {
            ChatRoomMember chatRoomMember = chatRoomMemberRepository.findFirstByChatRoomIdAndMbId(room.getChatRoomId(), room.getSenderMbId(), 0);
            if (chatRoomMember != null) {
                room.setTextNotificationEnabled(chatRoomMember.isTextNotificationEnabled());
            }
        });

        return results.getMappedResults();
    }


}
