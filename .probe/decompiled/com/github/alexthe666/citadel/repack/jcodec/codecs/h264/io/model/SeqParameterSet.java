package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CAVLCReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class SeqParameterSet {

    public int picOrderCntType;

    public boolean fieldPicFlag;

    public boolean deltaPicOrderAlwaysZeroFlag;

    public boolean mbAdaptiveFrameFieldFlag;

    public boolean direct8x8InferenceFlag;

    public ColorSpace chromaFormatIdc;

    public int log2MaxFrameNumMinus4;

    public int log2MaxPicOrderCntLsbMinus4;

    public int picHeightInMapUnitsMinus1;

    public int picWidthInMbsMinus1;

    public int bitDepthLumaMinus8;

    public int bitDepthChromaMinus8;

    public boolean qpprimeYZeroTransformBypassFlag;

    public int profileIdc;

    public boolean constraintSet0Flag;

    public boolean constraintSet1Flag;

    public boolean constraintSet2Flag;

    public boolean constraintSet3Flag;

    public boolean constraintSet4Flag;

    public boolean constraintSet5Flag;

    public int levelIdc;

    public int seqParameterSetId;

    public boolean separateColourPlaneFlag;

    public int offsetForNonRefPic;

    public int offsetForTopToBottomField;

    public int numRefFrames;

    public boolean gapsInFrameNumValueAllowedFlag;

    public boolean frameMbsOnlyFlag;

    public boolean frameCroppingFlag;

    public int frameCropLeftOffset;

    public int frameCropRightOffset;

    public int frameCropTopOffset;

    public int frameCropBottomOffset;

    public int[] offsetForRefFrame;

    public VUIParameters vuiParams;

    public int[][] scalingMatrix;

    public int numRefFramesInPicOrderCntCycle;

    public static ColorSpace getColor(int id) {
        switch(id) {
            case 0:
                return ColorSpace.MONO;
            case 1:
                return ColorSpace.YUV420J;
            case 2:
                return ColorSpace.YUV422;
            case 3:
                return ColorSpace.YUV444;
            default:
                throw new RuntimeException("Colorspace not supported");
        }
    }

    public static int fromColor(ColorSpace color) {
        if (color == ColorSpace.MONO) {
            return 0;
        } else if (color == ColorSpace.YUV420J) {
            return 1;
        } else if (color == ColorSpace.YUV422) {
            return 2;
        } else if (color == ColorSpace.YUV444) {
            return 3;
        } else {
            throw new RuntimeException("Colorspace not supported");
        }
    }

    public static SeqParameterSet read(ByteBuffer is) {
        BitReader _in = BitReader.createBitReader(is);
        SeqParameterSet sps = new SeqParameterSet();
        sps.profileIdc = CAVLCReader.readNBit(_in, 8, "SPS: profile_idc");
        sps.constraintSet0Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_0_flag");
        sps.constraintSet1Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_1_flag");
        sps.constraintSet2Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_2_flag");
        sps.constraintSet3Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_3_flag");
        sps.constraintSet4Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_4_flag");
        sps.constraintSet5Flag = CAVLCReader.readBool(_in, "SPS: constraint_set_5_flag");
        CAVLCReader.readNBit(_in, 2, "SPS: reserved_zero_2bits");
        sps.levelIdc = CAVLCReader.readNBit(_in, 8, "SPS: level_idc");
        sps.seqParameterSetId = CAVLCReader.readUEtrace(_in, "SPS: seq_parameter_set_id");
        if (sps.profileIdc != 100 && sps.profileIdc != 110 && sps.profileIdc != 122 && sps.profileIdc != 144) {
            sps.chromaFormatIdc = ColorSpace.YUV420J;
        } else {
            sps.chromaFormatIdc = getColor(CAVLCReader.readUEtrace(_in, "SPS: chroma_format_idc"));
            if (sps.chromaFormatIdc == ColorSpace.YUV444) {
                sps.separateColourPlaneFlag = CAVLCReader.readBool(_in, "SPS: separate_colour_plane_flag");
            }
            sps.bitDepthLumaMinus8 = CAVLCReader.readUEtrace(_in, "SPS: bit_depth_luma_minus8");
            sps.bitDepthChromaMinus8 = CAVLCReader.readUEtrace(_in, "SPS: bit_depth_chroma_minus8");
            sps.qpprimeYZeroTransformBypassFlag = CAVLCReader.readBool(_in, "SPS: qpprime_y_zero_transform_bypass_flag");
            boolean seqScalingMatrixPresent = CAVLCReader.readBool(_in, "SPS: seq_scaling_matrix_present_lag");
            if (seqScalingMatrixPresent) {
                readScalingListMatrix(_in, sps);
            }
        }
        sps.log2MaxFrameNumMinus4 = CAVLCReader.readUEtrace(_in, "SPS: log2_max_frame_num_minus4");
        sps.picOrderCntType = CAVLCReader.readUEtrace(_in, "SPS: pic_order_cnt_type");
        if (sps.picOrderCntType == 0) {
            sps.log2MaxPicOrderCntLsbMinus4 = CAVLCReader.readUEtrace(_in, "SPS: log2_max_pic_order_cnt_lsb_minus4");
        } else if (sps.picOrderCntType == 1) {
            sps.deltaPicOrderAlwaysZeroFlag = CAVLCReader.readBool(_in, "SPS: delta_pic_order_always_zero_flag");
            sps.offsetForNonRefPic = CAVLCReader.readSE(_in, "SPS: offset_for_non_ref_pic");
            sps.offsetForTopToBottomField = CAVLCReader.readSE(_in, "SPS: offset_for_top_to_bottom_field");
            sps.numRefFramesInPicOrderCntCycle = CAVLCReader.readUEtrace(_in, "SPS: num_ref_frames_in_pic_order_cnt_cycle");
            sps.offsetForRefFrame = new int[sps.numRefFramesInPicOrderCntCycle];
            for (int i = 0; i < sps.numRefFramesInPicOrderCntCycle; i++) {
                sps.offsetForRefFrame[i] = CAVLCReader.readSE(_in, "SPS: offsetForRefFrame [" + i + "]");
            }
        }
        sps.numRefFrames = CAVLCReader.readUEtrace(_in, "SPS: num_ref_frames");
        sps.gapsInFrameNumValueAllowedFlag = CAVLCReader.readBool(_in, "SPS: gaps_in_frame_num_value_allowed_flag");
        sps.picWidthInMbsMinus1 = CAVLCReader.readUEtrace(_in, "SPS: pic_width_in_mbs_minus1");
        sps.picHeightInMapUnitsMinus1 = CAVLCReader.readUEtrace(_in, "SPS: pic_height_in_map_units_minus1");
        sps.frameMbsOnlyFlag = CAVLCReader.readBool(_in, "SPS: frame_mbs_only_flag");
        if (!sps.frameMbsOnlyFlag) {
            sps.mbAdaptiveFrameFieldFlag = CAVLCReader.readBool(_in, "SPS: mb_adaptive_frame_field_flag");
        }
        sps.direct8x8InferenceFlag = CAVLCReader.readBool(_in, "SPS: direct_8x8_inference_flag");
        sps.frameCroppingFlag = CAVLCReader.readBool(_in, "SPS: frame_cropping_flag");
        if (sps.frameCroppingFlag) {
            sps.frameCropLeftOffset = CAVLCReader.readUEtrace(_in, "SPS: frame_crop_left_offset");
            sps.frameCropRightOffset = CAVLCReader.readUEtrace(_in, "SPS: frame_crop_right_offset");
            sps.frameCropTopOffset = CAVLCReader.readUEtrace(_in, "SPS: frame_crop_top_offset");
            sps.frameCropBottomOffset = CAVLCReader.readUEtrace(_in, "SPS: frame_crop_bottom_offset");
        }
        boolean vuiParametersPresentFlag = CAVLCReader.readBool(_in, "SPS: vui_parameters_present_flag");
        if (vuiParametersPresentFlag) {
            sps.vuiParams = readVUIParameters(_in);
        }
        return sps;
    }

    public static void writeScalingList(BitWriter out, int[][] scalingMatrix, int which) {
        boolean useDefaultScalingMatrixFlag = false;
        switch(which) {
            case 0:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], H264Const.defaultScalingList4x4Intra);
                break;
            case 1:
            case 2:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], scalingMatrix[0]);
                break;
            case 3:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], H264Const.defaultScalingList4x4Inter);
                break;
            case 4:
            case 5:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], scalingMatrix[3]);
                break;
            case 6:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], H264Const.defaultScalingList8x8Intra);
                break;
            case 7:
                useDefaultScalingMatrixFlag = Platform.arrayEqualsInt(scalingMatrix[which], H264Const.defaultScalingList8x8Inter);
        }
        int[] scalingList = scalingMatrix[which];
        if (useDefaultScalingMatrixFlag) {
            CAVLCWriter.writeSEtrace(out, -8, "SPS: ");
        } else {
            int lastScale = 8;
            int nextScale = 8;
            for (int j = 0; j < scalingList.length; j++) {
                if (nextScale != 0) {
                    int deltaScale = scalingList[j] - lastScale - 256;
                    CAVLCWriter.writeSEtrace(out, deltaScale, "SPS: ");
                }
                lastScale = scalingList[j];
            }
        }
    }

    public static int[] readScalingList(BitReader src, int sizeOfScalingList) {
        int[] scalingList = new int[sizeOfScalingList];
        int lastScale = 8;
        int nextScale = 8;
        for (int j = 0; j < sizeOfScalingList; j++) {
            if (nextScale != 0) {
                int deltaScale = CAVLCReader.readSE(src, "deltaScale");
                nextScale = (lastScale + deltaScale + 256) % 256;
                if (j == 0 && nextScale == 0) {
                    return null;
                }
            }
            scalingList[j] = nextScale == 0 ? lastScale : nextScale;
            lastScale = scalingList[j];
        }
        return scalingList;
    }

    private static void readScalingListMatrix(BitReader src, SeqParameterSet sps) {
        sps.scalingMatrix = new int[8][];
        for (int i = 0; i < 8; i++) {
            boolean seqScalingListPresentFlag = CAVLCReader.readBool(src, "SPS: seqScalingListPresentFlag");
            if (seqScalingListPresentFlag) {
                int scalingListSize = i < 6 ? 16 : 64;
                sps.scalingMatrix[i] = readScalingList(src, scalingListSize);
            }
        }
    }

    private static VUIParameters readVUIParameters(BitReader _in) {
        VUIParameters vuip = new VUIParameters();
        vuip.aspectRatioInfoPresentFlag = CAVLCReader.readBool(_in, "VUI: aspect_ratio_info_present_flag");
        if (vuip.aspectRatioInfoPresentFlag) {
            vuip.aspectRatio = AspectRatio.fromValue(CAVLCReader.readNBit(_in, 8, "VUI: aspect_ratio"));
            if (vuip.aspectRatio == AspectRatio.Extended_SAR) {
                vuip.sarWidth = CAVLCReader.readNBit(_in, 16, "VUI: sar_width");
                vuip.sarHeight = CAVLCReader.readNBit(_in, 16, "VUI: sar_height");
            }
        }
        vuip.overscanInfoPresentFlag = CAVLCReader.readBool(_in, "VUI: overscan_info_present_flag");
        if (vuip.overscanInfoPresentFlag) {
            vuip.overscanAppropriateFlag = CAVLCReader.readBool(_in, "VUI: overscan_appropriate_flag");
        }
        vuip.videoSignalTypePresentFlag = CAVLCReader.readBool(_in, "VUI: video_signal_type_present_flag");
        if (vuip.videoSignalTypePresentFlag) {
            vuip.videoFormat = CAVLCReader.readNBit(_in, 3, "VUI: video_format");
            vuip.videoFullRangeFlag = CAVLCReader.readBool(_in, "VUI: video_full_range_flag");
            vuip.colourDescriptionPresentFlag = CAVLCReader.readBool(_in, "VUI: colour_description_present_flag");
            if (vuip.colourDescriptionPresentFlag) {
                vuip.colourPrimaries = CAVLCReader.readNBit(_in, 8, "VUI: colour_primaries");
                vuip.transferCharacteristics = CAVLCReader.readNBit(_in, 8, "VUI: transfer_characteristics");
                vuip.matrixCoefficients = CAVLCReader.readNBit(_in, 8, "VUI: matrix_coefficients");
            }
        }
        vuip.chromaLocInfoPresentFlag = CAVLCReader.readBool(_in, "VUI: chroma_loc_info_present_flag");
        if (vuip.chromaLocInfoPresentFlag) {
            vuip.chromaSampleLocTypeTopField = CAVLCReader.readUEtrace(_in, "VUI chroma_sample_loc_type_top_field");
            vuip.chromaSampleLocTypeBottomField = CAVLCReader.readUEtrace(_in, "VUI chroma_sample_loc_type_bottom_field");
        }
        vuip.timingInfoPresentFlag = CAVLCReader.readBool(_in, "VUI: timing_info_present_flag");
        if (vuip.timingInfoPresentFlag) {
            vuip.numUnitsInTick = CAVLCReader.readNBit(_in, 32, "VUI: num_units_in_tick");
            vuip.timeScale = CAVLCReader.readNBit(_in, 32, "VUI: time_scale");
            vuip.fixedFrameRateFlag = CAVLCReader.readBool(_in, "VUI: fixed_frame_rate_flag");
        }
        boolean nalHRDParametersPresentFlag = CAVLCReader.readBool(_in, "VUI: nal_hrd_parameters_present_flag");
        if (nalHRDParametersPresentFlag) {
            vuip.nalHRDParams = readHRDParameters(_in);
        }
        boolean vclHRDParametersPresentFlag = CAVLCReader.readBool(_in, "VUI: vcl_hrd_parameters_present_flag");
        if (vclHRDParametersPresentFlag) {
            vuip.vclHRDParams = readHRDParameters(_in);
        }
        if (nalHRDParametersPresentFlag || vclHRDParametersPresentFlag) {
            vuip.lowDelayHrdFlag = CAVLCReader.readBool(_in, "VUI: low_delay_hrd_flag");
        }
        vuip.picStructPresentFlag = CAVLCReader.readBool(_in, "VUI: pic_struct_present_flag");
        boolean bitstreamRestrictionFlag = CAVLCReader.readBool(_in, "VUI: bitstream_restriction_flag");
        if (bitstreamRestrictionFlag) {
            vuip.bitstreamRestriction = new VUIParameters.BitstreamRestriction();
            vuip.bitstreamRestriction.motionVectorsOverPicBoundariesFlag = CAVLCReader.readBool(_in, "VUI: motion_vectors_over_pic_boundaries_flag");
            vuip.bitstreamRestriction.maxBytesPerPicDenom = CAVLCReader.readUEtrace(_in, "VUI max_bytes_per_pic_denom");
            vuip.bitstreamRestriction.maxBitsPerMbDenom = CAVLCReader.readUEtrace(_in, "VUI max_bits_per_mb_denom");
            vuip.bitstreamRestriction.log2MaxMvLengthHorizontal = CAVLCReader.readUEtrace(_in, "VUI log2_max_mv_length_horizontal");
            vuip.bitstreamRestriction.log2MaxMvLengthVertical = CAVLCReader.readUEtrace(_in, "VUI log2_max_mv_length_vertical");
            vuip.bitstreamRestriction.numReorderFrames = CAVLCReader.readUEtrace(_in, "VUI num_reorder_frames");
            vuip.bitstreamRestriction.maxDecFrameBuffering = CAVLCReader.readUEtrace(_in, "VUI max_dec_frame_buffering");
        }
        return vuip;
    }

    private static HRDParameters readHRDParameters(BitReader _in) {
        HRDParameters hrd = new HRDParameters();
        hrd.cpbCntMinus1 = CAVLCReader.readUEtrace(_in, "SPS: cpb_cnt_minus1");
        hrd.bitRateScale = CAVLCReader.readNBit(_in, 4, "HRD: bit_rate_scale");
        hrd.cpbSizeScale = CAVLCReader.readNBit(_in, 4, "HRD: cpb_size_scale");
        hrd.bitRateValueMinus1 = new int[hrd.cpbCntMinus1 + 1];
        hrd.cpbSizeValueMinus1 = new int[hrd.cpbCntMinus1 + 1];
        hrd.cbrFlag = new boolean[hrd.cpbCntMinus1 + 1];
        for (int SchedSelIdx = 0; SchedSelIdx <= hrd.cpbCntMinus1; SchedSelIdx++) {
            hrd.bitRateValueMinus1[SchedSelIdx] = CAVLCReader.readUEtrace(_in, "HRD: bit_rate_value_minus1");
            hrd.cpbSizeValueMinus1[SchedSelIdx] = CAVLCReader.readUEtrace(_in, "HRD: cpb_size_value_minus1");
            hrd.cbrFlag[SchedSelIdx] = CAVLCReader.readBool(_in, "HRD: cbr_flag");
        }
        hrd.initialCpbRemovalDelayLengthMinus1 = CAVLCReader.readNBit(_in, 5, "HRD: initial_cpb_removal_delay_length_minus1");
        hrd.cpbRemovalDelayLengthMinus1 = CAVLCReader.readNBit(_in, 5, "HRD: cpb_removal_delay_length_minus1");
        hrd.dpbOutputDelayLengthMinus1 = CAVLCReader.readNBit(_in, 5, "HRD: dpb_output_delay_length_minus1");
        hrd.timeOffsetLength = CAVLCReader.readNBit(_in, 5, "HRD: time_offset_length");
        return hrd;
    }

    public void write(ByteBuffer out) {
        BitWriter writer = new BitWriter(out);
        CAVLCWriter.writeNBit(writer, (long) this.profileIdc, 8, "SPS: profile_idc");
        CAVLCWriter.writeBool(writer, this.constraintSet0Flag, "SPS: constraint_set_0_flag");
        CAVLCWriter.writeBool(writer, this.constraintSet1Flag, "SPS: constraint_set_1_flag");
        CAVLCWriter.writeBool(writer, this.constraintSet2Flag, "SPS: constraint_set_2_flag");
        CAVLCWriter.writeBool(writer, this.constraintSet3Flag, "SPS: constraint_set_3_flag");
        CAVLCWriter.writeBool(writer, this.constraintSet4Flag, "SPS: constraint_set_4_flag");
        CAVLCWriter.writeBool(writer, this.constraintSet5Flag, "SPS: constraint_set_5_flag");
        CAVLCWriter.writeNBit(writer, 0L, 2, "SPS: reserved");
        CAVLCWriter.writeNBit(writer, (long) this.levelIdc, 8, "SPS: level_idc");
        CAVLCWriter.writeUEtrace(writer, this.seqParameterSetId, "SPS: seq_parameter_set_id");
        if (this.profileIdc == 100 || this.profileIdc == 110 || this.profileIdc == 122 || this.profileIdc == 144) {
            CAVLCWriter.writeUEtrace(writer, fromColor(this.chromaFormatIdc), "SPS: chroma_format_idc");
            if (this.chromaFormatIdc == ColorSpace.YUV444) {
                CAVLCWriter.writeBool(writer, this.separateColourPlaneFlag, "SPS: residual_color_transform_flag");
            }
            CAVLCWriter.writeUEtrace(writer, this.bitDepthLumaMinus8, "SPS: ");
            CAVLCWriter.writeUEtrace(writer, this.bitDepthChromaMinus8, "SPS: ");
            CAVLCWriter.writeBool(writer, this.qpprimeYZeroTransformBypassFlag, "SPS: qpprime_y_zero_transform_bypass_flag");
            CAVLCWriter.writeBool(writer, this.scalingMatrix != null, "SPS: ");
            if (this.scalingMatrix != null) {
                for (int i = 0; i < 8; i++) {
                    CAVLCWriter.writeBool(writer, this.scalingMatrix[i] != null, "SPS: ");
                    if (this.scalingMatrix[i] != null) {
                        writeScalingList(writer, this.scalingMatrix, i);
                    }
                }
            }
        }
        CAVLCWriter.writeUEtrace(writer, this.log2MaxFrameNumMinus4, "SPS: log2_max_frame_num_minus4");
        CAVLCWriter.writeUEtrace(writer, this.picOrderCntType, "SPS: pic_order_cnt_type");
        if (this.picOrderCntType == 0) {
            CAVLCWriter.writeUEtrace(writer, this.log2MaxPicOrderCntLsbMinus4, "SPS: log2_max_pic_order_cnt_lsb_minus4");
        } else if (this.picOrderCntType == 1) {
            CAVLCWriter.writeBool(writer, this.deltaPicOrderAlwaysZeroFlag, "SPS: delta_pic_order_always_zero_flag");
            CAVLCWriter.writeSEtrace(writer, this.offsetForNonRefPic, "SPS: offset_for_non_ref_pic");
            CAVLCWriter.writeSEtrace(writer, this.offsetForTopToBottomField, "SPS: offset_for_top_to_bottom_field");
            CAVLCWriter.writeUEtrace(writer, this.offsetForRefFrame.length, "SPS: ");
            for (int ix = 0; ix < this.offsetForRefFrame.length; ix++) {
                CAVLCWriter.writeSEtrace(writer, this.offsetForRefFrame[ix], "SPS: ");
            }
        }
        CAVLCWriter.writeUEtrace(writer, this.numRefFrames, "SPS: num_ref_frames");
        CAVLCWriter.writeBool(writer, this.gapsInFrameNumValueAllowedFlag, "SPS: gaps_in_frame_num_value_allowed_flag");
        CAVLCWriter.writeUEtrace(writer, this.picWidthInMbsMinus1, "SPS: pic_width_in_mbs_minus1");
        CAVLCWriter.writeUEtrace(writer, this.picHeightInMapUnitsMinus1, "SPS: pic_height_in_map_units_minus1");
        CAVLCWriter.writeBool(writer, this.frameMbsOnlyFlag, "SPS: frame_mbs_only_flag");
        if (!this.frameMbsOnlyFlag) {
            CAVLCWriter.writeBool(writer, this.mbAdaptiveFrameFieldFlag, "SPS: mb_adaptive_frame_field_flag");
        }
        CAVLCWriter.writeBool(writer, this.direct8x8InferenceFlag, "SPS: direct_8x8_inference_flag");
        CAVLCWriter.writeBool(writer, this.frameCroppingFlag, "SPS: frame_cropping_flag");
        if (this.frameCroppingFlag) {
            CAVLCWriter.writeUEtrace(writer, this.frameCropLeftOffset, "SPS: frame_crop_left_offset");
            CAVLCWriter.writeUEtrace(writer, this.frameCropRightOffset, "SPS: frame_crop_right_offset");
            CAVLCWriter.writeUEtrace(writer, this.frameCropTopOffset, "SPS: frame_crop_top_offset");
            CAVLCWriter.writeUEtrace(writer, this.frameCropBottomOffset, "SPS: frame_crop_bottom_offset");
        }
        CAVLCWriter.writeBool(writer, this.vuiParams != null, "SPS: ");
        if (this.vuiParams != null) {
            this.writeVUIParameters(this.vuiParams, writer);
        }
        CAVLCWriter.writeTrailingBits(writer);
    }

    private void writeVUIParameters(VUIParameters vuip, BitWriter writer) {
        CAVLCWriter.writeBool(writer, vuip.aspectRatioInfoPresentFlag, "VUI: aspect_ratio_info_present_flag");
        if (vuip.aspectRatioInfoPresentFlag) {
            CAVLCWriter.writeNBit(writer, (long) vuip.aspectRatio.getValue(), 8, "VUI: aspect_ratio");
            if (vuip.aspectRatio == AspectRatio.Extended_SAR) {
                CAVLCWriter.writeNBit(writer, (long) vuip.sarWidth, 16, "VUI: sar_width");
                CAVLCWriter.writeNBit(writer, (long) vuip.sarHeight, 16, "VUI: sar_height");
            }
        }
        CAVLCWriter.writeBool(writer, vuip.overscanInfoPresentFlag, "VUI: overscan_info_present_flag");
        if (vuip.overscanInfoPresentFlag) {
            CAVLCWriter.writeBool(writer, vuip.overscanAppropriateFlag, "VUI: overscan_appropriate_flag");
        }
        CAVLCWriter.writeBool(writer, vuip.videoSignalTypePresentFlag, "VUI: video_signal_type_present_flag");
        if (vuip.videoSignalTypePresentFlag) {
            CAVLCWriter.writeNBit(writer, (long) vuip.videoFormat, 3, "VUI: video_format");
            CAVLCWriter.writeBool(writer, vuip.videoFullRangeFlag, "VUI: video_full_range_flag");
            CAVLCWriter.writeBool(writer, vuip.colourDescriptionPresentFlag, "VUI: colour_description_present_flag");
            if (vuip.colourDescriptionPresentFlag) {
                CAVLCWriter.writeNBit(writer, (long) vuip.colourPrimaries, 8, "VUI: colour_primaries");
                CAVLCWriter.writeNBit(writer, (long) vuip.transferCharacteristics, 8, "VUI: transfer_characteristics");
                CAVLCWriter.writeNBit(writer, (long) vuip.matrixCoefficients, 8, "VUI: matrix_coefficients");
            }
        }
        CAVLCWriter.writeBool(writer, vuip.chromaLocInfoPresentFlag, "VUI: chroma_loc_info_present_flag");
        if (vuip.chromaLocInfoPresentFlag) {
            CAVLCWriter.writeUEtrace(writer, vuip.chromaSampleLocTypeTopField, "VUI: chroma_sample_loc_type_top_field");
            CAVLCWriter.writeUEtrace(writer, vuip.chromaSampleLocTypeBottomField, "VUI: chroma_sample_loc_type_bottom_field");
        }
        CAVLCWriter.writeBool(writer, vuip.timingInfoPresentFlag, "VUI: timing_info_present_flag");
        if (vuip.timingInfoPresentFlag) {
            CAVLCWriter.writeNBit(writer, (long) vuip.numUnitsInTick, 32, "VUI: num_units_in_tick");
            CAVLCWriter.writeNBit(writer, (long) vuip.timeScale, 32, "VUI: time_scale");
            CAVLCWriter.writeBool(writer, vuip.fixedFrameRateFlag, "VUI: fixed_frame_rate_flag");
        }
        CAVLCWriter.writeBool(writer, vuip.nalHRDParams != null, "VUI: ");
        if (vuip.nalHRDParams != null) {
            this.writeHRDParameters(vuip.nalHRDParams, writer);
        }
        CAVLCWriter.writeBool(writer, vuip.vclHRDParams != null, "VUI: ");
        if (vuip.vclHRDParams != null) {
            this.writeHRDParameters(vuip.vclHRDParams, writer);
        }
        if (vuip.nalHRDParams != null || vuip.vclHRDParams != null) {
            CAVLCWriter.writeBool(writer, vuip.lowDelayHrdFlag, "VUI: low_delay_hrd_flag");
        }
        CAVLCWriter.writeBool(writer, vuip.picStructPresentFlag, "VUI: pic_struct_present_flag");
        CAVLCWriter.writeBool(writer, vuip.bitstreamRestriction != null, "VUI: ");
        if (vuip.bitstreamRestriction != null) {
            CAVLCWriter.writeBool(writer, vuip.bitstreamRestriction.motionVectorsOverPicBoundariesFlag, "VUI: motion_vectors_over_pic_boundaries_flag");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.maxBytesPerPicDenom, "VUI: max_bytes_per_pic_denom");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.maxBitsPerMbDenom, "VUI: max_bits_per_mb_denom");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.log2MaxMvLengthHorizontal, "VUI: log2_max_mv_length_horizontal");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.log2MaxMvLengthVertical, "VUI: log2_max_mv_length_vertical");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.numReorderFrames, "VUI: num_reorder_frames");
            CAVLCWriter.writeUEtrace(writer, vuip.bitstreamRestriction.maxDecFrameBuffering, "VUI: max_dec_frame_buffering");
        }
    }

    private void writeHRDParameters(HRDParameters hrd, BitWriter writer) {
        CAVLCWriter.writeUEtrace(writer, hrd.cpbCntMinus1, "HRD: cpb_cnt_minus1");
        CAVLCWriter.writeNBit(writer, (long) hrd.bitRateScale, 4, "HRD: bit_rate_scale");
        CAVLCWriter.writeNBit(writer, (long) hrd.cpbSizeScale, 4, "HRD: cpb_size_scale");
        for (int SchedSelIdx = 0; SchedSelIdx <= hrd.cpbCntMinus1; SchedSelIdx++) {
            CAVLCWriter.writeUEtrace(writer, hrd.bitRateValueMinus1[SchedSelIdx], "HRD: ");
            CAVLCWriter.writeUEtrace(writer, hrd.cpbSizeValueMinus1[SchedSelIdx], "HRD: ");
            CAVLCWriter.writeBool(writer, hrd.cbrFlag[SchedSelIdx], "HRD: ");
        }
        CAVLCWriter.writeNBit(writer, (long) hrd.initialCpbRemovalDelayLengthMinus1, 5, "HRD: initial_cpb_removal_delay_length_minus1");
        CAVLCWriter.writeNBit(writer, (long) hrd.cpbRemovalDelayLengthMinus1, 5, "HRD: cpb_removal_delay_length_minus1");
        CAVLCWriter.writeNBit(writer, (long) hrd.dpbOutputDelayLengthMinus1, 5, "HRD: dpb_output_delay_length_minus1");
        CAVLCWriter.writeNBit(writer, (long) hrd.timeOffsetLength, 5, "HRD: time_offset_length");
    }

    public SeqParameterSet copy() {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        this.write(buf);
        buf.flip();
        return read(buf);
    }

    public int getPicOrderCntType() {
        return this.picOrderCntType;
    }

    public boolean isFieldPicFlag() {
        return this.fieldPicFlag;
    }

    public boolean isDeltaPicOrderAlwaysZeroFlag() {
        return this.deltaPicOrderAlwaysZeroFlag;
    }

    public boolean isMbAdaptiveFrameFieldFlag() {
        return this.mbAdaptiveFrameFieldFlag;
    }

    public boolean isDirect8x8InferenceFlag() {
        return this.direct8x8InferenceFlag;
    }

    public ColorSpace getChromaFormatIdc() {
        return this.chromaFormatIdc;
    }

    public int getLog2MaxFrameNumMinus4() {
        return this.log2MaxFrameNumMinus4;
    }

    public int getLog2MaxPicOrderCntLsbMinus4() {
        return this.log2MaxPicOrderCntLsbMinus4;
    }

    public int getPicHeightInMapUnitsMinus1() {
        return this.picHeightInMapUnitsMinus1;
    }

    public int getPicWidthInMbsMinus1() {
        return this.picWidthInMbsMinus1;
    }

    public int getBitDepthLumaMinus8() {
        return this.bitDepthLumaMinus8;
    }

    public int getBitDepthChromaMinus8() {
        return this.bitDepthChromaMinus8;
    }

    public boolean isQpprimeYZeroTransformBypassFlag() {
        return this.qpprimeYZeroTransformBypassFlag;
    }

    public int getProfileIdc() {
        return this.profileIdc;
    }

    public boolean isConstraintSet0Flag() {
        return this.constraintSet0Flag;
    }

    public boolean isConstraintSet1Flag() {
        return this.constraintSet1Flag;
    }

    public boolean isConstraintSet2Flag() {
        return this.constraintSet2Flag;
    }

    public boolean isConstraintSet3Flag() {
        return this.constraintSet3Flag;
    }

    public boolean isConstraintSet4Flag() {
        return this.constraintSet4Flag;
    }

    public boolean isConstraintSet5Flag() {
        return this.constraintSet5Flag;
    }

    public int getLevelIdc() {
        return this.levelIdc;
    }

    public int getSeqParameterSetId() {
        return this.seqParameterSetId;
    }

    public boolean isResidualColorTransformFlag() {
        return this.separateColourPlaneFlag;
    }

    public int getOffsetForNonRefPic() {
        return this.offsetForNonRefPic;
    }

    public int getOffsetForTopToBottomField() {
        return this.offsetForTopToBottomField;
    }

    public int getNumRefFrames() {
        return this.numRefFrames;
    }

    public boolean isGapsInFrameNumValueAllowedFlag() {
        return this.gapsInFrameNumValueAllowedFlag;
    }

    public boolean isFrameMbsOnlyFlag() {
        return this.frameMbsOnlyFlag;
    }

    public boolean isFrameCroppingFlag() {
        return this.frameCroppingFlag;
    }

    public int getFrameCropLeftOffset() {
        return this.frameCropLeftOffset;
    }

    public int getFrameCropRightOffset() {
        return this.frameCropRightOffset;
    }

    public int getFrameCropTopOffset() {
        return this.frameCropTopOffset;
    }

    public int getFrameCropBottomOffset() {
        return this.frameCropBottomOffset;
    }

    public int[] getOffsetForRefFrame() {
        return this.offsetForRefFrame;
    }

    public VUIParameters getVuiParams() {
        return this.vuiParams;
    }

    public int[][] getScalingMatrix() {
        return this.scalingMatrix;
    }

    public int getNumRefFramesInPicOrderCntCycle() {
        return this.numRefFramesInPicOrderCntCycle;
    }

    public static int getPicHeightInMbs(SeqParameterSet sps) {
        return sps.picHeightInMapUnitsMinus1 + 1 << (sps.frameMbsOnlyFlag ? 0 : 1);
    }
}