package gps.ia.framework.common;

/**
 * Représente un couple (état, action)
 */

public class StateActionPair {

    /**
     * Contient un State et une Action
     */
    
    private final State state;
    private final Action action;

    public StateActionPair(State s, Action a) {
        state = s;
        action = a;
    }

    public Action getAction(){
        return action;
    }

    public State getState(){
        return state;
    }
    
    /**
     * Ça évitera les doublons de temps en temps
     * @param o Une autre paire
     * @return Vrai si égal à nous
     */
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateActionPair sa)) return false;
        return state.equals(sa.state) &&
            action.equals(sa.action);
    }
    
    /**
     * Les paires sont utilisé comme clé de hasmap
     * par exemple. Elles doivent être hachées
     * @return le haché 
     */
    
    @Override
    public int hashCode() {
        int result = state.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }
}
