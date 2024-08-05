package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class QuantMatrixExtension implements MPEGHeader {

    public int[] intra_quantiser_matrix;

    public int[] non_intra_quantiser_matrix;

    public int[] chroma_intra_quantiser_matrix;

    public int[] chroma_non_intra_quantiser_matrix;

    public static final int Quant_Matrix_Extension = 3;

    public static QuantMatrixExtension read(BitReader _in) {
        QuantMatrixExtension qme = new QuantMatrixExtension();
        if (_in.read1Bit() != 0) {
            qme.intra_quantiser_matrix = readQMat(_in);
        }
        if (_in.read1Bit() != 0) {
            qme.non_intra_quantiser_matrix = readQMat(_in);
        }
        if (_in.read1Bit() != 0) {
            qme.chroma_intra_quantiser_matrix = readQMat(_in);
        }
        if (_in.read1Bit() != 0) {
            qme.chroma_non_intra_quantiser_matrix = readQMat(_in);
        }
        return qme;
    }

    private static int[] readQMat(BitReader _in) {
        int[] qmat = new int[64];
        for (int i = 0; i < 64; i++) {
            qmat[i] = _in.readNBit(8);
        }
        return qmat;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(3, 4);
        bw.write1Bit(this.intra_quantiser_matrix != null ? 1 : 0);
        if (this.intra_quantiser_matrix != null) {
            this.writeQMat(this.intra_quantiser_matrix, bw);
        }
        bw.write1Bit(this.non_intra_quantiser_matrix != null ? 1 : 0);
        if (this.non_intra_quantiser_matrix != null) {
            this.writeQMat(this.non_intra_quantiser_matrix, bw);
        }
        bw.write1Bit(this.chroma_intra_quantiser_matrix != null ? 1 : 0);
        if (this.chroma_intra_quantiser_matrix != null) {
            this.writeQMat(this.chroma_intra_quantiser_matrix, bw);
        }
        bw.write1Bit(this.chroma_non_intra_quantiser_matrix != null ? 1 : 0);
        if (this.chroma_non_intra_quantiser_matrix != null) {
            this.writeQMat(this.chroma_non_intra_quantiser_matrix, bw);
        }
        bw.flush();
    }

    private void writeQMat(int[] matrix, BitWriter ob) {
        for (int i = 0; i < 64; i++) {
            ob.writeNBit(matrix[i], 8);
        }
    }
}