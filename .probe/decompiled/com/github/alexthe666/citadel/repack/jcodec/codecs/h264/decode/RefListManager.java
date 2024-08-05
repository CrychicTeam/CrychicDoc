package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.IntObjectMap;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.util.Arrays;
import java.util.Comparator;

public class RefListManager {

    private SliceHeader sh;

    private int[] numRef;

    private Frame[] sRefs;

    private IntObjectMap<Frame> lRefs;

    private Frame frameOut;

    public RefListManager(SliceHeader sh, Frame[] sRefs, IntObjectMap<Frame> lRefs, Frame frameOut) {
        this.sh = sh;
        this.sRefs = sRefs;
        this.lRefs = lRefs;
        if (sh.numRefIdxActiveOverrideFlag) {
            this.numRef = new int[] { sh.numRefIdxActiveMinus1[0] + 1, sh.numRefIdxActiveMinus1[1] + 1 };
        } else {
            this.numRef = new int[] { sh.pps.numRefIdxActiveMinus1[0] + 1, sh.pps.numRefIdxActiveMinus1[1] + 1 };
        }
        this.frameOut = frameOut;
    }

    public Frame[][] getRefList() {
        Frame[][] refList = (Frame[][]) null;
        if (this.sh.sliceType == SliceType.P) {
            refList = new Frame[][] { this.buildRefListP(), null };
        } else if (this.sh.sliceType == SliceType.B) {
            refList = this.buildRefListB();
        }
        MBlockDecoderUtils.debugPrint("------");
        if (refList != null) {
            for (int l = 0; l < 2; l++) {
                if (refList[l] != null) {
                    for (int i = 0; i < refList[l].length; i++) {
                        if (refList[l][i] != null) {
                            MBlockDecoderUtils.debugPrint("REF[%d][%d]: ", l, i, refList[l][i].getPOC());
                        }
                    }
                }
            }
        }
        return refList;
    }

    private Frame[] buildRefListP() {
        int frame_num = this.sh.frameNum;
        int maxFrames = 1 << this.sh.sps.log2MaxFrameNumMinus4 + 4;
        Frame[] result = new Frame[this.numRef[0]];
        int refs = 0;
        for (int i = frame_num - 1; i >= frame_num - maxFrames && refs < this.numRef[0]; i--) {
            int fn = i < 0 ? i + maxFrames : i;
            if (this.sRefs[fn] != null) {
                result[refs] = this.sRefs[fn] == H264Const.NO_PIC ? null : this.sRefs[fn];
                refs++;
            }
        }
        int[] keys = this.lRefs.keys();
        Arrays.sort(keys);
        for (int ix = 0; ix < keys.length && refs < this.numRef[0]; ix++) {
            result[refs++] = this.lRefs.get(keys[ix]);
        }
        this.reorder(result, 0);
        return result;
    }

    private Frame[][] buildRefListB() {
        Frame[] l0 = this.buildList(Frame.POCDesc, Frame.POCAsc);
        Frame[] l1 = this.buildList(Frame.POCAsc, Frame.POCDesc);
        if (Platform.arrayEqualsObj(l0, l1) && this.count(l1) > 1) {
            Frame frame = l1[1];
            l1[1] = l1[0];
            l1[0] = frame;
        }
        Frame[][] result = new Frame[][] { Platform.copyOfObj(l0, this.numRef[0]), Platform.copyOfObj(l1, this.numRef[1]) };
        this.reorder(result[0], 0);
        this.reorder(result[1], 1);
        return result;
    }

    private Frame[] buildList(Comparator<Frame> cmpFwd, Comparator<Frame> cmpInv) {
        Frame[] refs = new Frame[this.sRefs.length + this.lRefs.size()];
        Frame[] fwd = this.copySort(cmpFwd, this.frameOut);
        Frame[] inv = this.copySort(cmpInv, this.frameOut);
        int nFwd = this.count(fwd);
        int nInv = this.count(inv);
        int ref = 0;
        for (int i = 0; i < nFwd; ref++) {
            refs[ref] = fwd[i];
            i++;
        }
        for (int i = 0; i < nInv; ref++) {
            refs[ref] = inv[i];
            i++;
        }
        int[] keys = this.lRefs.keys();
        Arrays.sort(keys);
        for (int i = 0; i < keys.length; ref++) {
            refs[ref] = this.lRefs.get(keys[i]);
            i++;
        }
        return refs;
    }

    private int count(Frame[] arr) {
        for (int nn = 0; nn < arr.length; nn++) {
            if (arr[nn] == null) {
                return nn;
            }
        }
        return arr.length;
    }

    private Frame[] copySort(Comparator<Frame> fwd, Frame dummy) {
        Frame[] copyOf = Platform.copyOfObj(this.sRefs, this.sRefs.length);
        for (int i = 0; i < copyOf.length; i++) {
            if (fwd.compare(dummy, copyOf[i]) > 0) {
                copyOf[i] = null;
            }
        }
        Arrays.sort(copyOf, fwd);
        return copyOf;
    }

    private void reorder(Picture[] result, int list) {
        if (this.sh.refPicReordering[list] != null) {
            int predict = this.sh.frameNum;
            int maxFrames = 1 << this.sh.sps.log2MaxFrameNumMinus4 + 4;
            for (int ind = 0; ind < this.sh.refPicReordering[list][0].length; ind++) {
                switch(this.sh.refPicReordering[list][0][ind]) {
                    case 0:
                        predict = MathUtil.wrap(predict - this.sh.refPicReordering[list][1][ind] - 1, maxFrames);
                        break;
                    case 1:
                        predict = MathUtil.wrap(predict + this.sh.refPicReordering[list][1][ind] + 1, maxFrames);
                        break;
                    case 2:
                        throw new RuntimeException("long term");
                }
                for (int i = this.numRef[list] - 1; i > ind; i--) {
                    result[i] = result[i - 1];
                }
                result[ind] = this.sRefs[predict];
                int i = ind + 1;
                for (int j = i; i < this.numRef[list] && result[i] != null; i++) {
                    if (result[i] != this.sRefs[predict]) {
                        result[j++] = result[i];
                    }
                }
            }
        }
    }
}