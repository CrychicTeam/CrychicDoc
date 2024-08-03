package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CAVLCReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class PictureParameterSet {

    public boolean entropyCodingModeFlag;

    public int[] numRefIdxActiveMinus1 = new int[2];

    public int sliceGroupChangeRateMinus1;

    public int picParameterSetId;

    public int seqParameterSetId;

    public boolean picOrderPresentFlag;

    public int numSliceGroupsMinus1;

    public int sliceGroupMapType;

    public boolean weightedPredFlag;

    public int weightedBipredIdc;

    public int picInitQpMinus26;

    public int picInitQsMinus26;

    public int chromaQpIndexOffset;

    public boolean deblockingFilterControlPresentFlag;

    public boolean constrainedIntraPredFlag;

    public boolean redundantPicCntPresentFlag;

    public int[] topLeft;

    public int[] bottomRight;

    public int[] runLengthMinus1;

    public boolean sliceGroupChangeDirectionFlag;

    public int[] sliceGroupId;

    public PictureParameterSet.PPSExt extended;

    public static PictureParameterSet read(ByteBuffer is) {
        BitReader _in = BitReader.createBitReader(is);
        PictureParameterSet pps = new PictureParameterSet();
        pps.picParameterSetId = CAVLCReader.readUEtrace(_in, "PPS: pic_parameter_set_id");
        pps.seqParameterSetId = CAVLCReader.readUEtrace(_in, "PPS: seq_parameter_set_id");
        pps.entropyCodingModeFlag = CAVLCReader.readBool(_in, "PPS: entropy_coding_mode_flag");
        pps.picOrderPresentFlag = CAVLCReader.readBool(_in, "PPS: pic_order_present_flag");
        pps.numSliceGroupsMinus1 = CAVLCReader.readUEtrace(_in, "PPS: num_slice_groups_minus1");
        if (pps.numSliceGroupsMinus1 > 0) {
            pps.sliceGroupMapType = CAVLCReader.readUEtrace(_in, "PPS: slice_group_map_type");
            pps.topLeft = new int[pps.numSliceGroupsMinus1 + 1];
            pps.bottomRight = new int[pps.numSliceGroupsMinus1 + 1];
            pps.runLengthMinus1 = new int[pps.numSliceGroupsMinus1 + 1];
            if (pps.sliceGroupMapType == 0) {
                for (int iGroup = 0; iGroup <= pps.numSliceGroupsMinus1; iGroup++) {
                    pps.runLengthMinus1[iGroup] = CAVLCReader.readUEtrace(_in, "PPS: run_length_minus1");
                }
            } else if (pps.sliceGroupMapType == 2) {
                for (int iGroup = 0; iGroup < pps.numSliceGroupsMinus1; iGroup++) {
                    pps.topLeft[iGroup] = CAVLCReader.readUEtrace(_in, "PPS: top_left");
                    pps.bottomRight[iGroup] = CAVLCReader.readUEtrace(_in, "PPS: bottom_right");
                }
            } else if (pps.sliceGroupMapType == 3 || pps.sliceGroupMapType == 4 || pps.sliceGroupMapType == 5) {
                pps.sliceGroupChangeDirectionFlag = CAVLCReader.readBool(_in, "PPS: slice_group_change_direction_flag");
                pps.sliceGroupChangeRateMinus1 = CAVLCReader.readUEtrace(_in, "PPS: slice_group_change_rate_minus1");
            } else if (pps.sliceGroupMapType == 6) {
                int NumberBitsPerSliceGroupId;
                if (pps.numSliceGroupsMinus1 + 1 > 4) {
                    NumberBitsPerSliceGroupId = 3;
                } else if (pps.numSliceGroupsMinus1 + 1 > 2) {
                    NumberBitsPerSliceGroupId = 2;
                } else {
                    NumberBitsPerSliceGroupId = 1;
                }
                int pic_size_in_map_units_minus1 = CAVLCReader.readUEtrace(_in, "PPS: pic_size_in_map_units_minus1");
                pps.sliceGroupId = new int[pic_size_in_map_units_minus1 + 1];
                for (int i = 0; i <= pic_size_in_map_units_minus1; i++) {
                    pps.sliceGroupId[i] = CAVLCReader.readU(_in, NumberBitsPerSliceGroupId, "PPS: slice_group_id [" + i + "]f");
                }
            }
        }
        pps.numRefIdxActiveMinus1 = new int[] { CAVLCReader.readUEtrace(_in, "PPS: num_ref_idx_l0_active_minus1"), CAVLCReader.readUEtrace(_in, "PPS: num_ref_idx_l1_active_minus1") };
        pps.weightedPredFlag = CAVLCReader.readBool(_in, "PPS: weighted_pred_flag");
        pps.weightedBipredIdc = CAVLCReader.readNBit(_in, 2, "PPS: weighted_bipred_idc");
        pps.picInitQpMinus26 = CAVLCReader.readSE(_in, "PPS: pic_init_qp_minus26");
        pps.picInitQsMinus26 = CAVLCReader.readSE(_in, "PPS: pic_init_qs_minus26");
        pps.chromaQpIndexOffset = CAVLCReader.readSE(_in, "PPS: chroma_qp_index_offset");
        pps.deblockingFilterControlPresentFlag = CAVLCReader.readBool(_in, "PPS: deblocking_filter_control_present_flag");
        pps.constrainedIntraPredFlag = CAVLCReader.readBool(_in, "PPS: constrained_intra_pred_flag");
        pps.redundantPicCntPresentFlag = CAVLCReader.readBool(_in, "PPS: redundant_pic_cnt_present_flag");
        if (CAVLCReader.moreRBSPData(_in)) {
            pps.extended = new PictureParameterSet.PPSExt();
            pps.extended.transform8x8ModeFlag = CAVLCReader.readBool(_in, "PPS: transform_8x8_mode_flag");
            boolean pic_scaling_matrix_present_flag = CAVLCReader.readBool(_in, "PPS: pic_scaling_matrix_present_flag");
            if (pic_scaling_matrix_present_flag) {
                pps.extended.scalingMatrix = new int[8][];
                for (int i = 0; i < 6 + 2 * (pps.extended.transform8x8ModeFlag ? 1 : 0); i++) {
                    int scalingListSize = i < 6 ? 16 : 64;
                    if (CAVLCReader.readBool(_in, "PPS: pic_scaling_list_present_flag")) {
                        pps.extended.scalingMatrix[i] = SeqParameterSet.readScalingList(_in, scalingListSize);
                    }
                }
            }
            pps.extended.secondChromaQpIndexOffset = CAVLCReader.readSE(_in, "PPS: second_chroma_qp_index_offset");
        }
        return pps;
    }

    public void write(ByteBuffer out) {
        BitWriter writer = new BitWriter(out);
        CAVLCWriter.writeUEtrace(writer, this.picParameterSetId, "PPS: pic_parameter_set_id");
        CAVLCWriter.writeUEtrace(writer, this.seqParameterSetId, "PPS: seq_parameter_set_id");
        CAVLCWriter.writeBool(writer, this.entropyCodingModeFlag, "PPS: entropy_coding_mode_flag");
        CAVLCWriter.writeBool(writer, this.picOrderPresentFlag, "PPS: pic_order_present_flag");
        CAVLCWriter.writeUEtrace(writer, this.numSliceGroupsMinus1, "PPS: num_slice_groups_minus1");
        if (this.numSliceGroupsMinus1 > 0) {
            CAVLCWriter.writeUEtrace(writer, this.sliceGroupMapType, "PPS: slice_group_map_type");
            if (this.sliceGroupMapType == 0) {
                for (int iGroup = 0; iGroup <= this.numSliceGroupsMinus1; iGroup++) {
                    CAVLCWriter.writeUEtrace(writer, this.runLengthMinus1[iGroup], "PPS: ");
                }
            } else if (this.sliceGroupMapType == 2) {
                for (int iGroup = 0; iGroup < this.numSliceGroupsMinus1; iGroup++) {
                    CAVLCWriter.writeUEtrace(writer, this.topLeft[iGroup], "PPS: ");
                    CAVLCWriter.writeUEtrace(writer, this.bottomRight[iGroup], "PPS: ");
                }
            } else if (this.sliceGroupMapType == 3 || this.sliceGroupMapType == 4 || this.sliceGroupMapType == 5) {
                CAVLCWriter.writeBool(writer, this.sliceGroupChangeDirectionFlag, "PPS: slice_group_change_direction_flag");
                CAVLCWriter.writeUEtrace(writer, this.sliceGroupChangeRateMinus1, "PPS: slice_group_change_rate_minus1");
            } else if (this.sliceGroupMapType == 6) {
                int NumberBitsPerSliceGroupId;
                if (this.numSliceGroupsMinus1 + 1 > 4) {
                    NumberBitsPerSliceGroupId = 3;
                } else if (this.numSliceGroupsMinus1 + 1 > 2) {
                    NumberBitsPerSliceGroupId = 2;
                } else {
                    NumberBitsPerSliceGroupId = 1;
                }
                CAVLCWriter.writeUEtrace(writer, this.sliceGroupId.length, "PPS: ");
                for (int i = 0; i <= this.sliceGroupId.length; i++) {
                    CAVLCWriter.writeU(writer, this.sliceGroupId[i], NumberBitsPerSliceGroupId);
                }
            }
        }
        CAVLCWriter.writeUEtrace(writer, this.numRefIdxActiveMinus1[0], "PPS: num_ref_idx_l0_active_minus1");
        CAVLCWriter.writeUEtrace(writer, this.numRefIdxActiveMinus1[1], "PPS: num_ref_idx_l1_active_minus1");
        CAVLCWriter.writeBool(writer, this.weightedPredFlag, "PPS: weighted_pred_flag");
        CAVLCWriter.writeNBit(writer, (long) this.weightedBipredIdc, 2, "PPS: weighted_bipred_idc");
        CAVLCWriter.writeSEtrace(writer, this.picInitQpMinus26, "PPS: pic_init_qp_minus26");
        CAVLCWriter.writeSEtrace(writer, this.picInitQsMinus26, "PPS: pic_init_qs_minus26");
        CAVLCWriter.writeSEtrace(writer, this.chromaQpIndexOffset, "PPS: chroma_qp_index_offset");
        CAVLCWriter.writeBool(writer, this.deblockingFilterControlPresentFlag, "PPS: deblocking_filter_control_present_flag");
        CAVLCWriter.writeBool(writer, this.constrainedIntraPredFlag, "PPS: constrained_intra_pred_flag");
        CAVLCWriter.writeBool(writer, this.redundantPicCntPresentFlag, "PPS: redundant_pic_cnt_present_flag");
        if (this.extended != null) {
            CAVLCWriter.writeBool(writer, this.extended.transform8x8ModeFlag, "PPS: transform_8x8_mode_flag");
            CAVLCWriter.writeBool(writer, this.extended.scalingMatrix != null, "PPS: scalindMatrix");
            if (this.extended.scalingMatrix != null) {
                for (int i = 0; i < 6 + 2 * (this.extended.transform8x8ModeFlag ? 1 : 0); i++) {
                    CAVLCWriter.writeBool(writer, this.extended.scalingMatrix[i] != null, "PPS: ");
                    if (this.extended.scalingMatrix[i] != null) {
                        SeqParameterSet.writeScalingList(writer, this.extended.scalingMatrix, i);
                    }
                }
            }
            CAVLCWriter.writeSEtrace(writer, this.extended.secondChromaQpIndexOffset, "PPS: ");
        }
        CAVLCWriter.writeTrailingBits(writer);
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + Arrays.hashCode(this.bottomRight);
        result = 31 * result + this.chromaQpIndexOffset;
        result = 31 * result + (this.constrainedIntraPredFlag ? 1231 : 1237);
        result = 31 * result + (this.deblockingFilterControlPresentFlag ? 1231 : 1237);
        result = 31 * result + (this.entropyCodingModeFlag ? 1231 : 1237);
        result = 31 * result + (this.extended == null ? 0 : this.extended.hashCode());
        result = 31 * result + this.numRefIdxActiveMinus1[0];
        result = 31 * result + this.numRefIdxActiveMinus1[1];
        result = 31 * result + this.numSliceGroupsMinus1;
        result = 31 * result + this.picInitQpMinus26;
        result = 31 * result + this.picInitQsMinus26;
        result = 31 * result + (this.picOrderPresentFlag ? 1231 : 1237);
        result = 31 * result + this.picParameterSetId;
        result = 31 * result + (this.redundantPicCntPresentFlag ? 1231 : 1237);
        result = 31 * result + Arrays.hashCode(this.runLengthMinus1);
        result = 31 * result + this.seqParameterSetId;
        result = 31 * result + (this.sliceGroupChangeDirectionFlag ? 1231 : 1237);
        result = 31 * result + this.sliceGroupChangeRateMinus1;
        result = 31 * result + Arrays.hashCode(this.sliceGroupId);
        result = 31 * result + this.sliceGroupMapType;
        result = 31 * result + Arrays.hashCode(this.topLeft);
        result = 31 * result + this.weightedBipredIdc;
        return 31 * result + (this.weightedPredFlag ? 1231 : 1237);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            PictureParameterSet other = (PictureParameterSet) obj;
            if (!Platform.arrayEqualsInt(this.bottomRight, other.bottomRight)) {
                return false;
            } else if (this.chromaQpIndexOffset != other.chromaQpIndexOffset) {
                return false;
            } else if (this.constrainedIntraPredFlag != other.constrainedIntraPredFlag) {
                return false;
            } else if (this.deblockingFilterControlPresentFlag != other.deblockingFilterControlPresentFlag) {
                return false;
            } else if (this.entropyCodingModeFlag != other.entropyCodingModeFlag) {
                return false;
            } else {
                if (this.extended == null) {
                    if (other.extended != null) {
                        return false;
                    }
                } else if (!this.extended.equals(other.extended)) {
                    return false;
                }
                if (this.numRefIdxActiveMinus1[0] != other.numRefIdxActiveMinus1[0]) {
                    return false;
                } else if (this.numRefIdxActiveMinus1[1] != other.numRefIdxActiveMinus1[1]) {
                    return false;
                } else if (this.numSliceGroupsMinus1 != other.numSliceGroupsMinus1) {
                    return false;
                } else if (this.picInitQpMinus26 != other.picInitQpMinus26) {
                    return false;
                } else if (this.picInitQsMinus26 != other.picInitQsMinus26) {
                    return false;
                } else if (this.picOrderPresentFlag != other.picOrderPresentFlag) {
                    return false;
                } else if (this.picParameterSetId != other.picParameterSetId) {
                    return false;
                } else if (this.redundantPicCntPresentFlag != other.redundantPicCntPresentFlag) {
                    return false;
                } else if (!Platform.arrayEqualsInt(this.runLengthMinus1, other.runLengthMinus1)) {
                    return false;
                } else if (this.seqParameterSetId != other.seqParameterSetId) {
                    return false;
                } else if (this.sliceGroupChangeDirectionFlag != other.sliceGroupChangeDirectionFlag) {
                    return false;
                } else if (this.sliceGroupChangeRateMinus1 != other.sliceGroupChangeRateMinus1) {
                    return false;
                } else if (!Platform.arrayEqualsInt(this.sliceGroupId, other.sliceGroupId)) {
                    return false;
                } else if (this.sliceGroupMapType != other.sliceGroupMapType) {
                    return false;
                } else if (!Platform.arrayEqualsInt(this.topLeft, other.topLeft)) {
                    return false;
                } else {
                    return this.weightedBipredIdc != other.weightedBipredIdc ? false : this.weightedPredFlag == other.weightedPredFlag;
                }
            }
        }
    }

    public PictureParameterSet copy() {
        ByteBuffer buf = ByteBuffer.allocate(2048);
        this.write(buf);
        buf.flip();
        return read(buf);
    }

    public boolean isEntropyCodingModeFlag() {
        return this.entropyCodingModeFlag;
    }

    public int[] getNumRefIdxActiveMinus1() {
        return this.numRefIdxActiveMinus1;
    }

    public int getSliceGroupChangeRateMinus1() {
        return this.sliceGroupChangeRateMinus1;
    }

    public int getPicParameterSetId() {
        return this.picParameterSetId;
    }

    public int getSeqParameterSetId() {
        return this.seqParameterSetId;
    }

    public boolean isPicOrderPresentFlag() {
        return this.picOrderPresentFlag;
    }

    public int getNumSliceGroupsMinus1() {
        return this.numSliceGroupsMinus1;
    }

    public int getSliceGroupMapType() {
        return this.sliceGroupMapType;
    }

    public boolean isWeightedPredFlag() {
        return this.weightedPredFlag;
    }

    public int getWeightedBipredIdc() {
        return this.weightedBipredIdc;
    }

    public int getPicInitQpMinus26() {
        return this.picInitQpMinus26;
    }

    public int getPicInitQsMinus26() {
        return this.picInitQsMinus26;
    }

    public int getChromaQpIndexOffset() {
        return this.chromaQpIndexOffset;
    }

    public boolean isDeblockingFilterControlPresentFlag() {
        return this.deblockingFilterControlPresentFlag;
    }

    public boolean isConstrainedIntraPredFlag() {
        return this.constrainedIntraPredFlag;
    }

    public boolean isRedundantPicCntPresentFlag() {
        return this.redundantPicCntPresentFlag;
    }

    public int[] getTopLeft() {
        return this.topLeft;
    }

    public int[] getBottomRight() {
        return this.bottomRight;
    }

    public int[] getRunLengthMinus1() {
        return this.runLengthMinus1;
    }

    public boolean isSliceGroupChangeDirectionFlag() {
        return this.sliceGroupChangeDirectionFlag;
    }

    public int[] getSliceGroupId() {
        return this.sliceGroupId;
    }

    public PictureParameterSet.PPSExt getExtended() {
        return this.extended;
    }

    public static class PPSExt {

        public boolean transform8x8ModeFlag;

        public int[][] scalingMatrix;

        public int secondChromaQpIndexOffset;

        public boolean isTransform8x8ModeFlag() {
            return this.transform8x8ModeFlag;
        }

        public int[][] getScalingMatrix() {
            return this.scalingMatrix;
        }

        public int getSecondChromaQpIndexOffset() {
            return this.secondChromaQpIndexOffset;
        }
    }
}