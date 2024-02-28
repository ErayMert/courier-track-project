package com.demo.courier.mapper;

import com.demo.courier.entity.Courier;
import com.demo.courier.entity.CourierTrack;
import com.demo.courier.model.request.CourierCreateRequest;
import com.demo.courier.model.request.CourierLocationRequest;
import com.demo.courier_track_core.model.GeoLocation;
import com.demo.model.courier.dto.CourierDto;
import com.demo.model.courier.event.CourierLocationEvent;
import org.mapstruct.Mapper;

@Mapper
public interface CourierMapper {
    Courier createCourierRequestToCourier(CourierCreateRequest request);

    CourierDto courierToCourierDto(Courier courier);

    CourierLocationEvent courierLocationRequestToCourierLocationEvent(CourierLocationRequest request);

    CourierTrack courierLocationRequestToCourierTrack(CourierLocationRequest request);

    GeoLocation courierTrackToGeoLocation(CourierTrack courierTrack);
}