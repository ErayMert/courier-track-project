package com.demo.store_proximity.client;

import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.dto.OrderDto;
import com.demo.model.order.enums.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "${feign.client.config.order-service.name}", url = "${feign.client.config.order-service.url}")
public interface OrderClient {

    @GetMapping("/orders/store/{storeId}")
    OrderDto getEarliestOrderForStoreAndStatus(@PathVariable("storeId") Long storeId,
                                               @RequestParam("status") OrderStatus status);

    @PatchMapping("/orders/{id}/status")
    void updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") OrderStatus status);

    @PatchMapping("/orders/{id}/status/{courierId}/status")
    void updateOrderStatusAndCourierStatus(@PathVariable("id") Long id,
                                           @RequestParam("status") OrderStatus status,
                                           @PathVariable("courierId") Long courierId,
                                           @RequestParam("courierStatus") CourierStatus courierStatus);
}
