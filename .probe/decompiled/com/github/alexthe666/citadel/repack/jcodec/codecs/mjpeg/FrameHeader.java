package com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg;

import java.nio.ByteBuffer;

public class FrameHeader {

    int headerLength;

    int bitsPerSample;

    int height;

    int width;

    int nComp;

    FrameHeader.Component[] components;

    public int getHmax() {
        int max = 0;
        for (int i = 0; i < this.components.length; i++) {
            FrameHeader.Component c = this.components[i];
            max = Math.max(max, c.subH);
        }
        return max;
    }

    public int getVmax() {
        int max = 0;
        for (int i = 0; i < this.components.length; i++) {
            FrameHeader.Component c = this.components[i];
            max = Math.max(max, c.subV);
        }
        return max;
    }

    public static FrameHeader read(ByteBuffer is) {
        FrameHeader frame = new FrameHeader();
        frame.headerLength = is.getShort() & '\uffff';
        frame.bitsPerSample = is.get() & 255;
        frame.height = is.getShort() & '\uffff';
        frame.width = is.getShort() & '\uffff';
        frame.nComp = is.get() & 255;
        frame.components = new FrameHeader.Component[frame.nComp];
        for (int i = 0; i < frame.components.length; i++) {
            FrameHeader.Component c = frame.components[i] = new FrameHeader.Component();
            c.index = is.get() & 255;
            int hv = is.get() & 255;
            c.subH = (hv & 240) >>> 4;
            c.subV = hv & 15;
            c.quantTable = is.get() & 255;
        }
        return frame;
    }

    public static class Component {

        int index;

        int subH;

        int subV;

        int quantTable;
    }
}