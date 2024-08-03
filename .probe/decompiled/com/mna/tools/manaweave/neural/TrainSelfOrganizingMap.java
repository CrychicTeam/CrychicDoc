package com.mna.tools.manaweave.neural;

public class TrainSelfOrganizingMap {

    private SelfOrganizingMap som;

    private LearningMethod learnMethod;

    private double learnRate;

    private double reduction = 0.99;

    private double globalError;

    private double totalError;

    private double bestError;

    private final int inputNeuronCount;

    private final int outputNeuronCount;

    private final SelfOrganizingMap bestNet;

    private double[][] train;

    private int[] won;

    private Matrix work;

    private Matrix correc;

    public TrainSelfOrganizingMap(SelfOrganizingMap som, double[][] trainingData, LearningMethod learnMethod, double learnRate) {
        this.som = som;
        this.train = trainingData;
        this.totalError = 1.0;
        this.learnMethod = learnMethod;
        this.learnRate = learnRate;
        this.outputNeuronCount = som.countOutputNeurons();
        this.inputNeuronCount = som.countInputNeurons();
        this.totalError = 1.0;
        for (int tset = 0; tset < this.train.length; tset++) {
            Matrix dptr = Matrix.createColumnMatrix(this.train[tset]);
            if (MatrixMath.vectorLength(dptr) < 1.0E-30) {
                throw new RuntimeException("Multiplicative normalization has null training case");
            }
        }
        this.bestNet = new SelfOrganizingMap(this.inputNeuronCount, this.outputNeuronCount, this.som.getNormalizationType());
        this.won = new int[this.outputNeuronCount];
        this.correc = new Matrix(this.outputNeuronCount, this.inputNeuronCount + 1);
        if (this.learnMethod == LearningMethod.ADDITIVE) {
            this.work = new Matrix(1, this.inputNeuronCount + 1);
        } else {
            this.work = null;
        }
        this.initialize();
        this.bestError = Double.MAX_VALUE;
    }

    public void initialize() {
        this.som.getOutputWeights().randomize(-1.0, 1.0);
        for (int i = 0; i < this.outputNeuronCount; i++) {
            this.normalizeWeight(this.som.getOutputWeights(), i);
        }
    }

    public double getBestError() {
        return this.bestError;
    }

    public double getTotalError() {
        return this.totalError;
    }

    protected void adjustWeights() {
        for (int i = 0; i < this.outputNeuronCount; i++) {
            if (this.won[i] != 0) {
                double f = 1.0 / (double) this.won[i];
                if (this.learnMethod == LearningMethod.SUBTRACTIVE) {
                    f *= this.learnRate;
                }
                for (int j = 0; j <= this.inputNeuronCount; j++) {
                    double corr = f * this.correc.get(i, j);
                    this.som.getOutputWeights().add(i, j, corr);
                }
            }
        }
    }

    private void copyWeights(SelfOrganizingMap source, SelfOrganizingMap target) {
        MatrixMath.copy(source.getOutputWeights(), target.getOutputWeights());
    }

    void evaluateErrors() throws RuntimeException {
        this.correc.clear();
        for (int i = 0; i < this.won.length; i++) {
            this.won[i] = 0;
        }
        this.globalError = 0.0;
        for (int tset = 0; tset < this.train.length; tset++) {
            NormalizeInput input = new NormalizeInput(this.train[tset], this.som.getNormalizationType());
            int best = this.som.winner(input);
            this.won[best]++;
            Matrix wptr = this.som.getOutputWeights().getRow(best);
            double length = 0.0;
            for (int i = 0; i < this.inputNeuronCount; i++) {
                double diff = this.train[tset][i] * input.getNormFac() - wptr.get(0, i);
                length += diff * diff;
                if (this.learnMethod == LearningMethod.SUBTRACTIVE) {
                    this.correc.add(best, i, diff);
                } else {
                    this.work.set(0, i, this.learnRate * this.train[tset][i] * input.getNormFac() + wptr.get(0, i));
                }
            }
            double diff = input.getSynth() - wptr.get(0, this.inputNeuronCount);
            length += diff * diff;
            if (this.learnMethod == LearningMethod.SUBTRACTIVE) {
                this.correc.add(best, this.inputNeuronCount, diff);
            } else {
                this.work.set(0, this.inputNeuronCount, this.learnRate * input.getSynth() + wptr.get(0, this.inputNeuronCount));
            }
            if (length > this.globalError) {
                this.globalError = length;
            }
            if (this.learnMethod == LearningMethod.ADDITIVE) {
                this.normalizeWeight(this.work, 0);
                for (int ix = 0; ix <= this.inputNeuronCount; ix++) {
                    this.correc.add(best, ix, this.work.get(0, ix) - wptr.get(0, ix));
                }
            }
        }
        this.globalError = Math.sqrt(this.globalError);
    }

    void forceWin() throws RuntimeException {
        int which = 0;
        Matrix outputWeights = this.som.getOutputWeights();
        double dist = Double.MAX_VALUE;
        for (int tset = 0; tset < this.train.length; tset++) {
            int best = this.som.winner(this.train[tset]);
            double[] output = this.som.getOutput();
            if (output[best] < dist) {
                dist = output[best];
                which = tset;
            }
        }
        NormalizeInput input = new NormalizeInput(this.train[which], this.som.getNormalizationType());
        int best = this.som.winner(input);
        double[] output = this.som.getOutput();
        dist = Double.MIN_VALUE;
        int i = this.outputNeuronCount;
        while (i-- > 0) {
            if (this.won[i] == 0 && output[i] > dist) {
                dist = output[i];
                which = i;
            }
        }
        for (int j = 0; j < input.getInputMatrix().getCols(); j++) {
            outputWeights.set(which, j, input.getInputMatrix().get(0, j));
        }
        this.normalizeWeight(outputWeights, which);
    }

    public void iteration() throws RuntimeException {
        this.evaluateErrors();
        this.totalError = this.globalError;
        if (this.totalError < this.bestError) {
            this.bestError = this.totalError;
            this.copyWeights(this.som, this.bestNet);
        }
        int winners = 0;
        for (int i = 0; i < this.won.length; i++) {
            if (this.won[i] != 0) {
                winners++;
            }
        }
        if (winners < this.outputNeuronCount && winners < this.train.length) {
            this.forceWin();
        } else {
            this.adjustWeights();
            if (this.learnRate > 0.01) {
                this.learnRate = this.learnRate * this.reduction;
            }
        }
    }

    protected void normalizeWeight(Matrix matrix, int row) {
        double len = MatrixMath.vectorLength(matrix.getRow(row));
        len = Math.max(len, 1.0E-30);
        len = 1.0 / len;
        for (int i = 0; i < this.inputNeuronCount; i++) {
            matrix.set(row, i, matrix.get(row, i) * len);
        }
        matrix.set(row, this.inputNeuronCount, 0.0);
    }
}