package gps.ia.framework.recherche;

import gps.ia.framework.common.StateActionPair;
import gps.ia.framework.common.Action;
import gps.ia.framework.common.Misc;
import gps.ia.framework.common.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
* Un Probleme où les états et les transitions sont spécifiées. 
*
*
* <p>Cette classe ajoute un ensemble détat et de transition à {@link games.ia.framework.recherche.SearchProblem}. Pour un exemple, voir la classe</p>
*
*/

public abstract class Problem extends SearchProblem {

    /**
     * La liste des états à remplir
     */
    
    protected static State[] STATES = null ;

    /**
     * La liste des transitions à remplir
     */
    protected static Transitions TRANSITIONS = new Transitions();
    
    
    public ArrayList<Action> getActions(State s){
        // rechercher dans toutes les transitions celles qui partent de l'état s
        Set<StateActionPair> sa = TRANSITIONS.getKeys();
        List<StateActionPair> state = sa.stream()
            .filter(k -> s.equals(k.getState()))
            .toList();

        // récupérer les actions depuis l'état s
        List<Action> actions = state
            .stream()
            .map(StateActionPair::getAction)
            .toList();

        // On trie pour les avoir dans le même ordre à chaque fois
        ArrayList<Action> result = new ArrayList<>(actions);
        Collections.sort(result) ;
        return result;
    }
    
    // exécute l'action a dans l'état s, retourne le nouvel état
    public State doAction(State s, Action a){
        return TRANSITIONS.getTransition(s,a);
    }
    
    // retourne le coût de faire l'action a dans l'état s
    public double getActionCost(State s, Action a){
        return TRANSITIONS.getCost(s,a);
    }

    /**
     * Affiche le graphe des états du problème.
     */ 
    public void printStateGraph(){
        StringBuilder res = new StringBuilder();
        for (State state : STATES) {
            res.append(state).append("{").append(Misc.collection2string(getActions(state), ',')).append("}\n");
        }
        
        System.out.println(res);
    }

    
}
