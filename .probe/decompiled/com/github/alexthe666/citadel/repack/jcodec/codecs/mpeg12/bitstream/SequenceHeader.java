package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class SequenceHeader implements MPEGHeader {

    private static boolean _hasExtensions;

    public int horizontal_size;

    public int vertical_size;

    public int aspect_ratio_information;

    public int frame_rate_code;

    public int bit_rate;

    public int vbv_buffer_size_value;

    public int constrained_parameters_flag;

    public int[] intra_quantiser_matrix;

    public int[] non_intra_quantiser_matrix;

    public SequenceExtension sequenceExtension;

    public SequenceScalableExtension sequenceScalableExtension;

    public SequenceDisplayExtension sequenceDisplayExtension;

    public static SequenceHeader createSequenceHeader(int horizontal_size, int vertical_size, int aspect_ratio_information, int frame_rate_code, int bit_rate, int vbv_buffer_size_value, int constrained_parameters_flag, int[] intra_quantiser_matrix, int[] non_intra_quantiser_matrix) {
        SequenceHeader sh = new SequenceHeader();
        sh.horizontal_size = horizontal_size;
        sh.vertical_size = vertical_size;
        sh.aspect_ratio_information = aspect_ratio_information;
        sh.frame_rate_code = frame_rate_code;
        sh.bit_rate = bit_rate;
        sh.vbv_buffer_size_value = vbv_buffer_size_value;
        sh.constrained_parameters_flag = constrained_parameters_flag;
        sh.intra_quantiser_matrix = intra_quantiser_matrix;
        sh.non_intra_quantiser_matrix = non_intra_quantiser_matrix;
        return sh;
    }

    private SequenceHeader() {
    }

    public static SequenceHeader read(ByteBuffer bb) {
        BitReader _in = BitReader.createBitReader(bb);
        SequenceHeader sh = new SequenceHeader();
        sh.horizontal_size = _in.readNBit(12);
        sh.vertical_size = _in.readNBit(12);
        sh.aspect_ratio_information = _in.readNBit(4);
        sh.frame_rate_code = _in.readNBit(4);
        sh.bit_rate = _in.readNBit(18);
        _in.read1Bit();
        sh.vbv_buffer_size_value = _in.readNBit(10);
        sh.constrained_parameters_flag = _in.read1Bit();
        if (_in.read1Bit() != 0) {
            sh.intra_quantiser_matrix = new int[64];
            for (int i = 0; i < 64; i++) {
                sh.intra_quantiser_matrix[i] = _in.readNBit(8);
            }
        }
        if (_in.read1Bit() != 0) {
            sh.non_intra_quantiser_matrix = new int[64];
            for (int i = 0; i < 64; i++) {
                sh.non_intra_quantiser_matrix[i] = _in.readNBit(8);
            }
        }
        return sh;
    }

    public static void readExtension(ByteBuffer bb, SequenceHeader sh) {
        _hasExtensions = true;
        BitReader _in = BitReader.createBitReader(bb);
        int extType = _in.readNBit(4);
        switch(extType) {
            case 1:
                sh.sequenceExtension = SequenceExtension.read(_in);
                break;
            case 2:
                sh.sequenceDisplayExtension = SequenceDisplayExtension.read(_in);
                break;
            case 3:
            case 4:
            default:
                throw new RuntimeException("Unsupported extension: " + extType);
            case 5:
                sh.sequenceScalableExtension = SequenceScalableExtension.read(_in);
        }
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(this.horizontal_size, 12);
        bw.writeNBit(this.vertical_size, 12);
        bw.writeNBit(this.aspect_ratio_information, 4);
        bw.writeNBit(this.frame_rate_code, 4);
        bw.writeNBit(this.bit_rate, 18);
        bw.write1Bit(1);
        bw.writeNBit(this.vbv_buffer_size_value, 10);
        bw.write1Bit(this.constrained_parameters_flag);
        bw.write1Bit(this.intra_quantiser_matrix != null ? 1 : 0);
        if (this.intra_quantiser_matrix != null) {
            for (int i = 0; i < 64; i++) {
                bw.writeNBit(this.intra_quantiser_matrix[i], 8);
            }
        }
        bw.write1Bit(this.non_intra_quantiser_matrix != null ? 1 : 0);
        if (this.non_intra_quantiser_matrix != null) {
            for (int i = 0; i < 64; i++) {
                bw.writeNBit(this.non_intra_quantiser_matrix[i], 8);
            }
        }
        bw.flush();
        this.writeExtensions(bb);
    }

    private void writeExtensions(ByteBuffer out) {
        if (this.sequenceExtension != null) {
            out.putInt(181);
            this.sequenceExtension.write(out);
        }
        if (this.sequenceScalableExtension != null) {
            out.putInt(181);
            this.sequenceScalableExtension.write(out);
        }
        if (this.sequenceDisplayExtension != null) {
            out.putInt(181);
            this.sequenceDisplayExtension.write(out);
        }
    }

    public boolean hasExtensions() {
        return _hasExtensions;
    }

    public void copyExtensions(SequenceHeader sh) {
        this.sequenceExtension = sh.sequenceExtension;
        this.sequenceScalableExtension = sh.sequenceScalableExtension;
        this.sequenceDisplayExtension = sh.sequenceDisplayExtension;
    }
}