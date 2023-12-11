package classification;

import java.util.List;

public abstract class AlgoClassification {

    public final List<Imagette> trainingImagettes;

    public AlgoClassification(List<Imagette> trainingImagettes) {
        this.trainingImagettes = trainingImagettes;
    }

    abstract public int classify(Imagette imagette);

}