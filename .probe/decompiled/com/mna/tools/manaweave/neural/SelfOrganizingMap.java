package com.mna.tools.manaweave.neural;

import java.util.Arrays;

public class SelfOrganizingMap {

    public static final double VERYSMALL = 1.0E-30;

    Matrix outputWeights;

    protected double[] output;

    protected int inputNeuronCount;

    protected int outputNeuronCount;

    private final NormalizationType normalizationType;

    public SelfOrganizingMap(int numInputNeurons, int numOutputNeurons, NormalizationType type) {
        this.normalizationType = type;
        this.outputNeuronCount = numOutputNeurons;
        this.inputNeuronCount = numInputNeurons;
        this.output = new double[this.outputNeuronCount];
        this.outputWeights = new Matrix(this.outputNeuronCount, this.inputNeuronCount + 1);
    }

    public NormalizationType getNormalizationType() {
        return this.normalizationType;
    }

    public int countInputNeurons() {
        return this.inputNeuronCount;
    }

    public int countOutputNeurons() {
        return this.outputNeuronCount;
    }

    public double[] getOutput() {
        return this.output;
    }

    public Matrix getOutputWeights() {
        return this.outputWeights;
    }

    public String serializeOutputWeights() {
        return Arrays.toString(this.outputWeights.toPackedArray());
    }

    public void setOutputWeights(Matrix outputWeights) {
        this.outputWeights = outputWeights;
    }

    public int winner(double[] input) {
        NormalizeInput normalizedInput = new NormalizeInput(input, this.normalizationType);
        return this.winner(normalizedInput);
    }

    public int winner(NormalizeInput input) {
        int win = 0;
        double biggest = Double.MIN_VALUE;
        for (int i = 0; i < this.outputNeuronCount; i++) {
            Matrix optr = this.outputWeights.getRow(i);
            this.output[i] = MatrixMath.dotProduct(input.getInputMatrix(), optr) * input.getNormFac();
            this.output[i] = (this.output[i] + 1.0) / 2.0;
            if (this.output[i] > biggest) {
                biggest = this.output[i];
                win = i;
            }
            if (this.output[i] < 0.0) {
                this.output[i] = 0.0;
            }
            if (this.output[i] > 1.0) {
                this.output[i] = 1.0;
            }
        }
        return win;
    }
}