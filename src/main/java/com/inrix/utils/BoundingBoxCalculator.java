package com.inrix.utils;


public class BoundingBoxCalculator {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double[] calculateBoundingBox(double latitude, double longitude, double widthMeters, double lengthMeters) {
        // Convert meters to kilometers
        double widthKm = widthMeters / 1000.0;
        double lengthKm = lengthMeters / 1000.0;

        // Calculate the angular distance covered by the box
        double widthAngular = Math.toDegrees(widthKm / EARTH_RADIUS_KM);
        double lengthAngular = Math.toDegrees(lengthKm / EARTH_RADIUS_KM);

        // Calculate the coordinates of the top left and bottom right corners
        double topLeftLat = latitude + (widthAngular / 2);
        double topLeftLon = longitude - (lengthAngular / 2);
        double bottomRightLat = latitude - (widthAngular / 2);
        double bottomRightLon = longitude + (lengthAngular / 2);

        return new double[] {topLeftLat, topLeftLon, bottomRightLat, bottomRightLon};
    }

    public static void main(String[] args) {
        double latitude = 37.80061562581566;
        double longitude = -122.40911926309002;
        double widthMeters = 500;
        double lengthMeters = 500;

        double[] boundingBox = calculateBoundingBox(latitude, longitude, widthMeters, lengthMeters);

        System.out.println("Top Left Coordinate (Lat, Lon): " + boundingBox[0] + ", " + boundingBox[1]);
        System.out.println("Bottom Right Coordinate (Lat, Lon): " + boundingBox[2] + ", " + boundingBox[3]);
    }
}
