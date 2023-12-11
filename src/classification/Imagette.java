package classification;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Imagette {

    private final int rows, cols;
    private final int[][] pixels;
    private final int label;

    public Imagette(int rows, int cols, int label) {
        this.rows = rows;
        this.cols = cols;
        this.label = label;
        this.pixels = new int[rows][cols];
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(cols, rows, BufferedImage.TYPE_INT_RGB);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int value = pixels[row][col];
                Color color = new Color(value, value, value);
                image.setRGB(col, row, color.getRGB());
            }
        }
        return image;
    }

    /**
     * @param path Path to save the image
     * @throws IOException If an error occurs while saving the image
     */
    public void saveImage(String path) throws IOException {
        ImageIO.write(this.toBufferedImage(), "png", new File(path));
    }

    public void setPixel(int row, int col, int value) {
        pixels[row][col] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getLabel() {
        return label;
    }

    public int[][] getPixels() {
        return pixels;
    }

}
