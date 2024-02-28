package com.demo.store_proximity.service.consumer;


import com.demo.model.courier.event.CourierLocationEvent;
import com.demo.store_proximity.service.StoreProximityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourierLocationEventConsumer {

    private final StoreProximityService storeProximityService;

    @KafkaListener(topics = "courier-location", containerFactory = "kafkaListenerContainerFactory")
    public void listenCourierLocation(CourierLocationEvent event) {
        log.info("Consumed courier location for store proximity {}", event.toString());
        storeProximityService.takeCourierOrder(event);
    }
}
