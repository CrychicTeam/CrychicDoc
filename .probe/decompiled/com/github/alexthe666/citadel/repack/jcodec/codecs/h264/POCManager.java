package com.github.alexthe666.citadel.repack.jcodec.codecs.h264;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnitType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.RefPicMarking;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;

public class POCManager {

    private int prevPOCMsb;

    private int prevPOCLsb;

    public int calcPOC(SliceHeader firstSliceHeader, NALUnit firstNu) {
        switch(firstSliceHeader.sps.picOrderCntType) {
            case 0:
                return this.calcPOC0(firstSliceHeader, firstNu);
            case 1:
                return this.calcPOC1(firstSliceHeader, firstNu);
            case 2:
                return this.calcPOC2(firstSliceHeader, firstNu);
            default:
                throw new RuntimeException("POC no!!!");
        }
    }

    private int calcPOC2(SliceHeader firstSliceHeader, NALUnit firstNu) {
        return firstSliceHeader.frameNum << 1;
    }

    private int calcPOC1(SliceHeader firstSliceHeader, NALUnit firstNu) {
        return firstSliceHeader.frameNum << 1;
    }

    private int calcPOC0(SliceHeader firstSliceHeader, NALUnit firstNu) {
        if (firstNu.type == NALUnitType.IDR_SLICE) {
            this.prevPOCMsb = this.prevPOCLsb = 0;
        }
        int maxPOCLsbDiv2 = 1 << firstSliceHeader.sps.log2MaxPicOrderCntLsbMinus4 + 3;
        int maxPOCLsb = maxPOCLsbDiv2 << 1;
        int POCLsb = firstSliceHeader.picOrderCntLsb;
        int POCMsb;
        if (POCLsb < this.prevPOCLsb && this.prevPOCLsb - POCLsb >= maxPOCLsbDiv2) {
            POCMsb = this.prevPOCMsb + maxPOCLsb;
        } else if (POCLsb > this.prevPOCLsb && POCLsb - this.prevPOCLsb > maxPOCLsbDiv2) {
            POCMsb = this.prevPOCMsb - maxPOCLsb;
        } else {
            POCMsb = this.prevPOCMsb;
        }
        int POC = POCMsb + POCLsb;
        if (firstNu.nal_ref_idc > 0) {
            if (this.hasMMCO5(firstSliceHeader, firstNu)) {
                this.prevPOCMsb = 0;
                this.prevPOCLsb = POC;
            } else {
                this.prevPOCMsb = POCMsb;
                this.prevPOCLsb = POCLsb;
            }
        }
        return POC;
    }

    private boolean hasMMCO5(SliceHeader firstSliceHeader, NALUnit firstNu) {
        if (firstNu.type != NALUnitType.IDR_SLICE && firstSliceHeader.refPicMarkingNonIDR != null) {
            RefPicMarking.Instruction[] instructions = firstSliceHeader.refPicMarkingNonIDR.getInstructions();
            for (int i = 0; i < instructions.length; i++) {
                RefPicMarking.Instruction instruction = instructions[i];
                if (instruction.getType() == RefPicMarking.InstrType.CLEAR) {
                    return true;
                }
            }
        }
        return false;
    }
}