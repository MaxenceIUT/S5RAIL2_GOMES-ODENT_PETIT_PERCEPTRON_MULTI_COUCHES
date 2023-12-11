import framework.MLP;
import framework.TransferFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class MLPCompute {

    /**
     * @param configuration Configuration de l'apprentissage
     * @param maxIterations Nombre d'itérations maximum
     * @return
     */
    public static Statistics compute(Configuration configuration) {
        Random random = new Random();

        // Récupération des paramètres depuis la configuration
        int[] layers = configuration.getLayers();
        double learningRate = configuration.getLearningRate();
        double errorTarget = configuration.getErrorTarget();
        TransferFunction transferFunction = configuration.getTransferFunction();
        DataSet dataSet = configuration.getDataSet();
        int maxIterations = configuration.getMaxIterations();

        // On affiche les paramètres utilisés
        StringBuilder params = new StringBuilder();
        params.append("-".repeat(50)).append("\n");
        params.append("Paramètres :").append("\n");
        params.append("\t").append("- Pas d'apprentissage : ").append(learningRate).append("\n");
        params.append("\t").append("- Taux d'erreur cible : ").append(errorTarget).append("\n");
        params.append("\t").append("- Fonction de transfert : ").append(transferFunction).append("\n");
        params.append("\t").append("- Couches : ").append(Arrays.toString(layers)).append("\n");
        params.append("\t").append("- Jeu de données : ").append(dataSet.name()).append("\n");
        params.append("-".repeat(50));
        System.out.println(params);

        // On instancie le perceptron multicouche
        MLP mlp = new MLP(layers, learningRate, transferFunction);

        // On démarre l'apprentissage
        Instant start = Instant.now();
        System.out.println("Apprentissage en cours...");

        // Création d'un tableau rempli de 1 (taille dataSet.getInputsArray().length)
        double[] trainingError = Arrays.stream(dataSet.getOutputsArray()).mapToDouble(input -> 1.0).toArray();
        int i = 0;
        // Tant que l'on a pas excédé le nombre d'itérations max et qu'au moins le taux d'erreur d'un exemple est supérieur à l'erreur cible
        while (i < maxIterations && Arrays.stream(trainingError).anyMatch(error -> error > errorTarget)) {
            int randomInput = random.nextInt(dataSet.getInputsArray().length);
            double[] input = dataSet.getInput(randomInput);
            double[] output = dataSet.getOutput(randomInput);

            trainingError[randomInput] = mlp.backPropagate(input, output);
            i++;
        }

        // Fin de l'apprentissage
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);

        return new Statistics(mlp, dataSet, trainingError, i, maxIterations, duration);
    }

}
