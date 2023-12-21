package gps;

public record Road(City city1, City city2, Road.Type type, double distance) {

    public enum Type {
        HIGHWAY, FAST_ROAD, B_ROAD
    }

}

