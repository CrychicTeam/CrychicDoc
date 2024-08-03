package com.github.alexthe666.citadel.repack.jcodec.codecs.mpa;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.AudioBuffer;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Mp3Decoder implements AudioDecoder {

    private static final boolean[] ALL_TRUE = new boolean[] { true, true, true, true };

    private static final int SAMPLES_PER_BAND = 18;

    private static final int NUM_BANDS = 32;

    private ChannelSynthesizer[] filter = new ChannelSynthesizer[] { null, null };

    private boolean initialized;

    private static final double fourByThree = 1.3333333333333333;

    private float[][] prevBlk;

    private ByteBuffer frameData = ByteBuffer.allocate(4096);

    private int channels;

    private int sfreq;

    private float[] samples = new float[32];

    private float[] mdctIn = new float[18];

    private float[] mdctOut = new float[36];

    private float[][] dequant = new float[2][576];

    private short[][] tmpOut = new short[2][576];

    private void init(MpaHeader header) {
        float scalefactor = 32700.0F;
        this.channels = header.mode == 3 ? 1 : 2;
        this.filter[0] = new ChannelSynthesizer(0, scalefactor);
        if (this.channels == 2) {
            this.filter[1] = new ChannelSynthesizer(1, scalefactor);
        }
        this.prevBlk = new float[2][576];
        this.sfreq = header.sampleFreq + (header.version == 1 ? 3 : (header.version == 2 ? 6 : 0));
        for (int ch = 0; ch < 2; ch++) {
            Arrays.fill(this.prevBlk[ch], 0.0F);
        }
        this.initialized = true;
    }

    private void decodeGranule(MpaHeader header, ByteBuffer output, Mp3Bitstream.MP3SideInfo si, BitReader br, Mp3Bitstream.ScaleFactors[] scalefac, int grInd) {
        Arrays.fill(this.dequant[0], 0.0F);
        Arrays.fill(this.dequant[1], 0.0F);
        for (int ch = 0; ch < this.channels; ch++) {
            int part2Start = br.position();
            Mp3Bitstream.Granule granule = si.granule[ch][grInd];
            if (header.version == 1) {
                Mp3Bitstream.ScaleFactors old = scalefac[ch];
                boolean[] scfi = grInd == 0 ? ALL_TRUE : si.scfsi[ch];
                scalefac[ch] = Mp3Bitstream.readScaleFactors(br, si.granule[ch][grInd], scfi);
                this.mergeScaleFac(scalefac[ch], old, scfi);
            } else {
                scalefac[ch] = Mp3Bitstream.readLSFScaleFactors(br, header, granule, ch);
            }
            int[] coeffs = new int[580];
            int nonzero = Mp3Bitstream.readCoeffs(br, granule, ch, part2Start, this.sfreq, coeffs);
            this.dequantizeCoeffs(coeffs, nonzero, granule, scalefac[ch], this.dequant[ch]);
        }
        boolean msStereo = header.mode == 1 && (header.modeExtension & 2) != 0;
        if (msStereo && this.channels == 2) {
            this.decodeMsStereo(header, si.granule[0][grInd], scalefac, this.dequant);
        }
        for (int ch = 0; ch < this.channels; ch++) {
            float[] out = this.dequant[ch];
            Mp3Bitstream.Granule granule = si.granule[ch][grInd];
            this.antialias(granule, out);
            this.mdctDecode(ch, granule, out);
            for (int sb18 = 18; sb18 < 576; sb18 += 36) {
                for (int ss = 1; ss < 18; ss += 2) {
                    out[sb18 + ss] = -out[sb18 + ss];
                }
            }
            int ss = 0;
            for (int off = 0; ss < 18; off += 32) {
                int sb18 = 0;
                for (int sb = 0; sb18 < 576; sb++) {
                    this.samples[sb] = out[sb18 + ss];
                    sb18 += 18;
                }
                this.filter[ch].synthesize(this.samples, this.tmpOut[ch], off);
                ss++;
            }
        }
        if (this.channels == 2) {
            appendSamplesInterleave(output, this.tmpOut[0], this.tmpOut[1], 576);
        } else {
            appendSamples(output, this.tmpOut[0], 576);
        }
    }

    public static void appendSamples(ByteBuffer buf, short[] f, int n) {
        for (int i = 0; i < n; i++) {
            buf.putShort(f[i]);
        }
    }

    public static void appendSamplesInterleave(ByteBuffer buf, short[] f0, short[] f1, int n) {
        for (int i = 0; i < n; i++) {
            buf.putShort(f0[i]);
            buf.putShort(f1[i]);
        }
    }

    private void mergeScaleFac(Mp3Bitstream.ScaleFactors sf, Mp3Bitstream.ScaleFactors old, boolean[] scfsi) {
        if (!scfsi[0]) {
            for (int i = 0; i < 6; i++) {
                sf.large[i] = old.large[i];
            }
        }
        if (!scfsi[1]) {
            for (int i = 6; i < 11; i++) {
                sf.large[i] = old.large[i];
            }
        }
        if (!scfsi[2]) {
            for (int i = 11; i < 16; i++) {
                sf.large[i] = old.large[i];
            }
        }
        if (!scfsi[3]) {
            for (int i = 16; i < 21; i++) {
                sf.large[i] = old.large[i];
            }
        }
    }

    private void dequantizeCoeffs(int[] input, int nonzero, Mp3Bitstream.Granule granule, Mp3Bitstream.ScaleFactors scalefac, float[] out) {
        float globalGain = (float) Math.pow(2.0, 0.25 * ((double) granule.globalGain - 210.0));
        if (!granule.windowSwitchingFlag || granule.blockType != 2) {
            this.dequantLong(input, nonzero, granule, scalefac, globalGain, out);
        } else if (granule.mixedBlockFlag) {
            this.dequantMixed(input, nonzero, granule, scalefac, globalGain, out);
        } else {
            this.dequantShort(input, nonzero, granule, scalefac, globalGain, out);
        }
    }

    private void dequantMixed(int[] input, int nonzero, Mp3Bitstream.Granule granule, Mp3Bitstream.ScaleFactors scalefac, float globalGain, float[] out) {
        int i = 0;
        for (int sfb = 0; sfb < 8 && i < nonzero; sfb++) {
            while (i < MpaConst.sfbLong[this.sfreq][sfb + 1] && i < nonzero) {
                int idx = scalefac.large[sfb] + (granule.preflag ? MpaConst.pretab[sfb] : 0) << granule.scalefacScale;
                out[i] = globalGain * this.pow43(input[i]) * MpaConst.quantizerTab[idx];
                i++;
            }
        }
        for (int sfb = 3; sfb < 12 && i < nonzero; sfb++) {
            int sfbSz = MpaConst.sfbShort[this.sfreq][sfb + 1] - MpaConst.sfbShort[this.sfreq][sfb];
            int sfbStart = i;
            for (int wnd = 0; wnd < 3; wnd++) {
                for (int j = 0; j < sfbSz && i < nonzero; i++) {
                    int idx = (scalefac.small[wnd][sfb] << granule.scalefacScale) + (granule.subblockGain[wnd] << 2);
                    out[sfbStart + j * 3 + wnd] = globalGain * this.pow43(input[i]) * MpaConst.quantizerTab[idx];
                    j++;
                }
            }
        }
    }

    private void dequantShort(int[] input, int nonzero, Mp3Bitstream.Granule granule, Mp3Bitstream.ScaleFactors scalefac, float globalGain, float[] out) {
        int sfb = 0;
        for (int i = 0; i < nonzero; sfb++) {
            int sfbSz = MpaConst.sfbShort[this.sfreq][sfb + 1] - MpaConst.sfbShort[this.sfreq][sfb];
            int sfbStart = i;
            for (int wnd = 0; wnd < 3; wnd++) {
                for (int j = 0; j < sfbSz && i < nonzero; i++) {
                    int idx = (scalefac.small[wnd][sfb] << granule.scalefacScale) + (granule.subblockGain[wnd] << 2);
                    out[sfbStart + j * 3 + wnd] = globalGain * this.pow43(input[i]) * MpaConst.quantizerTab[idx];
                    j++;
                }
            }
        }
    }

    private void dequantLong(int[] input, int nonzero, Mp3Bitstream.Granule granule, Mp3Bitstream.ScaleFactors scalefac, float globalGain, float[] out) {
        int i = 0;
        for (int sfb = 0; i < nonzero; i++) {
            if (i == MpaConst.sfbLong[this.sfreq][sfb + 1]) {
                sfb++;
            }
            int idx = scalefac.large[sfb] + (granule.preflag ? MpaConst.pretab[sfb] : 0) << granule.scalefacScale;
            out[i] = globalGain * this.pow43(input[i]) * MpaConst.quantizerTab[idx];
        }
    }

    private float pow43(int val) {
        if (val == 0) {
            return 0.0F;
        } else {
            int sign = 1 - (val >>> 31 << 1);
            int abs = MathUtil.abs(val);
            return abs < MpaConst.power43Tab.length ? (float) sign * MpaConst.power43Tab[abs] : (float) sign * (float) Math.pow((double) abs, 1.3333333333333333);
        }
    }

    private void decodeMsStereo(MpaHeader header, Mp3Bitstream.Granule granule, Mp3Bitstream.ScaleFactors[] scalefac, float[][] ro) {
        for (int i = 0; i < 576; i++) {
            float a = ro[0][i];
            float b = ro[1][i];
            ro[0][i] = (a + b) * 0.70710677F;
            ro[1][i] = (a - b) * 0.70710677F;
        }
    }

    private void antialias(Mp3Bitstream.Granule granule, float[] out) {
        if (!granule.windowSwitchingFlag || granule.blockType != 2 || granule.mixedBlockFlag) {
            int bands = granule.windowSwitchingFlag && granule.mixedBlockFlag && granule.blockType == 2 ? 1 : 31;
            int band = 0;
            for (int bandStart = 0; band < bands; bandStart += 18) {
                for (int sample = 0; sample < 8; sample++) {
                    int src_idx1 = bandStart + 17 - sample;
                    int src_idx2 = bandStart + 18 + sample;
                    float bu = out[src_idx1];
                    float bd = out[src_idx2];
                    out[src_idx1] = bu * MpaConst.cs[sample] - bd * MpaConst.ca[sample];
                    out[src_idx2] = bd * MpaConst.cs[sample] + bu * MpaConst.ca[sample];
                }
                band++;
            }
        }
    }

    private void mdctDecode(int ch, Mp3Bitstream.Granule granule, float[] out) {
        for (int sb18 = 0; sb18 < 576; sb18 += 18) {
            int blockType = granule.windowSwitchingFlag && granule.mixedBlockFlag && sb18 < 36 ? 0 : granule.blockType;
            for (int cc = 0; cc < 18; cc++) {
                this.mdctIn[cc] = out[cc + sb18];
            }
            if (blockType == 2) {
                Mp3Mdct.threeShort(this.mdctIn, this.mdctOut);
            } else {
                Mp3Mdct.oneLong(this.mdctIn, this.mdctOut);
                for (int i = 0; i < 36; i++) {
                    this.mdctOut[i] = this.mdctOut[i] * MpaConst.win[blockType][i];
                }
            }
            for (int i = 0; i < 18; i++) {
                out[i + sb18] = this.mdctOut[i] + this.prevBlk[ch][sb18 + i];
                this.prevBlk[ch][sb18 + i] = this.mdctOut[18 + i];
            }
        }
    }

    @Override
    public AudioBuffer decodeFrame(ByteBuffer frame, ByteBuffer dst) throws IOException {
        MpaHeader header = MpaHeader.read_header(frame);
        if (!this.initialized) {
            this.init(header);
        }
        boolean intensityStereo = header.mode == 1 && (header.modeExtension & 1) != 0;
        if (intensityStereo) {
            throw new RuntimeException("Intensity stereo is not supported.");
        } else {
            dst.order(ByteOrder.LITTLE_ENDIAN);
            Mp3Bitstream.MP3SideInfo si = Mp3Bitstream.readSideInfo(header, frame, this.channels);
            int reserve = this.frameData.position();
            this.frameData.put(NIOUtils.read(frame, header.frameBytes));
            this.frameData.flip();
            if (header.protectionBit == 0) {
                frame.getShort();
            }
            NIOUtils.skip(this.frameData, reserve - si.mainDataBegin);
            BitReader br = BitReader.createBitReader(this.frameData);
            Mp3Bitstream.ScaleFactors[] scalefac = new Mp3Bitstream.ScaleFactors[2];
            this.decodeGranule(header, dst, si, br, scalefac, 0);
            if (header.version == 1) {
                this.decodeGranule(header, dst, si, br, scalefac, 1);
            }
            br.terminate();
            NIOUtils.relocateLeftover(this.frameData);
            dst.flip();
            return new AudioBuffer(dst, null, 1);
        }
    }

    @Override
    public AudioCodecMeta getCodecMeta(ByteBuffer data) throws IOException {
        MpaHeader header = MpaHeader.read_header(data.duplicate());
        AudioFormat format = new AudioFormat(MpaConst.frequencies[header.version][header.sampleFreq], 16, header.mode == 3 ? 1 : 2, true, false);
        return AudioCodecMeta.fromAudioFormat(format);
    }
}