import framework.MLP;
import framework.TransferFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class MLPCompute {

    /**
     * @param configuration Configuration de l'apprentissage
     * @return Statistiques de l'apprentissage
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

        // On affiche la configuration utilisée
        System.out.println("-".repeat(50));
        System.out.println(configuration);
        System.out.println();

        // On instancie le perceptron multicouche
        MLP mlp = new MLP(layers, learningRate, transferFunction);

        // On démarre l'apprentissage
        Instant start = Instant.now();
        System.out.println("Apprentissage en cours...");

        // Création d'un tableau rempli de 1 (taille dataSet.getInputsArray().length)
        double[] trainingError = Arrays.stream(dataSet.getOutputsArray()).mapToDouble(input -> 1.0).toArray();
        int i = 0;
        /*
         Tant que :
          - le nombre d'itérations maximum n'est pas atteint ET QUE
          - le taux d'erreur cible n'est pas atteint par au moins un exemple
         */
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
