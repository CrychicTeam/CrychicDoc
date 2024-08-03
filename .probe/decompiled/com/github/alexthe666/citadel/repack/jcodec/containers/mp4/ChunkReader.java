package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AudioSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimeToSampleBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;

public class ChunkReader {

    private int curChunk;

    private int sampleNo;

    private int s2cIndex;

    private int ttsInd = 0;

    private int ttsSubInd = 0;

    private long chunkTv = 0L;

    private long[] chunkOffsets;

    private SampleToChunkBox.SampleToChunkEntry[] sampleToChunk;

    private SampleSizesBox stsz;

    private TimeToSampleBox.TimeToSampleEntry[] tts;

    private SampleDescriptionBox stsd;

    public ChunkReader(TrakBox trakBox) {
        TimeToSampleBox stts = trakBox.getStts();
        this.tts = stts.getEntries();
        ChunkOffsetsBox stco = trakBox.getStco();
        ChunkOffsets64Box co64 = trakBox.getCo64();
        this.stsz = trakBox.getStsz();
        SampleToChunkBox stsc = trakBox.getStsc();
        if (stco != null) {
            this.chunkOffsets = stco.getChunkOffsets();
        } else {
            this.chunkOffsets = co64.getChunkOffsets();
        }
        this.sampleToChunk = stsc.getSampleToChunk();
        this.stsd = trakBox.getStsd();
    }

    public boolean hasNext() {
        return this.curChunk < this.chunkOffsets.length;
    }

    public Chunk next() {
        if (this.curChunk >= this.chunkOffsets.length) {
            return null;
        } else {
            if (this.s2cIndex + 1 < this.sampleToChunk.length && (long) (this.curChunk + 1) == this.sampleToChunk[this.s2cIndex + 1].getFirst()) {
                this.s2cIndex++;
            }
            int sampleCount = this.sampleToChunk[this.s2cIndex].getCount();
            int[] samplesDur = null;
            int sampleDur = 0;
            if (this.ttsSubInd + sampleCount <= this.tts[this.ttsInd].getSampleCount()) {
                sampleDur = this.tts[this.ttsInd].getSampleDuration();
                this.ttsSubInd += sampleCount;
            } else {
                samplesDur = new int[sampleCount];
                for (int i = 0; i < sampleCount; i++) {
                    if (this.ttsSubInd >= this.tts[this.ttsInd].getSampleCount() && this.ttsInd < this.tts.length - 1) {
                        this.ttsSubInd = 0;
                        this.ttsInd++;
                    }
                    samplesDur[i] = this.tts[this.ttsInd].getSampleDuration();
                    this.ttsSubInd++;
                }
            }
            int size = 0;
            int[] sizes = null;
            if (this.stsz.getDefaultSize() > 0) {
                size = this.getFrameSize();
            } else {
                sizes = Platform.copyOfRangeI(this.stsz.getSizes(), this.sampleNo, this.sampleNo + sampleCount);
            }
            int dref = this.sampleToChunk[this.s2cIndex].getEntry();
            Chunk chunk = new Chunk(this.chunkOffsets[this.curChunk], this.chunkTv, sampleCount, size, sizes, sampleDur, samplesDur, dref);
            this.chunkTv = this.chunkTv + (long) chunk.getDuration();
            this.sampleNo += sampleCount;
            this.curChunk++;
            return chunk;
        }
    }

    private int getFrameSize() {
        int size = this.stsz.getDefaultSize();
        Box box = (Box) this.stsd.getBoxes().get(this.sampleToChunk[this.s2cIndex].getEntry() - 1);
        return box instanceof AudioSampleEntry ? ((AudioSampleEntry) box).calcFrameSize() : size;
    }

    public int size() {
        return this.chunkOffsets.length;
    }
}