package com.github.alexthe666.citadel.repack.jcodec.codecs.png;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import java.nio.ByteBuffer;

class IHDR {

    static final int PNG_COLOR_MASK_ALPHA = 4;

    static final int PNG_COLOR_MASK_COLOR = 2;

    static final int PNG_COLOR_MASK_PALETTE = 1;

    int width;

    int height;

    byte bitDepth;

    byte colorType;

    private byte compressionType;

    private byte filterType;

    byte interlaceType;

    void write(ByteBuffer data) {
        data.putInt(this.width);
        data.putInt(this.height);
        data.put(this.bitDepth);
        data.put(this.colorType);
        data.put(this.compressionType);
        data.put(this.filterType);
        data.put(this.interlaceType);
    }

    void parse(ByteBuffer data) {
        this.width = data.getInt();
        this.height = data.getInt();
        this.bitDepth = data.get();
        this.colorType = data.get();
        this.compressionType = data.get();
        this.filterType = data.get();
        this.interlaceType = data.get();
        data.getInt();
    }

    int rowSize() {
        return this.width * this.getBitsPerPixel() + 7 >> 3;
    }

    private int getNBChannels() {
        int channels = 1;
        if ((this.colorType & 3) == 2) {
            channels = 3;
        }
        if ((this.colorType & 4) != 0) {
            channels++;
        }
        return channels;
    }

    int getBitsPerPixel() {
        return this.bitDepth * this.getNBChannels();
    }

    ColorSpace colorSpace() {
        return ColorSpace.RGB;
    }
}