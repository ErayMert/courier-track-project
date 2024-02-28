package com.demo.courier.service.producer;


import com.demo.courier.config.properties.KafkaProperties;
import com.demo.model.courier.event.CourierLocationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourierLocationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public void sendCourierLocation(CourierLocationEvent event) {
        kafkaTemplate.send(kafkaProperties.getTopic().getCourierLocation(), event);
        log.info("Send courier location: {} ", event.toString());
    }
}