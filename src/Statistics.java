import framework.MLP;

import java.time.Duration;
import java.util.Arrays;

public class Statistics {

    public final MLP mlp;
    public final DataSet dataSet;
    public final double[] trainingError;
    public final int iterationCount, maxIterations;
    public final Duration duration;

    /**
     * Création d'un objet statistiques d'un apprentissage de MLP
     *
     * @param mlp            MLP entraîné
     * @param dataSet        Jeu de données utilisé
     * @param trainingError  Tableau des erreurs d'apprentissage finales
     * @param iterationCount Nombre d'itérations effectuées
     * @param maxIterations  Nombre d'itérations maximum
     * @param duration       Durée de l'apprentissage
     */
    public Statistics(MLP mlp, DataSet dataSet, double[] trainingError, int iterationCount, int maxIterations, Duration duration) {
        this.mlp = mlp;
        this.dataSet = dataSet;
        this.trainingError = trainingError;
        this.iterationCount = iterationCount;
        this.maxIterations = maxIterations;
        this.duration = duration;
    }

    public String toString() {
        String ms = duration.toMillis() + "ms";

        StringBuilder sb = new StringBuilder();
        sb.append("Apprentissage terminé en ").append(ms).append("\n");
        sb.append("Itérations effectuées : ").append(iterationCount).append(" (max. ").append(maxIterations).append(
                ")\n");
        sb.append("\n");
        Arrays.stream(trainingError).average().ifPresent(value -> {
            sb.append("Erreur finale moyenne : ").append(value).append("\n");
        });

        return sb.toString();
    }

    public MLP getMlp() {
        return mlp;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public double[] getTrainingError() {
        return trainingError;
    }

    public int getIterationCount() {
        return iterationCount;
    }

    public Duration getDuration() {
        return duration;
    }

}
