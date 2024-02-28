package com.demo.courier.service;


import com.demo.courier.entity.CourierTrack;
import com.demo.courier.mapper.CourierMapper;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier.repository.CourierTrackRepository;
import com.demo.courier_track_core.distance.DistanceCalculatorFactory;
import com.demo.courier_track_core.distance.DistanceStrategy;
import com.demo.courier_track_core.enums.DistanceType;
import com.demo.courier_track_core.model.GeoLocation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
@Service
public class CourierTrackService {

    private final CourierMapper courierMapper;
    private final CourierTrackRepository courierTrackRepository;

    public Double getTotalTravelDistance(Long courierId) {

        log.info("calculate total travel distance of courier {}", courierId);
        List<CourierTrack> locations = courierTrackRepository.findByCourierIdOrderByUpdatedDateAsc(courierId);

        if (locations.size() < 2) {
            return 0.0;
        }

        return IntStream.range(1, locations.size())
                .mapToDouble(i -> {
                    CourierTrack previous = locations.get(i - 1);
                    CourierTrack current = locations.get(i);
                    return calculateDistance(previous, current);
                })
                .sum();
    }

    private double calculateDistance(CourierTrack previous, CourierTrack current) {

        GeoLocation previousLocation = courierMapper.courierTrackToGeoLocation(previous);
        GeoLocation currentLocation = courierMapper.courierTrackToGeoLocation(current);
        DistanceStrategy strategy = DistanceCalculatorFactory.getInstance().getCalculator(DistanceType.HAVERSINE);
        return strategy.calculateDistance(previousLocation, currentLocation);
    }

    public void saveCourierTrack(CourierLocationRequest request, LocalDateTime currentTime) {
        CourierTrack courierTrack = courierMapper.courierLocationRequestToCourierTrack(request);
        courierTrack.setUpdatedDate(currentTime);
        courierTrackRepository.save(courierTrack);
    }

}