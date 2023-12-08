package framework;

public class MLP {

    protected double fLearningRate = 0.6;
    protected Layer[] fLayers;
    protected TransferFunction fTransferFunction;


    /**
     * @param layers       Nb neurones par couches
     * @param learningRate tx d'apprentissage
     * @param fun          Function de transfert
     */
    public MLP(int[] layers, double learningRate, TransferFunction fun) {
        fLearningRate = learningRate;
        fTransferFunction = fun;

        fLayers = new Layer[layers.length];
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) {
                fLayers[i] = new Layer(layers[i], layers[i - 1]);
            } else {
                fLayers[i] = new Layer(layers[i], 0);
            }
        }
    }


    /**
     * Réponse à une entrée
     *
     * @param input l'entrée testée
     * @return résultat de l'exécution
     */
    public double[] execute(double[] input) {
        int i, j, k;
        double newValue;

        double[] output = new double[fLayers[fLayers.length - 1].length];

        // input en entrée du réseau
        for (i = 0; i < fLayers[0].length; i++) {
            fLayers[0].neurons[i].value = input[i];
        }

        // calculs couches cachées et sortie
        for (k = 1; k < fLayers.length; k++) {
            for (i = 0; i < fLayers[k].length; i++) {
                newValue = 0.0;
                for (j = 0; j < fLayers[k - 1].length; j++)
                    newValue += fLayers[k].neurons[i].weights[j] * fLayers[k - 1].neurons[j].value;

                newValue -= fLayers[k].neurons[i].bias;
                fLayers[k].neurons[i].value = fTransferFunction.evaluate(newValue);
            }
        }

        // Renvoyer sortie
        for (i = 0; i < fLayers[fLayers.length - 1].length; i++) {
            output[i] = fLayers[fLayers.length - 1].neurons[i].value;
        }
        return output;
    }


    /**
     * Rétropropagation
     *
     * @param input  L'entrée courante
     * @param output Sortie souhaitée (apprentissage supervisé !)
     * @return Error différence entre la sortie calculée et la sortie souhaitée
     */

    public double backPropagate(double[] input, double[] output) {
        double[] newOutput = execute(input);
        double error;
        int i, j, k;


        // Erreur de sortie
        for (i = 0; i < fLayers[fLayers.length - 1].length; i++) {
            error = output[i] - newOutput[i];
            fLayers[fLayers.length - 1].neurons[i].delta = error * fTransferFunction.evaluateDer(newOutput[i]);
        }

        for (k = fLayers.length - 2; k >= 0; k--) {
            // Calcul de l'erreur courante pour les couches cachées
            // et mise à jour des Delta de chaque neurone
            for (i = 0; i < fLayers[k].length; i++) {
                error = 0.0;
                for (j = 0; j < fLayers[k + 1].length; j++)
                    error += fLayers[k + 1].neurons[j].delta * fLayers[k + 1].neurons[j].weights[i];
                fLayers[k].neurons[i].delta = error * fTransferFunction.evaluateDer(fLayers[k].neurons[i].value);
            }
            // Mise à jour des poids de la couche suivante
            for (i = 0; i < fLayers[k + 1].length; i++) {
                for (j = 0; j < fLayers[k].length; j++)
                    fLayers[k + 1].neurons[i].weights[j] += fLearningRate * fLayers[k + 1].neurons[i].delta * fLayers[k].neurons[j].value;
                fLayers[k + 1].neurons[i].bias -= fLearningRate * fLayers[k + 1].neurons[i].delta;
            }
        }

        // Calcul de l'erreur
        error = 0.0;
        for (i = 0; i < output.length; i++) {
            error += Math.abs(newOutput[i] - output[i]);
        }
        error = error / output.length;
        return error;
    }

    /**
     * @return LearningRate
     */
    public double getLearningRate() {
        return fLearningRate;
    }

    /**
     * maj LearningRate
     *
     * @param rate nouveau LearningRate
     */
    public void setLearningRate(double rate) {
        fLearningRate = rate;
    }

    /**
     * maj fonction de tranfert
     *
     * @param fun nouvelle fonction de tranfert
     */
    public void setTransferFunction(TransferFunction fun) {
        fTransferFunction = fun;
    }

    /**
     * @return Taille couche d'entrée
     */
    public int getInputLayerSize() {
        return fLayers[0].length;
    }


    /**
     * @return Taille couche de sortie
     */
    public int getOutputLayerSize() {
        return fLayers[fLayers.length - 1].length;
    }
}