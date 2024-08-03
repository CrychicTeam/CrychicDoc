package com.github.alexthe666.citadel.repack.jcodec.audio;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterGraph implements AudioFilter {

    private FilterSocket[] sockets;

    public static FilterGraph.Factory addLevel(AudioFilter first) {
        return new FilterGraph.Factory(first);
    }

    private FilterGraph(FilterSocket[] sockets) {
        this.sockets = sockets;
    }

    @Override
    public void filter(FloatBuffer[] ins, long[] pos, FloatBuffer[] outs) {
        this.sockets[0].setBuffers(ins, pos);
        for (int i = 0; i < this.sockets.length; i++) {
            FloatBuffer[] curOut = i < this.sockets.length - 1 ? this.sockets[i + 1].getBuffers() : outs;
            this.sockets[i].filter(curOut);
            if (i > 0) {
                this.sockets[i].rotate();
            }
            if (i < this.sockets.length - 1) {
                for (FloatBuffer b : curOut) {
                    b.flip();
                }
            }
        }
    }

    @Override
    public int getDelay() {
        return this.sockets[0].getFilters()[0].getDelay();
    }

    @Override
    public int getNInputs() {
        return this.sockets[0].getTotalInputs();
    }

    @Override
    public int getNOutputs() {
        return this.sockets[this.sockets.length - 1].getTotalOutputs();
    }

    public static class Factory {

        private List<FilterSocket> sockets = new ArrayList();

        protected Factory(AudioFilter firstFilter) {
            if (firstFilter.getDelay() != 0) {
                this.sockets.add(FilterSocket.createFilterSocket(new Audio.DummyFilter[] { new Audio.DummyFilter(firstFilter.getNInputs()) }));
                this.addLevel(new AudioFilter[] { firstFilter });
            } else {
                this.sockets.add(FilterSocket.createFilterSocket(new AudioFilter[] { firstFilter }));
            }
        }

        public FilterGraph.Factory addLevel(AudioFilter[] filters) {
            FilterSocket socket = FilterSocket.createFilterSocket(filters);
            socket.allocateBuffers(4096);
            this.sockets.add(socket);
            return this;
        }

        public FilterGraph.Factory addLevels(AudioFilter filter, int n) {
            AudioFilter[] filters = new AudioFilter[n];
            Arrays.fill(filters, filter);
            return this.addLevel(filters);
        }

        public FilterGraph.Factory addLevelSpan(AudioFilter filter) {
            int prevLevelOuts = ((FilterSocket) this.sockets.get(this.sockets.size() - 1)).getTotalOutputs();
            if (prevLevelOuts % filter.getNInputs() != 0) {
                throw new IllegalArgumentException("Can't fill " + prevLevelOuts + " with multiple of " + filter.getNInputs());
            } else {
                return this.addLevels(filter, prevLevelOuts / filter.getNInputs());
            }
        }

        public FilterGraph create() {
            return new FilterGraph((FilterSocket[]) this.sockets.toArray(new FilterSocket[0]));
        }
    }
}