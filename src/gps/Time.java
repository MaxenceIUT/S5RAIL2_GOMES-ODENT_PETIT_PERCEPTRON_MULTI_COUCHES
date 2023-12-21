package gps;

public class Time {

    public static final double HIGHWAY_SPEED = 130;
    public static final double FAST_ROAD_SPEED = 90;
    public static final double B_ROAD_SPEED = 50;

    public static double calculate(double distance, Road.Type roadType) {
        return switch (roadType) {
            case Road.Type.HIGHWAY -> distance / HIGHWAY_SPEED;
            case Road.Type.FAST_ROAD -> distance / FAST_ROAD_SPEED;
            case Road.Type.B_ROAD -> distance / B_ROAD_SPEED;
        };
    }

}
