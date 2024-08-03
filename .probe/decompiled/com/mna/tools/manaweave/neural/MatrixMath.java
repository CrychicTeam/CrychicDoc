package com.mna.tools.manaweave.neural;

import com.mna.tools.manaweave.neural.error.MatrixError;

public class MatrixMath {

    public static Matrix add(Matrix a, Matrix b) {
        if (a.getRows() == b.getRows() && a.getCols() == b.getCols()) {
            double[][] result = new double[a.getRows()][b.getCols()];
            for (int r = 0; r < a.getRows(); r++) {
                for (int c = 0; c < a.getCols(); c++) {
                    result[r][c] = a.get(r, c) + b.get(r, c);
                }
            }
            return new Matrix(result);
        } else {
            throw new MatrixError("Cannot add matrices that do not have the same number of rows and cols.");
        }
    }

    public static Matrix subtract(Matrix a, Matrix b) {
        if (a.getRows() == b.getRows() && a.getCols() == b.getCols()) {
            double[][] result = new double[a.getRows()][b.getCols()];
            for (int r = 0; r < a.getRows(); r++) {
                for (int c = 0; c < a.getCols(); c++) {
                    result[r][c] = a.get(r, c) - b.get(r, c);
                }
            }
            return new Matrix(result);
        } else {
            throw new MatrixError("Cannot subtract matrices that do not have the same number of rows and cols.");
        }
    }

    public static Matrix divide(Matrix a, double b) {
        double[][] result = new double[a.getRows()][a.getCols()];
        for (int row = 0; row < a.getRows(); row++) {
            for (int col = 0; col < a.getCols(); col++) {
                result[row][col] = a.get(row, col) / b;
            }
        }
        return new Matrix(result);
    }

    public static Matrix multiply(Matrix a, Matrix b) {
        if (a.getCols() != b.getRows()) {
            throw new MatrixError("To use ordinary matrix multiplication the number of columns on the first matrix must mat the number of rows on the second.");
        } else {
            double[][] result = new double[a.getRows()][b.getCols()];
            for (int r = 0; r < a.getRows(); r++) {
                for (int c = 0; c < b.getCols(); c++) {
                    double value = 0.0;
                    for (int i = 0; i < a.getCols(); i++) {
                        value += a.get(r, i) * b.get(i, c);
                    }
                    result[r][c] = value;
                }
            }
            return new Matrix(result);
        }
    }

    public static Matrix multiply(Matrix a, double b) {
        double[][] result = new double[a.getRows()][a.getCols()];
        for (int r = 0; r < a.getRows(); r++) {
            for (int c = 0; c < a.getCols(); c++) {
                result[r][c] = a.get(r, c) * b;
            }
        }
        return new Matrix(result);
    }

    public static Matrix transpose(Matrix input) {
        double[][] inverseMatrix = new double[input.getCols()][input.getRows()];
        for (int r = 0; r < input.getRows(); r++) {
            for (int c = 0; c < input.getCols(); c++) {
                inverseMatrix[c][r] = input.get(r, c);
            }
        }
        return new Matrix(inverseMatrix);
    }

    public static double dotProduct(Matrix a, Matrix b) {
        if (a.isVector() && b.isVector()) {
            double[] aArr = a.toPackedArray();
            double[] bArr = b.toPackedArray();
            if (aArr.length != bArr.length) {
                throw new MatrixError("To take a dot product, both matrices must be the same size/length");
            } else {
                double result = 0.0;
                int length = aArr.length;
                for (int i = 0; i < length; i++) {
                    result += aArr[i] * bArr[i];
                }
                return result;
            }
        } else {
            throw new MatrixError("Both matrices must be vectors to take a dot product.");
        }
    }

    public static double vectorLength(Matrix input) {
        if (!input.isVector()) {
            throw new MatrixError("Can only take the vector length of a vector");
        } else {
            double[] v = input.toPackedArray();
            double rtn = 0.0;
            for (int i = 0; i < v.length; i++) {
                rtn += Math.pow(v[i], 2.0);
            }
            return Math.sqrt(rtn);
        }
    }

    public static Matrix identity(int size) {
        if (size < 1) {
            throw new MatrixError("Identity matrix must be at least size 1");
        } else {
            Matrix result = new Matrix(size, size);
            for (int i = 0; i < size; i++) {
                result.set(i, i, 1.0);
            }
            return result;
        }
    }

    public static void copy(Matrix source, Matrix target) {
        for (int r = 0; r < source.getRows(); r++) {
            for (int c = 0; c < source.getCols(); c++) {
                target.set(r, c, source.get(r, c));
            }
        }
    }
}