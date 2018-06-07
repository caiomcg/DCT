package IM;

import com.sun.istack.internal.Nullable;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.image.BufferedImage;
import java.io.File;

public class Utils {
    @Nullable
    public static FileChooser createFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //Set extension filter
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        fileChooser.setTitle(title);
        return fileChooser;
    }

    public static String getFileExtension(String url) {
        int index = url.lastIndexOf(".");
        if (index < 0) {
            return "";
        }
        return url.substring(index + 1);
    }
    public static int[][] getMatrix(BufferedImage bufferedImage) {
        int[][] matrix = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                matrix[i][j] = bufferedImage.getRGB(i,j) & 0xFF;
            }
        }

        return matrix;
    }

    public static BufferedImage setImage(int[][] matrix) {
        BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (matrix[i][j] > 255) {
                    matrix[i][j] = 255;
                } else if (matrix[i][j] < 0) {
                    matrix[i][j] = 0;
                }
                image.setRGB(i, j, ((0xFF) << 24) | (matrix[i][j] << 16) | (matrix[i][j] << 8) | matrix[i][j]);
            }
        }
        return image;
    }

    public static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print("" + matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
