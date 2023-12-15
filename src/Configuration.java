import framework.Sigmoide;
import framework.Tanh;
import framework.TransferFunction;

import java.util.Arrays;

public class Configuration {

    private double learningRate = 0.6, errorTarget = 0.01;
    private int[] layers = {2, 2, 1};
    private TransferFunction transferFunction = new Sigmoide();
    private DataSet dataSet = DataSet.OR;
    private int maxIterations = 1_000_000;

    /**
     * Création d'une configuration de perceptron multicouche
     *
     * @param learningRate     Pas d'apprentissage
     * @param errorTarget      Taux d'erreur cible
     * @param layers           Couches (nombre de neurones par couche)
     * @param transferFunction Fonction de transfert
     * @param dataSet          Jeu de données
     */
    public Configuration(double learningRate, double errorTarget, int[] layers, TransferFunction transferFunction,
                         DataSet dataSet, int maxIterations) {
        this.learningRate = learningRate;
        this.errorTarget = errorTarget;
        this.layers = layers;
        this.transferFunction = transferFunction;
        this.dataSet = dataSet;
        this.maxIterations = maxIterations;
    }

    private Configuration() {
    }

    public static Configuration fromArgs(String[] args) {
        Configuration config = new Configuration();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-lr":
                    if (i + 1 < args.length) {
                        config.learningRate = Double.parseDouble(args[i + 1]);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -lr");
                    }
                    break;
                case "-et":
                    if (i + 1 < args.length) {
                        config.errorTarget = Double.parseDouble(args[i + 1]);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -et");
                    }
                    break;
                case "-l":
                    if (i + 1 < args.length) {
                        String[] layers = args[i + 1].split(",");
                        config.layers = new int[layers.length];
                        for (int j = 0; j < layers.length; j++) {
                            config.layers[j] = Integer.parseInt(layers[j]);
                        }
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -l");
                    }
                    break;
                case "-ft":
                    if (i + 1 < args.length) {
                        switch (args[i + 1]) {
                            case "tanh":
                                config.transferFunction = new Tanh();
                                break;
                            case "sigmoide":
                                config.transferFunction = new Sigmoide();
                                break;
                            default:
                                throw new IllegalArgumentException("Unknown transfer function: " + args[i + 1]);
                        }
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -ft");
                    }
                    break;
                case "-ds":
                    if (i + 1 < args.length) {
                        config.dataSet = DataSet.fromString(args[i + 1]);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -ds");
                    }
                    break;
                case "-mi":
                    if (i + 1 < args.length) {
                        config.maxIterations = Integer.parseInt(args[i + 1]);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Expected argument after -mi");
                    }
                case "-h":
                    System.out.println(getExample());
                    System.exit(0);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
            }
        }
        System.out.println("Pour voir l'aide, utilisez l'argument -h");
        return config;
    }

    public static String getExample() {
        String str = "Usage: [-lr <learning rate>] [-et <error target>] [-l <layers>] [-ds <dataset>] [-ft <fonction " +
                "de transfert>\n";
        str += "<layers>: Nombre de neurones par couche, séparés par des virgules (ex: 2,2,1)\n";
        str += "<dataset>: Dataset à utiliser (OR, AND, XOR)\n";
        str += "<fonction de transfert>: Fonction de transfert à utiliser (tanh, sigmoide)\n";
        str += "<max itérations>: Nombre d'itérations maximum\n";
        str += "\n";
        str += "Exemple: -lr 0.6 -et 0.01 -l 2,2,1 -ds OR -ft sigmoide -mi 1000000\n";
        return str;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "learningRate=" + learningRate +
                ", errorTarget=" + errorTarget +
                ", layers=" + Arrays.toString(layers) +
                ", transferFunction=" + transferFunction +
                ", dataSet=" + dataSet +
                ", maxIterations=" + maxIterations +
                '}';
    }

    public double getLearningRate() {
        return learningRate;
    }

    public double getErrorTarget() {
        return errorTarget;
    }

    public int[] getLayers() {
        return layers;
    }

    public TransferFunction getTransferFunction() {
        return transferFunction;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

}
