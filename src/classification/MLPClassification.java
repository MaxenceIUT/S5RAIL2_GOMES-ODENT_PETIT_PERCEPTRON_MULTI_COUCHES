package classification;

import framework.MLP;
import framework.TransferFunction;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MLPClassification extends AlgoClassification{

    private final MLP mlp;

    public MLPClassification(List<Imagette> trainingImagettes) {
        super(trainingImagettes);

        int[] layers = {10, 10, 10};
        double learningRate = 0.6;
        TransferFunction transferFunction = new Sigmoide();

        this.mlp = new MLP(layers, learningRate, transferFunction);

        var random = new Random();
        Imagette imagette = trainingImagettes.get(random.nextInt(trainingImagettes.size()));
        double[] inputs = new double[imagette.getRows() * imagette.getCols()];
        double[] outputs = new double[10];
        for (int row = 0; row < imagette.getRows(); row++) {
            for (int col = 0; col < imagette.getCols(); col++) {
                inputs[row * imagette.getCols() + col] = imagette.getPixels()[row][col];
            }
        }
        outputs[imagette.getLabel()] = 1;
        this.mlp.backPropagate(inputs, outputs);
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
