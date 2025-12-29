package com.research2.api.domain.kafka.config;


import com.research2.api.domain.kafka.dto.req.ShopOrderCreatedEventDto;
import com.research2.api.domain.kafka.utill.NonRetryableException;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
// Kafka 프로듀서/컨슈머 및 에러 핸들링 공통 설정
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String consumerGroupId;

    @Bean
    // JsonSerializer 기반 프로듀서 설정
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        // Kafka 브로커 주소 목록
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 메시지 키를 문자열로 직렬화
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 메시지 값을 JSON으로 직렬화
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        // 타입 정보 헤더를 추가하지 않음
        props.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    // 공용 KafkaTemplate
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> kafkaProducerFactory) {
        return new KafkaTemplate<>(kafkaProducerFactory);
    }

    @Bean
    // ShopOrderCreatedEvent 소비를 위한 JsonDeserializer 설정
    public ConsumerFactory<String, ShopOrderCreatedEventDto> shopOrderCreatedConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        // Kafka 브로커 주소 목록
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 컨슈머 그룹 ID
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        // 오프셋이 없을 때 earliest부터 읽기
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // 역직렬화를 허용할 패키지 범위
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.petnuri.petnuriappapi.domain.event");
        // 기본 역직렬화 타입 지정
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ShopOrderCreatedEventDto.class.getName());
        // 타입 정보 헤더를 사용하지 않음
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        // 오토 커밋 비활성화 (수동 ack 사용)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>()
        );
    }

    @Bean
    // 수동 Ack + 공통 에러 핸들러 적용
    public ConcurrentKafkaListenerContainerFactory<String, ShopOrderCreatedEventDto> shopEventKafkaListenerContainerFactory(
            ConsumerFactory<String, ShopOrderCreatedEventDto> shopOrderCreatedConsumerFactory,
            DefaultErrorHandler kafkaErrorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ShopOrderCreatedEventDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(shopOrderCreatedConsumerFactory);
        factory.setCommonErrorHandler(kafkaErrorHandler);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    @Bean
    // 고정 백오프 + DLQ 라우팅 에러 핸들러
    public DefaultErrorHandler kafkaErrorHandler(KafkaTemplate<String, Object> kafkaTemplate) {
        FixedBackOff backOff = new FixedBackOff(3000L, 3L);

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, ex) -> new org.apache.kafka.common.TopicPartition(
                                record.topic() + ".dlq",
                                record.partition()
                        )
                );

        DefaultErrorHandler errorHandler =
                new DefaultErrorHandler(recoverer, backOff);

        errorHandler.addNotRetryableExceptions(
                NonRetryableException.class,
                IllegalArgumentException.class
        );

        return errorHandler;
    }
}
