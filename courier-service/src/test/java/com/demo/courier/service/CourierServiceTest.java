package com.demo.courier.service;

import com.demo.courier.client.OrderClient;
import com.demo.courier.entity.Courier;
import com.demo.courier.exception.CourierRuntimeException;
import com.demo.courier.mapper.CourierMapper;
import com.demo.courier.model.request.CourierCreateRequest;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.repository.CourierRepository;
import com.demo.courier.service.producer.CourierLocationEventProducer;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.courier.event.CourierLocationEvent;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.dto.OrderLocationDto;
import com.demo.model.order.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierLocationEventProducer courierLocationEventProducer;

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private CourierTrackService courierTrackService;

    @Mock
    private OrderClient orderClient;

    @InjectMocks
    private CourierService courierService;

    @Test
    void testSendCurrentLocationAndSave() {
        CourierLocationRequest request = new CourierLocationRequest();
        Courier courier = new Courier();
        courier.setStatus(CourierStatus.AVAILABLE);

        CourierLocationEvent courierLocationEvent = new CourierLocationEvent();

        when(courierRepository.findById(1L)).thenReturn(Optional.of(courier));
        when(courierMapper.courierLocationRequestToCourierLocationEvent(request)).thenReturn(courierLocationEvent);

        courierService.sendCurrentLocationAndSave(1L,request);

        verify(courierTrackService, times(1)).saveCourierTrack(eq(request), any(LocalDateTime.class));
        verify(courierLocationEventProducer, times(1)).sendCourierLocation(any(CourierLocationEvent.class));
    }

    @Test
    void testSendCurrentLocationAndSaveCourierNotFound() {
        CourierLocationRequest request = new CourierLocationRequest();

        when(courierRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CourierRuntimeException.class, () -> courierService.sendCurrentLocationAndSave(1L, request));
    }

    @Test
    void testCreateCourier() {
        CourierCreateRequest request = new CourierCreateRequest();
        request.setIdentityNo("12345678901");
        Courier courier = new Courier();
        Courier savedCourier = new Courier();
        CourierDto courierDto = CourierDto.builder().build();

        when(courierRepository.existsByIdentityNo(request.getIdentityNo())).thenReturn(false);
        when(courierMapper.createCourierRequestToCourier(request)).thenReturn(courier);
        when(courierRepository.save(courier)).thenReturn(savedCourier);
        when(courierMapper.courierToCourierDto(any())).thenReturn(courierDto);

        CourierDto result = courierService.createCourier(request);

        assertNotNull(result);
        verify(courierRepository, times(1)).save(courier);
    }

    @Test
    void getCourier_success() {
        Long courierId = 1L;
        Courier courier = new Courier();
        CourierDto expectedDto = new CourierDto();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(courierMapper.courierToCourierDto(courier)).thenReturn(expectedDto);

        CourierDto result = courierService.getCourier(courierId);

        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(courierRepository).findById(courierId);
        verify(courierMapper).courierToCourierDto(courier);
    }

    @Test
    void getCourier_notFound() {
        Long courierId = 1L;

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(CourierRuntimeException.class, () -> courierService.getCourier(courierId));
    }

    @Test
    void updateCourierStatus_success() {
        Long courierId = 1L;
        CourierStatus status = CourierStatus.AVAILABLE;
        Courier courier = new Courier();
        courier.setId(courierId);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);

        courierService.updateCourierStatus(courierId, status);

        assertEquals(status, courier.getStatus());
        verify(courierRepository).findById(courierId);
        verify(courierRepository).save(courier);
    }

    @Test
    void updateCourierStatus_notFound() {
        Long courierId = 1L;
        CourierStatus status = CourierStatus.AVAILABLE;

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(CourierRuntimeException.class, () -> courierService.updateCourierStatus(courierId, status));
    }

    @Test
    void updateStatusAfterDelivery_WhenCourierIsAtLocation() {
        // Setup
        Long courierId = 1L;
        Long orderId = 1L;
        CourierLocationRequest request = CourierLocationRequest.builder()
                .latitude(10.0)
                .longitude(10.0)
                .build();

        Courier courier = new Courier();
        courier.setId(courierId);

        OrderDto order = OrderDto.builder()
                .orderLocation(OrderLocationDto.builder()
                        .latitude(10.0)
                        .longitude(10.0)
                        .build())
                .build();

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(orderClient.getOrderById(orderId)).thenReturn(order);

        courierService.updateStatusAfterDelivery(courierId, orderId, request);
        verify(orderClient).updateOrderStatus(orderId, OrderStatus.DELIVERED);
        verify(courierRepository).save(courier);
        assertEquals(CourierStatus.AVAILABLE, courier.getStatus());
    }

}