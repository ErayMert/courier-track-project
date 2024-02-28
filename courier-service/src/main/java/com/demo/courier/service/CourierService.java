package com.demo.courier.service;

import com.demo.courier.client.OrderClient;
import com.demo.courier.entity.Courier;
import com.demo.courier.exception.CourierRuntimeException;
import com.demo.courier.mapper.CourierMapper;
import com.demo.courier.model.request.CourierCreateRequest;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.repository.CourierRepository;
import com.demo.courier.service.producer.CourierLocationEventProducer;
import com.demo.courier_track_core.distance.DistanceCalculatorFactory;
import com.demo.courier_track_core.distance.DistanceStrategy;
import com.demo.courier_track_core.enums.DistanceType;
import com.demo.courier_track_core.model.GeoLocation;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.courier.event.CourierLocationEvent;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Slf4j
@Service
public class CourierService {

    public static final String COURIER_IS_NOT_FOUND = "Courier is not found";

    private final CourierRepository courierRepository;
    private final CourierLocationEventProducer courierLocationEventProducer;
    private final CourierMapper courierMapper;
    private final CourierTrackService courierTrackService;
    private final OrderClient orderClient;

    private static final Double THRESHOLD_LOCATION_METRES = 5.0;

    @Transactional
    public void sendCurrentLocationAndSave(Long id, CourierLocationRequest request) {

        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierRuntimeException(COURIER_IS_NOT_FOUND));

        LocalDateTime currentTime = LocalDateTime.now();
        courierTrackService.saveCourierTrack(request, currentTime);
        log.info("Courier track saved");

        sendCurrentLocationToKafka(request, courier, currentTime);
    }

    public CourierDto createCourier(CourierCreateRequest request) {

        if (courierRepository.existsByIdentityNo(request.getIdentityNo())) {
            throw new CourierRuntimeException("Already exist courier with this identity no");
        }

        Courier courier = courierMapper.createCourierRequestToCourier(request);
        courier.setStatus(CourierStatus.AVAILABLE);
        Courier savedCourier = courierRepository.save(courier);

        log.info("Created Courier: {} {}", courier.getFirstName(), courier.getLastName());
        return courierMapper.courierToCourierDto(savedCourier);
    }

    public CourierDto getCourier(Long id) {

        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierRuntimeException(COURIER_IS_NOT_FOUND));
        return courierMapper.courierToCourierDto(courier);
    }

    @Transactional
    public void updateStatusAfterDelivery(Long id, Long orderId, CourierLocationRequest request) {

        log.info("Called order service for order status");
        OrderDto order = orderClient.getOrderById(orderId);

        if(!isCourierAtTheOrderLocation(request, order)){
            throw new CourierRuntimeException("You is not at the order location!!!");
        }
        orderClient.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        updateCourierStatus(id, CourierStatus.AVAILABLE);
    }

    public void updateCourierStatus(Long id, CourierStatus status) {

        log.info("Courier {} status update", id);
        Courier courier = courierRepository.findById(id)
                .orElseThrow(() -> new CourierRuntimeException(COURIER_IS_NOT_FOUND));

        courier.setStatus(status);
        courierRepository.save(courier);
    }

    private void sendCurrentLocationToKafka(CourierLocationRequest request, Courier courier, LocalDateTime currentTime) {
        CourierLocationEvent courierLocationEvent = courierMapper.courierLocationRequestToCourierLocationEvent(request);
        courierLocationEvent.setCourierId(courier.getId());
        courierLocationEvent.setStatus(courier.getStatus());
        courierLocationEvent.setRecordedAt(currentTime);
        courierLocationEventProducer.sendCourierLocation(courierLocationEvent);
    }

    private boolean isCourierAtTheOrderLocation(CourierLocationRequest request, OrderDto order) {

        double distance = calculateDistance(request, order);
        return distance <= THRESHOLD_LOCATION_METRES;
    }

    private double calculateDistance(CourierLocationRequest request, OrderDto order) {

        GeoLocation courierLocation = GeoLocation.builder()
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        GeoLocation orderLocation = GeoLocation.builder()
                .latitude(order.getOrderLocation().getLatitude())
                .longitude(order.getOrderLocation().getLongitude())
                .build();
        DistanceStrategy strategy = DistanceCalculatorFactory.getInstance().getCalculator(DistanceType.HAVERSINE);
        return strategy.calculateDistance(courierLocation, orderLocation);
    }
}