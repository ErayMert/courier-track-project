package com.demo.store_proximity.service;

import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.courier.event.CourierLocationEvent;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.store_proximity.client.CourierClient;
import com.demo.store_proximity.client.OrderClient;
import com.demo.store_proximity.document.StoreEntryLog;
import com.demo.store_proximity.model.dto.StoreDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreProximityServiceTest {

    @Mock
    private StoreService storeService;
    
    @Mock
    private CourierClient courierClient;
    
    @Mock
    private OrderClient orderClient;
    
    @Mock
    private StoreEntryLogService storeEntryLogService;
    
    @InjectMocks
    private StoreProximityService storeProximityService;

    @Test
    void takeCourierOrder_CourierAvailable_OrderProcessed() {
        CourierLocationEvent event = new CourierLocationEvent();
        event.setCourierId(1L);
        event.setLatitude(10.0);
        event.setLongitude(20.0);
        event.setRecordedAt(LocalDateTime.now());
        
        CourierDto courier = new CourierDto();
        courier.setId(event.getCourierId());
        courier.setStatus(CourierStatus.AVAILABLE);
        
        StoreDto store = new StoreDto();
        store.setId(1L);
        store.setLatitude(10.0);
        store.setLongitude(20.0);
        
        OrderDto order = new OrderDto();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        
        when(courierClient.getCourier(event.getCourierId())).thenReturn(courier);
        when(storeService.getAllStores()).thenReturn(Collections.singletonList(store));
        when(orderClient.getEarliestOrderForStoreAndStatus(store.getId(), OrderStatus.PENDING)).thenReturn(order);
        doNothing().when(orderClient).updateOrderStatusAndCourierStatus(order.getId(), OrderStatus.IN_TRANSIT, courier.getId(), CourierStatus.ON_DELIVERY);
        when(storeEntryLogService.getLastEntryRecord(store.getId())).thenReturn(Optional.empty());
        
        storeProximityService.takeCourierOrder(event);
        
        verify(storeEntryLogService, times(1)).saveStoreEntryLog(any());
        verify(orderClient).updateOrderStatusAndCourierStatus(order.getId(),
                OrderStatus.IN_TRANSIT, courier.getId(), CourierStatus.ON_DELIVERY);
    }
}