package com.research2.api.domain.kafka.publisher;


import com.research2.api.domain.kafka.dto.event.ShopOrderCreatedEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
// Publish domain events to Kafka after transaction commitment
public class DomainEventKafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EventTopicMapper eventTopicMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(ShopOrderCreatedEventDto event) {
        String topic = eventTopicMapper.resolveTopic(event);
        String key = eventTopicMapper.resolveKey(event);

        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Domain Event Kafka Publisher failed eventId={}", event.getEventId(), ex);
                return;
            }
            log.info(
                    "Domain Event Kafka Is Issued eventId={} topic={} offset={}",
                    event.getEventId(),
                    topic,
                    result.getRecordMetadata().offset()
            );
        });
    }
}
