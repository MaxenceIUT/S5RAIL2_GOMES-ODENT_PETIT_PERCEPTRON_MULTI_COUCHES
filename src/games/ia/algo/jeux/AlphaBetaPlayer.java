package games.ia.algo.jeux;

import games.ia.framework.common.Action;
import games.ia.framework.common.ActionValuePair;
import games.ia.framework.jeux.Game;
import games.ia.framework.jeux.GameState;
import games.ia.framework.jeux.Player;
import games.ia.framework.recherche.HasHeuristic;
import games.ia.problemes.ConnectFourState;

public class AlphaBetaPlayer extends Player {

    private final static int MAX_DEPTH = 6;
    private int consideredStates = 0;

    public AlphaBetaPlayer(Game game, boolean p1) {
        super(game, p1);
        this.name = "AlphaBeta";
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair actionValuePair = null;
        int currentMaxDepth = MAX_DEPTH;

        while ((actionValuePair == null || actionValuePair.getAction() == null) && currentMaxDepth > 0) {
            if (state.getPlayerToMove() == ConnectFourState.X) {
                actionValuePair = maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, currentMaxDepth);
            } else {
                actionValuePair = minValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, currentMaxDepth);
            }
            currentMaxDepth--;
        }

        // Si malgré tout, on ne trouve pas de coup, on joue un coup aléatoire
        if (actionValuePair.getAction() == null) {
            Action randomAction = this.game.getRandomMove(state);
            actionValuePair = new ActionValuePair(randomAction, 0);
        }

        System.out.println("States considered: " + this.consideredStates);
        return actionValuePair.getAction();
    }

    private ActionValuePair maxValue(GameState state, double alpha, double beta, int depth) {
        if (this.game.endOfGame(state) || !(state instanceof HasHeuristic)) {
            return new ActionValuePair(null, state.getGameValue());
        } else if (depth < 0) {
            return new ActionValuePair(null, ((HasHeuristic) state).getHeuristic());
        }

        double vMax = Integer.MIN_VALUE;
        Action cMax = null;

        for (Action action : this.game.getActions(state)) {
            GameState newState = (GameState) this.game.doAction(state, action);
            this.consideredStates++;
            ActionValuePair actionValuePair = minValue(newState, alpha, beta, depth - 1);

            if (actionValuePair.getValue() > vMax) {
                vMax = actionValuePair.getValue();
                cMax = action;

                if (vMax > alpha) {
                    alpha = vMax;
                }
            }

            if (vMax >= beta) {
                return new ActionValuePair(cMax, vMax);
            }
        }

        return new ActionValuePair(cMax, vMax);
    }

    private ActionValuePair minValue(GameState state, double alpha, double beta, int depth) {
        if (this.game.endOfGame(state) || !(state instanceof HasHeuristic)) {
            return new ActionValuePair(null, state.getGameValue());
        } else if (depth < 0) {
            return new ActionValuePair(null, ((HasHeuristic) state).getHeuristic());
        }

        double vMin = Integer.MAX_VALUE;
        Action cMin = null;

        for (Action action : this.game.getActions(state)) {
            GameState newState = (GameState) this.game.doAction(state, action);
            ActionValuePair actionValuePair = maxValue(newState, alpha, beta, depth - 1);

            if (actionValuePair.getValue() < vMin) {
                vMin = actionValuePair.getValue();
                cMin = action;

                if (vMin < beta) {
                    beta = vMin;
                }
            }

            if (vMin <= alpha) {
                return new ActionValuePair(cMin, vMin);
            }

        }

        return new ActionValuePair(cMin, vMin);
    }

}
