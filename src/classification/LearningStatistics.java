package classification;

import java.util.ArrayList;
import java.util.List;

public class LearningStatistics {

    private final int[] layers;
    private final List<IterationStatistics> iterationStatistics;

    public LearningStatistics(int[] layers) {
        this.layers = layers;
        this.iterationStatistics = new ArrayList<>();
    }

    public void addIterationStatistics(IterationStatistics iterationStatistics) {
        this.iterationStatistics.add(iterationStatistics);
    }

    public List<IterationStatistics> getIterationStatistics() {
        return iterationStatistics;
    }

    public int[] getLayers() {
        return layers;
    }

}
