package com.pri.petcationbackend.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class DistanceUtils {

    private static final double EARTH_RADIUS = 6371;

    public double calculateDistance(double startLat, double startLong, double endLat, double endLong) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public boolean isTwoPointsInMaxDistance(int maxDistance, double startLat, double startLong, double endLat, double endLong) {
        return calculateDistance(startLat, startLong, endLat, endLong) <= maxDistance;
    }

    private double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
