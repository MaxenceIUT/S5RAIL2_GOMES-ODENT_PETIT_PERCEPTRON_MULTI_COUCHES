import classification.*;
import framework.Sigmoide;
import framework.TransferFunction;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestsMNIST {

    public static final String TRAIN_IMAGES_IDX3 = "numbers/train-images.idx3-ubyte";
    public static final String TRAIN_LABELS_IDX1 = "numbers/train-labels.idx1-ubyte";
    public static final String TEST_IMAGES_IDX3 = "numbers/t10k-images.idx3-ubyte";
    public static final String TEST_LABELS_IDX1 = "numbers/t10k-labels.idx1-ubyte";

    public static void main(String[] args) throws IOException {
        File resourceFolder = new File("MNIST");
        if (!resourceFolder.exists()) {
            System.out.println("Resources folder not found");
            return;
        }

        File trainingImagesFile = new File(resourceFolder, TRAIN_IMAGES_IDX3);
        File trainingLabelsFile = new File(resourceFolder, TRAIN_LABELS_IDX1);
        File testImagesFile = new File(resourceFolder, TEST_IMAGES_IDX3);
        File testLabelsFile = new File(resourceFolder, TEST_LABELS_IDX1);

        if (!trainingImagesFile.exists()) {
            System.out.println("Training images file not found");
            return;
        }
        if (!trainingLabelsFile.exists()) {
            System.out.println("Training labels file not found");
            return;
        }
        if (!testImagesFile.exists()) {
            System.out.println("Test images file not found");
            return;
        }
        if (!testLabelsFile.exists()) {
            System.out.println("Test labels file not found");
            return;
        }

        Donnees trainingData = new Donnees(trainingImagesFile, trainingLabelsFile);
        Donnees testData = new Donnees(testImagesFile, testLabelsFile);
        List<Imagette> trainingImagettes = trainingData.getImagettes();
        List<Imagette> testImagettes = testData.getImagettes();

        AlgoClassification knn = new KNN(trainingImagettes, 3);

        int[] layers = {150, 100};
        double learningRate = 0.6, errorTarget = 2;
        TransferFunction tf = new Sigmoide();
        AlgoClassification mlp = new MLPClassification(trainingImagettes, testImagettes, layers, learningRate, tf, errorTarget);

        List<AlgoClassification> algos = List.of(knn, mlp);

        for (AlgoClassification algo : algos) {
            int errorCount = 0;
            for (Imagette imagette : testImagettes) {
                int classifiedAs = mlp.classify(imagette);
                int expected = imagette.getLabel();
                if (classifiedAs != expected) errorCount++;
            }

            System.out.println("=".repeat(50));
            System.out.println("Algorithme: " + algo.getClass().getSimpleName());
            System.out.println("Nombre d'erreurs: " + errorCount + " (" + (errorCount * 100 / testImagettes.size()) + "%)");
        }
    }

}
