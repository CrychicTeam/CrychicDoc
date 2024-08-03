package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.GOPHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.SequenceScalableExtension;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.dct.SparseIDCT;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MPEGDecoder extends VideoDecoder {

    protected SequenceHeader sh;

    protected GOPHeader gh;

    private Picture[] refFrames = new Picture[2];

    private Picture[] refFields = new Picture[2];

    @Override
    public Picture decodeFrame(ByteBuffer buffer, byte[][] buf) {
        PictureHeader ph = this.readHeader(buffer);
        if ((this.refFrames[0] != null || ph.picture_coding_type <= 1) && (this.refFrames[1] != null || ph.picture_coding_type <= 2)) {
            MPEGDecoder.Context context = this.initContext(this.sh, ph);
            Picture pic = new Picture(context.codedWidth, context.codedHeight, buf, (byte[][]) null, context.color, 0, new Rect(0, 0, context.picWidth, context.picHeight));
            if (ph.pictureCodingExtension != null && ph.pictureCodingExtension.picture_structure != 3) {
                this.decodePicture(context, ph, buffer, buf, ph.pictureCodingExtension.picture_structure - 1, 1);
                ph = this.readHeader(buffer);
                context = this.initContext(this.sh, ph);
                this.decodePicture(context, ph, buffer, buf, ph.pictureCodingExtension.picture_structure - 1, 1);
            } else {
                this.decodePicture(context, ph, buffer, buf, 0, 0);
            }
            if (ph.picture_coding_type == 1 || ph.picture_coding_type == 2) {
                Picture unused = this.refFrames[1];
                this.refFrames[1] = this.refFrames[0];
                this.refFrames[0] = this.copyAndCreateIfNeeded(pic, unused);
            }
            return pic;
        } else {
            throw new RuntimeException("Not enough references to decode " + (ph.picture_coding_type == 1 ? "P" : "B") + " frame");
        }
    }

    private Picture copyAndCreateIfNeeded(Picture src, Picture dst) {
        if (dst == null || !dst.compatible(src)) {
            dst = src.createCompatible();
        }
        dst.copyFrom(src);
        return dst;
    }

    private PictureHeader readHeader(ByteBuffer buffer) {
        PictureHeader ph = null;
        ByteBuffer segment;
        for (ByteBuffer fork = buffer.duplicate(); (segment = MPEGUtil.nextSegment(fork)) != null; buffer.position(fork.position())) {
            int code = segment.getInt() & 0xFF;
            if (code == 179) {
                SequenceHeader newSh = SequenceHeader.read(segment);
                if (this.sh != null) {
                    newSh.copyExtensions(this.sh);
                }
                this.sh = newSh;
            } else if (code == 184) {
                this.gh = GOPHeader.read(segment);
            } else if (code == 0) {
                ph = PictureHeader.read(segment);
            } else if (code == 181) {
                int extType = segment.get(4) >> 4;
                if (extType != 1 && extType != 5 && extType != 2) {
                    PictureHeader.readExtension(segment, ph, this.sh);
                } else {
                    SequenceHeader.readExtension(segment, this.sh);
                }
            } else if (code != 178) {
                break;
            }
        }
        return ph;
    }

    protected MPEGDecoder.Context initContext(SequenceHeader sh, PictureHeader ph) {
        MPEGDecoder.Context context = new MPEGDecoder.Context();
        context.codedWidth = sh.horizontal_size + 15 & -16;
        context.codedHeight = getCodedHeight(sh, ph);
        context.mbWidth = sh.horizontal_size + 15 >> 4;
        context.mbHeight = sh.vertical_size + 15 >> 4;
        context.picWidth = sh.horizontal_size;
        context.picHeight = sh.vertical_size;
        int chromaFormat = 1;
        if (sh.sequenceExtension != null) {
            chromaFormat = sh.sequenceExtension.chroma_format;
        }
        context.color = this.getColor(chromaFormat);
        context.scan = MPEGConst.scan[ph.pictureCodingExtension == null ? 0 : ph.pictureCodingExtension.alternate_scan];
        int[] inter = sh.non_intra_quantiser_matrix == null ? this.zigzag(MPEGConst.defaultQMatInter, context.scan) : sh.non_intra_quantiser_matrix;
        int[] intra = sh.intra_quantiser_matrix == null ? this.zigzag(MPEGConst.defaultQMatIntra, context.scan) : sh.intra_quantiser_matrix;
        context.qMats = new int[][] { inter, inter, intra, intra };
        if (ph.quantMatrixExtension != null) {
            if (ph.quantMatrixExtension.non_intra_quantiser_matrix != null) {
                context.qMats[0] = ph.quantMatrixExtension.non_intra_quantiser_matrix;
            }
            if (ph.quantMatrixExtension.chroma_non_intra_quantiser_matrix != null) {
                context.qMats[1] = ph.quantMatrixExtension.chroma_non_intra_quantiser_matrix;
            }
            if (ph.quantMatrixExtension.intra_quantiser_matrix != null) {
                context.qMats[2] = ph.quantMatrixExtension.intra_quantiser_matrix;
            }
            if (ph.quantMatrixExtension.chroma_intra_quantiser_matrix != null) {
                context.qMats[3] = ph.quantMatrixExtension.chroma_intra_quantiser_matrix;
            }
        }
        return context;
    }

    private int[] zigzag(int[] array, int[] scan) {
        int[] result = new int[64];
        for (int i = 0; i < scan.length; i++) {
            result[i] = array[scan[i]];
        }
        return result;
    }

    public static int getCodedHeight(SequenceHeader sh, PictureHeader ph) {
        int field = ph.pictureCodingExtension != null && ph.pictureCodingExtension.picture_structure != 3 ? 1 : 0;
        return ((sh.vertical_size >> field) + 15 & -16) << field;
    }

    public Picture decodePicture(MPEGDecoder.Context context, PictureHeader ph, ByteBuffer buffer, byte[][] buf, int vertOff, int vertStep) {
        int planeSize = context.codedWidth * context.codedHeight;
        if (buf.length >= 3 && buf[0].length >= planeSize && buf[1].length >= planeSize && buf[2].length >= planeSize) {
            try {
                ByteBuffer segment;
                while ((segment = MPEGUtil.nextSegment(buffer)) != null) {
                    int startCode = segment.get(3) & 255;
                    if (startCode >= 1 && startCode <= 175) {
                        this.doDecodeSlice(context, ph, buf, vertOff, vertStep, segment);
                    } else {
                        if (startCode >= 179 && startCode != 182 && startCode != 183) {
                            throw new RuntimeException("Unexpected start code " + startCode);
                        }
                        if (startCode == 0) {
                            buffer.reset();
                            break;
                        }
                    }
                }
                Picture pic = Picture.createPicture(context.codedWidth, context.codedHeight, buf, context.color);
                if ((ph.picture_coding_type == 1 || ph.picture_coding_type == 2) && ph.pictureCodingExtension != null && ph.pictureCodingExtension.picture_structure != 3) {
                    this.refFields[ph.pictureCodingExtension.picture_structure - 1] = this.copyAndCreateIfNeeded(pic, this.refFields[ph.pictureCodingExtension.picture_structure - 1]);
                }
                return pic;
            } catch (IOException var10) {
                throw new RuntimeException(var10);
            }
        } else {
            throw new RuntimeException("ByteBuffer too small to hold output picture [" + context.codedWidth + "x" + context.codedHeight + "]");
        }
    }

    private void doDecodeSlice(MPEGDecoder.Context context, PictureHeader ph, byte[][] buf, int vertOff, int vertStep, ByteBuffer segment) throws IOException {
        int startCode = segment.get(3) & 255;
        ByteBuffer dup = segment.duplicate();
        dup.position(4);
        try {
            this.decodeSlice(ph, startCode, context, buf, BitReader.createBitReader(dup), vertOff, vertStep);
        } catch (RuntimeException var10) {
            var10.printStackTrace();
        }
    }

    private ColorSpace getColor(int chromaFormat) {
        switch(chromaFormat) {
            case 1:
                return ColorSpace.YUV420;
            case 2:
                return ColorSpace.YUV422;
            case 3:
                return ColorSpace.YUV444;
            default:
                return null;
        }
    }

    public void decodeSlice(PictureHeader ph, int verticalPos, MPEGDecoder.Context context, byte[][] buf, BitReader _in, int vertOff, int vertStep) throws IOException {
        int stride = context.codedWidth;
        this.resetDCPredictors(context, ph);
        int mbRow = verticalPos - 1;
        if (this.sh.vertical_size > 2800) {
            mbRow += _in.readNBit(3) << 7;
        }
        if (this.sh.sequenceScalableExtension != null && this.sh.sequenceScalableExtension.scalable_mode == 0) {
            int qScaleCode = _in.readNBit(7);
        }
        int qScaleCode = _in.readNBit(5);
        if (_in.read1Bit() == 1) {
            int intraSlice = _in.read1Bit();
            _in.skip(7);
            while (_in.read1Bit() == 1) {
                _in.readNBit(8);
            }
        }
        MPEGPred pred = new MPEGPred(ph.pictureCodingExtension != null ? ph.pictureCodingExtension.f_code : new int[][] { { ph.forward_f_code, ph.forward_f_code }, { ph.backward_f_code, ph.backward_f_code } }, this.sh.sequenceExtension != null ? this.sh.sequenceExtension.chroma_format : 1, ph.pictureCodingExtension == null || ph.pictureCodingExtension.top_field_first != 0);
        int[] ctx = new int[] { qScaleCode };
        for (int prevAddr = mbRow * context.mbWidth - 1; _in.checkNBit(23) != 0; context.mbNo++) {
            prevAddr = this.decodeMacroblock(ph, context, prevAddr, ctx, buf, stride, _in, vertOff, vertStep, pred);
        }
    }

    private void resetDCPredictors(MPEGDecoder.Context context, PictureHeader ph) {
        int rval = 128;
        if (ph.pictureCodingExtension != null) {
            rval = 1 << 7 + ph.pictureCodingExtension.intra_dc_precision;
        }
        context.intra_dc_predictor[0] = context.intra_dc_predictor[1] = context.intra_dc_predictor[2] = rval;
    }

    public int decodeMacroblock(PictureHeader ph, MPEGDecoder.Context context, int prevAddr, int[] qScaleCode, byte[][] buf, int stride, BitReader bits, int vertOff, int vertStep, MPEGPred pred) {
        int mbAddr;
        for (mbAddr = prevAddr; bits.checkNBit(11) == 8; mbAddr += 33) {
            bits.skip(11);
        }
        mbAddr += MPEGConst.vlcAddressIncrement.readVLC(bits) + 1;
        int chromaFormat = 1;
        if (this.sh.sequenceExtension != null) {
            chromaFormat = this.sh.sequenceExtension.chroma_format;
        }
        for (int i = prevAddr + 1; i < mbAddr; i++) {
            int[][] predFwd = new int[][] { new int[256], new int[1 << chromaFormat + 5], new int[1 << chromaFormat + 5] };
            int mbX = i % context.mbWidth;
            int mbY = i / context.mbWidth;
            if (ph.picture_coding_type == 2) {
                pred.reset();
            }
            this.mvZero(context, ph, pred, mbX, mbY, predFwd);
            this.put(predFwd, buf, stride, chromaFormat, mbX, mbY, context.codedWidth, context.codedHeight >> vertStep, vertOff, vertStep);
        }
        VLC vlcMBType = SequenceScalableExtension.vlcMBType(ph.picture_coding_type, this.sh.sequenceScalableExtension);
        MPEGConst.MBType[] mbTypeVal = SequenceScalableExtension.mbTypeVal(ph.picture_coding_type, this.sh.sequenceScalableExtension);
        MPEGConst.MBType mbType = mbTypeVal[vlcMBType.readVLC(bits)];
        if (mbType.macroblock_intra != 1 || mbAddr - prevAddr > 1) {
            this.resetDCPredictors(context, ph);
        }
        int spatial_temporal_weight_code = 0;
        if (mbType.spatial_temporal_weight_code_flag == 1 && ph.pictureSpatialScalableExtension != null && ph.pictureSpatialScalableExtension.spatial_temporal_weight_code_table_index != 0) {
            spatial_temporal_weight_code = bits.readNBit(2);
        }
        int motion_type = -1;
        if (mbType.macroblock_motion_forward != 0 || mbType.macroblock_motion_backward != 0) {
            if (ph.pictureCodingExtension != null && (ph.pictureCodingExtension.picture_structure != 3 || ph.pictureCodingExtension.frame_pred_frame_dct != 1)) {
                motion_type = bits.readNBit(2);
            } else {
                motion_type = 2;
            }
        }
        int dctType = 0;
        if (ph.pictureCodingExtension != null && ph.pictureCodingExtension.picture_structure == 3 && ph.pictureCodingExtension.frame_pred_frame_dct == 0 && (mbType.macroblock_intra != 0 || mbType.macroblock_pattern != 0)) {
            dctType = bits.read1Bit();
        }
        if (mbType.macroblock_quant != 0) {
            qScaleCode[0] = bits.readNBit(5);
        }
        boolean concealmentMv = ph.pictureCodingExtension != null && ph.pictureCodingExtension.concealment_motion_vectors != 0;
        int[][] predFwd = (int[][]) null;
        int mbX = mbAddr % context.mbWidth;
        int mbY = mbAddr / context.mbWidth;
        if (mbType.macroblock_intra == 1) {
            if (!concealmentMv) {
                pred.reset();
            }
        } else if (mbType.macroblock_motion_forward != 0) {
            int refIdx = ph.picture_coding_type == 2 ? 0 : 1;
            predFwd = new int[][] { new int[256], new int[1 << chromaFormat + 5], new int[1 << chromaFormat + 5] };
            if (ph.pictureCodingExtension == null || ph.pictureCodingExtension.picture_structure == 3) {
                pred.predictInFrame(this.refFrames[refIdx], mbX << 4, mbY << 4, predFwd, bits, motion_type, 0, spatial_temporal_weight_code);
            } else if (ph.picture_coding_type == 2) {
                pred.predictInField(this.refFields, mbX << 4, mbY << 4, predFwd, bits, motion_type, 0, ph.pictureCodingExtension.picture_structure - 1);
            } else {
                pred.predictInField(new Picture[] { this.refFrames[refIdx], this.refFrames[refIdx] }, mbX << 4, mbY << 4, predFwd, bits, motion_type, 0, ph.pictureCodingExtension.picture_structure - 1);
            }
        } else if (ph.picture_coding_type == 2) {
            predFwd = new int[][] { new int[256], new int[1 << chromaFormat + 5], new int[1 << chromaFormat + 5] };
            pred.reset();
            this.mvZero(context, ph, pred, mbX, mbY, predFwd);
        }
        int[][] predBack = (int[][]) null;
        if (mbType.macroblock_motion_backward != 0) {
            predBack = new int[][] { new int[256], new int[1 << chromaFormat + 5], new int[1 << chromaFormat + 5] };
            if (ph.pictureCodingExtension != null && ph.pictureCodingExtension.picture_structure != 3) {
                pred.predictInField(new Picture[] { this.refFrames[0], this.refFrames[0] }, mbX << 4, mbY << 4, predBack, bits, motion_type, 1, ph.pictureCodingExtension.picture_structure - 1);
            } else {
                pred.predictInFrame(this.refFrames[0], mbX << 4, mbY << 4, predBack, bits, motion_type, 1, spatial_temporal_weight_code);
            }
        }
        context.lastPredB = mbType;
        int[][] pp = mbType.macroblock_intra == 1 ? new int[][] { new int[256], new int[1 << chromaFormat + 5], new int[1 << chromaFormat + 5] } : buildPred(predFwd, predBack);
        if (mbType.macroblock_intra != 0 && concealmentMv) {
            Preconditions.checkState(1 == bits.read1Bit());
        }
        int cbp = mbType.macroblock_intra == 1 ? 4095 : 0;
        if (mbType.macroblock_pattern != 0) {
            cbp = this.readCbPattern(bits);
        }
        VLC vlcCoeff = MPEGConst.vlcCoeff0;
        if (ph.pictureCodingExtension != null && mbType.macroblock_intra == 1 && ph.pictureCodingExtension.intra_vlc_format == 1) {
            vlcCoeff = MPEGConst.vlcCoeff1;
        }
        int[] qScaleTab = ph.pictureCodingExtension != null && ph.pictureCodingExtension.q_scale_type == 1 ? MPEGConst.qScaleTab2 : MPEGConst.qScaleTab1;
        int qScale = qScaleTab[qScaleCode[0]];
        int intra_dc_mult = 8;
        if (ph.pictureCodingExtension != null) {
            intra_dc_mult = 8 >> ph.pictureCodingExtension.intra_dc_precision;
        }
        int blkCount = 6 + (chromaFormat == 1 ? 0 : (chromaFormat == 2 ? 2 : 6));
        int[] block = new int[64];
        int i = 0;
        for (int cbpMask = 1 << blkCount - 1; i < blkCount; cbpMask >>= 1) {
            if ((cbp & cbpMask) != 0) {
                int[] qmat = context.qMats[(i >= 4 ? 1 : 0) + (mbType.macroblock_intra << 1)];
                if (mbType.macroblock_intra == 1) {
                    this.blockIntra(bits, vlcCoeff, block, context.intra_dc_predictor, i, context.scan, !this.sh.hasExtensions() && !ph.hasExtensions() ? 8 : 12, intra_dc_mult, qScale, qmat);
                } else {
                    this.blockInter(bits, vlcCoeff, block, context.scan, !this.sh.hasExtensions() && !ph.hasExtensions() ? 8 : 12, qScale, qmat);
                }
                this.mapBlock(block, pp[MPEGConst.BLOCK_TO_CC[i]], i, dctType, chromaFormat);
            }
            i++;
        }
        this.put(pp, buf, stride, chromaFormat, mbX, mbY, context.codedWidth, context.codedHeight >> vertStep, vertOff, vertStep);
        return mbAddr;
    }

    protected void mapBlock(int[] block, int[] out, int blkIdx, int dctType, int chromaFormat) {
        int stepVert = chromaFormat != 1 || blkIdx != 4 && blkIdx != 5 ? dctType : 0;
        int log2stride = blkIdx < 4 ? 4 : 4 - MPEGConst.SQUEEZE_X[chromaFormat];
        int blkIdxExt = blkIdx + (dctType << 4);
        int x = MPEGConst.BLOCK_POS_X[blkIdxExt];
        int y = MPEGConst.BLOCK_POS_Y[blkIdxExt];
        int off = (y << log2stride) + x;
        int stride = 1 << log2stride + stepVert;
        int i = 0;
        for (int coeff = 0; i < 8; coeff += 8) {
            out[off] += block[coeff];
            out[off + 1] = out[off + 1] + block[coeff + 1];
            out[off + 2] = out[off + 2] + block[coeff + 2];
            out[off + 3] = out[off + 3] + block[coeff + 3];
            out[off + 4] = out[off + 4] + block[coeff + 4];
            out[off + 5] = out[off + 5] + block[coeff + 5];
            out[off + 6] = out[off + 6] + block[coeff + 6];
            out[off + 7] = out[off + 7] + block[coeff + 7];
            off += stride;
            i++;
        }
    }

    private static final int[][] buildPred(int[][] predFwd, int[][] predBack) {
        if (predFwd != null && predBack != null) {
            avgPred(predFwd, predBack);
            return predFwd;
        } else if (predFwd != null) {
            return predFwd;
        } else if (predBack != null) {
            return predBack;
        } else {
            throw new RuntimeException("Omited pred _in B-frames --> invalid");
        }
    }

    private static final void avgPred(int[][] predFwd, int[][] predBack) {
        for (int i = 0; i < predFwd.length; i++) {
            for (int j = 0; j < predFwd[i].length; j += 4) {
                predFwd[i][j] = predFwd[i][j] + predBack[i][j] + 1 >> 1;
                predFwd[i][j + 1] = predFwd[i][j + 1] + predBack[i][j + 1] + 1 >> 1;
                predFwd[i][j + 2] = predFwd[i][j + 2] + predBack[i][j + 2] + 1 >> 1;
                predFwd[i][j + 3] = predFwd[i][j + 3] + predBack[i][j + 3] + 1 >> 1;
            }
        }
    }

    private void mvZero(MPEGDecoder.Context context, PictureHeader ph, MPEGPred pred, int mbX, int mbY, int[][] mbPix) {
        if (ph.picture_coding_type == 2) {
            pred.predict16x16NoMV(this.refFrames[0], mbX << 4, mbY << 4, ph.pictureCodingExtension == null ? 3 : ph.pictureCodingExtension.picture_structure, 0, mbPix);
        } else {
            int[][] pp = mbPix;
            if (context.lastPredB.macroblock_motion_backward == 1) {
                pred.predict16x16NoMV(this.refFrames[0], mbX << 4, mbY << 4, ph.pictureCodingExtension == null ? 3 : ph.pictureCodingExtension.picture_structure, 1, mbPix);
                pp = new int[][] { new int[mbPix[0].length], new int[mbPix[1].length], new int[mbPix[2].length] };
            }
            if (context.lastPredB.macroblock_motion_forward == 1) {
                pred.predict16x16NoMV(this.refFrames[1], mbX << 4, mbY << 4, ph.pictureCodingExtension == null ? 3 : ph.pictureCodingExtension.picture_structure, 0, pp);
                if (mbPix != pp) {
                    avgPred(mbPix, pp);
                }
            }
        }
    }

    protected void put(int[][] mbPix, byte[][] buf, int stride, int chromaFormat, int mbX, int mbY, int width, int height, int vertOff, int vertStep) {
        int chromaStride = stride + (1 << MPEGConst.SQUEEZE_X[chromaFormat]) - 1 >> MPEGConst.SQUEEZE_X[chromaFormat];
        int chromaMBW = 4 - MPEGConst.SQUEEZE_X[chromaFormat];
        int chromaMBH = 4 - MPEGConst.SQUEEZE_Y[chromaFormat];
        this.putSub(buf[0], (mbY << 4) * (stride << vertStep) + vertOff * stride + (mbX << 4), stride << vertStep, mbPix[0], 4, 4);
        this.putSub(buf[1], (mbY << chromaMBH) * (chromaStride << vertStep) + vertOff * chromaStride + (mbX << chromaMBW), chromaStride << vertStep, mbPix[1], chromaMBW, chromaMBH);
        this.putSub(buf[2], (mbY << chromaMBH) * (chromaStride << vertStep) + vertOff * chromaStride + (mbX << chromaMBW), chromaStride << vertStep, mbPix[2], chromaMBW, chromaMBH);
    }

    protected void putSub(byte[] big, int off, int stride, int[] block, int mbW, int mbH) {
        int blOff = 0;
        if (mbW == 3) {
            for (int i = 0; i < 1 << mbH; i++) {
                big[off] = clipTo8Bit(block[blOff]);
                big[off + 1] = clipTo8Bit(block[blOff + 1]);
                big[off + 2] = clipTo8Bit(block[blOff + 2]);
                big[off + 3] = clipTo8Bit(block[blOff + 3]);
                big[off + 4] = clipTo8Bit(block[blOff + 4]);
                big[off + 5] = clipTo8Bit(block[blOff + 5]);
                big[off + 6] = clipTo8Bit(block[blOff + 6]);
                big[off + 7] = clipTo8Bit(block[blOff + 7]);
                blOff += 8;
                off += stride;
            }
        } else {
            for (int i = 0; i < 1 << mbH; i++) {
                big[off] = clipTo8Bit(block[blOff]);
                big[off + 1] = clipTo8Bit(block[blOff + 1]);
                big[off + 2] = clipTo8Bit(block[blOff + 2]);
                big[off + 3] = clipTo8Bit(block[blOff + 3]);
                big[off + 4] = clipTo8Bit(block[blOff + 4]);
                big[off + 5] = clipTo8Bit(block[blOff + 5]);
                big[off + 6] = clipTo8Bit(block[blOff + 6]);
                big[off + 7] = clipTo8Bit(block[blOff + 7]);
                big[off + 8] = clipTo8Bit(block[blOff + 8]);
                big[off + 9] = clipTo8Bit(block[blOff + 9]);
                big[off + 10] = clipTo8Bit(block[blOff + 10]);
                big[off + 11] = clipTo8Bit(block[blOff + 11]);
                big[off + 12] = clipTo8Bit(block[blOff + 12]);
                big[off + 13] = clipTo8Bit(block[blOff + 13]);
                big[off + 14] = clipTo8Bit(block[blOff + 14]);
                big[off + 15] = clipTo8Bit(block[blOff + 15]);
                blOff += 16;
                off += stride;
            }
        }
    }

    protected static final byte clipTo8Bit(int val) {
        return (byte) ((val < 0 ? 0 : (val > 255 ? 255 : val)) - 128);
    }

    protected static final int clip(int val) {
        return val < 0 ? 0 : (val > 255 ? 255 : val);
    }

    protected static final int quantInter(int level, int quant) {
        return ((level << 1) + 1) * quant >> 5;
    }

    protected static final int quantInterSigned(int level, int quant) {
        return level >= 0 ? quantInter(level, quant) : -quantInter(-level, quant);
    }

    protected void blockIntra(BitReader bits, VLC vlcCoeff, int[] block, int[] intra_dc_predictor, int blkIdx, int[] scan, int escSize, int intra_dc_mult, int qScale, int[] qmat) {
        int cc = MPEGConst.BLOCK_TO_CC[blkIdx];
        int size = (cc == 0 ? MPEGConst.vlcDCSizeLuma : MPEGConst.vlcDCSizeChroma).readVLC(bits);
        int delta = size != 0 ? mpegSigned(bits, size) : 0;
        intra_dc_predictor[cc] += delta;
        int dc = intra_dc_predictor[cc] * intra_dc_mult;
        SparseIDCT.start(block, dc);
        int idx = 0;
        while (idx < 64) {
            int readVLC = vlcCoeff.readVLC(bits);
            if (readVLC == 2048) {
                break;
            }
            int level;
            if (readVLC == 2049) {
                idx += bits.readNBit(6) + 1;
                level = twosSigned(bits, escSize) * qScale * qmat[idx];
                level = level >= 0 ? level >> 4 : -(-level >> 4);
            } else {
                idx += (readVLC >> 6) + 1;
                level = toSigned((readVLC & 63) * qScale * qmat[idx] >> 4, bits.read1Bit());
            }
            SparseIDCT.coeff(block, scan[idx], level);
        }
        SparseIDCT.finish(block);
    }

    protected void blockInter(BitReader bits, VLC vlcCoeff, int[] block, int[] scan, int escSize, int qScale, int[] qmat) {
        int idx = -1;
        if (vlcCoeff == MPEGConst.vlcCoeff0 && bits.checkNBit(1) == 1) {
            bits.read1Bit();
            int dc = toSigned(quantInter(1, qScale * qmat[0]), bits.read1Bit());
            SparseIDCT.start(block, dc);
            idx++;
        } else {
            SparseIDCT.start(block, 0);
        }
        while (idx < 64) {
            int readVLC = vlcCoeff.readVLC(bits);
            if (readVLC == 2048) {
                break;
            }
            int ac;
            if (readVLC == 2049) {
                idx += bits.readNBit(6) + 1;
                ac = quantInterSigned(twosSigned(bits, escSize), qScale * qmat[idx]);
            } else {
                idx += (readVLC >> 6) + 1;
                ac = toSigned(quantInter(readVLC & 63, qScale * qmat[idx]), bits.read1Bit());
            }
            SparseIDCT.coeff(block, scan[idx], ac);
        }
        SparseIDCT.finish(block);
    }

    public static final int twosSigned(BitReader bits, int size) {
        int shift = 32 - size;
        return bits.readNBit(size) << shift >> shift;
    }

    public static final int mpegSigned(BitReader bits, int size) {
        int val = bits.readNBit(size);
        int sign = val >>> size - 1 ^ 1;
        return val + sign - (sign << size);
    }

    public static final int toSigned(int val, int s) {
        int sign = s << 31 >> 31;
        return (val ^ sign) - sign;
    }

    private final int readCbPattern(BitReader bits) {
        int cbp420 = MPEGConst.vlcCBP.readVLC(bits);
        if (this.sh.sequenceExtension == null || this.sh.sequenceExtension.chroma_format == 1) {
            return cbp420;
        } else if (this.sh.sequenceExtension.chroma_format == 2) {
            return cbp420 << 2 | bits.readNBit(2);
        } else if (this.sh.sequenceExtension.chroma_format == 3) {
            return cbp420 << 6 | bits.readNBit(6);
        } else {
            throw new RuntimeException("Unsupported chroma format: " + this.sh.sequenceExtension.chroma_format);
        }
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        data = data.duplicate();
        data.order(ByteOrder.BIG_ENDIAN);
        for (int i = 0; i < 2 && MPEGUtil.gotoNextMarker(data) != null && data.hasRemaining(); i++) {
            int marker = data.getInt();
            if (marker == 256 || marker >= 432 && marker <= 440) {
                return 50 - i * 10;
            }
            if (marker > 256 && marker < 432) {
                return 20 - i * 10;
            }
        }
        return 0;
    }

    private static ByteBuffer getSequenceHeader(ByteBuffer data) {
        for (ByteBuffer segment = MPEGUtil.nextSegment(data); segment != null; segment = MPEGUtil.nextSegment(data)) {
            int marker = segment.getInt();
            if (marker == 435) {
                return segment;
            }
        }
        return null;
    }

    private static ByteBuffer getRawPictureHeader(ByteBuffer data) {
        for (ByteBuffer segment = MPEGUtil.nextSegment(data); segment != null; segment = MPEGUtil.nextSegment(data)) {
            int marker = segment.getInt();
            if (marker == 256) {
                return segment;
            }
        }
        return null;
    }

    public static int getSequenceNumber(ByteBuffer data) {
        PictureHeader ph = getPictureHeader(data);
        return ph == null ? -1 : ph.temporal_reference;
    }

    public static PictureHeader getPictureHeader(ByteBuffer data) {
        ByteBuffer bb = getRawPictureHeader(data);
        return bb == null ? null : PictureHeader.read(bb);
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer data) {
        ByteBuffer codecPrivate = getSequenceHeader(data.duplicate());
        SequenceHeader sh = SequenceHeader.read(codecPrivate.duplicate());
        return VideoCodecMeta.createSimpleVideoCodecMeta(new Size(sh.horizontal_size, sh.vertical_size), ColorSpace.YUV420);
    }

    public static class Context {

        int[] intra_dc_predictor = new int[3];

        public int mbWidth;

        int mbNo;

        public int codedWidth;

        public int codedHeight;

        public int mbHeight;

        public ColorSpace color;

        public MPEGConst.MBType lastPredB;

        public int[][] qMats;

        public int[] scan;

        public int picWidth;

        public int picHeight;
    }
}