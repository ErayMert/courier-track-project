package com.demo.order.controller;

import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.dto.OrderLocationDto;
import com.demo.model.order.enums.OrderStatus;
import com.demo.order.model.request.CreateOrderRequest;
import com.demo.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_ReturnsCreated() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        OrderDto response = new OrderDto();
        response.setId(1L);
        response.setOrderLocation(OrderLocationDto.builder()
                .latitude(10.0)
                .longitude(10.0)
                .build());
        response.setStatus(OrderStatus.PENDING);

        given(orderService.createOrder(request)).willReturn(response);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void getEarliestOrderForStoreAndStatus_ReturnsOrder() throws Exception {
        Long storeId = 1L;
        OrderStatus status = OrderStatus.PENDING;
        OrderDto response = new OrderDto();
        response.setStatus(status);
        response.setId(1L);

        given(orderService.getEarliestOrderForStoreAndStatus(storeId, status)).willReturn(response);

        mockMvc.perform(get("/orders/store/{storeId}", storeId)
                        .param("status", status.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateOrderStatus_ReturnsNoContent() throws Exception {
        Long orderId = 1L;
        OrderStatus status = OrderStatus.PENDING;

        mockMvc.perform(patch("/orders/{id}/status", orderId)
                        .param("status", status.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderById_ReturnsOrder() throws Exception {
        Long orderId = 1L;
        OrderDto response = new OrderDto();
        response.setOrderLocation(OrderLocationDto.builder()
                .latitude(10.0)
                .longitude(10.0)
                .build());
        response.setId(orderId);
        response.setStatus(OrderStatus.PENDING);
        given(orderService.getOrderById(orderId)).willReturn(response);

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateOrderStatusAndCourierStatus_ReturnsNoContent() throws Exception {
        Long orderId = 1L;
        Long courierId = 2L;
        OrderStatus orderStatus = OrderStatus.PENDING;
        CourierStatus courierStatus = CourierStatus.AVAILABLE;

        mockMvc.perform(patch("/orders/{id}/status/{courierId}/status", orderId, courierId)
                        .param("status", orderStatus.toString())
                        .param("courierStatus", courierStatus.toString()))
                .andExpect(status().isOk());

    }
}