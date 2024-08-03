package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer;

import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimecodeSampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.movtool.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TimecodeMP4MuxerTrack extends CodecMP4MuxerTrack {

    private TapeTimecode prevTimecode;

    private TapeTimecode firstTimecode;

    private int fpsEstimate;

    private long sampleDuration;

    private long samplePts;

    private int tcFrames;

    private List<Edit> lower = new ArrayList();

    private List<Packet> gop = new ArrayList();

    public TimecodeMP4MuxerTrack(int trackId) {
        super(trackId, MP4TrackType.TIMECODE, Codec.TIMECODE);
    }

    public void addTimecode(Packet packet) throws IOException {
        if (this._timescale == -1) {
            this._timescale = packet.getTimescale();
        }
        if (this._timescale != -1 && this._timescale != packet.getTimescale()) {
            throw new RuntimeException("MP4 timecode track doesn't support timescale switching.");
        } else {
            if (packet.isKeyFrame()) {
                this.processGop();
            }
            this.gop.add(Packet.createPacketWithData(packet, (ByteBuffer) null));
        }
    }

    private void processGop() throws IOException {
        if (this.gop.size() > 0) {
            for (Packet pkt : this.sortByDisplay(this.gop)) {
                this.addTimecodeInt(pkt);
            }
            this.gop.clear();
        }
    }

    private List<Packet> sortByDisplay(List<Packet> gop) {
        ArrayList<Packet> result = new ArrayList(gop);
        Collections.sort(result, new Comparator<Packet>() {

            public int compare(Packet o1, Packet o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return -1;
                } else if (o2 == null) {
                    return 1;
                } else {
                    return o1.getDisplayOrder() > o2.getDisplayOrder() ? 1 : (o1.getDisplayOrder() == o2.getDisplayOrder() ? 0 : -1);
                }
            }
        });
        return result;
    }

    @Override
    protected Box finish(MovieHeaderBox mvhd) throws IOException {
        this.processGop();
        this.outTimecodeSample();
        if (this.sampleEntries.size() == 0) {
            return null;
        } else {
            if (this.edits != null) {
                this.edits = Util.editsOnEdits(new Rational(1, 1), this.lower, this.edits);
            } else {
                this.edits = this.lower;
            }
            return super.finish(mvhd);
        }
    }

    private void addTimecodeInt(Packet packet) throws IOException {
        TapeTimecode tapeTimecode = packet.getTapeTimecode();
        boolean gap = this.isGap(this.prevTimecode, tapeTimecode);
        this.prevTimecode = tapeTimecode;
        if (gap) {
            this.outTimecodeSample();
            this.firstTimecode = tapeTimecode;
            this.fpsEstimate = tapeTimecode.isDropFrame() ? 30 : -1;
            this.samplePts = this.samplePts + this.sampleDuration;
            this.sampleDuration = 0L;
            this.tcFrames = 0;
        }
        this.sampleDuration = this.sampleDuration + packet.getDuration();
        this.tcFrames++;
    }

    private boolean isGap(TapeTimecode prevTimecode, TapeTimecode tapeTimecode) {
        boolean gap = false;
        if (prevTimecode == null && tapeTimecode != null) {
            gap = true;
        } else if (prevTimecode != null) {
            if (tapeTimecode == null) {
                gap = true;
            } else if (prevTimecode.isDropFrame() != tapeTimecode.isDropFrame()) {
                gap = true;
            } else {
                gap = this.isTimeGap(prevTimecode, tapeTimecode);
            }
        }
        return gap;
    }

    private boolean isTimeGap(TapeTimecode prevTimecode, TapeTimecode tapeTimecode) {
        boolean gap = false;
        int sec = toSec(tapeTimecode);
        int secDiff = sec - toSec(prevTimecode);
        if (secDiff == 0) {
            int frameDiff = tapeTimecode.getFrame() - prevTimecode.getFrame();
            if (this.fpsEstimate != -1) {
                frameDiff = (frameDiff + this.fpsEstimate) % this.fpsEstimate;
            }
            gap = frameDiff != 1;
        } else if (secDiff == 1) {
            if (this.fpsEstimate == -1) {
                if (tapeTimecode.getFrame() == 0) {
                    this.fpsEstimate = prevTimecode.getFrame() + 1;
                } else {
                    gap = true;
                }
            } else {
                int firstFrame = tapeTimecode.isDropFrame() && sec % 60 == 0 && sec % 600 != 0 ? 2 : 0;
                if (tapeTimecode.getFrame() != firstFrame || prevTimecode.getFrame() != this.fpsEstimate - 1) {
                    gap = true;
                }
            }
        } else {
            gap = true;
        }
        return gap;
    }

    private void outTimecodeSample() throws IOException {
        if (this.sampleDuration > 0L) {
            if (this.firstTimecode != null) {
                if (this.fpsEstimate == -1) {
                    this.fpsEstimate = this.prevTimecode.getFrame() + 1;
                }
                TimecodeSampleEntry tmcd = TimecodeSampleEntry.createTimecodeSampleEntry(this.firstTimecode.isDropFrame() ? 1 : 0, this._timescale, (int) (this.sampleDuration / (long) this.tcFrames), this.fpsEstimate);
                this.sampleEntries.add(tmcd);
                ByteBuffer sample = ByteBuffer.allocate(4);
                sample.putInt(this.toCounter(this.firstTimecode, this.fpsEstimate));
                sample.flip();
                this.addFrame(MP4Packet.createMP4Packet(sample, this.samplePts, this._timescale, this.sampleDuration, 0L, Packet.FrameType.KEY, null, 0, this.samplePts, this.sampleEntries.size() - 1));
                this.lower.add(new Edit(this.sampleDuration, this.samplePts, 1.0F));
            } else {
                this.lower.add(new Edit(this.sampleDuration, -1L, 1.0F));
            }
        }
    }

    private int toCounter(TapeTimecode tc, int fps) {
        int frames = toSec(tc) * fps + tc.getFrame();
        if (tc.isDropFrame()) {
            long D = (long) (frames / 18000);
            long M = (long) (frames % 18000);
            frames = (int) ((long) frames - (18L * D + 2L * ((M - 2L) / 1800L)));
        }
        return frames;
    }

    private static int toSec(TapeTimecode tc) {
        return tc.getHour() * 3600 + tc.getMinute() * 60 + tc.getSecond();
    }
}