package IM;

import com.sun.istack.internal.Nullable;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

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
    public static double[][] getMatrix(BufferedImage bufferedImage) {
        double[][] matrix = new double[bufferedImage.getWidth()][bufferedImage.getHeight()];

        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                matrix[i][j] = bufferedImage.getRGB(i,j) & 0xFF;
            }
        }

        return matrix;
    }

    public static BufferedImage setImage(double[][] matrix) {
        BufferedImage image = new BufferedImage(matrix.length, matrix[0].length, BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                double value = matrix[i][j];
                if (value > 255) {
                    value = 255;
                } else if (value < 0) {
                    value = 0;
                }
                image.setRGB(i, j, ((0xFF) << 24) | ((int)value << 16) | ((int)value << 8) | (int)value);
            }
        }
        return image;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print("" + matrix[i][j] + " ");
            }
            System.out.println("|");
        }
    }

    public static void sendToFile(String filename, int[][] matrix) throws IOException{
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                bw.write(matrix[i][j] + " ");
            }
            bw.newLine();
        }
        bw.flush();
    }

    public static double[][] filterMatrix(double[][] matrix, int filterValue) {
        int counter = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (counter++ > filterValue) {
                    matrix[i][j] = 0.0;
                }
            }
        }
        return matrix;
    }

    public static double[][] filterMatrixDiagonally(double[][] matrix, int filterValue) {
        // There will be ROW+COL-1 lines in the output
        int counter = 0;

        for (int line = 1; line <= (matrix.length + matrix[0].length - 1); line++) {
            int start_col = Math.max(0, line - matrix.length);
            int count = Math.min(Math.min(line, (matrix[0].length - start_col)), matrix.length);
            for (int j = 0; j < count; j++) {
                if (counter++ > filterValue) {
                    matrix[Math.min(matrix.length, line) - j - 1][start_col + j] = 0.0;
                }
            }
        }
        return matrix;
    }

    public static int[][] readFromFile(String fileName, int size) throws FileNotFoundException {
        Scanner input = new Scanner(new File(fileName));
        int[][] matrix = new int[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(input.hasNextInt()) {
                    matrix[i][j] = input.nextInt();
                }
            }
        }
        return matrix;
    }

    public static void filter(double[][] matrix, int filter) {
        ArrayList<Entity> entities = new ArrayList<>();

        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                entities.add(new Entity(i, j, matrix[i][j]));
            }
        }


        Collections.sort(entities);

        int counter = 0;

        for (Entity e : entities) {
            if (counter++ > filter) {
                System.out.println("Zeroing: " + "[" + e.x + "][" + e.y + "]");
                matrix[e.x][e.y] = 0.0;
            }
        }
    }

}
