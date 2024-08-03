package com.github.alexthe666.citadel.repack.jcodec.api;

import com.github.alexthe666.citadel.repack.jcodec.api.specific.AVCMP4Adaptor;
import com.github.alexthe666.citadel.repack.jcodec.api.specific.ContainerAdaptor;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Format;
import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer.MP4Demuxer;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FrameGrab {

    private SeekableDemuxerTrack videoTrack;

    private ContainerAdaptor decoder;

    private final ThreadLocal<byte[][]> buffers;

    public static FrameGrab createFrameGrab(SeekableByteChannel _in) throws IOException, JCodecException {
        ByteBuffer header = ByteBuffer.allocate(65536);
        _in.read(header);
        header.flip();
        Format detectFormat = JCodecUtil.detectFormatBuffer(header);
        if (detectFormat == null) {
            throw new UnsupportedFormatException("Could not detect the format of the input video.");
        } else if (Format.MOV == detectFormat) {
            MP4Demuxer d1 = MP4Demuxer.createMP4Demuxer(_in);
            SeekableDemuxerTrack videoTrack_ = (SeekableDemuxerTrack) d1.getVideoTrack();
            FrameGrab fg = new FrameGrab(videoTrack_, detectDecoder(videoTrack_));
            fg.decodeLeadingFrames();
            return fg;
        } else if (Format.MPEG_PS == detectFormat) {
            throw new UnsupportedFormatException("MPEG PS is temporarily unsupported.");
        } else if (Format.MPEG_TS == detectFormat) {
            throw new UnsupportedFormatException("MPEG TS is temporarily unsupported.");
        } else {
            throw new UnsupportedFormatException("Container format is not supported by JCodec");
        }
    }

    public FrameGrab(SeekableDemuxerTrack videoTrack, ContainerAdaptor decoder) {
        this.videoTrack = videoTrack;
        this.decoder = decoder;
        this.buffers = new ThreadLocal();
    }

    private SeekableDemuxerTrack sdt() throws JCodecException {
        if (!(this.videoTrack instanceof SeekableDemuxerTrack)) {
            throw new JCodecException("Not a seekable track");
        } else {
            return this.videoTrack;
        }
    }

    public FrameGrab seekToSecondPrecise(double second) throws IOException, JCodecException {
        this.sdt().seek(second);
        this.decodeLeadingFrames();
        return this;
    }

    public FrameGrab seekToFramePrecise(int frameNumber) throws IOException, JCodecException {
        this.sdt().gotoFrame((long) frameNumber);
        this.decodeLeadingFrames();
        return this;
    }

    public FrameGrab seekToSecondSloppy(double second) throws IOException, JCodecException {
        this.sdt().seek(second);
        this.goToPrevKeyframe();
        return this;
    }

    public FrameGrab seekToFrameSloppy(int frameNumber) throws IOException, JCodecException {
        this.sdt().gotoFrame((long) frameNumber);
        this.goToPrevKeyframe();
        return this;
    }

    private void goToPrevKeyframe() throws IOException, JCodecException {
        this.sdt().gotoFrame((long) this.detectKeyFrame((int) this.sdt().getCurFrame()));
    }

    private void decodeLeadingFrames() throws IOException, JCodecException {
        SeekableDemuxerTrack sdt = this.sdt();
        int curFrame = (int) sdt.getCurFrame();
        int keyFrame = this.detectKeyFrame(curFrame);
        sdt.gotoFrame((long) keyFrame);
        Packet frame = sdt.nextFrame();
        if (this.decoder == null) {
            this.decoder = detectDecoder(sdt);
        }
        while (frame.getFrameNo() < (long) curFrame) {
            this.decoder.decodeFrame(frame, this.getBuffer());
            frame = sdt.nextFrame();
        }
        sdt.gotoFrame((long) curFrame);
    }

    private byte[][] getBuffer() {
        byte[][] buf = (byte[][]) this.buffers.get();
        if (buf == null) {
            buf = this.decoder.allocatePicture();
            this.buffers.set(buf);
        }
        return buf;
    }

    private int detectKeyFrame(int start) throws IOException {
        int[] seekFrames = this.videoTrack.getMeta().getSeekFrames();
        if (seekFrames == null) {
            return start;
        } else {
            int prev = seekFrames[0];
            for (int i = 1; i < seekFrames.length && seekFrames[i] <= start; i++) {
                prev = seekFrames[i];
            }
            return prev;
        }
    }

    private static ContainerAdaptor detectDecoder(SeekableDemuxerTrack videoTrack) throws JCodecException {
        DemuxerTrackMeta meta = videoTrack.getMeta();
        if (Codec.H264 == meta.getCodec()) {
            return new AVCMP4Adaptor(meta);
        } else {
            throw new UnsupportedFormatException("Codec is not supported");
        }
    }

    public PictureWithMetadata getNativeFrameWithMetadata() throws IOException {
        Packet frame = this.videoTrack.nextFrame();
        if (frame == null) {
            return null;
        } else {
            Picture picture = this.decoder.decodeFrame(frame, this.getBuffer());
            return new PictureWithMetadata(picture, frame.getPtsD(), frame.getDurationD(), this.videoTrack.getMeta().getOrientation());
        }
    }

    public Picture getNativeFrame() throws IOException {
        Packet frame = this.videoTrack.nextFrame();
        return frame == null ? null : this.decoder.decodeFrame(frame, this.getBuffer());
    }

    public static Picture getFrameAtSec(File file, double second) throws IOException, JCodecException {
        FileChannelWrapper ch = null;
        Picture var4;
        try {
            ch = NIOUtils.readableChannel(file);
            var4 = createFrameGrab(ch).seekToSecondPrecise(second).getNativeFrame();
        } finally {
            NIOUtils.closeQuietly(ch);
        }
        return var4;
    }

    public static Picture getFrameFromChannelAtSec(SeekableByteChannel file, double second) throws JCodecException, IOException {
        return createFrameGrab(file).seekToSecondPrecise(second).getNativeFrame();
    }

    public static Picture getFrameFromFile(File file, int frameNumber) throws IOException, JCodecException {
        FileChannelWrapper ch = null;
        Picture var3;
        try {
            ch = NIOUtils.readableChannel(file);
            var3 = createFrameGrab(ch).seekToFramePrecise(frameNumber).getNativeFrame();
        } finally {
            NIOUtils.closeQuietly(ch);
        }
        return var3;
    }

    public static Picture getFrameFromChannel(SeekableByteChannel file, int frameNumber) throws JCodecException, IOException {
        return createFrameGrab(file).seekToFramePrecise(frameNumber).getNativeFrame();
    }

    public static Picture getNativeFrameAtFrame(SeekableDemuxerTrack vt, ContainerAdaptor decoder, int frameNumber) throws IOException, JCodecException {
        return new FrameGrab(vt, decoder).seekToFramePrecise(frameNumber).getNativeFrame();
    }

    public static Picture getNativeFrameAtSec(SeekableDemuxerTrack vt, ContainerAdaptor decoder, double second) throws IOException, JCodecException {
        return new FrameGrab(vt, decoder).seekToSecondPrecise(second).getNativeFrame();
    }

    public static Picture getNativeFrameSloppy(SeekableDemuxerTrack vt, ContainerAdaptor decoder, int frameNumber) throws IOException, JCodecException {
        return new FrameGrab(vt, decoder).seekToFrameSloppy(frameNumber).getNativeFrame();
    }

    public static Picture getNativeFrameAtSecSloppy(SeekableDemuxerTrack vt, ContainerAdaptor decoder, double second) throws IOException, JCodecException {
        return new FrameGrab(vt, decoder).seekToSecondSloppy(second).getNativeFrame();
    }

    public MediaInfo getMediaInfo() {
        return this.decoder.getMediaInfo();
    }

    public SeekableDemuxerTrack getVideoTrack() {
        return this.videoTrack;
    }

    public ContainerAdaptor getDecoder() {
        return this.decoder;
    }
}