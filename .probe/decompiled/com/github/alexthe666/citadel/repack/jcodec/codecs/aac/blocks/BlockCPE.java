package com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;

public class BlockCPE extends BlockICS {

    private int[] ms_mask;

    @Override
    public void parse(BitReader _in) {
        int common_window = _in.read1Bit();
        if (common_window != 0) {
            this.parseICSInfo(_in);
            int ms_present = _in.readNBit(2);
            if (ms_present == 3) {
                throw new RuntimeException("ms_present = 3 is reserved.");
            }
            if (ms_present != 0) {
                this.decodeMidSideStereo(_in, ms_present, 0, 0);
            }
        }
        BlockICS ics1 = new BlockICS();
        ics1.parse(_in);
        BlockICS ics2 = new BlockICS();
        ics2.parse(_in);
    }

    private void decodeMidSideStereo(BitReader _in, int ms_present, int numWindowGroups, int maxSfb) {
        if (ms_present == 1) {
            for (int idx = 0; idx < numWindowGroups * maxSfb; idx++) {
                this.ms_mask[idx] = _in.read1Bit();
            }
        }
    }
}