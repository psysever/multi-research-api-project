package com.research2.api.domain.kafka.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventKafkaPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final EventTopicMapper eventTopicMapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(Object event) {

        String topic = eventTopicMapper.topicOf(event);
        String key = eventTopicMapper.keyOf(event);

        log.info("Kafka publish → topic={}, key={}, event={}", topic, key, event);

        kafkaTemplate.send(topic, key, event);
    }
}
