package IM.DCT;

import IM.Utils;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DCTInverse {

    public double[][] process(double[][] matrix, int offset) {

        int N = matrix.length;

        double[][] output = Arrays.copyOf(matrix, matrix.length); //z
        double[][] result = new double[matrix.length][matrix[0].length]; //z
        double[][] result2 = new double[matrix.length][matrix[0].length]; //z
        for(int count = 0; count < matrix.length; count++) {
            result[count] = this.idct(matrix[count]);
        }

        result = this.transpose(result);
        for(int count = 0; count < N; count++) {
            result2[count] = this.idct(result[count]);
        }

        return result2;
    }


    public double[][] transpose(double[][] matrix) {
        double[][] transposed = new double[matrix.length][matrix[0].length];

        for(int x = 0; x < matrix.length; x++) {
            for(int y = 0; y < matrix.length; y++) {
                transposed[x][y] = matrix[y][x];
            }
        }
        return transposed;
    }

    public double[] idct(double[] matrix) {
        double[] output = new double[matrix.length]; //c2

        double alfa;
        double sum;
        int N = matrix.length;

        for (int n = 0; n < matrix.length; n++) {
            sum = 0;
            for (int k = 0; k < N; k++) {
                alfa = k == 0 ? Math.sqrt(1.0/N) : Math.sqrt(2.0/N);
                sum += alfa * matrix[k] * Math.cos((Math.PI * (2.0 * n + 1.0) * k) / (2.0 * N));
            }

            output[n] = sum;
        }


        return output;
    }
}
