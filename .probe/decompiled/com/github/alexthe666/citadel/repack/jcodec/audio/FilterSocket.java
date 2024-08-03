package com.github.alexthe666.citadel.repack.jcodec.audio;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.FloatBuffer;

public class FilterSocket {

    private FloatBuffer[] buffers;

    private long[] positions;

    private int[] delays;

    private AudioFilter[] filters;

    private int totalInputs;

    private int totalOutputs;

    public static FilterSocket createFilterSocket(AudioFilter[] filters) {
        FilterSocket fs = new FilterSocket();
        fs.totalInputs = 0;
        fs.totalOutputs = 0;
        for (int i = 0; i < filters.length; i++) {
            fs.totalInputs = fs.totalInputs + filters[i].getNInputs();
            fs.totalOutputs = fs.totalOutputs + filters[i].getNOutputs();
        }
        fs.buffers = new FloatBuffer[fs.totalInputs];
        fs.positions = new long[fs.totalInputs];
        fs.delays = new int[fs.totalInputs];
        int i = 0;
        for (int b = 0; i < filters.length; i++) {
            for (int j = 0; j < filters[i].getNInputs(); b++) {
                fs.delays[b] = filters[i].getDelay();
                j++;
            }
        }
        fs.filters = filters;
        return fs;
    }

    public void allocateBuffers(int bufferSize) {
        for (int i = 0; i < this.totalInputs; i++) {
            this.buffers[i] = FloatBuffer.allocate(bufferSize + this.delays[i] * 2);
            this.buffers[i].position(this.delays[i]);
        }
    }

    public static FilterSocket createFilterSocket2(AudioFilter filter, FloatBuffer[] buffers, long[] positions) {
        FilterSocket fs = new FilterSocket();
        fs.filters = new AudioFilter[] { filter };
        fs.buffers = buffers;
        fs.positions = positions;
        fs.delays = new int[] { filter.getDelay() };
        fs.totalInputs = filter.getNInputs();
        fs.totalOutputs = filter.getNOutputs();
        return fs;
    }

    private FilterSocket() {
    }

    public void filter(FloatBuffer[] outputs) {
        if (outputs.length != this.totalOutputs) {
            throw new IllegalArgumentException("Can not output to provided filter socket inputs != outputs (" + outputs.length + "!=" + this.totalOutputs + ")");
        } else {
            int i = 0;
            int ii = 0;
            for (int oi = 0; i < this.filters.length; i++) {
                this.filters[i].filter(Platform.copyOfRangeO(this.buffers, ii, this.filters[i].getNInputs() + ii), Platform.copyOfRangeL(this.positions, ii, this.filters[i].getNInputs() + ii), Platform.copyOfRangeO(outputs, oi, this.filters[i].getNOutputs() + oi));
                ii += this.filters[i].getNInputs();
                oi += this.filters[i].getNOutputs();
            }
        }
    }

    FloatBuffer[] getBuffers() {
        return this.buffers;
    }

    public void rotate() {
        for (int i = 0; i < this.buffers.length; i++) {
            this.positions[i] = this.positions[i] + (long) this.buffers[i].position();
            Audio.rotate(this.buffers[i]);
        }
    }

    public void setBuffers(FloatBuffer[] ins, long[] pos) {
        if (ins.length != this.totalInputs) {
            throw new IllegalArgumentException("Number of input buffers provided is less then the number of filter inputs.");
        } else if (pos.length != this.totalInputs) {
            throw new IllegalArgumentException("Number of input buffer positions provided is less then the number of filter inputs.");
        } else {
            this.buffers = ins;
            this.positions = pos;
        }
    }

    public int getTotalInputs() {
        return this.totalInputs;
    }

    public int getTotalOutputs() {
        return this.totalOutputs;
    }

    AudioFilter[] getFilters() {
        return this.filters;
    }

    public long[] getPositions() {
        return this.positions;
    }
}