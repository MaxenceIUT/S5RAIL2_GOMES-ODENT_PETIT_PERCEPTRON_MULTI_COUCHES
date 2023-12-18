package classification;

import utils.Progress;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Donnees {

    private final List<Imagette> imagettes;

    public Donnees(List<Imagette> imagettes) {
        this.imagettes = imagettes;
    }

    public Donnees(File trainingImagesFile, File trainingLabelsFile) throws IOException {
        this(load(trainingImagesFile, trainingLabelsFile));
    }

    public static List<Imagette> load(File trainingImagesFile, File trainingLabelsFile) throws IOException {
        InputStream imageStream = Files.newInputStream(trainingImagesFile.toPath());
        InputStream labelsStream = Files.newInputStream(trainingLabelsFile.toPath());
        DataInputStream trainingImages = new DataInputStream(imageStream);
        DataInputStream trainingLabels = new DataInputStream(labelsStream);

        int trainingType = trainingImages.readInt();
        int labelType = trainingLabels.readInt();
        int imageCount = trainingImages.readInt();
        int labelCount = trainingLabels.readInt();
        int numRows = trainingImages.readInt();
        int numCols = trainingImages.readInt();

        System.out.println("Il y a " + imageCount + " images et " + labelCount + " labels");
        System.out.println("Les images font du " + numRows + "x" + numCols + " pixels");

        List<Imagette> imagettes = new ArrayList<>();
        Progress progress = new Progress("Chargement des images... ");

        for (int i = 0; i < imageCount; i++) {
            int label = trainingLabels.readUnsignedByte();
            Imagette imagette = new Imagette(numRows, numCols, label);
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    int value = trainingImages.readUnsignedByte();
                    imagette.setPixel(row, col, value);
                }
            }
            imagettes.add(imagette);
            if (i % 25 == 0) {
                progress.setProgress(i + 1, imageCount);
            }
        }

        progress.complete("Chargement des images terminé !");
        return imagettes;
    }

    public List<Imagette> getImagettes() {
        return imagettes;
    }

}
