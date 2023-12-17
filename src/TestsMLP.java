import framework.Sigmoide;
import framework.Tanh;
import framework.TransferFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestsMLP {

    public static final int[][] testLayers = {
            {2},
            {5, 2},
            {2, 3}
    };
    public static final TransferFunction[] testFunctions = {
            new Tanh(),
            new Sigmoide()
    };
    public static final DataSet[] testDatasets = {
            DataSet.OR,
            DataSet.AND,
            DataSet.XOR,
            DataSet.ORAND
    };

    public static void main(String[] args) {
        // On génère toutes les configurations à tester
        List<Configuration> configurationsToTest = new ArrayList<>();
        for (int[] layer : testLayers) {
            for (TransferFunction testFunction : testFunctions) {
                for (DataSet testDataset : testDatasets) {
                    // On crée nos couches en ajoutant une couche d'entrée et une couche de sortie
                    int[] modifiedLayers = new int[layer.length + 2];
                    modifiedLayers[0] = testDataset.inputSize();
                    System.arraycopy(layer, 0, modifiedLayers, 1, layer.length);
                    modifiedLayers[modifiedLayers.length - 1] = testDataset.outputSize();

                    Configuration config = new Configuration(
                            0.6,
                            0.01,
                            modifiedLayers,
                            testFunction,
                            testDataset,
                            1_000_000
                    );
                    configurationsToTest.add(config);
                }
            }
        }
        // On lance les tests
        testConfigurations(configurationsToTest);
    }

    /**
     * @param tests Liste des configurations à tester
     */
    private static void testConfigurations(List<Configuration> tests) {
        for (Configuration config : tests) {
            // On exécute l'apprentissage de notre perceptron multicouche
            Statistics statistics = MLPCompute.compute(config);

            // On affiche les résultats et les valeurs obtenues sur les exemples
            System.out.println(statistics);
            System.out.println("Test des exemples: ");
            for (double[] doubles : config.getDataSet().getInputsArray()) {
                System.out.println(" - " + Arrays.toString(doubles) + " -> " + Arrays.toString(statistics.getMlp().execute(doubles)));
            }
        }
    }

}
