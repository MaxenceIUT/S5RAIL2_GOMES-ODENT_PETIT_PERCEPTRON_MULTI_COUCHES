public enum DataSet {

    OR(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {1}, {1}, {1}}),
    AND(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {0}, {0}, {1}}),
    XOR(new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}}, new double[][]{{0}, {1}, {1}, {0}});

    public final double[][] inputs;
    public final double[][] outputs;

    DataSet(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public int size() {
        return inputs.length;
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

    public static DataSet fromString(String name) {
        for (DataSet dataSet : DataSet.values()) {
            if (dataSet.name().equalsIgnoreCase(name)) {
                return dataSet;
            }
        }
        return null;
    }

}
