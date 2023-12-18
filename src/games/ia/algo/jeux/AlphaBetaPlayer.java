package games.ia.algo.jeux;

import games.ia.framework.common.Action;
import games.ia.framework.common.ActionValuePair;
import games.ia.framework.jeux.Game;
import games.ia.framework.jeux.GameState;
import games.ia.framework.jeux.Player;

public class AlphaBetaPlayer extends Player {

    private static final int maxDepth = 3;
    private int consideredStates = 0;

    public AlphaBetaPlayer(Game game, boolean p1) {
        super(game, p1);
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair actionValuePair;

        if (state.getPlayerToMove() == 'O') {
            actionValuePair = maxValue(state, Double.MIN_VALUE, Double.MAX_VALUE, maxDepth);
        } else {
            actionValuePair = minValue(state, Double.MIN_VALUE, Double.MAX_VALUE, maxDepth);
        }
        System.out.println("States considered: " + this.consideredStates);
        return actionValuePair.getAction();
    }

    private ActionValuePair maxValue(GameState state, double alpha, double beta, int depth) {
        if (this.game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        } else if (depth == 0) {
            return new ActionValuePair(null, this.game.getHeuristicPoints(state));
        }

        double vMax = Double.MIN_VALUE;
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
        if (this.game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        } else if (depth == 0) {
            return new ActionValuePair(null, this.game.getHeuristicPoints(state));
        }

        double vMin = Double.MAX_VALUE;
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