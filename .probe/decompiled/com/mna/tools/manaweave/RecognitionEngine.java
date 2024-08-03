package com.mna.tools.manaweave;

import com.mna.ManaAndArtifice;
import com.mna.tools.manaweave.neural.LearningMethod;
import com.mna.tools.manaweave.neural.NormalizationType;
import com.mna.tools.manaweave.neural.SelfOrganizingMap;
import com.mna.tools.manaweave.neural.TrainSelfOrganizingMap;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableInt;

public class RecognitionEngine {

    static final double MAX_ERROR = 0.01;

    public static final RecognitionEngine instance = new RecognitionEngine();

    SelfOrganizingMap net;

    final HashMap<ResourceLocation, ArrayList<SampleData>> trainingData;

    boolean halt = false;

    public RecognitionEngine() {
        this.trainingData = new HashMap();
    }

    public void clearTrainingData() {
        this.trainingData.clear();
    }

    public void registerTrainingDataSample(ResourceLocation pattern, byte[][] data) {
        ArrayList<SampleData> samples = (ArrayList<SampleData>) this.trainingData.getOrDefault(pattern, new ArrayList());
        SampleData ds = new SampleData(pattern, 11, 11);
        ds.setGrid(data);
        samples.add(ds);
        this.trainingData.put(pattern, samples);
        ManaAndArtifice.LOGGER.info("Received new training data for " + pattern.toString());
    }

    private void train() {
        int inputNeurons = 121;
        int outputNeurons = this.trainingData.size();
        double[][] set = new double[this.trainingData.size()][121];
        MutableInt t = new MutableInt(0);
        this.trainingData.forEach((rLoc, samples) -> {
            int idx = 0;
            if (samples.size() >= 1) {
                SampleData ds = (SampleData) samples.get(0);
                for (int y = 0; y < ds.getHeight(); y++) {
                    for (int x = 0; x < ds.getWidth(); x++) {
                        set[t.getValue()][idx++] = ds.getData(x, y) ? 0.5 : -0.5;
                    }
                }
                t.increment();
            }
        });
        this.net = new SelfOrganizingMap(121, outputNeurons, NormalizationType.MULTIPLICATIVE);
        TrainSelfOrganizingMap trainer = new TrainSelfOrganizingMap(this.net, set, LearningMethod.SUBTRACTIVE, 0.5);
        int tries = 1;
        do {
            trainer.iteration();
            this.logProgress(tries++, trainer.getTotalError(), trainer.getBestError());
        } while (trainer.getTotalError() > 0.01 && !this.halt);
        this.halt = true;
        this.logProgress(tries, trainer.getTotalError(), trainer.getBestError());
        ManaAndArtifice.LOGGER.info("Neural network training completed successfully.");
    }

    public ResourceLocation recognize(boolean[][] data) {
        if (!this.halt) {
            int tries = 0;
            do {
                try {
                    this.train();
                } catch (Throwable var8) {
                } finally {
                    tries++;
                }
            } while (!this.halt && tries < 10);
        }
        double[] map = new double[data.length * data[0].length];
        int idx = 0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                map[idx++] = data[i][j] ? 0.5 : -0.5;
            }
        }
        int winningIndex = this.net.winner(map);
        ResourceLocation[] mapped = this.mapToPatternsToNeurons();
        return mapped[winningIndex];
    }

    private ResourceLocation[] mapToPatternsToNeurons() {
        ResourceLocation[] map = new ResourceLocation[this.trainingData.size()];
        for (int i = 0; i < map.length; i++) {
            map[i] = null;
        }
        this.trainingData.forEach((rLoc, train) -> {
            int idx = 0;
            double[] input = new double[121];
            for (int y = 0; y < ((SampleData) train.get(0)).getHeight(); y++) {
                for (int x = 0; x < ((SampleData) train.get(0)).getWidth(); x++) {
                    input[idx++] = ((SampleData) train.get(0)).getData(x, y) ? 0.5 : -0.5;
                }
            }
            int best = this.net.winner(input);
            map[best] = rLoc;
        });
        return map;
    }

    private void logProgress(int tries, double totalError, double bestError) {
        ManaAndArtifice.LOGGER.info("On try " + tries + " total error: " + totalError + ", best error: " + bestError);
    }
}