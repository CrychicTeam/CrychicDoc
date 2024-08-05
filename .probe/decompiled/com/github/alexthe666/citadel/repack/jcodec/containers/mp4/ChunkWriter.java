package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.AliasBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataRefBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.IOException;

public class ChunkWriter {

    private long[] offsets;

    private SampleEntry[] entries;

    private SeekableByteChannel[] inputs;

    private int curChunk;

    private SeekableByteChannel out;

    byte[] buf = new byte[8092];

    private TrakBox trak;

    public ChunkWriter(TrakBox trak, SeekableByteChannel[] inputs, SeekableByteChannel out) {
        this.entries = trak.getSampleEntries();
        ChunkOffsetsBox stco = trak.getStco();
        ChunkOffsets64Box co64 = trak.getCo64();
        int size;
        if (stco != null) {
            size = stco.getChunkOffsets().length;
        } else {
            size = co64.getChunkOffsets().length;
        }
        this.inputs = inputs;
        this.offsets = new long[size];
        this.out = out;
        this.trak = trak;
    }

    public void apply() {
        NodeBox stbl = NodeBox.findFirstPath(this.trak, NodeBox.class, Box.path("mdia.minf.stbl"));
        stbl.removeChildren(new String[] { "stco", "co64" });
        stbl.add(ChunkOffsets64Box.createChunkOffsets64Box(this.offsets));
        this.cleanDrefs(this.trak);
    }

    private void cleanDrefs(TrakBox trak) {
        MediaInfoBox minf = trak.getMdia().getMinf();
        DataInfoBox dinf = trak.getMdia().getMinf().getDinf();
        if (dinf == null) {
            dinf = DataInfoBox.createDataInfoBox();
            minf.add(dinf);
        }
        DataRefBox dref = dinf.getDref();
        if (dref == null) {
            dref = DataRefBox.createDataRefBox();
            dinf.add(dref);
        }
        dref.getBoxes().clear();
        dref.add(AliasBox.createSelfRef());
        SampleEntry[] sampleEntries = trak.getSampleEntries();
        for (int i = 0; i < sampleEntries.length; i++) {
            SampleEntry entry = sampleEntries[i];
            entry.setDrefInd((short) 1);
        }
    }

    private SeekableByteChannel getInput(Chunk chunk) {
        SampleEntry se = this.entries[chunk.getEntry() - 1];
        return this.inputs[se.getDrefInd() - 1];
    }

    public void write(Chunk chunk) throws IOException {
        SeekableByteChannel input = this.getInput(chunk);
        input.setPosition(chunk.getOffset());
        long pos = this.out.position();
        this.out.write(NIOUtils.fetchFromChannel(input, (int) chunk.getSize()));
        this.offsets[this.curChunk++] = pos;
    }
}