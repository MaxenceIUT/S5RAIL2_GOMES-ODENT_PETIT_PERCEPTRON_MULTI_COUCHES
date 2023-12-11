import java.util.Arrays;
import java.util.List;

public class TestsMLP {

    public static void main(String[] args) {
        Configuration orTanh221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Tanh(),
                DataSet.OR,
                1_000_000
        );
        Configuration orSig221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Sigmoide(),
                DataSet.OR,
                1_000_000
        );
        Configuration andTanh221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Tanh(),
                DataSet.AND,
                1_000_000
        );
        Configuration andSig221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Sigmoide(),
                DataSet.AND,
                1_000_000
        );
        Configuration xorTanh221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Tanh(),
                DataSet.XOR,
                1_000_000
        );
        Configuration xorSig221Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 1},
                new Sigmoide(),
                DataSet.XOR,
                1_000_000
        );
        Configuration orAndTanh222Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 2},
                new Tanh(),
                DataSet.ORAND,
                1_000_000
        );
        Configuration orAndSig222Config = new Configuration(
                0.6,
                0.01,
                new int[]{2, 2, 2},
                new Sigmoide(),
                DataSet.ORAND,
                1_000_000
        );

        List<Configuration> tests = Arrays.asList(
                orTanh221Config,
                orSig221Config,
                andTanh221Config,
                andSig221Config,
                xorTanh221Config,
                xorSig221Config,
                orAndTanh222Config,
                orAndSig222Config
        );
        for (Configuration config : tests) {
            Statistics statistics = MLPCompute.compute(config);
            System.out.println(statistics);
            System.out.println("Test des exemples: ");
            for (double[] doubles : config.getDataSet().getInputsArray()) {
                System.out.println(" - " + Arrays.toString(doubles) + " -> " + Arrays.toString(statistics.getMlp().execute(doubles)));
            }
        }
    }

}
