package com.demo.order.client;

import com.demo.model.courier.enums.CourierStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.client.config.courier-service.name}", url = "${feign.client.config.courier-service.url}")
public interface CourierClient {

    @PatchMapping("/couriers/{id}/status")
    void updateCourierStatus(@PathVariable("id") Long id, @RequestParam("status") CourierStatus status);
}
