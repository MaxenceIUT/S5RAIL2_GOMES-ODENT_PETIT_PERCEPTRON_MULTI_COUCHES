package classification;

import framework.MLP;
import framework.Sigmoide;
import framework.TransferFunction;
import utils.Progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLPClassification extends AlgoClassification {

    private final MLP mlp;

    public MLPClassification(List<Imagette> trainingImagettes, List<Imagette> testImagettes, int[] middleLayers,
                             double learningRate, TransferFunction transferFunction, double errorTarget) {
        super(trainingImagettes);

        Imagette first = trainingImagettes.getFirst();
        int pixels = first.getRows() * first.getCols();

        int[] layers = new int[]{pixels};
        System.arraycopy(middleLayers, 0, layers, 1, middleLayers.length);
        layers[layers.length - 1] = 10;

        this.mlp = new MLP(layers, learningRate, transferFunction);

        double errorPercentage = 100;
        int maxIterations = 1_000_000;
        int i = 0;

        while (i < maxIterations && errorPercentage > errorTarget) {
            Progress progress = new Progress("Itération n°" + i + " - Apprentissage... ");
            for (int j = 0; j < 10; j++) {
                progress.setProgress(j, 10);

                List<Imagette> training = new ArrayList<>(trainingImagettes);
                Collections.shuffle(training);

                for (int k = 0; k < training.size(); k++) {
                    Imagette imagette = training.get(k);
                    double[] inputs = getInputsArray(imagette);
                    double[] outputs = new double[10];
                    outputs[imagette.getLabel()] = 1;
                    this.mlp.backPropagate(inputs, outputs);
                    if(k % 100 == 0) progress.setProgress(j + (double) k / training.size(), 10);
                }
            }
            progress.setLabel("Itération n°" + i + " - Test... ");

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

            errorPercentage = (double) unsatisfyingValues / shuffledImagette.size() * 100;
            progress.complete("Fin de l'itération n°" + i + " - Pourcentage d'erreur: " + errorPercentage + "%");

            i++;
        }
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
