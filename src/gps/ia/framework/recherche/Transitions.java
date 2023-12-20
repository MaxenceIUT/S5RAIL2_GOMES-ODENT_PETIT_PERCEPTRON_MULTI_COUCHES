package gps.ia.framework.recherche;

import gps.ia.framework.common.StateActionPair;
import gps.ia.framework.common.Action;
import gps.ia.framework.common.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Représentes la liste des transisions entre états
 *  
 *
 */

public class Transitions {

    private final Map<StateActionPair, State> transition =
            new HashMap<>();

    private final Map<StateActionPair, Double> costs =
            new HashMap<>();

    private Set<StateActionPair> keys = null;

    // Ajoute une transition à la liste de transitions
    
    public void addTransition(State s1, Action a, State s2, double c){
        this.transition.put(new StateActionPair(s1, a), s2);
        this.costs.put(new StateActionPair(s1, a), c);
    }

    public State getTransition(State s, Action a){
        return this.transition.get(new StateActionPair(s, a));
    }
    public double getCost(State s, Action a){
        return this.costs.get(new StateActionPair(s, a));
    }

    // retourn les couples (état, action) de la liste 
    public Set<StateActionPair> getKeys(){
        if(this.keys == null)
            this.keys =  this.transition.keySet();
        return this.keys;
        
    }



    
}
