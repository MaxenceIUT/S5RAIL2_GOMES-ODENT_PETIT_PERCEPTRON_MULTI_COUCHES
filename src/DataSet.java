public enum DataSet {

    OR(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {1}, {1}, {1}}),
    AND(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {0}, {0}, {1}}),
    XOR(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {1}, {1}, {0}}),
    ORAND(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0, 0}, {1, 0}, {1, 0}, {1, 1}});

    public final double[][] inputs;
    public final double[][] outputs;

    DataSet(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static DataSet fromString(String name) {
        for (DataSet dataSet : DataSet.values()) {
            if (dataSet.name().equalsIgnoreCase(name)) {
                return dataSet;
            }
        }
        return null;
    }

    public int size() {
        return inputs.length;
    }

    public int inputSize() {
        return inputs[0].length;
    }

    public int outputSize() {
        return outputs[0].length;
    }

    public double[] getInput(int index) {
        return inputs[index];
    }

    public double[] getOutput(int index) {
        return outputs[index];
    }

    public double[][] getInputsArray() {
        return inputs;
    }

    public double[][] getOutputsArray() {
        return outputs;
    }

}
