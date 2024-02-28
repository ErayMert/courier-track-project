package com.demo.courier.controller;


import com.demo.courier.service.CourierTrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("courier-tracks")
@RestController
@RequiredArgsConstructor
public class CourierTrackController {

    private final CourierTrackService courierTrackService;

    @GetMapping("/{id}-total-travel-distance")
    public Double getTotalTravelDistance(@PathVariable("id") Long courierId) {
        return courierTrackService.getTotalTravelDistance(courierId);
    }

}