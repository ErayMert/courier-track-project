package com.demo.courier.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.demo.courier.entity.CourierTrack;
import com.demo.courier.mapper.CourierMapper;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.repository.CourierTrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class CourierTrackServiceTest {

    @Mock
    private CourierMapper courierMapper;

    @Mock
    private CourierTrackRepository courierTrackRepository;

    @InjectMocks
    private CourierTrackService courierTrackService;


    @Test
    void saveCourierTrack_savesTrackSuccessfully() {
        CourierLocationRequest request = new CourierLocationRequest();
        LocalDateTime currentTime = LocalDateTime.now();
        CourierTrack courierTrack = new CourierTrack();

        when(courierMapper.courierLocationRequestToCourierTrack(request)).thenReturn(courierTrack);
        when(courierTrackRepository.save(any(CourierTrack.class))).thenReturn(courierTrack);

        courierTrackService.saveCourierTrack(request, currentTime);

        verify(courierMapper).courierLocationRequestToCourierTrack(request);
        verify(courierTrackRepository).save(courierTrack);
        assertEquals(currentTime, courierTrack.getUpdatedDate());
    }

}
