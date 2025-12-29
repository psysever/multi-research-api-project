package com.research2.api.domain.kafka.publisher;


import com.research2.api.domain.kafka.dto.req.ShopOrderCreatedEventDto;
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
// 트랜잭션 커밋 이후 도메인 이벤트를 Kafka로 발행
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
                log.error("도메인 이벤트 Kafka 발행 실패 eventId={}", event.getEventId(), ex);
                return;
            }
            log.info(
                    "도메인 이벤트 Kafka 발행 완료 eventId={} topic={} offset={}",
                    event.getEventId(),
                    topic,
                    result.getRecordMetadata().offset()
            );
        });
    }
}
