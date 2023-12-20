package gps;

import games.ia.framework.recherche.SearchNode;
import games.ia.framework.recherche.SearchProblem;
import games.ia.framework.recherche.TreeSearch;
import games.ia.framework.common.Action;
import games.ia.framework.common.State;

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
        frontier = new ArrayList<>();
        explored = new HashSet<>();

        SearchNode initialState = SearchNode.makeRootSearchNode(intial_state);
        frontier.add(initialState);

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

                    if (!frontier.contains((child)) && !explored.contains(child.getState())){
                        frontier.add(child);
                    } else if (frontier.contains(child) && frontier.get(frontier.indexOf(child)).getCost() > child.getCost()){
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