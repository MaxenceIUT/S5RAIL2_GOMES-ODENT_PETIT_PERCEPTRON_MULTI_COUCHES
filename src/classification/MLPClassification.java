package classification;

import framework.MLP;
import framework.TransferFunction;


import java.util.*;

public class MLPClassification extends AlgoClassification{

    private final MLP mlp;

    public MLPClassification(List<Imagette> trainingImagettes) {
        super(trainingImagettes);

        int[] layers = {10, 10, 10};
        double learningRate = 0.6;
        TransferFunction transferFunction = new Sigmoide();

        this.mlp = new MLP(layers, learningRate, transferFunction);

        var random = new Random();
        var trainingError = new ArrayList<>(Collections.nCopies(10, 1.0));
        double errorTarget = 0.01;
        int maxIterations = 1_000_000;
        int i = 0;

        while (i < maxIterations && trainingError.stream().anyMatch(error -> error > errorTarget)) {
            int randomInput = random.nextInt(trainingImagettes.size());
            Imagette imagette = trainingImagettes.get(randomInput);
            double[] inputs = new double[imagette.getRows() * imagette.getCols()];
            double[] outputs = new double[10];

            for (int row = 0; row < imagette.getRows(); row++) {
                for (int col = 0; col < imagette.getCols(); col++) {
                    inputs[row * imagette.getCols() + col] = imagette.getPixels()[row][col];
                }
            }

            outputs[imagette.getLabel()] = 1;
            trainingError.set(randomInput, this.mlp.backPropagate(inputs, outputs));
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
