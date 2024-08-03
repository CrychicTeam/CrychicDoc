package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

import java.io.IOException;

public class TreeBinarizer {

    private Context[] models;

    public TreeBinarizer() {
        this.initContextModels();
    }

    private void initContextModels() {
        this.models = new Context[255];
        for (int i = 0; i < 255; i++) {
            this.models[i] = new Context(0, 0);
        }
    }

    public void binarize(int symbol, MQEncoder encoder) throws IOException {
        int inverted = 0;
        int nextModel = 0;
        int levelOffset = 0;
        for (int i = 0; i < 8; i++) {
            int bin = symbol >> 7 - i & 1;
            encoder.encode(bin, this.models[nextModel]);
            inverted |= bin << i;
            levelOffset += 1 << i;
            nextModel = levelOffset + inverted;
        }
    }

    public int debinarize(MQDecoder decoder) throws IOException {
        int symbol = 0;
        int inverted = 0;
        int nextModel = 0;
        int levelOffset = 0;
        for (int i = 0; i < 8; i++) {
            int bin = decoder.decode(this.models[nextModel]);
            symbol |= bin << 7 - i;
            inverted |= bin << i;
            levelOffset += 1 << i;
            nextModel = levelOffset + inverted;
        }
        return symbol;
    }
}