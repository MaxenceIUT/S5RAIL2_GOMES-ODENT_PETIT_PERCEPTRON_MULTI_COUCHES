package framework;

public class Layer {

    public Neuron[] neurons;
    public int length;

    /**
     * Couche de Neurones
     *
     * @param l    Taille de la couche
     * @param prev Taille de la couche précédente
     */
    public Layer(int l, int prev) {
        length = l;
        neurons = new Neuron[l];

        for (int j = 0; j < length; j++)
            neurons[j] = new Neuron(prev);
    }

}