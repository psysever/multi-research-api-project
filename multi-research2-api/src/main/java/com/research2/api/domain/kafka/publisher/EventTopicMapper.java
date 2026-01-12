package com.research2.api.domain.kafka.publisher;


import com.research2.api.domain.kafka.dto.event.ShopOrderCreatedEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
// Topic/key mapping by event type
public class EventTopicMapper {

    @Value("${app.kafka.topics.order-created-v1}")
    private String orderCreatedTopic;

    public String resolveTopic(Object event) {
        if (event instanceof ShopOrderCreatedEventDto) {
            return orderCreatedTopic;
        }
        throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
    }

    public String resolveKey(Object event) {
        if (event instanceof ShopOrderCreatedEventDto shopOrderCreatedEvent) {
            return shopOrderCreatedEvent.getOrderId();
        }
        throw new IllegalArgumentException("Unsupported event type: " + event.getClass().getName());
    }
}
