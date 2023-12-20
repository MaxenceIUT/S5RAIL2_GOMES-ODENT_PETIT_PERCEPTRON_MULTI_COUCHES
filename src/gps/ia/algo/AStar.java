package gps.ia.algo;

import gps.ia.framework.recherche.TreeSearch;
import gps.ia.framework.common.Action;
import gps.ia.framework.common.State;
import gps.ia.framework.recherche.SearchNode;
import gps.ia.framework.recherche.SearchProblem;

import java.util.ArrayList;
import java.util.HashSet;

public class AStar extends TreeSearch {
    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public AStar(SearchProblem p, State s) {
        super(p, s);
    }

    @Override
    public boolean solve() {
        SearchNode etatInitial = SearchNode.makeRootSearchNode(intial_state);
        frontier = new ArrayList<>();
        frontier.add(etatInitial);
        explored = new HashSet<>();

        while (!frontier.isEmpty()) {
            SearchNode node = frontier.removeFirst();
            State state = node.getState();
            if (problem.isGoalState(state)) {
                end_node = node;
                return true;
            } else {
                explored.add(state);
                ArrayList<Action> actions = problem.getActions(state);
                for (Action action : actions) {
                    SearchNode child = SearchNode.makeChildSearchNode(problem, node, action);
                    //Cette estimation est la somme des deux coûts (g+h) le coût d'arriver à ce nœud (réel lui) plus l'estimation de ce qui reste à faire.
                    double estimation = child.getCost() + child.getHeuristic();
                    if (!frontier.contains((child)) && !explored.contains(child.getState())){
                        frontier.add(child);
                    }else if(frontier.contains(child) && frontier.get(frontier.indexOf(child)).getCost() > child.getCost()){
                        frontier.remove(child);
                        frontier.add(child);
                    }
                }
                frontier.sort((a, b) -> {
                    double aEstimation = a.getCost() + a.getHeuristic();
                    double bEstimation = b.getCost() + b.getHeuristic();
                    return Double.compare(aEstimation, bEstimation);
                });
            }
        }
        return false;
    }
}