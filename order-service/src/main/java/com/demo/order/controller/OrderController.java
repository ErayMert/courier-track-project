package com.demo.order.controller;

import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.order.model.request.CreateOrderRequest;
import com.demo.order.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request) {
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/store/{storeId}")
    public OrderDto getEarliestOrderForStoreAndStatus(@PathVariable("storeId") Long storeId,
                                            @RequestParam("status") OrderStatus status) {
        return orderService.getEarliestOrderForStoreAndStatus(storeId,status);
    }

    @PatchMapping("/{id}/status")
    public void updateOrderStatus(@PathVariable Long id, @RequestParam("status") OrderStatus status) {
        orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable("id") Long id) {
        return orderService.getOrderById(id);
    }

    @PatchMapping("/{id}/status/{courierId}/status")
    public void updateOrderStatusAndCourierStatus(@PathVariable("id") Long id,
                                           @RequestParam("status") OrderStatus status,
                                           @PathVariable("courierId") Long courierId,
                                           @RequestParam("courierStatus") CourierStatus courierStatus){

        orderService.updateOrderStatusAndCourierStatus(id, status, courierId, courierStatus);
    }
}
