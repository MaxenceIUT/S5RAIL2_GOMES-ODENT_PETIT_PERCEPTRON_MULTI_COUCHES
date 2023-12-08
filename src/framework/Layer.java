package framework;

public class Layer {

    public Neuron[] neurons;
    public int length;

    /**
     * Couche de neurones
     *
     * @param length Taille de la couche
     * @param prev   Taille de la couche précédente
     */
    public Layer(int length, int prev) {
        this.length = length;
        neurons = new Neuron[length];

        for (int j = 0; j < this.length; j++)
            neurons[j] = new Neuron(prev);
    }

}