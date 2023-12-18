package games.ia.problemes;

import games.ia.framework.common.Action;
import games.ia.framework.common.State;
import games.ia.framework.jeux.Game;
import games.ia.framework.jeux.GameState;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Classe qui represente le jeux connect 4
 */
public class ConnectFour extends Game {

    private int rows;
    private int cols;

    public ConnectFour(int r, int c) {
        cols = c;
        rows = r;

        // Construire le tableaux d'actions  
        // une action par colonne
        // laisser tomber une pièce 

        ACTIONS = new Action[c];
        for (int i = 0; i < c; i++)
            ACTIONS[i] = new Action(Integer.toString(i));
    }

    /**
     * {@inheritDoc}
     * <p>Crée une grille vide</p>
     */
    public GameState init() {
        return new ConnectFourState(rows, cols);
    }

    /**
     * {@inheritDoc}
     * <p>retourne les actions possibles en fonction
     * du joueur et de l'état du jeux</p>
     */
    public ArrayList<Action> getActions(State s) {
        ArrayList<Action> actions = new ArrayList<Action>();
        for (Action a : ACTIONS) {
            int action_idx = Integer.parseInt(a.getName());
            ConnectFourState game_state = (ConnectFourState) s;
            if (game_state.isLegal(action_idx))
                actions.add(a);
        }
        return actions;
    }

    /**
     * {@inheritDoc}
     * <p>retourne retourn la nouvelle grille après l'action</p>
     */
    public State doAction(State s, Action a) {
        ConnectFourState new_s = (ConnectFourState) s.clone();
        int action_idx = Integer.parseInt(a.getName());
        new_s.play(action_idx);
        return new_s;
    }

    /**
     * {@inheritDoc}
     * <p>Fin dès qu'il y a une ligne ou plus de place de jeux</p>
     */
    public boolean endOfGame(GameState s) {
        return (((ConnectFourState) s).getGameValue()) > -1.0;

    }

    /**
     * Retourne une action depuis une entrée utilisateur
     */
    public Action getHumanMove(GameState state) {

        Scanner in = new Scanner(System.in);
        Boolean played = false;

        while (!played) {
            System.out.print("A toi de jouer [0 à " + cols + "] > ");
            try {
                int move = in.nextInt();
                if (move >= 0 && move < cols &&
                        ((ConnectFourState) state).isLegal(move))
                    return ACTIONS[move];
                else
                    System.out.println("Invalide !");

            } catch (java.util.InputMismatchException e) {
                in.nextLine();
                System.out.println("Invalide !");
            }
        }

        return null; // faire plaisir a javac
    }

    /**
     * @param s l'état du jeux
     * @return Une heuristique pour l'état s
     */
    @Override
    public double getHeuristicPoints(GameState s) {
        int playerToMove = s.getPlayerToMove();
        ConnectFourState state = (ConnectFourState) s;

        double points = 0;
        int[][] board = state.getBoard();

        // If 4 in a row, add 1000 points
        // If 3 in a row, add 100 points
        // If 2 in a row, add 10 points

        // Check rows
        for (int i = 0; i < rows; i++) {
            int count = 0;
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == playerToMove) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == 4) {
                    points += 1000;
                } else if (count == 3) {
                    points += 100;
                } else if (count == 2) {
                    points += 10;
                }
            }
        }

        // Check columns
        for (int i = 0; i < cols; i++) {
            int count = 0;
            for (int j = 0; j < rows; j++) {
                if (board[j][i] == playerToMove) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == 4) {
                    points += 1000;
                } else if (count == 3) {
                    points += 100;
                } else if (count == 2) {
                    points += 10;
                }
            }
        }

        // Check diagonals
        for (int i = 0; i < rows - 3; i++) {
            for (int j = 0; j < cols - 3; j++) {
                int count = 0;
                for (int k = 0; k < 4; k++) {
                    if (board[i + k][j + k] == playerToMove) {
                        count++;
                    } else {
                        count = 0;
                    }
                    if (count == 4) {
                        points += 1000;
                    } else if (count == 3) {
                        points += 100;
                    } else if (count == 2) {
                        points += 10;
                    }
                }
            }
        }

        return points;
    }


}
