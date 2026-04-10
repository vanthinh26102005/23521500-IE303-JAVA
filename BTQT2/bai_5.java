import java.util.Locale;
import java.util.Scanner;

public class bai_5 {
    private static final double EPS = 1e-9;

    private static boolean isEvenPositive(double value) {
        if (value <= 0) {
            return false;
        }
        long rounded = Math.round(value);
        return Math.abs(value - rounded) < EPS && rounded % 2 == 0;
    }

    private static Double minEvenPositive(double[][] matrix) {
        Double min = null;
        for (double[] row : matrix) {
            for (double value : row) {
                if (isEvenPositive(value)) {
                    if (min == null || value < min) {
                        min = value;
                    }
                }
            }
        }
        return min;
    }

    private static double averageOfColumnMins(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        double sum = 0.0;

        for (int col = 0; col < n; col++) {
            double colMin = matrix[0][col];
            for (int row = 1; row < m; row++) {
                if (matrix[row][col] < colMin) {
                    colMin = matrix[row][col];
                }
            }
            sum += colMin;
        }
        return sum / n;
    }

    private static double[][] removeRowWithLargestSum(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        if (m == 1) {
            return new double[0][n];
        }

        int removeRow = 0;
        double maxSum = Double.NEGATIVE_INFINITY;

        for (int row = 0; row < m; row++) {
            double rowSum = 0.0;
            for (int col = 0; col < n; col++) {
                rowSum += matrix[row][col];
            }
            if (rowSum > maxSum) {
                maxSum = rowSum;
                removeRow = row;
            }
        }

        double[][] result = new double[m - 1][n];
        int newRow = 0;
        for (int row = 0; row < m; row++) {
            if (row == removeRow) {
                continue;
            }
            System.arraycopy(matrix[row], 0, result[newRow], 0, n);
            newRow++;
        }
        return result;
    }

    private static boolean isSquare(double[][] matrix) {
        return matrix.length == matrix[0].length;
    }

    private static boolean isUpperTriangular(double[][] matrix) {
        int n = matrix.length;
        for (int row = 1; row < n; row++) {
            for (int col = 0; col < row; col++) {
                if (Math.abs(matrix[row][col]) > EPS) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isLowerTriangular(double[][] matrix) {
        int n = matrix.length;
        for (int row = 0; row < n; row++) {
            for (int col = row + 1; col < n; col++) {
                if (Math.abs(matrix[row][col]) > EPS) {
                    return false;
                }
            }
        }
        return true;
    }

    private static double[][] multiplyMatrices(double[][] a, double[][] b) {
        int aRows = a.length;
        int aCols = a[0].length;
        int bCols = b[0].length;

        double[][] product = new double[aRows][bCols];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bCols; j++) {
                double sum = 0.0;
                for (int k = 0; k < aCols; k++) {
                    sum += a[i][k] * b[k][j];
                }
                product[i][j] = sum;
            }
        }
        return product;
    }

    private static double[][] transpose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] transposed = new double[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    private static void printMatrix(double[][] matrix) {
        if (matrix.length == 0) {
            System.out.println("(ma tran rong)");
            return;
        }

        for (double[] row : matrix) {
            StringBuilder line = new StringBuilder();
            for (double value : row) {
                line.append(String.format(Locale.US, "%10.3f", value));
            }
            System.out.println(line);
        }
    }

    private static double[][] readMatrix(Scanner sc, int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = sc.nextDouble();
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhap so dong m va so cot n cua ma tran M: ");
        int m = sc.nextInt();
        int n = sc.nextInt();

        if (m <= 0 || n <= 0) {
            System.out.println("Kich thuoc ma tran khong hop le.");
            sc.close();
            return;
        }

        System.out.println("Nhap cac phan tu cua ma tran M (" + m + "x" + n + "):");
        double[][] mMatrix = readMatrix(sc, m, n);

        Double minEvenPos = minEvenPositive(mMatrix);
        if (minEvenPos == null) {
            System.out.println("a) Khong co phan tu chan duong trong ma tran.");
        } else {
            System.out.printf(Locale.US, "a) Phan tu chan duong nho nhat: %.3f%n", minEvenPos);
        }

        double avgColMin = averageOfColumnMins(mMatrix);
        System.out.printf(Locale.US, "b) Trung binh cac phan tu nho nhat tren moi cot: %.3f%n", avgColMin);

        double[][] removed = removeRowWithLargestSum(mMatrix);
        System.out.println("c) Ma tran sau khi xoa dong co tong lon nhat:");
        printMatrix(removed);

        if (isSquare(mMatrix)) {
            System.out.println("d) M la ma tran vuong.");
            boolean upper = isUpperTriangular(mMatrix);
            boolean lower = isLowerTriangular(mMatrix);

            if (upper && lower) {
                System.out.println("   M vua la ma tran tam giac tren vua la ma tran tam giac duoi (ma tran duong cheo).");
            } else if (upper) {
                System.out.println("   M la ma tran tam giac tren.");
            } else if (lower) {
                System.out.println("   M la ma tran tam giac duoi.");
            } else {
                System.out.println("   M khong phai ma tran tam giac tren/duoi.");
            }
        } else {
            System.out.println("d) M khong phai la ma tran vuong.");
        }

        System.out.println("Nhap cac phan tu cua ma tran N (" + n + "x" + m + "):");
        double[][] nMatrix = readMatrix(sc, n, m);

        double[][] product = multiplyMatrices(mMatrix, nMatrix);
        System.out.println("e) Tich M x N:");
        printMatrix(product);

        double[][] transposed = transpose(product);
        System.out.println("f) Chuyen vi cua tich M x N:");
        printMatrix(transposed);

        sc.close();
    }
}
