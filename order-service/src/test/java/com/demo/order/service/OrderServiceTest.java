package com.demo.order.service;

import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.dto.OrderLocationDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.order.client.CourierClient;
import com.demo.order.entity.Order;
import com.demo.order.entity.OrderLocation;
import com.demo.order.exception.OrderRuntimeException;
import com.demo.order.mapper.OrderMapper;
import com.demo.order.model.request.CreateOrderRequest;
import com.demo.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private CourierClient courierClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void testCreateOrder() {
        Long storeId = 1L;

        CreateOrderRequest request = CreateOrderRequest.builder()
                .latitude(10.0)
                .latitude(10.0)
                .storeId(storeId)
                .build();
        OrderLocation orderLocation = new OrderLocation();
        orderLocation.setLatitude(10.0);
        orderLocation.setLongitude(10.0);
        OrderStatus status = OrderStatus.PENDING;
        Order order = new Order();
        order.setStoreId(storeId);
        order.setStatus(status);
        order.setOrderLocation(orderLocation);
        OrderDto expectedDto = OrderDto.builder()
                .storeId(storeId)
                .orderLocation(OrderLocationDto.builder()
                        .longitude(10.0)
                        .latitude(10.0)
                        .build())
                .status(status)
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderDto(any(Order.class))).thenReturn(expectedDto);

        OrderDto result = orderService.createOrder(request);

        assertEquals(expectedDto, result);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).orderToOrderDto(any(Order.class));
    }

    @Test
    void testGetEarliestOrderForStoreAndStatus() {

        Long storeId = 1L;
        OrderLocation orderLocation = new OrderLocation();
        orderLocation.setLatitude(10.0);
        orderLocation.setLongitude(10.0);
        OrderStatus status = OrderStatus.PENDING;
        Order order = new Order();
        order.setStoreId(storeId);
        order.setStatus(status);
        order.setOrderLocation(orderLocation);
        OrderDto expectedDto = OrderDto.builder()
                .storeId(storeId)
                .orderLocation(OrderLocationDto.builder()
                        .longitude(10.0)
                        .latitude(10.0)
                        .build())
                .status(status)
                .build();

        when(orderRepository.findFirstByStoreIdAndStatusOrderByIdAsc(storeId, status))
                .thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.getEarliestOrderForStoreAndStatus(storeId, status);

        assertEquals(expectedDto, result);
        verify(orderRepository).findFirstByStoreIdAndStatusOrderByIdAsc(storeId, status);
        verify(orderMapper).orderToOrderDto(order);
    }

    @Test
    void testUpdateOrderStatusAndCourierStatus() {
        Long orderId = 1L;
        Long courierId = 1L;
        OrderStatus orderStatus = OrderStatus.DELIVERED;
        CourierStatus courierStatus = CourierStatus.AVAILABLE;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(courierClient).updateCourierStatus(courierId, courierStatus);

        orderService.updateOrderStatusAndCourierStatus(orderId, orderStatus, courierId, courierStatus);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderRepository).save(order);
        verify(courierClient).updateCourierStatus(courierId, courierStatus);
    }

    @Test
    void testGetOrderById() {
        Long storeId = 1L;
        OrderLocation orderLocation = new OrderLocation();
        orderLocation.setLatitude(10.0);
        orderLocation.setLongitude(10.0);
        OrderStatus status = OrderStatus.PENDING;
        Order order = new Order();
        order.setStoreId(1L);
        order.setStoreId(storeId);
        order.setStatus(status);
        order.setOrderLocation(orderLocation);
        OrderDto expectedDto = OrderDto.builder()
                .storeId(storeId)
                .id(1L)
                .orderLocation(OrderLocationDto.builder()
                        .longitude(10.0)
                        .latitude(10.0)
                        .build())
                .status(status)
                .build();

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderDto(order)).thenReturn(expectedDto);

        OrderDto result = orderService.getOrderById(1L);

        assertEquals(expectedDto, result);
        verify(orderRepository).findById(1L);
        verify(orderMapper).orderToOrderDto(order);
    }

    @Test
    void testUpdateOrderStatus() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.DELIVERED;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(orderId, newStatus);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(order);
    }

    @Test
    void testGetOrderById_NotFound() {
        Long id = 1L;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(OrderRuntimeException.class, () -> orderService.getOrderById(id));
    }
}