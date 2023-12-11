import framework.MLP;
import framework.TransferFunction;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class Main {

    private static final int MAX_ITERATIONS = 1_000_000;

    public static void main(String[] args) {
        int[] layers = {2, 2, 1};
        double learningRate = 0.003;
        double errorTarget = 0.01;
        TransferFunction transferFunction = new Sigmoide();

        /* OR */
        double[][] orInput = {
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        };
        double[][] orOutput = {
                {0},
                {1},
                {1},
                {1}
        };

        MLP mlp = new MLP(layers, learningRate, transferFunction);
        Random random = new Random();

        Instant start = Instant.now();
        System.out.println("Apprentissage en cours...");

        double[] trainingError = Arrays.stream(orInput).mapToDouble(input -> 1.0).toArray();
        int i = 0;
        while(i < MAX_ITERATIONS && Arrays.stream(trainingError).anyMatch(error -> error > errorTarget)) {
            int randomInput = random.nextInt(orInput.length);
            double[] input = orInput[randomInput];
            double[] output = orOutput[randomInput];

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

        System.out.println(Arrays.toString(mlp.execute(new double[]{0, 1})));

        System.out.println(sb);
    }

}
