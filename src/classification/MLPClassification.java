package classification;

import framework.MLP;
import framework.Sigmoide;
import framework.TransferFunction;
import utils.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLPClassification extends AlgoClassification {

    private MLP mlp;
    private final int trainingIterationsCount;
    private final List<Imagette> testImagettes;
    private final int[] middleLayers;
    private final double initialLearningRate;
    private final double finalLearningRate;
    private final TransferFunction transferFunction;

    public MLPClassification(List<Imagette> trainingImagettes, int trainingIterationsCount, List<Imagette> testImagettes, int[] middleLayers, double initialLearningRate, double finalLearningRate, TransferFunction transferFunction) {
        super(trainingImagettes);
        this.trainingIterationsCount = trainingIterationsCount;
        this.testImagettes = testImagettes;
        this.middleLayers = middleLayers;
        this.initialLearningRate = initialLearningRate;
        this.finalLearningRate = finalLearningRate;
        this.transferFunction = transferFunction;
    }

    public LearningStatistics train() {
        LearningStatistics stats = new LearningStatistics(middleLayers);

        // On récupère le nombre de pixels d'une imagette
        Imagette first = trainingImagettes.getFirst();
        int pixels = first.getRows() * first.getCols();

        // On définit les couches
        int[] layers = getLayers(pixels, middleLayers);

        // On crée le MLP
        this.mlp = new MLP(layers, initialLearningRate, transferFunction);

        // On lance l'apprentissage
        for (int i = 1; i <= trainingIterationsCount; i++) {
            Progress progress = new Progress("Itération n°" + i + " - Apprentissage... ");
            double learningFailureRate = 0;

            // On s'entraîne sur toutes les imagettes d'apprentissage 10 fois, en mélangeant les imagettes à chaque fois
            for (int j = 0; j < 10; j++) {
                progress.setProgress(j, 10);

                List<Imagette> training = new ArrayList<>(trainingImagettes);
                Collections.shuffle(training);

                for (int k = 0; k < training.size(); k++) {
                    Imagette imagette = training.get(k);
                    double[] inputs = getInputsArray(imagette);
                    double[] outputs = new double[10];
                    outputs[imagette.getLabel()] = 1;
                    learningFailureRate += this.mlp.backPropagate(inputs, outputs);
                    if(k % 100 == 0) progress.setProgress(j + (double) k / training.size(), 10);
                }
            }

            learningFailureRate /= 10 * trainingImagettes.size();
            progress.setLabel("Itération n°" + i + " - Test... ");

            // Après ces 10 itérations, on calcule notre pourcentage d'erreur sur la base de test (1000 imagettes aléatoires)

            Collections.shuffle(testImagettes);
            List<Imagette> shuffledImagette = testImagettes.stream().limit(1000).toList();

            int unsatisfyingValues = 0;
            int j = 0;
            for (Imagette imagette : shuffledImagette) {
                progress.setProgress(j, shuffledImagette.size());
                // On récupère la sortie de notre MLP pour l'entrée courante
                double[] inputs = getInputsArray(imagette);
                double[] outputs = this.mlp.execute(inputs);

                // On considère la valeur devinée comme l'indice du neurone ayant la plus grande valeur
                int guessedLabel = getGuessedLabel(outputs);

                // Si la valeur devinée est différente de la valeur attendue, on incrémente le nombre d'erreurs
                if (imagette.getLabel() != guessedLabel) unsatisfyingValues++;

                j++;
            }

            // On calcule donc le pourcentage d'erreur de reconnaissance
            double errorPercentage = (double) unsatisfyingValues / shuffledImagette.size() * 100;
            progress.complete("Fin de l'itération n°" + i + " - Pourcentage d'erreur: " + errorPercentage + "% - " +
                    "Pourcentage d'erreur sur la base d'apprentissage: " + learningFailureRate * 100 + "%");

            // On génère un objet contenant les statistiques de l'itération
            IterationStatistics iterationStatistics = new IterationStatistics(i, errorPercentage, learningFailureRate);
            stats.addIterationStatistics(iterationStatistics);

            // On met à jour le learning rate (qui est dégressif)
            double slope = (finalLearningRate - initialLearningRate) / trainingIterationsCount;
            mlp.setLearningRate(initialLearningRate + slope * i);
        }

        // Une fois l'apprentissage terminé, on retourne les statistiques
        return stats;
    }

    private int[] getLayers(int pixels, int[] middleLayers) {
        int[] layers = new int[middleLayers.length + 2];
        layers[0] = pixels;
        System.arraycopy(middleLayers, 0, layers, 1, middleLayers.length);
        layers[layers.length - 1] = 10;
        return layers;
    }

    /**
     * @param outputs Sortie du réseau
     * @return Indice du neurone ayant la plus grande valeur
     */
    private static int getGuessedLabel(double[] outputs) {
        int guessedLabel = 0;
        for (int j = 0; j < outputs.length; j++) {
            if (outputs[j] > outputs[guessedLabel]) {
                guessedLabel = j;
            }
        }
        return guessedLabel;
    }

    /**
     * @param imagette Imagette à transformer en tableau d'entrées
     * @return Tableau d'entrées
     */
    private static double[] getInputsArray(Imagette imagette) {
        double[] inputs = new double[imagette.getRows() * imagette.getCols()];
        for (int row = 0; row < imagette.getRows(); row++) {
            for (int col = 0; col < imagette.getCols(); col++) {
                inputs[row * imagette.getCols() + col] = imagette.getPixels()[row][col] / 255.0;
            }
        }
        return inputs;
    }

    @Override
    public int classify(Imagette imagette) {
        double[] inputs = getInputsArray(imagette);
        double[] outputs = mlp.execute(inputs);
        return getGuessedLabel(outputs);
    }

}
