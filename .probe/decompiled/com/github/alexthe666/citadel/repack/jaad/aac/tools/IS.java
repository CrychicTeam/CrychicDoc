package com.github.alexthe666.citadel.repack.jaad.aac.tools;

import com.github.alexthe666.citadel.repack.jaad.aac.huffman.HCB;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.CPE;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;

public final class IS implements Constants, ISScaleTable, HCB {

    private IS() {
    }

    public static void process(CPE cpe, float[] specL, float[] specR) {
        ICStream ics = cpe.getRightChannel();
        ICSInfo info = ics.getInfo();
        int[] offsets = info.getSWBOffsets();
        int windowGroups = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[] sfbCB = ics.getSfbCB();
        int[] sectEnd = ics.getSectEnd();
        float[] scaleFactors = ics.getScaleFactors();
        int idx = 0;
        int groupOff = 0;
        for (int g = 0; g < windowGroups; g++) {
            int i = 0;
            while (i < maxSFB) {
                if (sfbCB[idx] != 15 && sfbCB[idx] != 14) {
                    int end = sectEnd[idx];
                    idx += end - i;
                    i = end;
                } else {
                    for (int end = sectEnd[idx]; i < end; idx++) {
                        int c = sfbCB[idx] == 15 ? 1 : -1;
                        if (cpe.isMSMaskPresent()) {
                            c *= cpe.isMSUsed(idx) ? -1 : 1;
                        }
                        float scale = (float) c * scaleFactors[idx];
                        for (int w = 0; w < info.getWindowGroupLength(g); w++) {
                            int off = groupOff + w * 128 + offsets[i];
                            for (int j = 0; j < offsets[i + 1] - offsets[i]; j++) {
                                specR[off + j] = specL[off + j] * scale;
                            }
                        }
                        i++;
                    }
                }
            }
            groupOff += info.getWindowGroupLength(g) * 128;
        }
    }
}