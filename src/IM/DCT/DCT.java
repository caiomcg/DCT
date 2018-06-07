package IM.DCT;

import IM.Utils;

import java.awt.image.BufferedImage;

public class DCT implements DCTInterface {
    @Override
    public BufferedImage process(BufferedImage input) {

        int[][] matrix = Utils.getMatrix(input);
        int[][] output = new int[matrix.length][matrix[0].length];

        double ci, cj, dct1, sum;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {

                // ci and cj depends on frequency as well as
                // number of row and columns of specified matrix
                if (i == 0)
                    ci = 1 / Math.sqrt(matrix.length);
                else
                    ci = Math.sqrt(2) / Math.sqrt(matrix.length);
                if (j == 0)
                    cj = 1 / Math.sqrt(matrix[0].length);
                else
                    cj = Math.sqrt(2) / Math.sqrt(matrix[0].length);

                // sum will temporarily store the sum of
                // cosine signals
                sum = 0;
                for (int k = 0; k < matrix.length; k++) {
                    for (int l = 0; l < matrix[0].length; l++) {
                        dct1 = matrix[k][l] *
                                Math.cos((2 * k + 1) * i * Math.PI / (2 * matrix.length)) *
                                Math.cos((2 * l + 1) * j * Math.PI / (2 * matrix[0].length));
                        sum = sum + dct1;
                    }
                }
                output[i][j] = (int) (ci * cj * sum);
            }
            System.err.println("Setting: [" + matrix.length + "x" + matrix[0].length + "] " + i);
        }
        return Utils.setImage(output);
    }
}
