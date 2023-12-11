import framework.MLP;
import framework.TransferFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int MAX_ITERATIONS = 1_000_000;
    private static final double[][] INPUT = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    private static final double[][] AND_OUTPUT = {{0}, {0}, {0}, {1}};
    private static final double[][] OR_OUTPUT = {{0}, {1}, {1}, {1}};
    private static final double[][] XOR_OUTPUT = {{0}, {1}, {1}, {0}};

    public static void main(String[] args) {
        int[] layers = {2, 2, 1};
        double learningRate = 0.6;
        double errorTarget = 0.01;
        TransferFunction transferFunction = new Sigmoide();

        MLP mlp = new MLP(layers, learningRate, transferFunction);
        Random random = new Random();

        Instant start = Instant.now();
        System.out.println("Apprentissage en cours...");

        // Création d'un tableau rempli de 1 (taille INPUT.length)
        double[] trainingError = Arrays.stream(INPUT).mapToDouble(input -> 1.0).toArray();
        int i = 0;
        // Tant que l'on a pas excédé le nombre d'itérations max et qu'au moins le taux d'erreur d'un exemple est supérieur à l'erreur cible
        while (i < MAX_ITERATIONS && Arrays.stream(trainingError).anyMatch(error -> error > errorTarget)) {
            int randomInput = random.nextInt(INPUT.length);
            double[] input = INPUT[randomInput];
            double[] output = OR_OUTPUT[randomInput];

            trainingError[randomInput] = mlp.backPropagate(input, output);
            i++;
        }

        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        String ms = duration.toMillis() + "ms";

        StringBuilder sb = new StringBuilder();
        sb.append("-".repeat(50)).append("\n");
        sb.append("Apprentissage terminé en ").append(ms).append("\n");
        sb.append("Itérations effectuées : ").append(i).append(" (max. ").append(MAX_ITERATIONS).append(")\n");
        sb.append("\n");
        Arrays.stream(trainingError).average().ifPresent(value -> {
            sb.append("Erreur finale moyenne : ").append(value).append("\n");
        });
        sb.append("-".repeat(50));

        for (double[] doubles : OR_OUTPUT) {
            System.out.println(Arrays.toString(doubles) + " -> " + Arrays.toString(mlp.execute(doubles)));
        }

        System.out.println(sb);
    }

}
