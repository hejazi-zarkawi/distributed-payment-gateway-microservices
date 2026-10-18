package com.hejazi.distributed_payment_gateway.common_lib.config;

import com.hejazi.distributed_payment_gateway.common_lib.enums.EventAggregateType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@ConfigurationProperties(prefix = "app.kafka")
@Getter
@Setter
public class KafkaProperties {
    private Map<String, String> topics = new HashMap<>();

    public String topicFor(EventAggregateType aggregateType) {
        String topic = topics.get(aggregateType.name().toLowerCase());
        if (topic == null) {
            throw new IllegalStateException("No Kafka topic is configured for aggregateType: "+aggregateType);
        }
        return topic;
    }
}
