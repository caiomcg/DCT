package IM.DCT;

import IM.Utils;

import java.awt.image.BufferedImage;

public class DCTInverse {

    public double[][] process(double[][] matrix, int offset) {

        int n = matrix.length;
        int m = matrix[0].length;
        double[][] c = new double[n][m];
        double[][] c2 = new double[n][m];
        double alfa;

        for (int k = 0; k < n; k++) {
            for (int l = 0; l < m; l++) {
                c[k][l] = 0;
                for (int i = 0; i < n; i++) {
                    alfa = i == 0 ? 1. / Math.sqrt(n) : Math.sqrt(2. / n);
                    c[k][l] += alfa * matrix[i][l]
                            * Math.cos((Math.PI * (2 * k + 1) * i) / (2 * n));
                }
            }
        }

        for (int l = 0; l < m; l++) {
            for (int k = 0; k < n; k++) {
                c2[k][l] = 0;
                for (int j = 0; j < m; j++) {
                    alfa = j == 0 ? 1. / Math.sqrt(m) : Math.sqrt(2. / m);
                    c2[k][l] += alfa * c[k][j]
                            * Math.cos((Math.PI * (2 * l + 1) * j) / (2 * m));
                }
                c2[k][l] += offset;
            }
        }

        return c2;
    }
}
