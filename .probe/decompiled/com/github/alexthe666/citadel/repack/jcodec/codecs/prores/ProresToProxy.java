package com.github.alexthe666.citadel.repack.jcodec.codecs.prores;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import java.nio.ByteBuffer;

public class ProresToProxy {

    private int[] qMatLumaTo = ProresConsts.QMAT_LUMA_APCO;

    private int[] qMatChromaTo = ProresConsts.QMAT_CHROMA_APCO;

    private int frameSize;

    private static final int START_QP = 6;

    private int bitsPer1024;

    private int bitsPer1024High;

    private int bitsPer1024Low;

    private int nCoeffs;

    public ProresToProxy(int width, int height, int frameSize) {
        this.frameSize = frameSize;
        int headerBytes = (height >> 4) * ((width >> 4) + 7 >> 3) * 8 + 148;
        int dataBits = frameSize - headerBytes << 3;
        this.bitsPer1024 = (dataBits << 10) / (width * height);
        this.bitsPer1024High = this.bitsPer1024 - this.bitsPer1024 / 10;
        this.bitsPer1024Low = this.bitsPer1024 - this.bitsPer1024 / 20;
        this.nCoeffs = Math.max(Math.min(33000 / (width * height >> 8), 64), 4);
    }

    public int getFrameSize() {
        return this.frameSize;
    }

    void requant(BitReader ib, BitWriter ob, int blocksPerSlice, int[] qMatFrom, int[] qMatTo, int[] scan, int mbX, int mbY, int plane) {
        int[] out = new int[blocksPerSlice << 6];
        try {
            ProresDecoder.readDCCoeffs(ib, qMatFrom, out, blocksPerSlice, 64);
            ProresDecoder.readACCoeffs(ib, qMatFrom, out, blocksPerSlice, scan, this.nCoeffs, 6);
        } catch (RuntimeException var12) {
        }
        for (int i = 0; i < out.length; i++) {
            out[i] <<= 2;
        }
        ProresEncoder.writeDCCoeffs(ob, qMatTo, out, blocksPerSlice);
        ProresEncoder.writeACCoeffs(ob, qMatTo, out, blocksPerSlice, scan, this.nCoeffs);
        ob.flush();
    }

    public void transcode(ByteBuffer inBuf, ByteBuffer outBuf) {
        ByteBuffer fork = outBuf.duplicate();
        ProresConsts.FrameHeader fh = ProresDecoder.readFrameHeader(inBuf);
        ProresEncoder.writeFrameHeader(outBuf, fh);
        int beforePicture = outBuf.position();
        if (fh.frameType == 0) {
            this.transcodePicture(inBuf, outBuf, fh);
        } else {
            this.transcodePicture(inBuf, outBuf, fh);
            this.transcodePicture(inBuf, outBuf, fh);
        }
        fh.qMatLuma = this.qMatLumaTo;
        fh.qMatChroma = this.qMatChromaTo;
        fh.payloadSize = outBuf.position() - beforePicture;
        ProresEncoder.writeFrameHeader(fork, fh);
    }

    private void transcodePicture(ByteBuffer inBuf, ByteBuffer outBuf, ProresConsts.FrameHeader fh) {
        ProresConsts.PictureHeader ph = ProresDecoder.readPictureHeader(inBuf);
        ProresEncoder.writePictureHeader(ph.log2SliceMbWidth, ph.sliceSizes.length, outBuf);
        ByteBuffer sliceSizes = outBuf.duplicate();
        outBuf.position(outBuf.position() + (ph.sliceSizes.length << 1));
        int mbX = 0;
        int mbY = 0;
        int mbWidth = fh.width + 15 >> 4;
        int sliceMbCount = 1 << ph.log2SliceMbWidth;
        int balance = 0;
        int qp = 6;
        for (int i = 0; i < ph.sliceSizes.length; i++) {
            while (mbWidth - mbX < sliceMbCount) {
                sliceMbCount >>= 1;
            }
            int savedPoint = outBuf.position();
            this.transcodeSlice(inBuf, outBuf, fh.qMatLuma, fh.qMatChroma, fh.scan, sliceMbCount, mbX, mbY, ph.sliceSizes[i], qp);
            short encodedSize = (short) (outBuf.position() - savedPoint);
            sliceSizes.putShort(encodedSize);
            int max = (sliceMbCount * this.bitsPer1024High >> 5) + 6;
            int low = (sliceMbCount * this.bitsPer1024Low >> 5) + 6;
            if (encodedSize > max && qp < 128) {
                if (encodedSize > max + balance && ++qp < 128) {
                    qp++;
                }
            } else if (encodedSize < low && qp > 2 && balance > 0) {
                qp--;
            }
            balance += max - encodedSize;
            mbX += sliceMbCount;
            if (mbX == mbWidth) {
                sliceMbCount = 1 << ph.log2SliceMbWidth;
                mbX = 0;
                mbY++;
            }
        }
    }

    private void transcodeSlice(ByteBuffer inBuf, ByteBuffer outBuf, int[] qMatLuma, int[] qMatChroma, int[] scan, int sliceMbCount, int mbX, int mbY, short sliceSize, int qp) {
        int hdrSize = (inBuf.get() & 255) >> 3;
        int qScaleOrig = ProresDecoder.clip(inBuf.get() & 255, 1, 224);
        int qScale = qScaleOrig > 128 ? qScaleOrig - 96 << 2 : qScaleOrig;
        int yDataSize = inBuf.getShort();
        int uDataSize = inBuf.getShort();
        int vDataSize = sliceSize - uDataSize - yDataSize - hdrSize;
        outBuf.put((byte) 48);
        outBuf.put((byte) qp);
        ByteBuffer beforeSizes = outBuf.duplicate();
        outBuf.putInt(0);
        int beforeY = outBuf.position();
        this.requant(ProresDecoder.bitstream(inBuf, yDataSize), new BitWriter(outBuf), sliceMbCount << 2, ProresDecoder.scaleMat(qMatLuma, qScale), ProresDecoder.scaleMat(this.qMatLumaTo, qp), scan, mbX, mbY, 0);
        int beforeCb = outBuf.position();
        this.requant(ProresDecoder.bitstream(inBuf, uDataSize), new BitWriter(outBuf), sliceMbCount << 1, ProresDecoder.scaleMat(qMatChroma, qScale), ProresDecoder.scaleMat(this.qMatChromaTo, qp), scan, mbX, mbY, 1);
        int beforeCr = outBuf.position();
        this.requant(ProresDecoder.bitstream(inBuf, vDataSize), new BitWriter(outBuf), sliceMbCount << 1, ProresDecoder.scaleMat(qMatChroma, qScale), ProresDecoder.scaleMat(this.qMatChromaTo, qp), scan, mbX, mbY, 2);
        beforeSizes.putShort((short) (beforeCb - beforeY));
        beforeSizes.putShort((short) (beforeCr - beforeCb));
    }
}