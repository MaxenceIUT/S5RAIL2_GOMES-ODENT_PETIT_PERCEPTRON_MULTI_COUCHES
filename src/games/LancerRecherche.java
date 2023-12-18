package games;

import games.ia.framework.common.ArgParse;
import games.ia.framework.common.State;
import games.ia.framework.recherche.SearchProblem;
import games.ia.framework.recherche.TreeSearch;

/**
 * Lance un algorithme de recherche  
 * sur un problème donné et affiche le resultat
 */
public class LancerRecherche {

    public static void main(String[] args){

        // fixer le message d'aide
        ArgParse.setUsage
            ("Utilisation :\n\n"
             + "java LancerRecherche [-prob problem] [-algo algoname]"
             + "[-v] [-h]\n"
             + "-prob : Le nom du problem {dum, map, vac, puz}. Par défautl vac\n"
             + "-algo : L'agorithme {rnd, bfs, dfs, ucs, gfs, astar}. Par défault rnd\n"
             + "-v    : Rendre bavard (mettre à la fin)\n"
             + "-h    : afficher ceci (mettre à la fin)"
             );

        
        // récupérer les option de la ligne de commande
        String prob_name = ArgParse.getProblemFromCmd(args);
        String algo_name = ArgParse.getAlgoFromCmd(args);

        // créer un problem, un état intial et un  algo
        SearchProblem p = ArgParse.makeProblem(prob_name);
        State s = ArgParse.makeInitialState(prob_name);
        TreeSearch algo = ArgParse.makeAlgo(algo_name, p, s);
        
        // resoudre 
        if( algo.solve() )
            algo.printSuccess();
        else
            algo.printFailure();
            
    }
}
