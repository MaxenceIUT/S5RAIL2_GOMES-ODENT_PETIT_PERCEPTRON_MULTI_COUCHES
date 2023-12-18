package games.ia.algo.jeux;

import games.ia.framework.common.Action;
import games.ia.framework.common.ActionValuePair;
import games.ia.framework.common.State;
import games.ia.framework.jeux.Game;
import games.ia.framework.jeux.GameState;
import games.ia.framework.jeux.Player;

public class MinMaxPlayer extends Player {

    private int consideredStates = 0;

    public MinMaxPlayer(Game game, boolean p1) {
        super(game, p1);
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair actionValuePair;
        if (state.getPlayerToMove() == 1) {
            actionValuePair = maxValue(state);
        } else {
            actionValuePair = minValue(state);
        }
        System.out.println("States considered: " + this.consideredStates);
        return actionValuePair.getAction();
    }

    private ActionValuePair maxValue(GameState state) {
        if (this.game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMax = Double.MIN_VALUE;
        Action cMax = null;

        for (Action action : this.game.getActions(state)) {
            GameState newState = (GameState) this.game.doAction(state, action);
            this.consideredStates++;
            ActionValuePair actionValuePair = minValue(newState);
            if(actionValuePair.getValue() > vMax) {
                vMax = actionValuePair.getValue();
                cMax = action;
            }
        }

        return new ActionValuePair(cMax, vMax);
    }

    private ActionValuePair minValue(GameState state) {
        if(this.game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMin = Double.MAX_VALUE;
        Action cMin = null;

        for (Action action : this.game.getActions(state)) {
            GameState newState = (GameState) this.game.doAction(state, action);
            ActionValuePair actionValuePair = maxValue(newState);
            if(actionValuePair.getValue() < vMin) {
                vMin = actionValuePair.getValue();
                cMin = action;
            }
        }

        return new ActionValuePair(cMin, vMin);
    }

}
