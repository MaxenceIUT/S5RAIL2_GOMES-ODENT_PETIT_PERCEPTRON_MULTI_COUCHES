import framework.MLP;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Configuration config = Configuration.fromArgs(args);
        Statistics statistics = MLPCompute.compute(config);
        MLP trainedMlp = statistics.getMlp();

        // On affiche les statistiques
        System.out.println(statistics);

        System.out.println("\nTest des exemples: ");
        for (double[] doubles : statistics.getDataSet().getInputsArray()) {
            System.out.println(" - " + Arrays.toString(doubles) + " -> " + Arrays.toString(trainedMlp.execute(doubles)));
        }

    }

}
