package com.example.supercalculator;

//класс для работы с матрицами
public class Matrix {
    //вычитвание матриц
    public static double[][] subtraction(double[][] m1, double[][] m2) {
        double[][] sub = new double[m1.length][m1[0].length];
        if (m1.length == m2.length && m1[0].length == m2[0].length) {
            for (int i = 0; i < m1.length; i++)
                for (int j = 0; j < m1[0].length; j++)
                    sub[i][j] = m1[i][j] - m2[i][j];
            return sub;
        } else return null;
    }

    //сложение матриц
    public static double[][] addition(double[][] m1, double[][] m2) {
        double[][] add = new double[m1.length][m1[0].length];
        if (m1.length == m2.length && m1[0].length == m2[0].length) {
            for (int i = 0; i < m1.length; i++)
                for (int j = 0; j < m1[0].length; j++)
                    add[i][j] = m1[i][j] + m2[i][j];
            return add;
        } else return null;
    }

    //умножение на скаляр
    public static double[][] constMultiply(double l, double[][] matrix) {
        double[][] cMult = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[0].length; j++)
                cMult[i][j] = matrix[i][j] * l;
        return cMult;
    }

    //умножение матриц
    public static double[][] multiply(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length;
        int m2RowLength = m2.length;
        if (m1ColLength != m2RowLength) return null; //
        int mRRowLength = m1.length;
        int mRColLength = m2[0].length;
        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {
            for (int j = 0; j < mRColLength; j++) {
                for (int k = 0; k < m1ColLength; k++) {
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return mResult;
    }

    //транспонирование матрицы
    public static double[][] transpose(double[][] matrix) {
        double[][] trasnp = new double[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix.length; j++)
                trasnp[i][j] = matrix[j][i];
        return trasnp;
    }

    //нахождение детерминанта матрицы
    public static double determinant(double[][] matrix) {
        if (matrix.length != matrix[0].length)
            throw new IllegalStateException("invalid dimensions");

        if(matrix.length==1)
            return matrix[0][0];

        if (matrix.length == 2)
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];

        double det = 0;
        for (int i = 0; i < matrix[0].length; i++)
            det += Math.pow(-1, i) * matrix[0][i]
                    * determinant(minor(matrix, 0, i));
        return det;
    }

    //нахождение минора матрицы
    public static double[][] minor(double[][] matrix, int row, int column) {
        double[][] minor = new double[matrix.length - 1][matrix.length - 1];

        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; i != row && j < matrix[i].length; j++)
                if (j != column)
                    minor[i < row ? i : i - 1][j < column ? j : j - 1] = matrix[i][j];
        return minor;
    }

    //нахождение обратной матрицы
    public static double[][] invert(double a[][])
    {
        int n = a.length;
        double x[][] = new double[n][n];
        double b[][] = new double[n][n];
        int index[] = new int[n];
        for (int i=0; i<n; ++i)
            b[i][i] = 1;

        //преобразование матрицы в верхний треугольник
        gaussian(a, index);

        //обновление матрицы с сохраненением соотношений
        for (int i=0; i<n-1; ++i)
            for (int j=i+1; j<n; ++j)
                for (int k=0; k<n; ++k)
                    b[index[j]][k] -= a[index[j]][i]*b[index[i]][k];

        //выполнение обратных преобразований
        for (int i=0; i<n; ++i)
        {
            x[n-1][i] = b[index[n-1]][i]/a[index[n-1]][n-1];
            for (int j=n-2; j>=0; --j)
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<n; ++k)
                {
                    x[j][i] -= a[index[j]][k]*x[k][i];
                }
                x[j][i] /= a[index[j]][j];
            }
        }
        return x;
    }

    //метод последовательного исключения переменных
    //здесь index[] хранит порядок поворота

    public static void gaussian(double a[][], int index[])
    {
        int n = index.length;
        double c[] = new double[n];

        for (int i=0; i<n; ++i)
            index[i] = i;

        //нахождение коэффициентов пересчета, по одному в каждой строке
        for (int i=0; i<n; ++i)
        {
            double c1 = 0;
            for (int j=0; j<n; ++j)
            {
                double c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }

        //поиск поворотного элемента в каждой строке
        int k = 0;
        for (int j=0; j<n-1; ++j)
        {
            double pi1 = 0;
            for (int i=j; i<n; ++i)
            {
                double pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1)
                {
                    pi1 = pi0;
                    k = i;
                }
            }

            //чередование строк в соответствии с порядком поворота
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i)
            {
                double pj = a[index[i]][j]/a[index[j]][j];

                //запсиь коэффициентов поворота ниже диагонали
                a[index[i]][j] = pj;

                //изменение остальных элементов соответственно
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }

    //перевод двумерного массива в строку
    public static String toString(double[][] matrix) {
        String result = "";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result += String.format("%11.2f", matrix[i][j]);
            }
            result += "\n";
        }
        return result;
    }
}