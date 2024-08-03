package com.mna.tools.manaweave.neural;

import com.mna.tools.manaweave.neural.error.MatrixError;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.mutable.MutableInt;

public class Matrix implements Cloneable {

    private double[][] matrix;

    public Matrix(boolean[][] sourceMatrix) {
        this.matrix = new double[sourceMatrix.length][sourceMatrix[0].length];
        this.iterateRC((r, c) -> this.set(r, c, sourceMatrix[r][c] ? 1.0 : -1.0));
    }

    public Matrix(double[][] sourceMatrix) {
        this.matrix = new double[sourceMatrix.length][sourceMatrix[0].length];
        this.iterateRC((r, c) -> this.set(r, c, sourceMatrix[r][c]));
    }

    public Matrix(int rows, int cols) {
        this.matrix = new double[rows][cols];
    }

    public static Matrix createColumnMatrix(double[] input) {
        double[][] d = new double[input.length][1];
        for (int row = 0; row < d.length; row++) {
            d[row][0] = input[row];
        }
        return new Matrix(d);
    }

    public static Matrix createRowMatrix(double[] input) {
        double[][] d = new double[1][input.length];
        System.arraycopy(input, 0, d[0], 0, input.length);
        return new Matrix(d);
    }

    private void iterateRC(BiConsumer<Integer, Integer> callable) {
        for (int r = 0; r < this.getRows(); r++) {
            for (int c = 0; c < this.getCols(); c++) {
                callable.accept(r, c);
            }
        }
    }

    public Matrix clone() {
        return new Matrix(this.matrix);
    }

    public boolean equals(Matrix matrix) {
        return this.equals(matrix, 10);
    }

    public boolean equals(Matrix matrix, int precision) {
        if (precision < 0) {
            throw new MatrixError("Precision can't be less than zero.");
        } else {
            double test = Math.pow(10.0, (double) precision);
            if (!Double.isInfinite(test) && !(test > 9.223372E18F)) {
                precision = (int) Math.pow(10.0, (double) precision);
                for (int r = 0; r < this.getRows(); r++) {
                    for (int c = 0; c < this.getCols(); c++) {
                        if ((long) this.get(r, c) * (long) precision != (long) (matrix.get(r, c) * (double) precision)) {
                            return false;
                        }
                    }
                }
                return true;
            } else {
                throw new MatrixError("Precision of " + precision + " decimal places is not supported.");
            }
        }
    }

    public int fromPackedArray(Double[] array, int index) {
        MutableInt idx = new MutableInt(index);
        this.iterateRC((r, c) -> this.matrix[r][c] = array[idx.getAndAdd(1)]);
        return index;
    }

    public double[] toPackedArray() {
        double[] result = new double[this.getRows() * this.getCols()];
        MutableInt index = new MutableInt(0);
        this.iterateRC((r, c) -> result[index.getAndIncrement()] = this.matrix[r][c]);
        return result;
    }

    public void add(int row, int col, double value) {
        this.validate(row, col);
        double newValue = this.get(row, col) + value;
        this.set(row, col, newValue);
    }

    public void clear() {
        this.iterateRC((r, c) -> this.set(r, c, 0.0));
    }

    public double get(int row, int col) {
        this.validate(row, col);
        return this.matrix[row][col];
    }

    public Matrix getCol(int col) {
        if (col > this.getCols()) {
            throw new MatrixError("Can't get column #" + col + " because it does not exist");
        } else {
            double[][] newMatrix = new double[this.getRows()][1];
            for (int row = 0; row < this.getRows(); row++) {
                newMatrix[row][0] = this.matrix[row][col];
            }
            return new Matrix(newMatrix);
        }
    }

    public int getCols() {
        return this.matrix[0].length;
    }

    public Matrix getRow(int row) {
        if (row > this.getRows()) {
            throw new MatrixError("Can't get row #" + row + " because it does not exist");
        } else {
            double[][] newMatrix = new double[1][this.getCols()];
            for (int col = 0; col < this.getCols(); col++) {
                newMatrix[0][col] = this.matrix[row][col];
            }
            return new Matrix(newMatrix);
        }
    }

    public int getRows() {
        return this.matrix.length;
    }

    public boolean isVector() {
        return this.getRows() == 1 ? true : this.getCols() == 1;
    }

    public boolean isZero() {
        for (int r = 0; r < this.getRows(); r++) {
            for (int c = 0; c < this.getCols(); c++) {
                if (this.matrix[r][c] != 0.0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void randomize(double min, double max) {
        this.iterateRC((r, c) -> this.matrix[r][c] = Math.random() * (max - min) + min);
    }

    public void set(int row, int col, double value) {
        this.validate(row, col);
        if (!Double.isInfinite(value) && !Double.isNaN(value)) {
            this.matrix[row][col] = value;
        } else {
            throw new MatrixError("Trying to assign invalid number to matrix: " + value);
        }
    }

    public double sum() {
        MutableDouble result = new MutableDouble(0.0);
        this.iterateRC((r, c) -> result.add(this.matrix[r][c]));
        return result.getValue();
    }

    private void validate(int row, int col) {
        if (row >= this.getRows() || row < 0) {
            throw new MatrixError("The row: " + row + " is out of range: " + this.getRows());
        } else if (col >= this.getCols() || col < 0) {
            throw new MatrixError("The column: " + col + " is out of range: " + this.getCols());
        }
    }
}