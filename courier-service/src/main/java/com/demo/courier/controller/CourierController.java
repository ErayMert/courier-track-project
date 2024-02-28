package com.demo.courier.controller;

import com.demo.courier.model.request.CourierCreateRequest;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.service.CourierService;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("couriers")
@RestController
@RequiredArgsConstructor
public class CourierController {

    public static final String SUCCESS = "Success";

    private final CourierService courierService;

    @PostMapping
    public ResponseEntity<CourierDto> createCourier(@Valid @RequestBody CourierCreateRequest request) {
        CourierDto courier = courierService.createCourier(request);
        return new ResponseEntity<>(courier, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/location")
    public ResponseEntity<Void> sendCurrentLocation(@PathVariable("id") Long id,
                                                    @Valid @RequestBody CourierLocationRequest request) {
        courierService.sendCurrentLocationAndSave(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public CourierDto getCourier(@PathVariable("id") Long id) {
        return courierService.getCourier(id);
    }

    @PatchMapping("/{id}/status")
    public void updateCourierStatus(@PathVariable("id") Long id, @RequestParam("status") CourierStatus status) {
        courierService.updateCourierStatus(id, status);
    }

    @PatchMapping("/{id}/status/{orderId}/order-status")
    public ResponseEntity<String> updateStatusAfterDelivery(@PathVariable("id") Long id,
                                                            @PathVariable("orderId") Long orderId,
                                                            @RequestBody CourierLocationRequest request) {

        courierService.updateStatusAfterDelivery(id, orderId, request);
        return ResponseEntity.ok(SUCCESS);
    }
}