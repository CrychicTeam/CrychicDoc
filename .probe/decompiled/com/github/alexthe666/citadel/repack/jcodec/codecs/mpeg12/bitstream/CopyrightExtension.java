package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class CopyrightExtension implements MPEGHeader {

    public int copyright_flag;

    public int copyright_identifier;

    public int original_or_copy;

    public int copyright_number_1;

    public int copyright_number_2;

    public int copyright_number_3;

    public static final int Copyright_Extension = 4;

    public static CopyrightExtension read(BitReader _in) {
        CopyrightExtension ce = new CopyrightExtension();
        ce.copyright_flag = _in.read1Bit();
        ce.copyright_identifier = _in.readNBit(8);
        ce.original_or_copy = _in.read1Bit();
        _in.skip(7);
        _in.read1Bit();
        ce.copyright_number_1 = _in.readNBit(20);
        _in.read1Bit();
        ce.copyright_number_2 = _in.readNBit(22);
        _in.read1Bit();
        ce.copyright_number_3 = _in.readNBit(22);
        return ce;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(4, 4);
        bw.write1Bit(this.copyright_flag);
        bw.writeNBit(this.copyright_identifier, 8);
        bw.write1Bit(this.original_or_copy);
        bw.writeNBit(0, 7);
        bw.write1Bit(1);
        bw.writeNBit(this.copyright_number_1, 20);
        bw.write1Bit(1);
        bw.writeNBit(this.copyright_number_2, 22);
        bw.write1Bit(1);
        bw.writeNBit(this.copyright_number_3, 22);
        bw.flush();
    }
}