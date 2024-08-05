package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class PictureTemporalScalableExtension implements MPEGHeader {

    public int reference_select_code;

    public int forward_temporal_reference;

    public int backward_temporal_reference;

    public static final int Picture_Temporal_Scalable_Extension = 16;

    public static PictureTemporalScalableExtension read(BitReader _in) {
        PictureTemporalScalableExtension ptse = new PictureTemporalScalableExtension();
        ptse.reference_select_code = _in.readNBit(2);
        ptse.forward_temporal_reference = _in.readNBit(10);
        _in.read1Bit();
        ptse.backward_temporal_reference = _in.readNBit(10);
        return ptse;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(16, 4);
        bw.writeNBit(this.reference_select_code, 2);
        bw.writeNBit(this.forward_temporal_reference, 10);
        bw.write1Bit(1);
        bw.writeNBit(this.backward_temporal_reference, 10);
        bw.flush();
    }
}