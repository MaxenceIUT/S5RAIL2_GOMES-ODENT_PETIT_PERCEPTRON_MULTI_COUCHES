package gps.ia.framework.recherche;

public class Distance {

    public final double R = 6371;

    public double calculate(double latitude1, double longitude1, double latitude2, double longitude2) {
        double latitude1Radians = Math.toRadians(latitude1);
        double latitude2Radians = Math.toRadians(latitude2);
        double longitude1Radians = Math.toRadians(longitude1);
        double longitude2Radians = Math.toRadians(longitude2);

        double a = Math.pow(Math.sin(latitude1Radians - latitude2Radians / 2), 2)
                + Math.cos(latitude1)
                * Math.cos(latitude2)
                * Math.pow(Math.sin(longitude1Radians - longitude2Radians / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
