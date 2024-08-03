package com.github.alexthe666.citadel.repack.jaad.mp4.api.codec;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.EAC3SpecificBox;
import java.util.ArrayList;
import java.util.List;

public class EAC3DecoderInfo extends DecoderInfo {

    private EAC3SpecificBox box;

    private EAC3DecoderInfo.IndependentSubstream[] is;

    public EAC3DecoderInfo(CodecSpecificBox box) {
        this.box = (EAC3SpecificBox) box;
        this.is = new EAC3DecoderInfo.IndependentSubstream[this.box.getIndependentSubstreamCount()];
        for (int i = 0; i < this.is.length; i++) {
            this.is[i] = new EAC3DecoderInfo.IndependentSubstream(i);
        }
    }

    public int getDataRate() {
        return this.box.getDataRate();
    }

    public EAC3DecoderInfo.IndependentSubstream[] getIndependentSubstreams() {
        return this.is;
    }

    public static enum DependentSubstream {

        LC_RC_PAIR,
        LRS_RRS_PAIR,
        CS,
        TS,
        LSD_RSD_PAIR,
        LW_RW_PAIR,
        LVH_RVH_PAIR,
        CVH,
        LFE2
    }

    public class IndependentSubstream {

        private final int index;

        private final EAC3DecoderInfo.DependentSubstream[] dependentSubstreams;

        private IndependentSubstream(int index) {
            this.index = index;
            int loc = EAC3DecoderInfo.this.box.getDependentSubstreamLocation()[index];
            List<EAC3DecoderInfo.DependentSubstream> list = new ArrayList();
            for (int i = 0; i < 9; i++) {
                if ((loc >> 8 - i & 1) == 1) {
                    list.add(EAC3DecoderInfo.DependentSubstream.values()[i]);
                }
            }
            this.dependentSubstreams = (EAC3DecoderInfo.DependentSubstream[]) list.toArray(new EAC3DecoderInfo.DependentSubstream[list.size()]);
        }

        public int getFscod() {
            return EAC3DecoderInfo.this.box.getFscods()[this.index];
        }

        public int getBsid() {
            return EAC3DecoderInfo.this.box.getBsids()[this.index];
        }

        public int getBsmod() {
            return EAC3DecoderInfo.this.box.getBsmods()[this.index];
        }

        public int getAcmod() {
            return EAC3DecoderInfo.this.box.getAcmods()[this.index];
        }

        public boolean isLfeon() {
            return EAC3DecoderInfo.this.box.getLfeons()[this.index];
        }

        public EAC3DecoderInfo.DependentSubstream[] getDependentSubstreams() {
            return this.dependentSubstreams;
        }
    }
}