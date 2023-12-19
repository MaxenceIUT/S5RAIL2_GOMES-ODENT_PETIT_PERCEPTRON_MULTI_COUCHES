package gps.ia.framework.recherche;

/**
 * Ajoute la notion d'heuristique aux états de certains problèmes
 */

public interface HasHeuristic {

    /**
     * Retourne la valeur de l'heuristique 
     * @return Le résultat 
     */
    double getHeuristic();
    
}
