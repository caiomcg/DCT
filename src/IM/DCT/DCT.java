package IM.DCT;

import IM.Utils;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class DCT {
//    public BufferedImage process(BufferedImage input) {
//
//        int[][] matrix = Utils.getMatrix(input);
//        int[][] dctLine = new int[matrix.length][matrix[0].length];
//        int[][] dctColumn = new int[matrix.length][matrix[0].length];
//        int[][] output = new int[matrix.length][matrix[0].length];
//
//        double ci, cj, dct1, dct2, sumLine, sumColumn;
//
//        for (int i = 0; i < matrix.length; i++) {
//
//
//            // ci and cj depends on frequency as well as
//            // number of row and columns of specified matrix
//            if (i == 0)
//                ci = 1 / Math.sqrt(matrix.length);
//            else
//                ci = Math.sqrt(2) / Math.sqrt(matrix.length);
//
//
//            // sum will temporarily store the sum of
//            // cosine signals
//
//            for (int k = 0; k < matrix.length; k++) {
//                sumLine = 0;
//                for (int l = 0; l < matrix[0].length; l++) {
//                    dct1 = matrix[k][l] *
//                            Math.cos((2 * k + 1) * i * Math.PI / (2 * matrix.length));
//                    sumLine = sumLine + dct1;
//                }
//                dctLine[i][k] = (int) (ci * sumLine);
//            }
//        }
//        for (int j = 0; j < matrix[0].length; j++) {
//            if (j == 0)
//                cj = 1 / Math.sqrt(matrix[0].length);
//            else
//                cj = Math.sqrt(2) / Math.sqrt(matrix[0].length);
//
//            for (int k = 0; k < matrix.length; k++) {
//                sumColumn = 0;
//                for (int l = 0; l < matrix[0].length; l++) {
//                    dct2 = dctLine[k][l] *
//                            Math.cos((2 * l + 1) * j * Math.PI / (2 * matrix[0].length));
//                    sumColumn = sumColumn + dct2;
//                }
//                dctColumn[k][j] = (int) (cj * sumColumn);
//            }
//        }
//            //System.err.println("Setting: [" + matrix.length + "x" + matrix[0].length + "] " + i);
//        for (int i = 0; i < matrix.length; i++) {
//            for (int k = 0; k < matrix[0].length; k++) {
//                output[i][k] = dctLine[i][k] * dctColumn[i][k];
//            }
//        }
//        return Utils.setImage(output);
//    }

//    public double[][] process(double[][] input) {
//
//        double[][] matrix = input; //z
//        double[][] output = new double[matrix.length][matrix[0].length]; //c
//        double[][] output2 = new double[matrix.length][matrix[0].length]; //c2
//
//        double alfa;
//        double sum;
//        double offset = 256;
//
//        for (int k = 0; k < matrix.length; k++) {
//            for (int l = 0; l < matrix[0].length; l++) {
//                sum = 0;
//                for (int i = 0; i < matrix.length; i++) {
//                    sum += (matrix[i][l] + offset)
//                            * Math.cos((Math.PI * (2. * i + 1.) * k) / (2. * matrix.length));
//                }
//                alfa = k == 0 ? 1. / Math.sqrt(matrix.length) : Math.sqrt(2. / matrix.length);
//                output[k][l] = (alfa * sum);
//            }
//        }
//
//        for (int l = 0; l < matrix[0].length; l++) {
//            for (int k = 0; k < matrix.length; k++) {
//                sum = 0;
//                for (int j = 0; j < matrix[0].length; j++) {
//                    sum += output[k][j]
//                            * Math.cos((Math.PI * (2. * j + 1.) * l) / (2. * matrix[0].length));
//                }
//                alfa = l == 0 ? 1. / Math.sqrt(matrix[0].length) : Math.sqrt(2. / matrix[0].length);
//                output2[k][l] = (alfa * sum);
//            }
//        }
//
//        return output2;
//    }

    public double[][] process(double[][] input) {

        double[][] matrix = Arrays.copyOf(input, input.length); //z
        double[][] result = new double[input.length][input[0].length]; //z
        double[][] result2 = new double[input.length][input[0].length]; //z
        for(int count = 0; count < matrix.length; count++) {
            result[count] = this.dct(matrix[count]);
        }

        result = this.transpose(result);
        for(int count = 0; count < matrix.length; count++) {
            result2[count] = this.dct(result[count]);
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

    public double[] dct(double[] matrix) {
        int N = matrix.length;
        double[] output = new double[N]; //X[n]

        double alfa;
        double sum;

        for (int k = 0; k < N; k++) {
            alfa = k == 0 ? Math.sqrt(1.0/N) : Math.sqrt(2.0/N);
            sum = 0.0;
            for (int n = 0; n < N; n++) {
                sum += matrix[n] * Math.cos((Math.PI * (2.0 * n + 1.0) * k) / (2.0 * N));
            }
            output[k] = (alfa * sum);
        }


        return output;
    }
}
