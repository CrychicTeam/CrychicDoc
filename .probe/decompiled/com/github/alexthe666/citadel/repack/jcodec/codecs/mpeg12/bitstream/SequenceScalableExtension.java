package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.MPEGConst;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import java.nio.ByteBuffer;

public class SequenceScalableExtension implements MPEGHeader {

    public static final int DATA_PARTITIONING = 0;

    public static final int SPATIAL_SCALABILITY = 1;

    public static final int SNR_SCALABILITY = 2;

    public static final int TEMPORAL_SCALABILITY = 3;

    public int scalable_mode;

    public int layer_id;

    public int lower_layer_prediction_horizontal_size;

    public int lower_layer_prediction_vertical_size;

    public int horizontal_subsampling_factor_m;

    public int horizontal_subsampling_factor_n;

    public int vertical_subsampling_factor_m;

    public int vertical_subsampling_factor_n;

    public int picture_mux_enable;

    public int mux_to_progressive_sequence;

    public int picture_mux_order;

    public int picture_mux_factor;

    public static final int Sequence_Scalable_Extension = 5;

    public static SequenceScalableExtension read(BitReader _in) {
        SequenceScalableExtension sse = new SequenceScalableExtension();
        sse.scalable_mode = _in.readNBit(2);
        sse.layer_id = _in.readNBit(4);
        if (sse.scalable_mode == 1) {
            sse.lower_layer_prediction_horizontal_size = _in.readNBit(14);
            _in.read1Bit();
            sse.lower_layer_prediction_vertical_size = _in.readNBit(14);
            sse.horizontal_subsampling_factor_m = _in.readNBit(5);
            sse.horizontal_subsampling_factor_n = _in.readNBit(5);
            sse.vertical_subsampling_factor_m = _in.readNBit(5);
            sse.vertical_subsampling_factor_n = _in.readNBit(5);
        }
        if (sse.scalable_mode == 3) {
            sse.picture_mux_enable = _in.read1Bit();
            if (sse.picture_mux_enable != 0) {
                sse.mux_to_progressive_sequence = _in.read1Bit();
            }
            sse.picture_mux_order = _in.readNBit(3);
            sse.picture_mux_factor = _in.readNBit(3);
        }
        return sse;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(5, 4);
        bw.writeNBit(this.scalable_mode, 2);
        bw.writeNBit(this.layer_id, 4);
        if (this.scalable_mode == 1) {
            bw.writeNBit(this.lower_layer_prediction_horizontal_size, 14);
            bw.write1Bit(1);
            bw.writeNBit(this.lower_layer_prediction_vertical_size, 14);
            bw.writeNBit(this.horizontal_subsampling_factor_m, 5);
            bw.writeNBit(this.horizontal_subsampling_factor_n, 5);
            bw.writeNBit(this.vertical_subsampling_factor_m, 5);
            bw.writeNBit(this.vertical_subsampling_factor_n, 5);
        }
        if (this.scalable_mode == 3) {
            bw.write1Bit(this.picture_mux_enable);
            if (this.picture_mux_enable != 0) {
                bw.write1Bit(this.mux_to_progressive_sequence);
            }
            bw.writeNBit(this.picture_mux_order, 3);
            bw.writeNBit(this.picture_mux_factor, 3);
        }
        bw.flush();
    }

    public static MPEGConst.MBType[] mbTypeVal(int picture_coding_type, SequenceScalableExtension sse) {
        if (sse != null && sse.scalable_mode == 2) {
            return MPEGConst.mbTypeValSNR;
        } else if (sse != null && sse.scalable_mode == 1) {
            return picture_coding_type == 1 ? MPEGConst.mbTypeValISpat : (picture_coding_type == 2 ? MPEGConst.mbTypeValPSpat : MPEGConst.mbTypeValBSpat);
        } else {
            return picture_coding_type == 1 ? MPEGConst.mbTypeValI : (picture_coding_type == 2 ? MPEGConst.mbTypeValP : MPEGConst.mbTypeValB);
        }
    }

    public static VLC vlcMBType(int picture_coding_type, SequenceScalableExtension sse) {
        if (sse != null && sse.scalable_mode == 2) {
            return MPEGConst.vlcMBTypeSNR;
        } else if (sse != null && sse.scalable_mode == 1) {
            return picture_coding_type == 1 ? MPEGConst.vlcMBTypeISpat : (picture_coding_type == 2 ? MPEGConst.vlcMBTypePSpat : MPEGConst.vlcMBTypeBSpat);
        } else {
            return picture_coding_type == 1 ? MPEGConst.vlcMBTypeI : (picture_coding_type == 2 ? MPEGConst.vlcMBTypeP : MPEGConst.vlcMBTypeB);
        }
    }
}