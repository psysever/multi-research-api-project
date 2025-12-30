package com.research2.api.domain.kafka.publisher;


import com.research2.api.domain.kafka.dto.event.ShopOrderCreatedEventDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
// 이벤트 타입별 토픽/키 매핑
public class EventTopicMapper {

    @Value("${app.kafka.topics.order-created-v1}")
    private String orderCreatedTopic;

    public String resolveTopic(Object event) {
        if (event instanceof ShopOrderCreatedEventDto) {
            return orderCreatedTopic;
        }
        throw new IllegalArgumentException("지원하지 않는 이벤트 타입: " + event.getClass().getName());
    }

    public String resolveKey(Object event) {
        if (event instanceof ShopOrderCreatedEventDto shopOrderCreatedEvent) {
            return shopOrderCreatedEvent.getOrderId();
        }
        throw new IllegalArgumentException("지원하지 않는 이벤트 타입: " + event.getClass().getName());
    }
}
