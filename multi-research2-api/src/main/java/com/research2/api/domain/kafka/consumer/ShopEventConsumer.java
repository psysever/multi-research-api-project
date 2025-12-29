package com.research2.api.domain.kafka.consumer;


import com.research2.api.domain.kafka.dto.req.OrderListDto;
import com.research2.api.domain.kafka.dto.req.ProcessedEventDto;
import com.research2.api.domain.kafka.dto.req.ShopOrderCreatedEventDto;
import com.research2.api.domain.kafka.entity.ShopOrder;
import com.research2.api.domain.kafka.repository.ProcessedEventRepository;
import com.research2.api.domain.kafka.repository.ShopRepository;
import com.research2.api.domain.kafka.utill.NonRetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
// 주문 이벤트 소비 및 멱등 처리
public class ShopEventConsumer {

    // 처리된 이벤트 저장소 (멱등 처리용)
    private final ProcessedEventRepository processedEventRepository;
    // 주문 조회를 위한 저장소
    private final ShopRepository shopRepository;

    @KafkaListener(
            topics = "${app.kafka.topics.order-created-v1}",
            containerFactory = "shopEventKafkaListenerContainerFactory"
    )
    @Transactional
    public void consumeOrderCreated(
            ShopOrderCreatedEventDto event,
            Acknowledgment ack,
            @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String orderId
    ) {

        String resolvedOrderId = orderId != null ? orderId : event.getOrderId();
        if (resolvedOrderId == null) {
            throw new NonRetryableException("orderId is required");
        }

        ShopOrder order = shopRepository.findByOrderId(resolvedOrderId);
        if (order == null) {
            throw new NonRetryableException("Order not found. orderId=" + resolvedOrderId);
        }

        // 멱등 insert (PK)
        try {
            processedEventRepository.kafkaInsert(
                    ProcessedEventDto.builder()
                            .eventId(event.getEventId())
                            .eventType("order-created.v1")
                            .aggregateId(resolvedOrderId)
                            .processedAt(Instant.now())
                            .build()
            );
        } catch (DuplicateKeyException e) {
            ack.acknowledge();
            return;
        }

        // 포인트 차감 (이력 기반)
        if (event.getOdReceiptPoint() > 0) {
            shopRepository.updateMemberUsePoint(
                    event.getUserId(),
                    event.getOdReceiptPoint()
            );
        }

        ack.acknowledge();
    }


}
