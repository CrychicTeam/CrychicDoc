package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.AACDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.BufferH264ES;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Decoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg.JpegDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg.JpegToThumb2x2;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mjpeg.JpegToThumb4x4;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.MPEGDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.Mpeg2Thumb2x2;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.Mpeg2Thumb4x4;
import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.MPEG4Decoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.png.PNGDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresToThumb;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresToThumb2x2;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresToThumb4x4;
import com.github.alexthe666.citadel.repack.jcodec.codecs.raw.RAWVideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VP8Decoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.wav.WavDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Format;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.Tuple;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.AudioBuffer;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.imgseq.ImageSequenceDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.demuxer.MKVDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp3.MPEGAudioDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer.MP4Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPEGDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPSDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MTSDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.webp.WebpDemuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.y4m.Y4MDemuxer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SourceImpl implements Source, PacketSource {

    private String sourceName;

    private SeekableByteChannel sourceStream;

    private Demuxer demuxVideo;

    private Demuxer demuxAudio;

    private Format inputFormat;

    private DemuxerTrack videoInputTrack;

    private DemuxerTrack audioInputTrack;

    private Tuple._3<Integer, Integer, Codec> inputVideoCodec;

    private Tuple._3<Integer, Integer, Codec> inputAudioCodec;

    private List<VideoFrameWithPacket> frameReorderBuffer;

    private List<Packet> videoPacketReorderBuffer;

    private PixelStore pixelStore;

    private VideoCodecMeta videoCodecMeta;

    private AudioCodecMeta audioCodecMeta;

    private AudioDecoder audioDecoder;

    private VideoDecoder videoDecoder;

    private int downscale = 1;

    public static MPEGDecoder createMpegDecoder(int downscale) {
        if (downscale == 2) {
            return new Mpeg2Thumb4x4();
        } else {
            return (MPEGDecoder) (downscale == 4 ? new Mpeg2Thumb2x2() : new MPEGDecoder());
        }
    }

    public static ProresDecoder createProresDecoder(int downscale) {
        if (2 == downscale) {
            return new ProresToThumb4x4();
        } else if (4 == downscale) {
            return new ProresToThumb2x2();
        } else {
            return (ProresDecoder) (8 == downscale ? new ProresToThumb() : new ProresDecoder());
        }
    }

    public void initDemuxer() throws FileNotFoundException, IOException {
        if (this.inputFormat != Format.IMG) {
            this.sourceStream = NIOUtils.readableFileChannel(this.sourceName);
        }
        if (Format.MOV == this.inputFormat) {
            this.demuxVideo = this.demuxAudio = MP4Demuxer.createMP4Demuxer(this.sourceStream);
        } else if (Format.MKV == this.inputFormat) {
            this.demuxVideo = this.demuxAudio = new MKVDemuxer(this.sourceStream);
        } else if (Format.IMG == this.inputFormat) {
            this.demuxVideo = new ImageSequenceDemuxer(this.sourceName, Integer.MAX_VALUE);
        } else if (Format.WEBP == this.inputFormat) {
            this.demuxVideo = new WebpDemuxer(this.sourceStream);
        } else if (Format.MPEG_PS == this.inputFormat) {
            this.demuxVideo = this.demuxAudio = new MPSDemuxer(this.sourceStream);
        } else if (Format.Y4M == this.inputFormat) {
            Y4MDemuxer y4mDemuxer = new Y4MDemuxer(this.sourceStream);
            this.demuxVideo = this.demuxAudio = y4mDemuxer;
            this.videoInputTrack = y4mDemuxer;
        } else if (Format.H264 == this.inputFormat) {
            this.demuxVideo = new BufferH264ES(NIOUtils.fetchAllFromChannel(this.sourceStream));
        } else if (Format.WAV == this.inputFormat) {
            this.demuxAudio = new WavDemuxer(this.sourceStream);
        } else if (Format.MPEG_AUDIO == this.inputFormat) {
            this.demuxAudio = new MPEGAudioDemuxer(this.sourceStream);
        } else {
            if (Format.MPEG_TS != this.inputFormat) {
                throw new RuntimeException("Input format: " + this.inputFormat + " is not supported.");
            }
            MTSDemuxer mtsDemuxer = new MTSDemuxer(this.sourceStream);
            MPSDemuxer mpsDemuxer = null;
            if (this.inputVideoCodec != null) {
                mpsDemuxer = new MPSDemuxer(mtsDemuxer.getProgram(this.inputVideoCodec.v0));
                this.videoInputTrack = this.openTSTrack(mpsDemuxer, this.inputVideoCodec.v1);
                this.demuxVideo = mpsDemuxer;
            }
            if (this.inputAudioCodec != null) {
                if (this.inputVideoCodec == null || this.inputVideoCodec.v0 != this.inputAudioCodec.v0) {
                    mpsDemuxer = new MPSDemuxer(mtsDemuxer.getProgram(this.inputAudioCodec.v0));
                }
                this.audioInputTrack = this.openTSTrack(mpsDemuxer, this.inputAudioCodec.v1);
                this.demuxAudio = mpsDemuxer;
            }
            for (int pid : mtsDemuxer.getPrograms()) {
                if ((this.inputVideoCodec == null || pid != this.inputVideoCodec.v0) && (this.inputAudioCodec == null || pid != this.inputAudioCodec.v0)) {
                    Logger.info("Unused program: " + pid);
                    mtsDemuxer.getProgram(pid).close();
                }
            }
        }
        if (this.demuxVideo != null && this.inputVideoCodec != null) {
            List<? extends DemuxerTrack> videoTracks = this.demuxVideo.getVideoTracks();
            if (videoTracks.size() > 0) {
                this.videoInputTrack = (DemuxerTrack) videoTracks.get(this.inputVideoCodec.v1);
            }
        }
        if (this.demuxAudio != null && this.inputAudioCodec != null) {
            List<? extends DemuxerTrack> audioTracks = this.demuxAudio.getAudioTracks();
            if (audioTracks.size() > 0) {
                this.audioInputTrack = (DemuxerTrack) audioTracks.get(this.inputAudioCodec.v1);
            }
        }
    }

    protected int seekToKeyFrame(int frame) throws IOException {
        if (this.videoInputTrack instanceof SeekableDemuxerTrack) {
            SeekableDemuxerTrack seekable = (SeekableDemuxerTrack) this.videoInputTrack;
            seekable.gotoSyncFrame((long) frame);
            return (int) seekable.getCurFrame();
        } else {
            Logger.warn("Can not seek in " + this.videoInputTrack + " container.");
            return -1;
        }
    }

    private MPEGDemuxer.MPEGDemuxerTrack openTSTrack(MPSDemuxer demuxerVideo, Integer selectedTrack) {
        int trackNo = 0;
        for (MPEGDemuxer.MPEGDemuxerTrack track : demuxerVideo.getTracks()) {
            if (trackNo == selectedTrack) {
                return track;
            }
            track.ignore();
            trackNo++;
        }
        return null;
    }

    @Override
    public Packet inputVideoPacket() throws IOException {
        Packet packet;
        do {
            packet = this.getNextVideoPacket();
            if (packet != null) {
                this.videoPacketReorderBuffer.add(packet);
            }
        } while (packet != null && this.videoPacketReorderBuffer.size() <= 7);
        if (this.videoPacketReorderBuffer.size() == 0) {
            return null;
        } else {
            Packet out = (Packet) this.videoPacketReorderBuffer.remove(0);
            int duration = Integer.MAX_VALUE;
            for (Packet packet2 : this.videoPacketReorderBuffer) {
                int cand = (int) (packet2.getPts() - out.getPts());
                if (cand > 0 && cand < duration) {
                    duration = cand;
                }
            }
            if (duration != Integer.MAX_VALUE) {
                out.setDuration((long) duration);
            }
            return out;
        }
    }

    private Packet getNextVideoPacket() throws IOException {
        if (this.videoInputTrack == null) {
            return null;
        } else {
            Packet nextFrame = this.videoInputTrack.nextFrame();
            if (this.videoDecoder == null) {
                this.videoDecoder = this.createVideoDecoder(this.inputVideoCodec.v2, this.downscale, nextFrame.getData(), null);
                if (this.videoDecoder != null) {
                    this.videoCodecMeta = this.videoDecoder.getCodecMeta(nextFrame.getData());
                }
            }
            return nextFrame;
        }
    }

    @Override
    public Packet inputAudioPacket() throws IOException {
        if (this.audioInputTrack == null) {
            return null;
        } else {
            Packet audioPkt = this.audioInputTrack.nextFrame();
            if (this.audioDecoder == null && audioPkt != null) {
                this.audioDecoder = this.createAudioDecoder(audioPkt.getData());
                if (this.audioDecoder != null) {
                    this.audioCodecMeta = this.audioDecoder.getCodecMeta(audioPkt.getData());
                }
            }
            return audioPkt;
        }
    }

    public DemuxerTrackMeta getTrackVideoMeta() {
        return this.videoInputTrack == null ? null : this.videoInputTrack.getMeta();
    }

    public DemuxerTrackMeta getAudioMeta() {
        return this.audioInputTrack == null ? null : this.audioInputTrack.getMeta();
    }

    @Override
    public boolean haveAudio() {
        return this.audioInputTrack != null;
    }

    @Override
    public void finish() {
        if (this.sourceStream != null) {
            IOUtils.closeQuietly(this.sourceStream);
        }
    }

    public SourceImpl(String sourceName, Format inputFormat, Tuple._3<Integer, Integer, Codec> inputVideoCodec, Tuple._3<Integer, Integer, Codec> inputAudioCodec) {
        this.sourceName = sourceName;
        this.inputFormat = inputFormat;
        this.inputVideoCodec = inputVideoCodec;
        this.inputAudioCodec = inputAudioCodec;
        this.frameReorderBuffer = new ArrayList();
        this.videoPacketReorderBuffer = new ArrayList();
    }

    @Override
    public void init(PixelStore pixelStore) throws IOException {
        this.pixelStore = pixelStore;
        this.initDemuxer();
    }

    private AudioDecoder createAudioDecoder(ByteBuffer codecPrivate) throws AACException {
        if (Codec.AAC == this.inputAudioCodec.v2) {
            return new AACDecoder(codecPrivate);
        } else {
            return Codec.PCM == this.inputAudioCodec.v2 ? new SourceImpl.RawAudioDecoder(this.getAudioMeta().getAudioCodecMeta().getFormat()) : null;
        }
    }

    private VideoDecoder createVideoDecoder(Codec codec, int downscale, ByteBuffer codecPrivate, VideoCodecMeta videoCodecMeta) {
        if (Codec.H264 == codec) {
            return H264Decoder.createH264DecoderFromCodecPrivate(codecPrivate);
        } else if (Codec.PNG == codec) {
            return new PNGDecoder();
        } else if (Codec.MPEG2 == codec) {
            return createMpegDecoder(downscale);
        } else if (Codec.PRORES == codec) {
            return createProresDecoder(downscale);
        } else if (Codec.VP8 == codec) {
            return new VP8Decoder();
        } else if (Codec.JPEG == codec) {
            return createJpegDecoder(downscale);
        } else if (Codec.MPEG4 == codec) {
            return new MPEG4Decoder();
        } else if (Codec.RAW == codec) {
            Size dim = videoCodecMeta.getSize();
            return new RAWVideoDecoder(dim.getWidth(), dim.getHeight());
        } else {
            return null;
        }
    }

    public Picture decodeVideo(ByteBuffer data, Picture target1) {
        return this.videoDecoder.decodeFrame(data, target1.getData());
    }

    protected ByteBuffer decodeAudio(ByteBuffer audioPkt) throws IOException {
        if (this.inputAudioCodec.v2 == Codec.PCM) {
            return audioPkt;
        } else {
            AudioBuffer decodeFrame = this.audioDecoder.decodeFrame(audioPkt, null);
            return decodeFrame.getData();
        }
    }

    @Override
    public void seekFrames(int seekFrames) throws IOException {
        if (seekFrames != 0) {
            int skipFrames = seekFrames - this.seekToKeyFrame(seekFrames);
            Packet inVideoPacket;
            while (skipFrames > 0 && (inVideoPacket = this.getNextVideoPacket()) != null) {
                PixelStore.LoanerPicture loanerBuffer = this.getPixelBuffer(inVideoPacket.getData());
                Picture decodedFrame = this.decodeVideo(inVideoPacket.getData(), loanerBuffer.getPicture());
                if (decodedFrame == null) {
                    this.pixelStore.putBack(loanerBuffer);
                } else {
                    this.frameReorderBuffer.add(new VideoFrameWithPacket(inVideoPacket, new PixelStore.LoanerPicture(decodedFrame, 1)));
                    if (this.frameReorderBuffer.size() > 7) {
                        Collections.sort(this.frameReorderBuffer);
                        VideoFrameWithPacket removed = (VideoFrameWithPacket) this.frameReorderBuffer.remove(0);
                        skipFrames--;
                        if (removed.getFrame() != null) {
                            this.pixelStore.putBack(removed.getFrame());
                        }
                    }
                }
            }
        }
    }

    private void detectFrameType(Packet inVideoPacket) {
        if (this.inputVideoCodec.v2 == Codec.H264) {
            inVideoPacket.setFrameType(H264Utils.isByteBufferIDRSlice(inVideoPacket.getData()) ? Packet.FrameType.KEY : Packet.FrameType.INTER);
        }
    }

    protected PixelStore.LoanerPicture getPixelBuffer(ByteBuffer firstFrame) {
        VideoCodecMeta videoMeta = this.getVideoCodecMeta();
        Size size = videoMeta.getSize();
        return this.pixelStore.getPicture(size.getWidth() + 15 & -16, size.getHeight() + 15 & -16, videoMeta.getColor());
    }

    @Override
    public VideoCodecMeta getVideoCodecMeta() {
        if (this.videoCodecMeta != null) {
            return this.videoCodecMeta;
        } else {
            DemuxerTrackMeta meta = this.getTrackVideoMeta();
            if (meta != null && meta.getVideoCodecMeta() != null) {
                this.videoCodecMeta = meta.getVideoCodecMeta();
            }
            return this.videoCodecMeta;
        }
    }

    @Override
    public VideoFrameWithPacket getNextVideoFrame() throws IOException {
        Packet inVideoPacket;
        while ((inVideoPacket = this.getNextVideoPacket()) != null) {
            if (inVideoPacket.getFrameType() == Packet.FrameType.UNKNOWN) {
                this.detectFrameType(inVideoPacket);
            }
            Picture decodedFrame = null;
            PixelStore.LoanerPicture pixelBuffer = this.getPixelBuffer(inVideoPacket.getData());
            decodedFrame = this.decodeVideo(inVideoPacket.getData(), pixelBuffer.getPicture());
            if (decodedFrame == null) {
                this.pixelStore.putBack(pixelBuffer);
            } else {
                this.frameReorderBuffer.add(new VideoFrameWithPacket(inVideoPacket, new PixelStore.LoanerPicture(decodedFrame, 1)));
                if (this.frameReorderBuffer.size() > 7) {
                    return this.removeFirstFixDuration(this.frameReorderBuffer);
                }
            }
        }
        return this.frameReorderBuffer.size() > 0 ? this.removeFirstFixDuration(this.frameReorderBuffer) : null;
    }

    private VideoFrameWithPacket removeFirstFixDuration(List<VideoFrameWithPacket> reorderBuffer) {
        Collections.sort(reorderBuffer);
        VideoFrameWithPacket frame = (VideoFrameWithPacket) reorderBuffer.remove(0);
        if (!reorderBuffer.isEmpty()) {
            VideoFrameWithPacket nextFrame = (VideoFrameWithPacket) reorderBuffer.get(0);
            frame.getPacket().setDuration(nextFrame.getPacket().getPts() - frame.getPacket().getPts());
        }
        return frame;
    }

    @Override
    public AudioFrameWithPacket getNextAudioFrame() throws IOException {
        Packet audioPkt = this.inputAudioPacket();
        if (audioPkt == null) {
            return null;
        } else {
            AudioBuffer audioBuffer;
            if (this.inputAudioCodec.v2 == Codec.PCM) {
                DemuxerTrackMeta audioMeta = this.getAudioMeta();
                audioBuffer = new AudioBuffer(audioPkt.getData(), audioMeta.getAudioCodecMeta().getFormat(), audioMeta.getTotalFrames());
            } else {
                audioBuffer = this.audioDecoder.decodeFrame(audioPkt.getData(), null);
            }
            return new AudioFrameWithPacket(audioBuffer, audioPkt);
        }
    }

    public Tuple._3<Integer, Integer, Codec> getIntputVideoCodec() {
        return this.inputVideoCodec;
    }

    public Tuple._3<Integer, Integer, Codec> getInputAudioCode() {
        return this.inputAudioCodec;
    }

    @Override
    public void setOption(Options option, Object value) {
        if (option == Options.DOWNSCALE) {
            this.downscale = (Integer) value;
        }
    }

    @Override
    public AudioCodecMeta getAudioCodecMeta() {
        return this.audioInputTrack != null && this.audioInputTrack.getMeta() != null && this.audioInputTrack.getMeta().getAudioCodecMeta() != null ? this.audioInputTrack.getMeta().getAudioCodecMeta() : this.audioCodecMeta;
    }

    @Override
    public boolean isVideo() {
        if (!this.inputFormat.isVideo()) {
            return false;
        } else {
            List<? extends DemuxerTrack> tracks = this.demuxVideo.getVideoTracks();
            return tracks != null && tracks.size() > 0;
        }
    }

    @Override
    public boolean isAudio() {
        if (!this.inputFormat.isAudio()) {
            return false;
        } else {
            List<? extends DemuxerTrack> tracks = this.demuxAudio.getAudioTracks();
            return tracks != null && tracks.size() > 0;
        }
    }

    public static JpegDecoder createJpegDecoder(int downscale) {
        if (downscale == 2) {
            return new JpegToThumb4x4();
        } else {
            return (JpegDecoder) (downscale == 4 ? new JpegToThumb2x2() : new JpegDecoder());
        }
    }

    private static class RawAudioDecoder implements AudioDecoder {

        private AudioFormat format;

        public RawAudioDecoder(AudioFormat format) {
            this.format = format;
        }

        @Override
        public AudioBuffer decodeFrame(ByteBuffer frame, ByteBuffer dst) throws IOException {
            return new AudioBuffer(frame, this.format, frame.remaining() / this.format.getFrameSize());
        }

        @Override
        public AudioCodecMeta getCodecMeta(ByteBuffer data) throws IOException {
            return AudioCodecMeta.fromAudioFormat(this.format);
        }
    }
}