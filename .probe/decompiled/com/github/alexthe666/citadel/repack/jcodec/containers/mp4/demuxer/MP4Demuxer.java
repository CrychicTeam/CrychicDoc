package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer;

import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.Fourcc;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.HandlerBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MP4Demuxer implements Demuxer {

    private List<AbstractMP4DemuxerTrack> tracks;

    private TimecodeMP4DemuxerTrack timecodeTrack;

    MovieBox movie;

    protected SeekableByteChannel input;

    public static MP4Demuxer createMP4Demuxer(SeekableByteChannel input) throws IOException {
        return new MP4Demuxer(input);
    }

    public static MP4Demuxer createRawMP4Demuxer(SeekableByteChannel input) throws IOException {
        return new MP4Demuxer(input) {

            @Override
            protected AbstractMP4DemuxerTrack newTrack(TrakBox trak) {
                return new MP4DemuxerTrack(this.movie, trak, this.input);
            }
        };
    }

    private AbstractMP4DemuxerTrack fromTrakBox(TrakBox trak) {
        SampleSizesBox stsz = NodeBox.findFirstPath(trak, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        return (AbstractMP4DemuxerTrack) (stsz.getDefaultSize() == 0 ? this.newTrack(trak) : new PCMMP4DemuxerTrack(this.movie, trak, this.input));
    }

    protected AbstractMP4DemuxerTrack newTrack(TrakBox trak) {
        return new CodecMP4DemuxerTrack(this.movie, trak, this.input);
    }

    MP4Demuxer(SeekableByteChannel input) throws IOException {
        this.input = input;
        this.tracks = new LinkedList();
        this.findMovieBox(input);
    }

    private void findMovieBox(SeekableByteChannel input) throws IOException {
        MP4Util.Movie mv = MP4Util.parseFullMovieChannel(input);
        if (mv != null && mv.getMoov() != null) {
            this.movie = mv.getMoov();
            this.processHeader(this.movie);
        } else {
            throw new IOException("Could not find movie meta information box");
        }
    }

    private void processHeader(NodeBox moov) throws IOException {
        TrakBox tt = null;
        TrakBox[] trakBoxs = NodeBox.findAll(moov, TrakBox.class, "trak");
        for (int i = 0; i < trakBoxs.length; i++) {
            TrakBox trak = trakBoxs[i];
            SampleEntry se = NodeBox.findFirstPath(trak, SampleEntry.class, new String[] { "mdia", "minf", "stbl", "stsd", null });
            if (se != null && "tmcd".equals(se.getFourcc())) {
                tt = trak;
            } else {
                this.tracks.add(this.fromTrakBox(trak));
            }
        }
        if (tt != null) {
            DemuxerTrack video = this.getVideoTrack();
            if (video != null) {
                this.timecodeTrack = new TimecodeMP4DemuxerTrack(this.movie, tt, this.input);
            }
        }
    }

    public static MP4TrackType getTrackType(TrakBox trak) {
        HandlerBox handler = NodeBox.findFirstPath(trak, HandlerBox.class, Box.path("mdia.hdlr"));
        return MP4TrackType.fromHandler(handler.getComponentSubType());
    }

    public DemuxerTrack getVideoTrack() {
        for (AbstractMP4DemuxerTrack demuxerTrack : this.tracks) {
            if (demuxerTrack.box.isVideo()) {
                return demuxerTrack;
            }
        }
        return null;
    }

    public MovieBox getMovie() {
        return this.movie;
    }

    public AbstractMP4DemuxerTrack getTrack(int no) {
        for (AbstractMP4DemuxerTrack track : this.tracks) {
            if (track.getNo() == no) {
                return track;
            }
        }
        return null;
    }

    @Override
    public List<AbstractMP4DemuxerTrack> getTracks() {
        return new ArrayList(this.tracks);
    }

    @Override
    public List<DemuxerTrack> getVideoTracks() {
        ArrayList<DemuxerTrack> result = new ArrayList();
        for (AbstractMP4DemuxerTrack demuxerTrack : this.tracks) {
            if (demuxerTrack.box.isVideo()) {
                result.add(demuxerTrack);
            }
        }
        return result;
    }

    @Override
    public List<DemuxerTrack> getAudioTracks() {
        ArrayList<DemuxerTrack> result = new ArrayList();
        for (AbstractMP4DemuxerTrack demuxerTrack : this.tracks) {
            if (demuxerTrack.box.isAudio()) {
                result.add(demuxerTrack);
            }
        }
        return result;
    }

    public TimecodeMP4DemuxerTrack getTimecodeTrack() {
        return this.timecodeTrack;
    }

    @UsedViaReflection
    public static int probe(ByteBuffer b) {
        ByteBuffer fork = b.duplicate();
        int success = 0;
        int total = 0;
        while (fork.remaining() >= 8) {
            long len = Platform.unsignedInt(fork.getInt());
            int fcc = fork.getInt();
            int hdrLen = 8;
            if (len == 1L) {
                len = fork.getLong();
                hdrLen = 16;
            } else if (len < 8L) {
                break;
            }
            if (fcc == Fourcc.ftyp && len < 64L || fcc == Fourcc.moov && len < 104857600L || fcc == Fourcc.free || fcc == Fourcc.mdat || fcc == Fourcc.wide) {
                success++;
            }
            total++;
            if (len >= 2147483647L) {
                break;
            }
            NIOUtils.skip(fork, (int) (len - (long) hdrLen));
        }
        return total == 0 ? 0 : success * 100 / total;
    }

    public void close() throws IOException {
        this.input.close();
    }
}