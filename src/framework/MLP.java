package framework;

public class MLP {

    protected double learningRate = 0.6;
    protected Layer[] layers;
    protected TransferFunction transferFunction;

    /**
     * @param layers           Nombre de neurones par couche
     * @param learningRate     Taux d'apprentissage
     * @param transferFunction Fonction de transfert
     */
    public MLP(int[] layers, double learningRate, TransferFunction transferFunction) {
        this.learningRate = learningRate;
        this.transferFunction = transferFunction;

        this.layers = new Layer[layers.length];
        for (int i = 0; i < layers.length; i++) {
            if (i != 0) {
                this.layers[i] = new Layer(layers[i], layers[i - 1]);
            } else {
                this.layers[i] = new Layer(layers[i], 0);
            }
        }
    }

    /**
     * Réponse à une entrée
     *
     * @param input L'entrée testée
     * @return Résultat de l'exécution
     */
    public double[] execute(double[] input) {
        int i, j, k;
        double newValue;

        double[] output = new double[layers[layers.length - 1].length];

        // input en entrée du réseau
        for (i = 0; i < layers[0].length; i++) {
            layers[0].neurons[i].value = input[i];
        }

        // calculs couches cachées et sortie
        for (k = 1; k < layers.length; k++) {
            for (i = 0; i < layers[k].length; i++) {
                newValue = 0.0;
                for (j = 0; j < layers[k - 1].length; j++)
                    newValue += layers[k].neurons[i].weights[j] * layers[k - 1].neurons[j].value;

                newValue -= layers[k].neurons[i].bias;
                layers[k].neurons[i].value = transferFunction.evaluate(newValue);
            }
        }

        // Renvoyer sortie
        for (i = 0; i < layers[layers.length - 1].length; i++) {
            output[i] = layers[layers.length - 1].neurons[i].value;
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
        for (i = 0; i < layers[layers.length - 1].length; i++) {
            error = output[i] - newOutput[i];
            layers[layers.length - 1].neurons[i].delta = error * transferFunction.evaluateDer(newOutput[i]);
        }

        for (k = layers.length - 2; k >= 0; k--) {
            // Calcul de l'erreur courante pour les couches cachées
            // et mise à jour des Delta de chaque neurone
            for (i = 0; i < layers[k].length; i++) {
                error = 0.0;
                for (j = 0; j < layers[k + 1].length; j++)
                    error += layers[k + 1].neurons[j].delta * layers[k + 1].neurons[j].weights[i];
                layers[k].neurons[i].delta = error * transferFunction.evaluateDer(layers[k].neurons[i].value);
            }
            // Mise à jour des poids de la couche suivante
            for (i = 0; i < layers[k + 1].length; i++) {
                for (j = 0; j < layers[k].length; j++)
                    layers[k + 1].neurons[i].weights[j] += learningRate * layers[k + 1].neurons[i].delta * layers[k].neurons[j].value;
                layers[k + 1].neurons[i].bias -= learningRate * layers[k + 1].neurons[i].delta;
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
        return learningRate;
    }

    /**
     * maj LearningRate
     *
     * @param rate nouveau LearningRate
     */
    public void setLearningRate(double rate) {
        learningRate = rate;
    }

    /**
     * maj fonction de tranfert
     *
     * @param fun nouvelle fonction de tranfert
     */
    public void setTransferFunction(TransferFunction fun) {
        transferFunction = fun;
    }

    /**
     * @return Taille couche d'entrée
     */
    public int getInputLayerSize() {
        return layers[0].length;
    }


    /**
     * @return Taille couche de sortie
     */
    public int getOutputLayerSize() {
        return layers[layers.length - 1].length;
    }

}