package gps;

import games.ia.framework.common.State;
import games.ia.framework.recherche.HasHeuristic;

public class GPSState extends State implements HasHeuristic {

    private final City city;
    private double distToGoal;

    public GPSState(City city, double distToGoal){
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
        return STR."{\{city.name()}, \{distToGoal}}";
    }

    @Override
    public double getHeuristic() {
        return distToGoal;
    }

    public void setHeuristic(double heuristic) {
        this.distToGoal = heuristic;
    }

    public City getCity() {
        return city;
    }
}
