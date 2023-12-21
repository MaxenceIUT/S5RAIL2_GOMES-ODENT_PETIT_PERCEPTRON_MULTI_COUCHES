package gps;

import games.ia.framework.common.State;
import games.ia.framework.recherche.HasHeuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GPSState extends State implements HasHeuristic {

    private final City city;
    private double distToGoal;
    private final List<Road> road = new ArrayList<>();

    public GPSState(City city, double distToGoal) {
        this.city = city;
        this.distToGoal = distToGoal;
    }


    @Override
    protected State cloneState() {
        return new GPSState(city, distToGoal);
    }

    @Override
    protected boolean equalsState(State o) {
        return ((GPSState) o).city.equals(city)
                && ((GPSState) o).distToGoal == distToGoal;
    }

    @Override
    protected int hashState() {
        return 31 * Double.hashCode(distToGoal) + city.hashCode();
    }

    public String toString() {
        Optional<Road> first = findRoadToArrival();
        double time = Time.calculate(distToGoal, first.map(Road::type).orElse(Road.Type.B_ROAD));

        return first
                .map(value -> "{" + city.name() + " - " + distToGoal + "km - " + time + ("h - " +
                        "Route utilisée: " + first.get().type()) + "}")
                .orElseGet(() -> "{" + city.name() + " - " + distToGoal + "km - " + time + "h}");
    }

    private Optional<Road> findRoadToArrival() {
        return GPS.ARRIVAL.getRoad().stream().filter(road1 -> road1.city1().equals(city) || road1.city2().equals(city)).findFirst();
    }

    @Override
    public double getHeuristic() {
        Optional<Road> first = findRoadToArrival();
        double time = Time.calculate(distToGoal, first.map(Road::type).orElse(Road.Type.B_ROAD));
        return time;
    }

    public List<Road> getRoad() {
        return road;
    }

    public void addRoad(Road road) {
        this.road.add(road);
    }

    public void setHeuristic(double heuristic) {
        this.distToGoal = heuristic;
    }

    public City getCity() {
        return city;
    }
}
