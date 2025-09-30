public class App {
    public static void main(String[] args) {
        int[] w = new int[20 - 2];
        for (int i = 0; i < w.length; ++i)
            w[i] = 19 - i;

        float[] x = new float[14];
        for (int i = 0; i < x.length; ++i)
            x[i] = (float) (Math.random() * (15 + 4) - 4);

        double[][] w1 = new double[18][14];
        for (int i = 0; i < w1.length; ++i) {
            for (int j = 0; j < w1[0].length; ++j)
                w1[i][j] = calcMatrixElem(i, j, w, x);
        }

        printMatrix(w1);
    }

    private static double calcMatrixElem(int i, int j, int[] w, float[] x) {
        if (w[i] == 16)
            return Math.pow(
                    4 * (0.25 + Math.cbrt(Math.pow(
                            0.5 * x[j],
                            2))),
                    2);
        if (w[i] <= 5 || w[i] == 7 || w[i] == 10 || w[i] == 15 || w[i] == 17 || w[i] == 18)
            return Math.pow(
                    Math.asin(Math.cos(x[j])),
                    0.5 * (3 + Math.sin(x[j] / 2)));
        return Math.pow(
                (Math.exp(Math.pow(
                        (x[j] + 0.5) / 0.5,
                        x[j]) / 2)) / (1 - Math.cbrt(Math.cos(Math.exp(x[j])))),
                2);
    }

    private static void printMatrix(double[][] m) {
        for (double[] i : m) {
            for (double j : i)
                System.out.printf("%.3f\t", j);
            System.out.println();
        }
    }
}
