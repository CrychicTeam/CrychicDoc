package com.github.alexthe666.citadel.repack.jcodec.containers.mps.index;

import com.github.alexthe666.citadel.repack.jcodec.common.RunLength;
import java.nio.ByteBuffer;

public class MPSIndex {

    protected long[] pesTokens;

    protected RunLength.Integer pesStreamIds;

    protected MPSIndex.MPSStreamIndex[] streams;

    public MPSIndex(long[] pesTokens, RunLength.Integer pesStreamIds, MPSIndex.MPSStreamIndex[] streams) {
        this.pesTokens = pesTokens;
        this.pesStreamIds = pesStreamIds;
        this.streams = streams;
    }

    public long[] getPesTokens() {
        return this.pesTokens;
    }

    public RunLength.Integer getPesStreamIds() {
        return this.pesStreamIds;
    }

    public MPSIndex.MPSStreamIndex[] getStreams() {
        return this.streams;
    }

    public static MPSIndex parseIndex(ByteBuffer index) {
        int pesCnt = index.getInt();
        long[] pesTokens = new long[pesCnt];
        for (int i = 0; i < pesCnt; i++) {
            pesTokens[i] = index.getLong();
        }
        RunLength.Integer pesStreamId = RunLength.Integer.parse(index);
        int nStreams = index.getInt();
        MPSIndex.MPSStreamIndex[] streams = new MPSIndex.MPSStreamIndex[nStreams];
        for (int i = 0; i < nStreams; i++) {
            streams[i] = MPSIndex.MPSStreamIndex.parseIndex(index);
        }
        return new MPSIndex(pesTokens, pesStreamId, streams);
    }

    public void serializeTo(ByteBuffer index) {
        index.putInt(this.pesTokens.length);
        for (int i = 0; i < this.pesTokens.length; i++) {
            index.putLong(this.pesTokens[i]);
        }
        this.pesStreamIds.serialize(index);
        index.putInt(this.streams.length);
        for (MPSIndex.MPSStreamIndex mpsStreamIndex : this.streams) {
            mpsStreamIndex.serialize(index);
        }
    }

    public int estimateSize() {
        int size = (this.pesTokens.length << 3) + this.pesStreamIds.estimateSize();
        for (MPSIndex.MPSStreamIndex mpsStreamIndex : this.streams) {
            size += mpsStreamIndex.estimateSize();
        }
        return size + 64;
    }

    public static long makePESToken(long leading, long pesLen, long payloadLen) {
        return leading << 48 | pesLen << 24 | payloadLen;
    }

    public static int leadingSize(long token) {
        return (int) (token >> 48) & 65535;
    }

    public static int pesLen(long token) {
        return (int) (token >> 24) & 16777215;
    }

    public static int payLoadSize(long token) {
        return (int) token & 16777215;
    }

    public static class MPSStreamIndex {

        protected int streamId;

        protected int[] fsizes;

        protected int[] fpts;

        protected int[] fdur;

        protected int[] sync;

        public MPSStreamIndex(int streamId, int[] fsizes, int[] fpts, int[] fdur, int[] sync) {
            this.streamId = streamId;
            this.fsizes = fsizes;
            this.fpts = fpts;
            this.fdur = fdur;
            this.sync = sync;
        }

        public int getStreamId() {
            return this.streamId;
        }

        public int[] getFsizes() {
            return this.fsizes;
        }

        public int[] getFpts() {
            return this.fpts;
        }

        public int[] getFdur() {
            return this.fdur;
        }

        public int[] getSync() {
            return this.sync;
        }

        public static MPSIndex.MPSStreamIndex parseIndex(ByteBuffer index) {
            int streamId = index.get() & 255;
            int fCnt = index.getInt();
            int[] fsizes = new int[fCnt];
            for (int i = 0; i < fCnt; i++) {
                fsizes[i] = index.getInt();
            }
            int fptsCnt = index.getInt();
            int[] fpts = new int[fptsCnt];
            for (int i = 0; i < fptsCnt; i++) {
                fpts[i] = index.getInt();
            }
            int fdurCnt = index.getInt();
            int[] fdur = new int[fdurCnt];
            for (int i = 0; i < fdurCnt; i++) {
                fdur[i] = index.getInt();
            }
            int syncCount = index.getInt();
            int[] sync = new int[syncCount];
            for (int i = 0; i < syncCount; i++) {
                sync[i] = index.getInt();
            }
            return new MPSIndex.MPSStreamIndex(streamId, fsizes, fpts, fdur, sync);
        }

        public void serialize(ByteBuffer index) {
            index.put((byte) this.streamId);
            index.putInt(this.fsizes.length);
            for (int i = 0; i < this.fsizes.length; i++) {
                index.putInt(this.fsizes[i]);
            }
            index.putInt(this.fpts.length);
            for (int i = 0; i < this.fpts.length; i++) {
                index.putInt(this.fpts[i]);
            }
            index.putInt(this.fdur.length);
            for (int i = 0; i < this.fdur.length; i++) {
                index.putInt(this.fdur[i]);
            }
            index.putInt(this.sync.length);
            for (int i = 0; i < this.sync.length; i++) {
                index.putInt(this.sync[i]);
            }
        }

        public int estimateSize() {
            return (this.fpts.length << 2) + (this.fdur.length << 2) + (this.sync.length << 2) + (this.fsizes.length << 2) + 64;
        }
    }
}