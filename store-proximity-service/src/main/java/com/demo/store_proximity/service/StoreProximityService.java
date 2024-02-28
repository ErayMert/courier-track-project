package com.demo.store_proximity.service;

import com.demo.courier_track_core.distance.DistanceCalculatorFactory;
import com.demo.courier_track_core.distance.DistanceStrategy;
import com.demo.courier_track_core.enums.DistanceType;
import com.demo.courier_track_core.model.GeoLocation;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.courier.event.CourierLocationEvent;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.store_proximity.client.CourierClient;
import com.demo.store_proximity.client.OrderClient;
import com.demo.store_proximity.document.StoreEntryLog;
import com.demo.store_proximity.model.dto.StoreDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class StoreProximityService {

    private final StoreService storeService;
    private final CourierClient courierClient;
    private final OrderClient orderClient;
    private final StoreEntryLogService storeEntryLogService;

    private static final double RADIUS = 100;
    private static final int MINUTES = 1;

    @Transactional
    public void takeCourierOrder(CourierLocationEvent event) {
        CourierDto courier = courierClient.getCourier(event.getCourierId());
        if (!CourierStatus.AVAILABLE.equals(courier.getStatus())) {
            log.info("{} courier is not available", courier.getFirstName());
            return;
        }

        storeService.getAllStores().stream()
                .filter(store -> isWithin100Meters(event, store) && shouldLogEntry(store.getId(), event.getRecordedAt()))
                .findFirst()
                .ifPresent(store -> processOrderForStore(courier, store));
    }

    private void processOrderForStore(CourierDto courier, StoreDto store) {

        OrderDto order = orderClient.getEarliestOrderForStoreAndStatus(store.getId(), OrderStatus.PENDING);
        if (Objects.isNull(order)) {
            log.warn("No orders are pending");
            return;
        }

        saveStoreEntryLog(courier.getId(), store.getId(), order.getId());
        orderClient.updateOrderStatusAndCourierStatus(order.getId(), OrderStatus.IN_TRANSIT,
                courier.getId(), CourierStatus.ON_DELIVERY);
        log.info("Processed order {} for store {} by courier {}",
                order.getId(), store.getId(), courier.getFirstName());
    }

    private void saveStoreEntryLog(Long courierId, Long storeId, Long orderId) {

        StoreEntryLog storeEntryLog = StoreEntryLog.builder()
                .courierId(courierId)
                .storeId(storeId)
                .orderId(orderId)
                .pickupDate(LocalDateTime.now())
                .build();
        storeEntryLogService.saveStoreEntryLog(storeEntryLog);
    }

    private boolean isWithin100Meters(CourierLocationEvent event, StoreDto store) {

        double distance = getDistance(event, store);
        return distance <= RADIUS;
    }

    private static double getDistance(CourierLocationEvent event, StoreDto store) {
        GeoLocation previousLocation = GeoLocation.builder()
                .latitude(event.getLatitude())
                .longitude(event.getLongitude())
                .build();
        GeoLocation currentLocation = GeoLocation.builder()
                .latitude(store.getLatitude())
                .longitude(store.getLongitude())
                .build();
        DistanceStrategy strategy = DistanceCalculatorFactory.getInstance().getCalculator(DistanceType.HAVERSINE);
        return strategy.calculateDistance(previousLocation, currentLocation);
    }

    private boolean shouldLogEntry(Long storeId, LocalDateTime timestamp) {

        return storeEntryLogService.getLastEntryRecord(storeId)
                .map(lastEntry -> Duration.between(lastEntry.getPickupDate(), timestamp).toMinutes() > MINUTES)
                .orElse(true);
    }
}
