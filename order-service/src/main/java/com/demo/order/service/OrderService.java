package com.demo.order.service;

import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.order.client.CourierClient;
import com.demo.order.entity.Order;
import com.demo.order.entity.OrderLocation;
import com.demo.order.exception.OrderRuntimeException;
import com.demo.order.mapper.OrderMapper;
import com.demo.order.model.request.CreateOrderRequest;
import com.demo.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CourierClient courierClient;

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {

        Order order = getOrder(request);
        orderRepository.save(order);
        OrderDto orderDto = orderMapper.orderToOrderDto(order);
        log.info("Created order {}", orderDto);

        return orderDto;
    }

    @Transactional
    public OrderDto getEarliestOrderForStoreAndStatus(Long storeId, OrderStatus status) {

        log.info("get earliest order by status {} in store {}", status, storeId);
        Order order = orderRepository.findFirstByStoreIdAndStatusOrderByIdAsc(storeId, status)
                .orElse(null);
        return orderMapper.orderToOrderDto(order);
    }

    private static Order getOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setStoreId(request.getStoreId());
        order.setStatus(OrderStatus.PENDING);

        OrderLocation orderLocation = new OrderLocation();
        orderLocation.setLatitude(request.getLatitude());
        orderLocation.setLongitude(request.getLongitude());
        order.setOrderLocation(orderLocation);
        return order;
    }

    @Transactional
    public void updateOrderStatusAndCourierStatus(Long id, OrderStatus status,
                                                  Long courierId, CourierStatus courierStatus) {
        log.info("Called courier service for status update");
        courierClient.updateCourierStatus(courierId, courierStatus);
        updateOrderStatus(id, status);
    }

    public void updateOrderStatus(Long id, OrderStatus status) {

        log.info("Order {} status update", id);
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderRuntimeException("Order is not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).
                orElseThrow(() -> new OrderRuntimeException("Order is not found"));

        return orderMapper.orderToOrderDto(order);
    }
}
