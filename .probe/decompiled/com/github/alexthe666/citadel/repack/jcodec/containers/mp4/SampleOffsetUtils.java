package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

public class SampleOffsetUtils {

    public static ByteBuffer getSampleData(int sample, File file) throws IOException {
        MovieBox moov = MP4Util.parseMovie(file);
        MediaInfoBox minf = ((TrakBox) moov.getAudioTracks().get(0)).getMdia().getMinf();
        ChunkOffsetsBox stco = NodeBox.findFirstPath(minf, ChunkOffsetsBox.class, Box.path("stbl.stco"));
        SampleToChunkBox stsc = NodeBox.findFirstPath(minf, SampleToChunkBox.class, Box.path("stbl.stsc"));
        SampleSizesBox stsz = NodeBox.findFirstPath(minf, SampleSizesBox.class, Box.path("stbl.stsz"));
        long sampleOffset = getSampleOffset(sample, stsc, stco, stsz);
        MappedByteBuffer map = NIOUtils.mapFile(file);
        map.position((int) sampleOffset);
        map.limit(map.position() + stsz.getSizes()[sample]);
        return map;
    }

    public static long getSampleOffset(int sample, SampleToChunkBox stsc, ChunkOffsetsBox stco, SampleSizesBox stsz) {
        int chunkBySample = getChunkBySample(sample, stco, stsc);
        int firstSampleAtChunk = getFirstSampleAtChunk(chunkBySample, stsc, stco);
        long offset = stco.getChunkOffsets()[chunkBySample - 1];
        int[] sizes = stsz.getSizes();
        for (int i = firstSampleAtChunk; i < sample; i++) {
            offset += (long) sizes[i];
        }
        return offset;
    }

    public static int getFirstSampleAtChunk(int chunk, SampleToChunkBox stsc, ChunkOffsetsBox stco) {
        int chunks = stco.getChunkOffsets().length;
        int samples = 0;
        for (int i = 1; i <= chunks && i != chunk; i++) {
            int samplesInChunk = getSamplesInChunk(i, stsc);
            samples += samplesInChunk;
        }
        return samples;
    }

    public static int getChunkBySample(int sampleOfInterest, ChunkOffsetsBox stco, SampleToChunkBox stsc) {
        int chunks = stco.getChunkOffsets().length;
        int startSample = 0;
        int endSample = 0;
        for (int i = 1; i <= chunks; i++) {
            int samplesInChunk = getSamplesInChunk(i, stsc);
            endSample = startSample + samplesInChunk;
            if (sampleOfInterest >= startSample && sampleOfInterest < endSample) {
                return i;
            }
            startSample = endSample;
        }
        return -1;
    }

    public static int getSamplesInChunk(int chunk, SampleToChunkBox stsc) {
        SampleToChunkBox.SampleToChunkEntry[] sampleToChunk = stsc.getSampleToChunk();
        int sampleCount = 0;
        for (SampleToChunkBox.SampleToChunkEntry sampleToChunkEntry : sampleToChunk) {
            if (sampleToChunkEntry.getFirst() > (long) chunk) {
                return sampleCount;
            }
            sampleCount = sampleToChunkEntry.getCount();
        }
        return sampleCount;
    }
}