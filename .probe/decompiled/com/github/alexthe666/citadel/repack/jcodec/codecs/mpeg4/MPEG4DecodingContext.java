package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MPEG4DecodingContext {

    public int width;

    public int height;

    public int horiz_mc_ref;

    public int vert_mc_ref;

    public short[] intraMpegQuantMatrix;

    public short[] interMpegQuantMatrix;

    public int[][] gmcWarps;

    public int mbWidth;

    public int mbHeight;

    public int spriteEnable;

    public int shape;

    public int quant;

    public int quantBits;

    public int timeIncrementBits;

    public int intraDCThreshold;

    public int spriteWarpingPoints;

    public boolean reducedResolutionEnable;

    public int fcodeForward;

    public int fcodeBackward;

    public boolean newPredEnable;

    public boolean rounding;

    public boolean quarterPel;

    public boolean cartoonMode;

    public int lastTimeBase;

    public int timeBase;

    public int time;

    public int lastNonBTime;

    public int pframeTs;

    public int bframeTs;

    public boolean topFieldFirst;

    public boolean alternateVerticalScan;

    int volVersionId;

    int timestampMSB;

    int timestampLSB;

    boolean complexityEstimationDisable;

    boolean interlacing;

    boolean spriteBrightnessChange;

    boolean scalability;

    MPEG4DecodingContext.Estimation estimation;

    private static final int VIDOBJ_START_CODE = 256;

    private static final int VIDOBJLAY_START_CODE = 288;

    private static final int VISOBJSEQ_START_CODE = 432;

    private static final int VISOBJSEQ_STOP_CODE = 433;

    private static final int USERDATA_START_CODE = 434;

    private static final int GRPOFVOP_START_CODE = 435;

    private static final int VISOBJ_START_CODE = 437;

    private static final int VISOBJ_TYPE_VIDEO = 1;

    private static final int VIDOBJLAY_AR_EXTPAR = 15;

    private static final int VIDOBJLAY_SHAPE_RECTANGULAR = 0;

    private static final int VIDOBJLAY_SHAPE_BINARY = 1;

    private static final int VIDOBJLAY_SHAPE_BINARY_ONLY = 2;

    private static final int VIDOBJLAY_SHAPE_GRAYSCALE = 3;

    private static final int VOP_START_CODE = 438;

    private static final int VIDOBJ_START_CODE_MASK = 31;

    private static final int VIDOBJLAY_START_CODE_MASK = 15;

    private static final int SPRITE_STATIC = 1;

    private static final int SPRITE_GMC = 2;

    private static final int VLC_CODE = 0;

    private static final int VLC_LEN = 1;

    private int timeIncrementResolution;

    private boolean packedMode;

    public int codingType;

    public boolean quantType;

    public int bsVersion = 65535;

    public MPEG4DecodingContext() {
        this.intraMpegQuantMatrix = new short[64];
        this.interMpegQuantMatrix = new short[64];
        this.gmcWarps = new int[3][2];
        this.estimation = new MPEG4DecodingContext.Estimation();
    }

    public static MPEG4DecodingContext readFromHeaders(ByteBuffer bb) {
        MPEG4DecodingContext ret = new MPEG4DecodingContext();
        return ret.readHeaders(bb) ? ret : null;
    }

    public static void getMatrix(BitReader br, short[] matrix) {
        int value = 0;
        int i = 0;
        int last;
        do {
            last = value;
            value = br.readNBit(8);
            matrix[MPEG4Consts.SCAN_TABLES[0][i++]] = (short) value;
        } while (value != 0 && i < 64);
        i--;
        while (i < 64) {
            matrix[MPEG4Consts.SCAN_TABLES[0][i++]] = (short) last;
        }
    }

    public boolean readHeaders(ByteBuffer bb) {
        bb.order(ByteOrder.BIG_ENDIAN);
        while (bb.remaining() >= 4) {
            int startCode = bb.getInt();
            while ((startCode & -256) != 256 && bb.hasRemaining()) {
                startCode <<= 8;
                startCode |= bb.get() & 255;
            }
            if (startCode == 432) {
                byte var17 = bb.get();
            } else if (startCode != 433) {
                if (startCode == 437) {
                    BitReader br = BitReader.createBitReader(bb);
                    if (br.readBool()) {
                        int verId = br.readNBit(4);
                        br.skip(3);
                    } else {
                        int verId = 1;
                    }
                    int visual_object_type = br.readNBit(4);
                    if (visual_object_type != 1) {
                        return false;
                    }
                    if (br.readBool()) {
                        br.skip(3);
                        br.skip(1);
                        if (br.readBool()) {
                            br.skip(8);
                            br.skip(8);
                            br.skip(8);
                        }
                    }
                    br.terminate();
                } else if ((startCode & -32) != 256) {
                    if ((startCode & -16) == 288) {
                        BitReader brx = BitReader.createBitReader(bb);
                        brx.skip(1);
                        brx.skip(8);
                        if (brx.readBool()) {
                            this.volVersionId = brx.readNBit(4);
                            brx.skip(3);
                        } else {
                            this.volVersionId = 1;
                        }
                        int aspectRatio = brx.readNBit(4);
                        if (aspectRatio == 15) {
                            brx.readNBit(8);
                            brx.readNBit(8);
                        }
                        if (brx.readBool()) {
                            brx.skip(2);
                            boolean lowDelay = brx.readBool();
                            if (brx.readBool()) {
                                int bitrate = brx.readNBit(15) << 15;
                                brx.skip(1);
                                bitrate |= brx.readNBit(15);
                                brx.skip(1);
                                int bufferSize = brx.readNBit(15) << 3;
                                brx.skip(1);
                                bufferSize |= brx.readNBit(3);
                                int occupancy = brx.readNBit(11) << 15;
                                brx.skip(1);
                                occupancy |= brx.readNBit(15);
                                brx.skip(1);
                            }
                        }
                        this.shape = brx.readNBit(2);
                        if (this.shape != 0) {
                        }
                        if (this.shape == 3 && this.volVersionId != 1) {
                            brx.skip(4);
                        }
                        brx.skip(1);
                        this.timeIncrementResolution = brx.readNBit(16);
                        if (this.timeIncrementResolution > 0) {
                            this.timeIncrementBits = Math.max(MathUtil.log2(this.timeIncrementResolution - 1) + 1, 1);
                        } else {
                            this.timeIncrementBits = 1;
                        }
                        brx.skip(1);
                        if (brx.readBool()) {
                            brx.skip(this.timeIncrementBits);
                        }
                        if (this.shape != 2) {
                            if (this.shape == 0) {
                                brx.skip(1);
                                this.width = brx.readNBit(13);
                                brx.skip(1);
                                this.height = brx.readNBit(13);
                                brx.skip(1);
                                this.calcSizes();
                            }
                            this.interlacing = brx.readBool();
                            if (!brx.readBool()) {
                            }
                            this.spriteEnable = brx.readNBit(this.volVersionId == 1 ? 1 : 2);
                            if (this.spriteEnable == 1 || this.spriteEnable == 2) {
                                if (this.spriteEnable != 2) {
                                    brx.readNBit(13);
                                    brx.skip(1);
                                    brx.readNBit(13);
                                    brx.skip(1);
                                    brx.readNBit(13);
                                    brx.skip(1);
                                    brx.readNBit(13);
                                    brx.skip(1);
                                }
                                this.spriteWarpingPoints = brx.readNBit(6);
                                brx.readNBit(2);
                                this.spriteBrightnessChange = brx.readBool();
                                if (this.spriteEnable != 2) {
                                    brx.readNBit(1);
                                }
                            }
                            if (this.volVersionId != 1 && this.shape != 0) {
                                brx.skip(1);
                            }
                            if (brx.readBool()) {
                                this.quantBits = brx.readNBit(4);
                                brx.skip(4);
                            } else {
                                this.quantBits = 5;
                            }
                            if (this.shape == 3) {
                                brx.skip(1);
                                brx.skip(1);
                                brx.skip(1);
                            }
                            this.quantType = brx.readBool();
                            if (this.quantType) {
                                if (brx.readBool()) {
                                    getMatrix(brx, this.intraMpegQuantMatrix);
                                } else {
                                    System.arraycopy(MPEG4Consts.DEFAULT_INTRA_MATRIX, 0, this.intraMpegQuantMatrix, 0, this.intraMpegQuantMatrix.length);
                                }
                                if (brx.readBool()) {
                                    getMatrix(brx, this.interMpegQuantMatrix);
                                } else {
                                    System.arraycopy(MPEG4Consts.DEFAULT_INTER_MATRIX, 0, this.interMpegQuantMatrix, 0, this.interMpegQuantMatrix.length);
                                }
                                if (this.shape == 3) {
                                    return false;
                                }
                            }
                            if (this.volVersionId != 1) {
                                this.quarterPel = brx.readBool();
                            } else {
                                this.quarterPel = false;
                            }
                            this.complexityEstimationDisable = brx.readBool();
                            if (!this.complexityEstimationDisable) {
                                this.readVolComplexityEstimationHeader(brx, this.estimation);
                            }
                            brx.skip(1);
                            if (brx.readBool()) {
                                brx.skip(1);
                            }
                            if (this.volVersionId != 1) {
                                this.newPredEnable = brx.readBool();
                                if (this.newPredEnable) {
                                    brx.skip(2);
                                    brx.skip(1);
                                }
                                this.reducedResolutionEnable = brx.readBool();
                            } else {
                                this.newPredEnable = false;
                                this.reducedResolutionEnable = false;
                            }
                            this.scalability = brx.readBool();
                            if (this.scalability) {
                                brx.skip(1);
                                brx.skip(4);
                                brx.skip(1);
                                brx.skip(5);
                                brx.skip(5);
                                brx.skip(5);
                                brx.skip(5);
                                brx.skip(1);
                                if (this.shape == 1) {
                                    brx.skip(1);
                                    brx.skip(1);
                                    brx.skip(5);
                                    brx.skip(5);
                                    brx.skip(5);
                                    brx.skip(5);
                                }
                                return false;
                            }
                        } else {
                            if (this.volVersionId != 1) {
                                this.scalability = brx.readBool();
                                if (this.scalability) {
                                    brx.skip(4);
                                    brx.skip(5);
                                    brx.skip(5);
                                    brx.skip(5);
                                    brx.skip(5);
                                    return false;
                                }
                            }
                            brx.skip(1);
                        }
                        brx.terminate();
                    } else if (startCode == 435) {
                        BitReader brxx = BitReader.createBitReader(bb);
                        int hours = brxx.readNBit(5);
                        int minutes = brxx.readNBit(6);
                        brxx.skip(1);
                        int seconds = brxx.readNBit(6);
                        brxx.skip(1);
                        brxx.skip(1);
                        brxx.terminate();
                    } else {
                        if (startCode == 438) {
                            return true;
                        }
                        if (startCode != 434) {
                            Logger.debug("Unknown");
                        } else {
                            byte[] tmp = new byte[256];
                            int i = 0;
                            tmp[i++] = bb.get();
                            while ((tmp[i] = bb.get()) != 0) {
                                i++;
                            }
                            bb.position(bb.position() - 1);
                            String userData = new String(tmp, 0, i);
                            if (userData.startsWith("XviD")) {
                                if (tmp[userData.length() - 1] == 67) {
                                    this.bsVersion = Integer.parseInt(userData.substring(4, userData.length() - 1));
                                    this.cartoonMode = true;
                                } else {
                                    this.bsVersion = Integer.parseInt(userData.substring(4));
                                }
                            }
                            if (userData.startsWith("DivX")) {
                                int buildIndex = userData.indexOf("Build");
                                if (buildIndex == -1) {
                                    buildIndex = userData.indexOf("b");
                                }
                                try {
                                    int version = Integer.parseInt(userData.substring(4, buildIndex));
                                    int build = Integer.parseInt(userData.substring(buildIndex + 1, userData.length() - 1));
                                    char packed = userData.charAt(userData.length() - 1);
                                    this.packedMode = packed == 'p';
                                } catch (Exception var11) {
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void calcSizes() {
        this.mbWidth = (this.width + 15) / 16;
        this.mbHeight = (this.height + 15) / 16;
    }

    private void readVolComplexityEstimationHeader(BitReader br, MPEG4DecodingContext.Estimation estimation) {
        estimation.method = br.readNBit(2);
        if (estimation.method == 0 || estimation.method == 1) {
            if (!br.readBool()) {
                estimation.opaque = br.readBool();
                estimation.transparent = br.readBool();
                estimation.intraCae = br.readBool();
                estimation.interCae = br.readBool();
                estimation.noUpdate = br.readBool();
                estimation.upsampling = br.readBool();
            }
            if (!br.readBool()) {
                estimation.intraBlocks = br.readBool();
                estimation.interBlocks = br.readBool();
                estimation.inter4vBlocks = br.readBool();
                estimation.notCodedBlocks = br.readBool();
            }
        }
        br.skip(1);
        if (!br.readBool()) {
            estimation.dctCoefs = br.readBool();
            estimation.dctLines = br.readBool();
            estimation.vlcSymbols = br.readBool();
            estimation.vlcBits = br.readBool();
        }
        if (!br.readBool()) {
            estimation.apm = br.readBool();
            estimation.npm = br.readBool();
            estimation.interpolateMcQ = br.readBool();
            estimation.forwBackMcQ = br.readBool();
            estimation.halfpel2 = br.readBool();
            estimation.halfpel4 = br.readBool();
        }
        br.skip(1);
        if (estimation.method == 1 && !br.readBool()) {
            estimation.sadct = br.readBool();
            estimation.quarterpel = br.readBool();
        }
    }

    public boolean readVOPHeader(BitReader br) {
        this.rounding = false;
        this.quant = 2;
        this.codingType = br.readNBit(2);
        while (br.readBool()) {
            this.timestampMSB++;
        }
        br.skip(1);
        if (this.getTimeIncrementBits() != 0) {
            this.timestampLSB = br.readNBit(this.getTimeIncrementBits());
        }
        br.skip(1);
        if (!br.readBool()) {
            return false;
        } else {
            if (this.newPredEnable) {
                int vopId = br.readNBit(Math.min(this.getTimeIncrementBits() + 3, 15));
                if (br.readBool()) {
                    int i = br.readNBit(Math.min(this.getTimeIncrementBits() + 3, 15));
                }
                br.skip(1);
            }
            if (this.shape != 2 && (this.codingType == 1 || this.codingType == 3 && this.spriteEnable == 2)) {
                this.rounding = br.readBool();
            }
            if (this.reducedResolutionEnable && this.shape == 0 && (this.codingType == 1 || this.codingType == 0) && br.readBool()) {
            }
            if (this.shape != 0) {
                if (this.spriteEnable != 1 || this.codingType != 0) {
                    this.width = br.readNBit(13);
                    br.skip(1);
                    this.height = br.readNBit(13);
                    br.skip(1);
                    this.horiz_mc_ref = br.readNBit(13);
                    br.skip(1);
                    this.vert_mc_ref = br.readNBit(13);
                    br.skip(1);
                    this.calcSizes();
                }
                br.skip(1);
                if (br.readBool()) {
                    br.skip(8);
                }
            }
            MPEG4DecodingContext.Estimation estimation = new MPEG4DecodingContext.Estimation();
            if (this.shape != 2) {
                if (!this.complexityEstimationDisable) {
                    this.readVopComplexityEstimationHeader(br, estimation, this.spriteEnable, this.codingType);
                }
                this.intraDCThreshold = MPEG4Consts.INTRA_DC_THRESHOLD_TABLE[br.readNBit(3)];
                if (this.interlacing) {
                    this.topFieldFirst = br.readBool();
                    this.alternateVerticalScan = br.readBool();
                }
            }
            if ((this.spriteEnable == 1 || this.spriteEnable == 2) && this.codingType == 3) {
                for (int i = 0; i < this.spriteWarpingPoints; i++) {
                    int x = 0;
                    int y = 0;
                    int length = this.getSpriteTrajectory(br);
                    if (length > 0) {
                        x = br.readNBit(length);
                        if (x >> length - 1 == 0) {
                            x = -(x ^ (1 << length) - 1);
                        }
                    }
                    br.skip(1);
                    length = this.getSpriteTrajectory(br);
                    if (length > 0) {
                        y = br.readNBit(length);
                        if (y >> length - 1 == 0) {
                            y = -(y ^ (1 << length) - 1);
                        }
                    }
                    br.skip(1);
                    this.gmcWarps[i][0] = x;
                    this.gmcWarps[i][1] = y;
                }
                if (this.spriteBrightnessChange) {
                }
                if (this.spriteEnable == 1) {
                }
            }
            if ((this.quant = br.readNBit(this.quantBits)) < 1) {
                this.quant = 1;
            }
            if (this.codingType != 0) {
                this.fcodeForward = br.readNBit(3);
            }
            if (this.codingType == 2) {
                this.fcodeBackward = br.readNBit(3);
            }
            if (!this.scalability && this.shape != 0 && this.codingType != 0) {
                br.skip(1);
            }
            if (this.codingType != 2) {
                this.lastTimeBase = this.timeBase;
                this.timeBase = this.timeBase + this.timestampMSB;
                this.time = this.timeBase * this.getTimeIncrementResolution() + this.timestampLSB;
                this.pframeTs = this.time - this.lastNonBTime;
                this.lastNonBTime = this.time;
            } else {
                this.time = (this.lastTimeBase + this.timestampMSB) * this.getTimeIncrementResolution() + this.timestampLSB;
                this.bframeTs = this.pframeTs - (this.lastNonBTime - this.time);
            }
            return true;
        }
    }

    private int getSpriteTrajectory(BitReader br) {
        for (int i = 0; i < 12; i++) {
            if (br.checkNBit(MPEG4Consts.SPRITE_TRAJECTORY_LEN[i][1]) == MPEG4Consts.SPRITE_TRAJECTORY_LEN[i][0]) {
                br.skip(MPEG4Consts.SPRITE_TRAJECTORY_LEN[i][1]);
                return i;
            }
        }
        return -1;
    }

    private void readVopComplexityEstimationHeader(BitReader br, MPEG4DecodingContext.Estimation estimation, int spriteEnable, int codingType) {
        if (estimation.method == 0 || estimation.method == 1) {
            if (codingType == 0) {
                if (estimation.opaque) {
                    br.skip(8);
                }
                if (estimation.transparent) {
                    br.skip(8);
                }
                if (estimation.intraCae) {
                    br.skip(8);
                }
                if (estimation.interCae) {
                    br.skip(8);
                }
                if (estimation.noUpdate) {
                    br.skip(8);
                }
                if (estimation.upsampling) {
                    br.skip(8);
                }
                if (estimation.intraBlocks) {
                    br.skip(8);
                }
                if (estimation.notCodedBlocks) {
                    br.skip(8);
                }
                if (estimation.dctCoefs) {
                    br.skip(8);
                }
                if (estimation.dctLines) {
                    br.skip(8);
                }
                if (estimation.vlcSymbols) {
                    br.skip(8);
                }
                if (estimation.vlcBits) {
                    br.skip(8);
                }
                if (estimation.sadct) {
                    br.skip(8);
                }
            }
            if (codingType == 1) {
                if (estimation.opaque) {
                    br.skip(8);
                }
                if (estimation.transparent) {
                    br.skip(8);
                }
                if (estimation.intraCae) {
                    br.skip(8);
                }
                if (estimation.interCae) {
                    br.skip(8);
                }
                if (estimation.noUpdate) {
                    br.skip(8);
                }
                if (estimation.upsampling) {
                    br.skip(8);
                }
                if (estimation.intraBlocks) {
                    br.skip(8);
                }
                if (estimation.notCodedBlocks) {
                    br.skip(8);
                }
                if (estimation.dctCoefs) {
                    br.skip(8);
                }
                if (estimation.dctLines) {
                    br.skip(8);
                }
                if (estimation.vlcSymbols) {
                    br.skip(8);
                }
                if (estimation.vlcBits) {
                    br.skip(8);
                }
                if (estimation.interBlocks) {
                    br.skip(8);
                }
                if (estimation.inter4vBlocks) {
                    br.skip(8);
                }
                if (estimation.apm) {
                    br.skip(8);
                }
                if (estimation.npm) {
                    br.skip(8);
                }
                if (estimation.forwBackMcQ) {
                    br.skip(8);
                }
                if (estimation.halfpel2) {
                    br.skip(8);
                }
                if (estimation.halfpel4) {
                    br.skip(8);
                }
                if (estimation.sadct) {
                    br.skip(8);
                }
                if (estimation.quarterpel) {
                    br.skip(8);
                }
            }
            if (codingType == 2) {
                if (estimation.opaque) {
                    br.skip(8);
                }
                if (estimation.transparent) {
                    br.skip(8);
                }
                if (estimation.intraCae) {
                    br.skip(8);
                }
                if (estimation.interCae) {
                    br.skip(8);
                }
                if (estimation.noUpdate) {
                    br.skip(8);
                }
                if (estimation.upsampling) {
                    br.skip(8);
                }
                if (estimation.intraBlocks) {
                    br.skip(8);
                }
                if (estimation.notCodedBlocks) {
                    br.skip(8);
                }
                if (estimation.dctCoefs) {
                    br.skip(8);
                }
                if (estimation.dctLines) {
                    br.skip(8);
                }
                if (estimation.vlcSymbols) {
                    br.skip(8);
                }
                if (estimation.vlcBits) {
                    br.skip(8);
                }
                if (estimation.interBlocks) {
                    br.skip(8);
                }
                if (estimation.inter4vBlocks) {
                    br.skip(8);
                }
                if (estimation.apm) {
                    br.skip(8);
                }
                if (estimation.npm) {
                    br.skip(8);
                }
                if (estimation.forwBackMcQ) {
                    br.skip(8);
                }
                if (estimation.halfpel2) {
                    br.skip(8);
                }
                if (estimation.halfpel4) {
                    br.skip(8);
                }
                if (estimation.interpolateMcQ) {
                    br.skip(8);
                }
                if (estimation.sadct) {
                    br.skip(8);
                }
                if (estimation.quarterpel) {
                    br.skip(8);
                }
            }
            if (codingType == 3 && spriteEnable == 1) {
                if (estimation.intraBlocks) {
                    br.skip(8);
                }
                if (estimation.notCodedBlocks) {
                    br.skip(8);
                }
                if (estimation.dctCoefs) {
                    br.skip(8);
                }
                if (estimation.dctLines) {
                    br.skip(8);
                }
                if (estimation.vlcSymbols) {
                    br.skip(8);
                }
                if (estimation.vlcBits) {
                    br.skip(8);
                }
                if (estimation.interBlocks) {
                    br.skip(8);
                }
                if (estimation.inter4vBlocks) {
                    br.skip(8);
                }
                if (estimation.apm) {
                    br.skip(8);
                }
                if (estimation.npm) {
                    br.skip(8);
                }
                if (estimation.forwBackMcQ) {
                    br.skip(8);
                }
                if (estimation.halfpel2) {
                    br.skip(8);
                }
                if (estimation.halfpel4) {
                    br.skip(8);
                }
                if (estimation.interpolateMcQ) {
                    br.skip(8);
                }
            }
        }
    }

    public boolean getPackedMode() {
        return this.packedMode;
    }

    public int getTimeIncrementBits() {
        return this.timeIncrementBits;
    }

    public int getTimeIncrementResolution() {
        return this.timeIncrementResolution;
    }

    private static class Estimation {

        public int method;

        public boolean opaque;

        public boolean transparent;

        public boolean intraCae;

        public boolean interCae;

        public boolean noUpdate;

        public boolean upsampling;

        public boolean intraBlocks;

        public boolean interBlocks;

        public boolean inter4vBlocks;

        public boolean notCodedBlocks;

        public boolean dctCoefs;

        public boolean dctLines;

        public boolean vlcSymbols;

        public boolean vlcBits;

        public boolean apm;

        public boolean npm;

        public boolean interpolateMcQ;

        public boolean forwBackMcQ;

        public boolean halfpel2;

        public boolean halfpel4;

        public boolean sadct;

        public boolean quarterpel;

        private Estimation() {
        }
    }
}