package com.github.alexthe666.citadel.repack.jcodec.codecs.h264;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.SliceHeaderReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnitType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.RefPicMarking;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.IntObjectMap;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BufferH264ES implements DemuxerTrack, Demuxer {

    private ByteBuffer bb;

    private IntObjectMap<PictureParameterSet> pps = new IntObjectMap<>();

    private IntObjectMap<SeqParameterSet> sps = new IntObjectMap<>();

    private int prevFrameNumOffset;

    private int prevFrameNum;

    private int prevPicOrderCntMsb;

    private int prevPicOrderCntLsb;

    private int frameNo;

    public BufferH264ES(ByteBuffer bb) {
        this.bb = bb;
        this.frameNo = 0;
    }

    @Override
    public Packet nextFrame() {
        ByteBuffer result = this.bb.duplicate();
        NALUnit prevNu = null;
        SliceHeader prevSh = null;
        while (true) {
            this.bb.mark();
            ByteBuffer buf = H264Utils.nextNALUnit(this.bb);
            if (buf == null) {
                break;
            }
            NALUnit nu = NALUnit.read(buf);
            if (nu.type == NALUnitType.IDR_SLICE || nu.type == NALUnitType.NON_IDR_SLICE) {
                SliceHeader sh = this.readSliceHeader(buf, nu);
                if (prevNu != null && prevSh != null && !this.sameFrame(prevNu, nu, prevSh, sh)) {
                    this.bb.reset();
                    break;
                }
                prevSh = sh;
                prevNu = nu;
            } else if (nu.type == NALUnitType.PPS) {
                PictureParameterSet read = PictureParameterSet.read(buf);
                this.pps.put(read.picParameterSetId, read);
            } else if (nu.type == NALUnitType.SPS) {
                SeqParameterSet read = SeqParameterSet.read(buf);
                this.sps.put(read.seqParameterSetId, read);
            }
        }
        result.limit(this.bb.position());
        return prevSh == null ? null : this.detectPoc(result, prevNu, prevSh);
    }

    private SliceHeader readSliceHeader(ByteBuffer buf, NALUnit nu) {
        BitReader br = BitReader.createBitReader(buf);
        SliceHeader sh = SliceHeaderReader.readPart1(br);
        PictureParameterSet pp = this.pps.get(sh.picParameterSetId);
        SliceHeaderReader.readPart2(sh, nu, this.sps.get(pp.seqParameterSetId), pp, br);
        return sh;
    }

    private boolean sameFrame(NALUnit nu1, NALUnit nu2, SliceHeader sh1, SliceHeader sh2) {
        if (sh1.picParameterSetId != sh2.picParameterSetId) {
            return false;
        } else if (sh1.frameNum != sh2.frameNum) {
            return false;
        } else {
            SeqParameterSet sps = sh1.sps;
            if (sps.picOrderCntType == 0 && sh1.picOrderCntLsb != sh2.picOrderCntLsb) {
                return false;
            } else if (sps.picOrderCntType != 1 || sh1.deltaPicOrderCnt[0] == sh2.deltaPicOrderCnt[0] && sh1.deltaPicOrderCnt[1] == sh2.deltaPicOrderCnt[1]) {
                if ((nu1.nal_ref_idc == 0 || nu2.nal_ref_idc == 0) && nu1.nal_ref_idc != nu2.nal_ref_idc) {
                    return false;
                } else {
                    return nu1.type == NALUnitType.IDR_SLICE != (nu2.type == NALUnitType.IDR_SLICE) ? false : sh1.idrPicId == sh2.idrPicId;
                }
            } else {
                return false;
            }
        }
    }

    private Packet detectPoc(ByteBuffer result, NALUnit nu, SliceHeader sh) {
        int maxFrameNum = 1 << sh.sps.log2MaxFrameNumMinus4 + 4;
        if (this.detectGap(sh, maxFrameNum)) {
            this.issueNonExistingPic(sh, maxFrameNum);
        }
        int absFrameNum = this.updateFrameNumber(sh.frameNum, maxFrameNum, this.detectMMCO5(sh.refPicMarkingNonIDR));
        int poc = 0;
        if (nu.type == NALUnitType.NON_IDR_SLICE) {
            poc = this.calcPoc(absFrameNum, nu, sh);
        }
        return new Packet(result, (long) absFrameNum, 1, 1L, (long) (this.frameNo++), nu.type == NALUnitType.IDR_SLICE ? Packet.FrameType.KEY : Packet.FrameType.INTER, null, poc);
    }

    private int updateFrameNumber(int frameNo, int maxFrameNum, boolean mmco5) {
        int frameNumOffset;
        if (this.prevFrameNum > frameNo) {
            frameNumOffset = this.prevFrameNumOffset + maxFrameNum;
        } else {
            frameNumOffset = this.prevFrameNumOffset;
        }
        int absFrameNum = frameNumOffset + frameNo;
        this.prevFrameNum = mmco5 ? 0 : frameNo;
        this.prevFrameNumOffset = frameNumOffset;
        return absFrameNum;
    }

    private void issueNonExistingPic(SliceHeader sh, int maxFrameNum) {
        int nextFrameNum = (this.prevFrameNum + 1) % maxFrameNum;
        this.prevFrameNum = nextFrameNum;
    }

    private boolean detectGap(SliceHeader sh, int maxFrameNum) {
        return sh.frameNum != this.prevFrameNum && sh.frameNum != (this.prevFrameNum + 1) % maxFrameNum;
    }

    private int calcPoc(int absFrameNum, NALUnit nu, SliceHeader sh) {
        if (sh.sps.picOrderCntType == 0) {
            return this.calcPOC0(nu, sh);
        } else {
            return sh.sps.picOrderCntType == 1 ? this.calcPOC1(absFrameNum, nu, sh) : this.calcPOC2(absFrameNum, nu, sh);
        }
    }

    private int calcPOC2(int absFrameNum, NALUnit nu, SliceHeader sh) {
        return nu.nal_ref_idc == 0 ? 2 * absFrameNum - 1 : 2 * absFrameNum;
    }

    private int calcPOC1(int absFrameNum, NALUnit nu, SliceHeader sh) {
        if (sh.sps.numRefFramesInPicOrderCntCycle == 0) {
            absFrameNum = 0;
        }
        if (nu.nal_ref_idc == 0 && absFrameNum > 0) {
            absFrameNum--;
        }
        int expectedDeltaPerPicOrderCntCycle = 0;
        for (int i = 0; i < sh.sps.numRefFramesInPicOrderCntCycle; i++) {
            expectedDeltaPerPicOrderCntCycle += sh.sps.offsetForRefFrame[i];
        }
        int expectedPicOrderCnt;
        if (absFrameNum > 0) {
            int picOrderCntCycleCnt = (absFrameNum - 1) / sh.sps.numRefFramesInPicOrderCntCycle;
            int frameNumInPicOrderCntCycle = (absFrameNum - 1) % sh.sps.numRefFramesInPicOrderCntCycle;
            expectedPicOrderCnt = picOrderCntCycleCnt * expectedDeltaPerPicOrderCntCycle;
            for (int i = 0; i <= frameNumInPicOrderCntCycle; i++) {
                expectedPicOrderCnt += sh.sps.offsetForRefFrame[i];
            }
        } else {
            expectedPicOrderCnt = 0;
        }
        if (nu.nal_ref_idc == 0) {
            expectedPicOrderCnt += sh.sps.offsetForNonRefPic;
        }
        return expectedPicOrderCnt + sh.deltaPicOrderCnt[0];
    }

    private int calcPOC0(NALUnit nu, SliceHeader sh) {
        int pocCntLsb = sh.picOrderCntLsb;
        int maxPicOrderCntLsb = 1 << sh.sps.log2MaxPicOrderCntLsbMinus4 + 4;
        int picOrderCntMsb;
        if (pocCntLsb < this.prevPicOrderCntLsb && this.prevPicOrderCntLsb - pocCntLsb >= maxPicOrderCntLsb / 2) {
            picOrderCntMsb = this.prevPicOrderCntMsb + maxPicOrderCntLsb;
        } else if (pocCntLsb > this.prevPicOrderCntLsb && pocCntLsb - this.prevPicOrderCntLsb > maxPicOrderCntLsb / 2) {
            picOrderCntMsb = this.prevPicOrderCntMsb - maxPicOrderCntLsb;
        } else {
            picOrderCntMsb = this.prevPicOrderCntMsb;
        }
        if (nu.nal_ref_idc != 0) {
            this.prevPicOrderCntMsb = picOrderCntMsb;
            this.prevPicOrderCntLsb = pocCntLsb;
        }
        return picOrderCntMsb + pocCntLsb;
    }

    private boolean detectMMCO5(RefPicMarking refPicMarkingNonIDR) {
        if (refPicMarkingNonIDR == null) {
            return false;
        } else {
            RefPicMarking.Instruction[] instructions = refPicMarkingNonIDR.getInstructions();
            for (int i = 0; i < instructions.length; i++) {
                RefPicMarking.Instruction instr = instructions[i];
                if (instr.getType() == RefPicMarking.InstrType.CLEAR) {
                    return true;
                }
            }
            return false;
        }
    }

    public SeqParameterSet[] getSps() {
        return this.sps.values(new SeqParameterSet[0]);
    }

    public PictureParameterSet[] getPps() {
        return this.pps.values(new PictureParameterSet[0]);
    }

    @Override
    public DemuxerTrackMeta getMeta() {
        return null;
    }

    public void close() throws IOException {
    }

    @Override
    public List<? extends DemuxerTrack> getTracks() {
        return this.getVideoTracks();
    }

    @Override
    public List<? extends DemuxerTrack> getVideoTracks() {
        List<DemuxerTrack> tracks = new ArrayList();
        tracks.add(this);
        return tracks;
    }

    @Override
    public List<? extends DemuxerTrack> getAudioTracks() {
        List<DemuxerTrack> tracks = new ArrayList();
        return tracks;
    }
}