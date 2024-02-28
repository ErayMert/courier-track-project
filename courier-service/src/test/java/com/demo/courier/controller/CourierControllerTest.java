package com.demo.courier.controller;

import com.demo.courier.model.request.CourierCreateRequest;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.service.CourierService;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.enums.CourierStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CourierController.class)
class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCourier_ReturnsCourierDto_CreatedStatus() throws Exception {
        CourierCreateRequest request = new CourierCreateRequest();
        request.setIdentityNo("12321312434");
        request.setFirstName("test");
        request.setLastName("test");

        CourierDto response = new CourierDto();
        response.setId(1L);
        response.setFirstName("test");
        response.setStatus(CourierStatus.AVAILABLE);
        response.setLastName("test");

        given(courierService.createCourier(request)).willReturn(response);


        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void createCourier_WithInvalidParameters_ReturnsBadRequest() throws Exception {
        CourierCreateRequest request = new CourierCreateRequest();

        request.setFirstName("test");
        request.setLastName("test");

        mockMvc.perform(post("/couriers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendCurrentLocation_ReturnsOkStatus() throws Exception {
        CourierLocationRequest request = new CourierLocationRequest();
        request.setLatitude(10.01);
        request.setLongitude(20.02);

        Long courierId = 1L;

        mockMvc.perform(post("/couriers/id/location", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void getCourier_ReturnsCourierDto() throws Exception {
        Long courierId = 1L;
        CourierDto response = new CourierDto();

        response.setId(1L);
        response.setFirstName("test");
        response.setStatus(CourierStatus.AVAILABLE);
        response.setLastName("test");

        given(courierService.getCourier(courierId)).willReturn(response);

        mockMvc.perform(get("/couriers/{id}", courierId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void updateCourierStatus_ReturnsOkStatus() throws Exception {
        Long courierId = 1L;
        CourierStatus status = CourierStatus.AVAILABLE;

        mockMvc.perform(patch("/couriers/{id}/status", courierId)
                        .param("status", status.name()))
                .andExpect(status().isOk());

        verify(courierService).updateCourierStatus(courierId, status);
    }

    @Test
    void updateCourierStatusAndOrderStatus_ReturnsOkStatus() throws Exception {
        Long courierId = 1L;
        Long orderId = 1L;

        CourierLocationRequest request = new CourierLocationRequest();
        request.setLatitude(10.01);
        request.setLongitude(20.02);

        mockMvc.perform(patch("/couriers/{id}/status/{orderId}/status", courierId, orderId)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(courierService).updateStatusAfterDelivery(courierId, orderId, request);
    }

}