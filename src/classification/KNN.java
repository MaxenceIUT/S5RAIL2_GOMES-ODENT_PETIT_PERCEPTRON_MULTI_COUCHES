package classification;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class KNN extends AlgoClassification {

    private final int k;

    public KNN(List<Imagette> trainingImagettes, int k) {
        super(trainingImagettes);
        this.k = k;
    }

    @Override
    public int classify(Imagette imagette) {
        List<Double> distances = trainingImagettes.stream().map((trainingImagette) -> {
            double distance = 0;
            for (int row = 0; row < trainingImagette.getRows(); row++) {
                for (int col = 0; col < trainingImagette.getCols(); col++) {
                    int value = trainingImagette.getPixels()[row][col];
                    int imagetteValue = imagette.getPixels()[row][col];
                    distance += Math.pow(value - imagetteValue, 2);
                }
            }
            return distance;
        }).toList();

        List<Double> kDistances = distances.stream().sorted().limit(k).toList();
        List<Integer> kLabels = kDistances.stream().map((kDistance) -> trainingImagettes.get(distances.indexOf(kDistance)).getLabel()).toList();
        return Collections.max(kLabels, Comparator.comparingInt(o -> Collections.frequency(kLabels, o)));
    }

}
