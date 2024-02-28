package com.demo.courier_track_core.distance;


import com.demo.courier_track_core.model.GeoLocation;

public interface DistanceStrategy {
    double calculateDistance(GeoLocation startLocation, GeoLocation endLocation);
}