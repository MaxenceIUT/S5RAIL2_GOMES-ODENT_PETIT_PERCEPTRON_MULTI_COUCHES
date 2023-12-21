package gps;

public class Distance {

    public static final double R = 6371;

    public static double calculate(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitude1Radians = Math.toRadians(latitude1);
        double latitude2Radians = Math.toRadians(latitude2);
        double longitude1Radians = Math.toRadians(longitude1);
        double longitude2Radians = Math.toRadians(longitude2);

        double a = Math.pow(Math.sin((latitude2Radians - latitude1Radians) / 2), 2)
                + Math.cos(latitude1Radians)
                * Math.cos(latitude2Radians)
                * Math.pow(Math.sin((longitude2Radians - longitude1Radians) / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

}
