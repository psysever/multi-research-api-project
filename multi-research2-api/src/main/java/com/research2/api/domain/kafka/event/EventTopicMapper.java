package com.research2.api.domain.kafka.event;

import com.research2.api.domain.kafka.dto.req.ShopOrderCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class EventTopicMapper {

    public String topicOf(Object event) {
        if (event instanceof ShopOrderCreatedEvent) return "order-created.v1";
        throw new IllegalArgumentException("Unknown event type: " + event.getClass());
    }

    public String keyOf(Object event) {
        if (event instanceof ShopOrderCreatedEvent e) return e.getOrderId();
        return null;
    }
}