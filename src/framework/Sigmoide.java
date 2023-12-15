package framework;

public class Sigmoide implements TransferFunction {

    @Override
    public double evaluate(double value) {
        return 1 / (1 + Math.exp(-value));
    }

    /**
     * @param value Entrée de la fonction (evaluate(x))
     * @return Sortie de la fonction dérivée sur l'entrée
     */
    @Override
    public double evaluateDer(double value) {
        return value - Math.pow(value, 2);
    }

    @Override
    public String toString() {
        return "framework.Sigmoide";
    }
}
