package gps.ia.problemes;

import gps.ia.framework.common.State;
import gps.ia.framework.recherche.HasHeuristic;

public class GPSState extends State implements HasHeuristic {

    private final City city;
    private final double distToGoal;

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

    @Override
    public double getHeuristic() {
        return distToGoal;
    }

    public City getCity() {
        return city;
    }
}
