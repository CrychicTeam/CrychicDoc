package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CAVLCReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class SeqParameterSetExt {

    public int seq_parameter_set_id;

    public int aux_format_idc;

    public int bit_depth_aux_minus8;

    public boolean alpha_incr_flag;

    public boolean additional_extension_flag;

    public int alpha_opaque_value;

    public int alpha_transparent_value;

    public static SeqParameterSetExt read(ByteBuffer is) {
        BitReader _in = BitReader.createBitReader(is);
        SeqParameterSetExt spse = new SeqParameterSetExt();
        spse.seq_parameter_set_id = CAVLCReader.readUEtrace(_in, "SPSE: seq_parameter_set_id");
        spse.aux_format_idc = CAVLCReader.readUEtrace(_in, "SPSE: aux_format_idc");
        if (spse.aux_format_idc != 0) {
            spse.bit_depth_aux_minus8 = CAVLCReader.readUEtrace(_in, "SPSE: bit_depth_aux_minus8");
            spse.alpha_incr_flag = CAVLCReader.readBool(_in, "SPSE: alpha_incr_flag");
            spse.alpha_opaque_value = CAVLCReader.readU(_in, spse.bit_depth_aux_minus8 + 9, "SPSE: alpha_opaque_value");
            spse.alpha_transparent_value = CAVLCReader.readU(_in, spse.bit_depth_aux_minus8 + 9, "SPSE: alpha_transparent_value");
        }
        spse.additional_extension_flag = CAVLCReader.readBool(_in, "SPSE: additional_extension_flag");
        return spse;
    }

    public void write(ByteBuffer out) {
        BitWriter writer = new BitWriter(out);
        CAVLCWriter.writeTrailingBits(writer);
    }
}