import classification.Donnees;
import classification.Imagette;
import classification.KNN;
import classification.MLPClassification;
import framework.Sigmoide;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestsMLPClassification {
    public static void main(String[] args) throws IOException {
        var images  = new File("MNIST/train-images.idx3-ubyte");
        var labels = new File("MNIST/train-labels.idx1-ubyte");
        List<Imagette> imagettes = Donnees.load(images, labels);

        var knn = new KNN(imagettes, 5);

        int[] layersExceptFirsAndLast = new int[]{30, 20};
        double learningRate = 0.6;
        var transferFunction = new Sigmoide();
        double errorTarget = 5.0;
        int maxIterations = 1_000_000;
        var mlpClassification = new MLPClassification(imagettes, layersExceptFirsAndLast, learningRate, transferFunction, errorTarget, maxIterations);
    }
}
