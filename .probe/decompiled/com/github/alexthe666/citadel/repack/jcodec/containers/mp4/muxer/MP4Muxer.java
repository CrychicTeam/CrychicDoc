package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.Brand;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Util;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.FileTypeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieHeaderBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MP4Muxer implements Muxer {

    private List<AbstractMP4MuxerTrack> tracks;

    protected long mdatOffset;

    private int nextTrackId = 1;

    protected SeekableByteChannel out;

    public static MP4Muxer createMP4MuxerToChannel(SeekableByteChannel output) throws IOException {
        return new MP4Muxer(output, Brand.MP4.getFileTypeBox());
    }

    public static MP4Muxer createMP4Muxer(SeekableByteChannel output, Brand brand) throws IOException {
        return new MP4Muxer(output, brand.getFileTypeBox());
    }

    public MP4Muxer(SeekableByteChannel output, FileTypeBox ftyp) throws IOException {
        this.tracks = new ArrayList();
        this.out = output;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        ftyp.write(buf);
        Header.createHeader("wide", 8L).write(buf);
        Header.createHeader("mdat", 1L).write(buf);
        this.mdatOffset = (long) buf.position();
        buf.putLong(0L);
        buf.flip();
        output.write(buf);
    }

    public TimecodeMP4MuxerTrack addTimecodeTrack() {
        return this.addTrack(new TimecodeMP4MuxerTrack(this.nextTrackId++));
    }

    public CodecMP4MuxerTrack addTrackWithId(MP4TrackType type, Codec codec, int trackId) {
        Preconditions.checkArgument(!this.hasTrackId(trackId), "track with id %s already exists", trackId);
        CodecMP4MuxerTrack track = new CodecMP4MuxerTrack(trackId, type, codec);
        this.tracks.add(track);
        this.nextTrackId = Math.max(this.nextTrackId, trackId + 1);
        return track;
    }

    public int getNextTrackId() {
        return this.nextTrackId;
    }

    private CodecMP4MuxerTrack doAddTrack(MP4TrackType type, Codec codec) {
        return this.addTrack(new CodecMP4MuxerTrack(this.nextTrackId++, type, codec));
    }

    public <T extends AbstractMP4MuxerTrack> T addTrack(T track) {
        Preconditions.checkNotNull(track, "track can not be null");
        int trackId = track.getTrackId();
        Preconditions.checkArgument(trackId <= this.nextTrackId);
        Preconditions.checkArgument(!this.hasTrackId(trackId), "track with id %s already exists", trackId);
        this.tracks.add(track.setOut(this.out));
        this.nextTrackId = Math.max(trackId + 1, this.nextTrackId);
        return track;
    }

    public boolean hasTrackId(int trackId) {
        for (AbstractMP4MuxerTrack t : this.tracks) {
            if (t.getTrackId() == trackId) {
                return true;
            }
        }
        return false;
    }

    public List<AbstractMP4MuxerTrack> getTracks() {
        return Collections.unmodifiableList(this.tracks);
    }

    @Override
    public void finish() throws IOException {
        Preconditions.checkState(this.tracks.size() != 0, "Can not save header with 0 tracks.");
        MovieBox movie = this.finalizeHeader();
        this.storeHeader(movie);
    }

    public void storeHeader(MovieBox movie) throws IOException {
        long mdatSize = this.out.position() - this.mdatOffset + 8L;
        MP4Util.writeMovie(this.out, movie);
        this.out.setPosition(this.mdatOffset);
        NIOUtils.writeLong(this.out, mdatSize);
    }

    public MovieBox finalizeHeader() throws IOException {
        MovieBox movie = MovieBox.createMovieBox();
        MovieHeaderBox mvhd = this.movieHeader();
        movie.addFirst(mvhd);
        for (AbstractMP4MuxerTrack track : this.tracks) {
            Box trak = track.finish(mvhd);
            if (trak != null) {
                movie.add(trak);
            }
        }
        return movie;
    }

    public AbstractMP4MuxerTrack getVideoTrack() {
        for (AbstractMP4MuxerTrack frameMuxer : this.tracks) {
            if (frameMuxer.isVideo()) {
                return frameMuxer;
            }
        }
        return null;
    }

    public AbstractMP4MuxerTrack getTimecodeTrack() {
        for (AbstractMP4MuxerTrack frameMuxer : this.tracks) {
            if (frameMuxer.isTimecode()) {
                return frameMuxer;
            }
        }
        return null;
    }

    public List<AbstractMP4MuxerTrack> getAudioTracks() {
        ArrayList<AbstractMP4MuxerTrack> result = new ArrayList();
        for (AbstractMP4MuxerTrack frameMuxer : this.tracks) {
            if (frameMuxer.isAudio()) {
                result.add(frameMuxer);
            }
        }
        return result;
    }

    private MovieHeaderBox movieHeader() {
        int timescale = ((AbstractMP4MuxerTrack) this.tracks.get(0)).getTimescale();
        long duration = ((AbstractMP4MuxerTrack) this.tracks.get(0)).getTrackTotalDuration();
        AbstractMP4MuxerTrack videoTrack = this.getVideoTrack();
        if (videoTrack != null) {
            timescale = videoTrack.getTimescale();
            duration = videoTrack.getTrackTotalDuration();
        }
        return MovieHeaderBox.createMovieHeaderBox(timescale, duration, 1.0F, 1.0F, new Date().getTime(), new Date().getTime(), new int[] { 65536, 0, 0, 0, 65536, 0, 0, 0, 1073741824 }, this.nextTrackId);
    }

    public PCMMP4MuxerTrack addPCMAudioTrack(AudioFormat format) {
        return this.addTrack(new PCMMP4MuxerTrack(this.nextTrackId++, format));
    }

    public CodecMP4MuxerTrack addCompressedAudioTrack(Codec codec, AudioFormat format) {
        CodecMP4MuxerTrack track = this.doAddTrack(MP4TrackType.SOUND, codec);
        track.addAudioSampleEntry(format);
        return track;
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        CodecMP4MuxerTrack track = this.doAddTrack(MP4TrackType.VIDEO, codec);
        Preconditions.checkArgument(meta != null || codec == Codec.H264, "VideoCodecMeta is required upfront for all codecs but H.264");
        track.addVideoSampleEntry(meta);
        return track;
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        AudioFormat format = meta.getFormat();
        return (MuxerTrack) (codec == Codec.PCM ? this.addPCMAudioTrack(format) : this.addCompressedAudioTrack(codec, format));
    }
}