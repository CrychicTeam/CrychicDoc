package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class SequenceDisplayExtension implements MPEGHeader {

    public int video_format;

    public int display_horizontal_size;

    public int display_vertical_size;

    public SequenceDisplayExtension.ColorDescription colorDescription;

    public static final int Sequence_Display_Extension = 2;

    public static SequenceDisplayExtension read(BitReader _in) {
        SequenceDisplayExtension sde = new SequenceDisplayExtension();
        sde.video_format = _in.readNBit(3);
        if (_in.read1Bit() == 1) {
            sde.colorDescription = SequenceDisplayExtension.ColorDescription.read(_in);
        }
        sde.display_horizontal_size = _in.readNBit(14);
        _in.read1Bit();
        sde.display_vertical_size = _in.readNBit(14);
        return sde;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(2, 4);
        bw.writeNBit(this.video_format, 3);
        bw.write1Bit(this.colorDescription != null ? 1 : 0);
        if (this.colorDescription != null) {
            this.colorDescription.write(bw);
        }
        bw.writeNBit(this.display_horizontal_size, 14);
        bw.write1Bit(1);
        bw.writeNBit(this.display_vertical_size, 14);
        bw.flush();
    }

    public static class ColorDescription {

        int colour_primaries;

        int transfer_characteristics;

        int matrix_coefficients;

        public static SequenceDisplayExtension.ColorDescription read(BitReader _in) {
            SequenceDisplayExtension.ColorDescription cd = new SequenceDisplayExtension.ColorDescription();
            cd.colour_primaries = _in.readNBit(8);
            cd.transfer_characteristics = _in.readNBit(8);
            cd.matrix_coefficients = _in.readNBit(8);
            return cd;
        }

        public void write(BitWriter out) {
            out.writeNBit(this.colour_primaries, 8);
            out.writeNBit(this.transfer_characteristics, 8);
            out.writeNBit(this.matrix_coefficients, 8);
        }
    }
}