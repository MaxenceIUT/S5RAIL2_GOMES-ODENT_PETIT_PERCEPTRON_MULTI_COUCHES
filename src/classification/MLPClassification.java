package classification;

import framework.MLP;
import framework.TransferFunction;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MLPClassification extends AlgoClassification {

    private final MLP mlp;

    public MLPClassification(List<Imagette> trainingImagettes, int[] layersExceptFirstAndLast, double learningRate, TransferFunction transferFunction, double errorTarget, int maxIterations) {
        super(trainingImagettes);

        int pixels = trainingImagettes.getFirst().getRows() * trainingImagettes.getFirst().getCols();
        int[] layers = new int[]{pixels};
        System.arraycopy(layersExceptFirstAndLast, 0, layers, 1, layersExceptFirstAndLast.length);
        layers[layers.length - 1] = 10;

        this.mlp = new MLP(layers, learningRate, transferFunction);

        double errorPercentage = 100;
        int i = 0;

        while (i < maxIterations && errorPercentage > errorTarget) {

            List<Imagette> shuffledImagette;
            double[] inputs = new double[trainingImagettes.getFirst().getRows() * trainingImagettes.getFirst().getCols()];
            double[] outputs = new double[10];

            for (int j = 0; j <= 10; j++) {

                Collections.shuffle(trainingImagettes);
                shuffledImagette = trainingImagettes.stream().limit(1000).toList();

                for (Imagette imagette : shuffledImagette) {
                    for (int row = 0; row < imagette.getRows(); row++) {
                        for (int col = 0; col < imagette.getCols(); col++) {
                            inputs[row * imagette.getCols() + col] = imagette.getPixels()[row][col];
                        }
                    }
                    outputs[imagette.getLabel()] = 1;
                    this.mlp.backPropagate(inputs, outputs);
                }
            }

            Collections.shuffle(trainingImagettes);
            shuffledImagette = trainingImagettes.stream().limit(1000).toList();

            int unsatisfyingValues = 0;
            for (Imagette imagette : shuffledImagette) {
                outputs = this.mlp.execute(inputs);
                if (outputs[imagette.getLabel()] != 1) {
                    unsatisfyingValues++;
                }
            }
            errorPercentage = (double) unsatisfyingValues / shuffledImagette.size() * 100;

            i++;
        }
    }

    @Override
    public int classify(Imagette imagette) {
        double[] inputs = new double[imagette.getRows() * imagette.getCols()];
        for (int row = 0; row < imagette.getRows(); row++) {
            for (int col = 0; col < imagette.getCols(); col++) {
                inputs[row * imagette.getCols() + col] = imagette.getPixels()[row][col];
            }
        }
        double[] outputs = mlp.execute(inputs);
        return (int) Arrays.stream(outputs).max().orElseThrow();
    }
}
