package com.demo.courier.client;

import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "${feign.client.config.order-service.name}", url = "${feign.client.config.order-service.url}")
public interface OrderClient {

    @PatchMapping("/orders/{id}/status")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") OrderStatus status);

    @GetMapping("/orders/{id}")
    OrderDto getOrderById(@PathVariable("id") Long id);

}
