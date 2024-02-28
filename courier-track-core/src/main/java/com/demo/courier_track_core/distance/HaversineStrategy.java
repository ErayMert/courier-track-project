package com.demo.courier_track_core.distance;


import com.demo.courier_track_core.model.GeoLocation;

public class HaversineStrategy implements DistanceStrategy {
    private static final int RADIUS = 6371;

    @Override
    public double calculateDistance(GeoLocation startLocation, GeoLocation endLocation) {

        double distanceLatitude = Math.toRadians(endLocation.getLatitude() - startLocation.getLatitude());
        double distanceLongitude = Math.toRadians(endLocation.getLongitude() - startLocation.getLongitude());

        double startLatitude = Math.toRadians(startLocation.getLatitude());
        double endLatitude = Math.toRadians(endLocation.getLatitude());

        double a = Math.sin(distanceLatitude / 2) * Math.sin(distanceLatitude / 2) +
                Math.cos(startLatitude) * Math.cos(endLatitude) *
                        Math.sin(distanceLongitude / 2) * Math.sin(distanceLongitude / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return RADIUS * c * 1000;
    }
}