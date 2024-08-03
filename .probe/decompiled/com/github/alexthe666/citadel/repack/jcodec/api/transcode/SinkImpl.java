package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Encoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.png.PNGEncoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresEncoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.raw.RAWVideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.IVFMuxer;
import com.github.alexthe666.citadel.repack.jcodec.codecs.vpx.VP8Encoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.wav.WavMuxer;
import com.github.alexthe666.citadel.repack.jcodec.codecs.y4m.Y4MMuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Format;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.AudioBuffer;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.imgseq.ImageSequenceMuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.muxer.MKVMuxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer.MP4Muxer;
import com.github.alexthe666.citadel.repack.jcodec.containers.raw.RawMuxer;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SinkImpl implements Sink, PacketSink {

    private String destName;

    private SeekableByteChannel destStream;

    private Muxer muxer;

    private MuxerTrack videoOutputTrack;

    private MuxerTrack audioOutputTrack;

    private boolean framesOutput;

    private Codec outputVideoCodec;

    private Codec outputAudioCodec;

    private Format outputFormat;

    private final ThreadLocal<ByteBuffer> bufferStore;

    private AudioEncoder audioEncoder;

    private VideoEncoder videoEncoder;

    private String profile;

    private boolean interlaced;

    @Override
    public void outputVideoPacket(Packet packet, VideoCodecMeta codecMeta) throws IOException {
        if (this.outputFormat.isVideo()) {
            if (this.videoOutputTrack == null) {
                this.videoOutputTrack = this.muxer.addVideoTrack(this.outputVideoCodec, codecMeta);
            }
            this.videoOutputTrack.addFrame(packet);
            this.framesOutput = true;
        }
    }

    @Override
    public void outputAudioPacket(Packet audioPkt, AudioCodecMeta audioCodecMeta) throws IOException {
        if (this.outputFormat.isAudio()) {
            if (this.audioOutputTrack == null) {
                this.audioOutputTrack = this.muxer.addAudioTrack(this.outputAudioCodec, audioCodecMeta);
            }
            this.audioOutputTrack.addFrame(audioPkt);
            this.framesOutput = true;
        }
    }

    public void initMuxer() throws IOException {
        if (this.destStream == null && this.outputFormat != Format.IMG) {
            this.destStream = NIOUtils.writableFileChannel(this.destName);
        }
        if (Format.MKV == this.outputFormat) {
            this.muxer = new MKVMuxer(this.destStream);
        } else if (Format.MOV == this.outputFormat) {
            this.muxer = MP4Muxer.createMP4MuxerToChannel(this.destStream);
        } else if (Format.IVF == this.outputFormat) {
            this.muxer = new IVFMuxer(this.destStream);
        } else if (Format.IMG == this.outputFormat) {
            this.muxer = new ImageSequenceMuxer(this.destName);
        } else if (Format.WAV == this.outputFormat) {
            this.muxer = new WavMuxer(this.destStream);
        } else if (Format.Y4M == this.outputFormat) {
            this.muxer = new Y4MMuxer(this.destStream);
        } else {
            if (Format.RAW != this.outputFormat) {
                throw new RuntimeException("The output format " + this.outputFormat + " is not supported.");
            }
            this.muxer = new RawMuxer(this.destStream);
        }
    }

    @Override
    public void finish() throws IOException {
        if (this.framesOutput) {
            this.muxer.finish();
        } else {
            Logger.warn("No frames output.");
        }
        if (this.destStream != null) {
            IOUtils.closeQuietly(this.destStream);
        }
    }

    public SinkImpl(String destName, Format outputFormat, Codec outputVideoCodec, Codec outputAudioCodec) {
        if (destName == null && outputFormat == Format.IMG) {
            throw new IllegalArgumentException("A destination file should be specified for the image muxer.");
        } else {
            this.destName = destName;
            this.outputFormat = outputFormat;
            this.outputVideoCodec = outputVideoCodec;
            this.outputAudioCodec = outputAudioCodec;
            this.outputFormat = outputFormat;
            this.bufferStore = new ThreadLocal();
        }
    }

    public static SinkImpl createWithStream(SeekableByteChannel destStream, Format outputFormat, Codec outputVideoCodec, Codec outputAudioCodec) {
        SinkImpl result = new SinkImpl(null, outputFormat, outputVideoCodec, outputAudioCodec);
        result.destStream = destStream;
        return result;
    }

    @Override
    public void init() throws IOException {
        this.initMuxer();
        if (this.outputFormat.isVideo() && this.outputVideoCodec != null) {
            if (Codec.PRORES == this.outputVideoCodec) {
                this.videoEncoder = ProresEncoder.createProresEncoder(this.profile, this.interlaced);
            } else if (Codec.H264 == this.outputVideoCodec) {
                this.videoEncoder = H264Encoder.createH264Encoder();
            } else if (Codec.VP8 == this.outputVideoCodec) {
                this.videoEncoder = VP8Encoder.createVP8Encoder(10);
            } else if (Codec.PNG == this.outputVideoCodec) {
                this.videoEncoder = new PNGEncoder();
            } else {
                if (Codec.RAW != this.outputVideoCodec) {
                    throw new RuntimeException("Could not find encoder for the codec: " + this.outputVideoCodec);
                }
                this.videoEncoder = new RAWVideoEncoder();
            }
        }
    }

    protected VideoEncoder.EncodedFrame encodeVideo(Picture frame, ByteBuffer _out) {
        return !this.outputFormat.isVideo() ? null : this.videoEncoder.encodeFrame(frame, _out);
    }

    private AudioEncoder createAudioEncoder(Codec codec, AudioFormat format) {
        if (codec != Codec.PCM) {
            throw new RuntimeException("Only PCM audio encoding (RAW audio) is supported.");
        } else {
            return new SinkImpl.RawAudioEncoder();
        }
    }

    protected ByteBuffer encodeAudio(AudioBuffer audioBuffer) {
        if (this.audioEncoder == null) {
            AudioFormat format = audioBuffer.getFormat();
            this.audioEncoder = this.createAudioEncoder(this.outputAudioCodec, format);
        }
        return this.audioEncoder.encode(audioBuffer.getData(), null);
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setInterlaced(Boolean interlaced) {
        this.interlaced = interlaced;
    }

    @Override
    public void outputVideoFrame(VideoFrameWithPacket videoFrame) throws IOException {
        if (this.outputFormat.isVideo() && this.outputVideoCodec != null) {
            ByteBuffer buffer = (ByteBuffer) this.bufferStore.get();
            int bufferSize = this.videoEncoder.estimateBufferSize(videoFrame.getFrame().getPicture());
            if (buffer == null || bufferSize < buffer.capacity()) {
                buffer = ByteBuffer.allocate(bufferSize);
                this.bufferStore.set(buffer);
            }
            buffer.clear();
            Picture frame = videoFrame.getFrame().getPicture();
            VideoEncoder.EncodedFrame enc = this.encodeVideo(frame, buffer);
            Packet outputVideoPacket = Packet.createPacketWithData(videoFrame.getPacket(), NIOUtils.clone(enc.getData()));
            outputVideoPacket.setFrameType(enc.isKeyFrame() ? Packet.FrameType.KEY : Packet.FrameType.INTER);
            this.outputVideoPacket(outputVideoPacket, VideoCodecMeta.createSimpleVideoCodecMeta(new Size(frame.getWidth(), frame.getHeight()), frame.getColor()));
        }
    }

    @Override
    public void outputAudioFrame(AudioFrameWithPacket audioFrame) throws IOException {
        if (this.outputFormat.isAudio() && this.outputAudioCodec != null) {
            this.outputAudioPacket(Packet.createPacketWithData(audioFrame.getPacket(), this.encodeAudio(audioFrame.getAudio())), AudioCodecMeta.fromAudioFormat(audioFrame.getAudio().getFormat()));
        }
    }

    @Override
    public ColorSpace getInputColor() {
        if (this.videoEncoder == null) {
            throw new IllegalStateException("Video encoder has not been initialized, init() must be called before using this class.");
        } else {
            ColorSpace[] colorSpaces = this.videoEncoder.getSupportedColorSpaces();
            return colorSpaces == null ? null : colorSpaces[0];
        }
    }

    @Override
    public void setOption(Options option, Object value) {
        if (option == Options.PROFILE) {
            this.profile = (String) value;
        } else if (option == Options.INTERLACED) {
            this.interlaced = (Boolean) value;
        }
    }

    @Override
    public boolean isVideo() {
        return this.outputFormat.isVideo();
    }

    @Override
    public boolean isAudio() {
        return this.outputFormat.isAudio();
    }

    private static class RawAudioEncoder implements AudioEncoder {

        private RawAudioEncoder() {
        }

        @Override
        public ByteBuffer encode(ByteBuffer audioPkt, ByteBuffer buf) {
            return audioPkt;
        }
    }
}