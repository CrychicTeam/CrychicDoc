package com.mna.tools.manaweave.neural;

public class NormalizeInput {

    private final NormalizationType type;

    protected double normFac;

    protected double synth;

    protected Matrix inputMatrix;

    public NormalizeInput(double[] input, NormalizationType type) {
        this.type = type;
        this.calculateFactors(input);
        this.inputMatrix = this.createInputMatrix(input, this.synth);
    }

    protected Matrix createInputMatrix(double[] pattern, double extra) {
        Matrix result = new Matrix(1, pattern.length + 1);
        for (int i = 0; i < pattern.length; i++) {
            result.set(0, i, pattern[i]);
        }
        result.set(0, pattern.length, extra);
        return result;
    }

    public Matrix getInputMatrix() {
        return this.inputMatrix;
    }

    public double getNormFac() {
        return this.normFac;
    }

    public double getSynth() {
        return this.synth;
    }

    protected void calculateFactors(double[] input) {
        Matrix inputMatrix = Matrix.createColumnMatrix(input);
        double len = MatrixMath.vectorLength(inputMatrix);
        len = Math.max(len, 1.0E-30);
        int numInputs = input.length;
        if (this.type == NormalizationType.MULTIPLICATIVE) {
            this.normFac = 1.0 / len;
            this.synth = 0.0;
        } else {
            this.normFac = 1.0 / Math.sqrt((double) numInputs);
            double d = (double) numInputs - Math.pow(len, 2.0);
            if (d > 0.0) {
                this.synth = Math.sqrt(d) * this.normFac;
            } else {
                this.synth = 0.0;
            }
        }
    }
}