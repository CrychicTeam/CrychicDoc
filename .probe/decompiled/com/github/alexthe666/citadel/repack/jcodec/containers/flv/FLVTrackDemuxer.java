package com.github.alexthe666.citadel.repack.jcodec.containers.flv;

import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.LongArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.TrackType;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class FLVTrackDemuxer {

    private static final int MAX_CRAWL_DISTANCE_SEC = 10;

    private FLVReader demuxer;

    private FLVTrackDemuxer.FLVDemuxerTrack video;

    private FLVTrackDemuxer.FLVDemuxerTrack audio;

    private LinkedList<FLVTag> packets = new LinkedList();

    private SeekableByteChannel _in;

    public FLVTrackDemuxer(SeekableByteChannel _in) throws IOException {
        this._in = _in;
        _in.setPosition(0L);
        this.demuxer = new FLVReader(_in);
        this.video = new FLVTrackDemuxer.FLVDemuxerTrack(this, FLVTag.Type.VIDEO);
        this.audio = new FLVTrackDemuxer.FLVDemuxerTrack(this, FLVTag.Type.AUDIO);
    }

    private void resetToPosition(long position) throws IOException {
        this._in.setPosition(position);
        this.demuxer.reset();
        this.packets.clear();
    }

    private void seekI(double second) throws IOException {
        this.packets.clear();
        FLVTag base;
        while ((base = this.demuxer.readNextPacket()) != null && base.getPtsD() == 0.0) {
        }
        if (base != null) {
            this._in.setPosition(base.getPosition() + 1048576L);
            this.demuxer.reposition();
            FLVTag off = this.demuxer.readNextPacket();
            int byteRate = (int) ((double) (off.getPosition() - base.getPosition()) / (off.getPtsD() - base.getPtsD()));
            long offset = base.getPosition() + (long) ((second - base.getPtsD()) * (double) byteRate);
            this._in.setPosition(offset);
            this.demuxer.reposition();
            for (int i = 0; i < 5; i++) {
                FLVTag pkt = this.demuxer.readNextPacket();
                double distance = second - pkt.getPtsD();
                if (distance > 0.0 && distance < 10.0) {
                    System.out.println("Crawling forward: " + distance);
                    FLVTag testPkt;
                    while ((testPkt = this.demuxer.readNextPacket()) != null && testPkt.getPtsD() < second) {
                    }
                    if (testPkt != null) {
                        this.packets.add(pkt);
                    }
                    return;
                }
                if (distance < 0.0 && distance > -10.0) {
                    System.out.println("Overshoot by: " + -distance);
                    this._in.setPosition(pkt.getPosition() + (long) ((distance - 1.0) * (double) byteRate));
                    this.demuxer.reposition();
                }
            }
        }
    }

    private FLVTag nextFrameI(FLVTag.Type type, boolean remove) throws IOException {
        Iterator<FLVTag> it = this.packets.iterator();
        while (it.hasNext()) {
            FLVTag pkt = (FLVTag) it.next();
            if (pkt.getType() == type) {
                if (remove) {
                    it.remove();
                }
                return pkt;
            }
        }
        while ((pkt = this.demuxer.readNextPacket()) != null && pkt.getType() != type) {
            this.packets.add(pkt);
        }
        if (!remove) {
            this.packets.add(pkt);
        }
        return pkt;
    }

    private FLVTag prevFrameI(FLVTag.Type type, boolean remove) throws IOException {
        ListIterator<FLVTag> it = this.packets.listIterator();
        while (it.hasPrevious()) {
            FLVTag pkt = (FLVTag) it.previous();
            if (pkt.getType() == type) {
                if (remove) {
                    it.remove();
                }
                return pkt;
            }
        }
        while ((pkt = this.demuxer.readPrevPacket()) != null && pkt.getType() != type) {
            this.packets.add(0, pkt);
        }
        if (!remove) {
            this.packets.add(0, pkt);
        }
        return pkt;
    }

    public DemuxerTrack[] getTracks() {
        return new DemuxerTrack[] { this.video, this.audio };
    }

    public DemuxerTrack getVideoTrack() {
        return this.video;
    }

    public DemuxerTrack getAudioTrack() {
        return this.video;
    }

    public static class FLVDemuxerTrack implements SeekableDemuxerTrack {

        private FLVTag.Type type;

        private int curFrame;

        private Codec codec;

        private LongArrayList framePositions = LongArrayList.createLongArrayList();

        private byte[] codecPrivate;

        private FLVTrackDemuxer demuxer;

        public FLVDemuxerTrack(FLVTrackDemuxer demuxer, FLVTag.Type type) throws IOException {
            this.demuxer = demuxer;
            this.type = type;
            FLVTag frame = demuxer.nextFrameI(type, false);
            this.codec = frame.getTagHeader().getCodec();
        }

        @Override
        public Packet nextFrame() throws IOException {
            FLVTag frame = this.demuxer.nextFrameI(this.type, true);
            this.framePositions.add(frame.getPosition());
            return this.toPacket(frame);
        }

        public Packet prevFrame() throws IOException {
            FLVTag frame = this.demuxer.prevFrameI(this.type, true);
            return this.toPacket(frame);
        }

        public Packet pickFrame() throws IOException {
            FLVTag frame = this.demuxer.nextFrameI(this.type, false);
            return this.toPacket(frame);
        }

        private Packet toPacket(FLVTag frame) {
            return null;
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            TrackType t = this.type == FLVTag.Type.VIDEO ? TrackType.VIDEO : TrackType.AUDIO;
            return new DemuxerTrackMeta(t, this.codec, 0.0, null, 0, ByteBuffer.wrap(this.codecPrivate), null, null);
        }

        @Override
        public boolean gotoFrame(long i) throws IOException {
            if (i >= (long) this.framePositions.size()) {
                return false;
            } else {
                this.demuxer.resetToPosition(this.framePositions.get((int) i));
                return true;
            }
        }

        @Override
        public boolean gotoSyncFrame(long i) {
            throw new RuntimeException();
        }

        @Override
        public long getCurFrame() {
            return (long) this.curFrame;
        }

        @Override
        public void seek(double second) throws IOException {
            this.demuxer.seekI(second);
        }
    }
}