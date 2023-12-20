import classification.*;
import framework.Sigmoide;
import framework.TransferFunction;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestsMNIST {

    public static final String TRAIN_IMAGES_IDX3 = "numbers/train-images.idx3-ubyte";
    public static final String TRAIN_LABELS_IDX1 = "numbers/train-labels.idx1-ubyte";
    public static final String TEST_IMAGES_IDX3 = "numbers/t10k-images.idx3-ubyte";
    public static final String TEST_LABELS_IDX1 = "numbers/t10k-labels.idx1-ubyte";

    public static final int TRAINING_ITERATIONS_COUNT = 25;

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

        List<AlgoClassification> trainedMlps = createAndTrainMlps(trainingImagettes, testImagettes);

        List<AlgoClassification> algos = new ArrayList<>(trainedMlps);
        algos.add(knn);

        for (AlgoClassification algo : algos) {
            int errorCount = 0;
            for (Imagette imagette : testImagettes) {
                int classifiedAs = algo.classify(imagette);
                int expected = imagette.getLabel();
                if (classifiedAs != expected) errorCount++;
            }

            System.out.println("=".repeat(50));
            System.out.println("Algorithme: " + algo.getClass().getSimpleName());
            System.out.println("Nombre d'erreurs: " + errorCount + " (" + (errorCount * 100 / testImagettes.size()) + "%)");
        }
    }

    private static List<AlgoClassification> createAndTrainMlps(List<Imagette> trainingImagettes, List<Imagette> testImagettes) {
        List<AlgoClassification> mlps = new ArrayList<>();
        List<int[]> possibleLayers = List.of(
                new int[]{100, 100, 100},
                new int[]{100, 200},
                new int[]{300},
                new int[]{150, 100},
                new int[]{30, 10},
                new int[]{300, 100},
                new int[]{50, 100},
                new int[]{150, 100}
        );
        double initialLearningRate = 0.6, finalLearningRate = 0.2;
        TransferFunction tf = new Sigmoide();

        ExecutorService executorService = Executors.newFixedThreadPool(possibleLayers.size());
        List<Future<AlgoClassification>> futures = new ArrayList<>();

        for (int[] layers : possibleLayers) {
            Callable<AlgoClassification> task = () -> {
                MLPClassification mlp = new MLPClassification(trainingImagettes, TRAINING_ITERATIONS_COUNT, testImagettes, layers,
                        initialLearningRate, finalLearningRate, tf);

                LearningStatistics stats = mlp.train();
                CSVExporter exporter = new CSVExporter(stats);
                try {
                    exporter.export("statistics/");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return mlp;
            };

            futures.add(executorService.submit(task));
        }

        executorService.shutdown();

        for (Future<AlgoClassification> future : futures) {
            try {
                mlps.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return mlps;
    }
}
