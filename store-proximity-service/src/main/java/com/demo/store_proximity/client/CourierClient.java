package com.demo.store_proximity.client;

import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.demo.model.order.enums.OrderStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.client.config.courier-service.name}", url = "${feign.client.config.courier-service.url}")
public interface CourierClient {

    @GetMapping("/couriers/{id}")
    CourierDto getCourier(@PathVariable("id") Long id);

    @PatchMapping("/couriers/{id}/status")
    void updateCourierStatus(@PathVariable("id") Long id, @RequestParam("status") CourierStatus status);
}
