package classification;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class CSVExporter {

    private final LearningStatistics learningStatistics;

    public CSVExporter(LearningStatistics learningStatistics) {
        this.learningStatistics = learningStatistics;
    }

    public void export(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        try (FileWriter fileWriter = new FileWriter(new File(file, getFileName()))) {
            fileWriter.write("Layers;Iteration;Recognition Failure Rate;Learning Failure Rate\n");
            for (IterationStatistics iterationStatistic : this.learningStatistics.getIterationStatistics()) {
                String line = Arrays.toString(this.learningStatistics.getLayers()) + ";"
                        + iterationStatistic.iteration() + ";"
                        + iterationStatistic.recognitionFailureRate() + ";"
                        + iterationStatistic.learningFailureRate() + "\n";
                fileWriter.write(line);
            }
        }
    }

    private String getFileName() {
        return "stats-" +
                Arrays.stream(learningStatistics.getLayers())
                        .mapToObj(String::valueOf)
                        .reduce((s, s2) -> s + "-" + s2)
                        .orElse("")
                + ".csv";
    }

}
