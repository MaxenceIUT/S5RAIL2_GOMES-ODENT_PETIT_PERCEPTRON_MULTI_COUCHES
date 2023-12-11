import framework.MLP;
import framework.TransferFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int MAX_ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        Configuration configuration = Configuration.fromArgs(args);
        Random random = new Random();

        // Récupération des paramètres depuis la configuration
        int[] layers = configuration.getLayers();
        double learningRate = configuration.getLearningRate();
        double errorTarget = configuration.getErrorTarget();
        TransferFunction transferFunction = configuration.getTransferFunction();
        DataSet dataSet = configuration.getDataSet();

        // On affiche les paramètres utilisés
        StringBuilder params = new StringBuilder();
        params.append("-".repeat(50)).append("\n");
        params.append("Paramètres :").append("\n");
        params.append("\t").append("- Pas d'apprentissage : ").append(learningRate).append("\n");
        params.append("\t").append("- Taux d'erreur cible : ").append(errorTarget).append("\n");
        params.append("\t").append("- Fonction de transfert : ").append(transferFunction.getClass().getSimpleName()).append("\n");
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
        while (i < MAX_ITERATIONS && Arrays.stream(trainingError).anyMatch(error -> error > errorTarget)) {
            int randomInput = random.nextInt(dataSet.getInputsArray().length);
            double[] input = dataSet.getInput(randomInput);
            double[] output = dataSet.getOutput(randomInput);

            trainingError[randomInput] = mlp.backPropagate(input, output);
            i++;
        }

        // Fin de l'apprentissage
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String ms = duration.toMillis() + "ms";

        // On affiche les statistiques
        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(50)).append("\n");
        sb.append("Apprentissage terminé en ").append(ms).append("\n");
        sb.append("Itérations effectuées : ").append(i).append(" (max. ").append(MAX_ITERATIONS).append(")\n");
        sb.append("\n");
        Arrays.stream(trainingError).average().ifPresent(value -> {
            sb.append("Erreur finale moyenne : ").append(value).append("\n");
        });
        sb.append("-".repeat(50));
        System.out.println(sb);

        System.out.println("\nTest des exemples: ");
        for (double[] doubles : dataSet.getInputsArray()) {
            System.out.println(" - " + Arrays.toString(doubles) + " -> " + Arrays.toString(mlp.execute(doubles)));
        }

    }

}
