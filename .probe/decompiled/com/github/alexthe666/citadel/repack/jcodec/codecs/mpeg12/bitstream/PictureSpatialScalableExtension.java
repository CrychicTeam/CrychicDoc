package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class PictureSpatialScalableExtension implements MPEGHeader {

    public int lower_layer_temporal_reference;

    public int lower_layer_horizontal_offset;

    public int lower_layer_vertical_offset;

    public int spatial_temporal_weight_code_table_index;

    public int lower_layer_progressive_frame;

    public int lower_layer_deinterlaced_field_select;

    public static final int Picture_Spatial_Scalable_Extension = 9;

    public static PictureSpatialScalableExtension read(BitReader _in) {
        PictureSpatialScalableExtension psse = new PictureSpatialScalableExtension();
        psse.lower_layer_temporal_reference = _in.readNBit(10);
        _in.read1Bit();
        psse.lower_layer_horizontal_offset = _in.readNBit(15);
        _in.read1Bit();
        psse.lower_layer_vertical_offset = _in.readNBit(15);
        psse.spatial_temporal_weight_code_table_index = _in.readNBit(2);
        psse.lower_layer_progressive_frame = _in.read1Bit();
        psse.lower_layer_deinterlaced_field_select = _in.read1Bit();
        return psse;
    }

    @Override
    public void write(ByteBuffer bb) {
        BitWriter bw = new BitWriter(bb);
        bw.writeNBit(9, 4);
        bw.writeNBit(this.lower_layer_temporal_reference, 10);
        bw.write1Bit(1);
        bw.writeNBit(this.lower_layer_horizontal_offset, 15);
        bw.write1Bit(1);
        bw.writeNBit(this.lower_layer_vertical_offset, 15);
        bw.writeNBit(this.spatial_temporal_weight_code_table_index, 2);
        bw.write1Bit(this.lower_layer_progressive_frame);
        bw.write1Bit(this.lower_layer_deinterlaced_field_select);
        bw.flush();
    }
}