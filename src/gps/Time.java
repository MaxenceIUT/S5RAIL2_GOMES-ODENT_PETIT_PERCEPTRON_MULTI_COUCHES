package gps;

public class Time {

    public final double HIGHWAY_SPEED = 130;
    public final double FAST_ROAD_SPEED = 90;
    public final double B_ROAD_SPEED = 50;

    public double calculate(double distance, String roadType) {
        return switch (roadType) {
            case "highway" -> distance / HIGHWAY_SPEED;
            case "fast road" -> distance / FAST_ROAD_SPEED;
            case "B road" -> distance / B_ROAD_SPEED;
            default -> throw new IllegalStateException("Unexpected value: " + roadType);
        };
    }
}
