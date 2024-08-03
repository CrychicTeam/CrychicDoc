package com.github.alexthe666.citadel.repack.jcodec.api;

import com.github.alexthe666.citadel.repack.jcodec.api.transcode.PixelStore;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.PixelStoreImpl;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.Sink;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.SinkImpl;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.VideoFrameWithPacket;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Format;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.scale.ColorUtil;
import com.github.alexthe666.citadel.repack.jcodec.scale.Transform;
import java.io.File;
import java.io.IOException;

public class SequenceEncoder {

    private Transform transform;

    private int frameNo;

    private int timestamp;

    private Rational fps;

    private Sink sink;

    private PixelStore pixelStore;

    public static SequenceEncoder createSequenceEncoder(File out, int fps) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(fps, 1), Format.MOV, Codec.H264, null);
    }

    public static SequenceEncoder create25Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(25, 1), Format.MOV, Codec.H264, null);
    }

    public static SequenceEncoder create30Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30, 1), Format.MOV, Codec.H264, null);
    }

    public static SequenceEncoder create2997Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(30000, 1001), Format.MOV, Codec.H264, null);
    }

    public static SequenceEncoder create24Fps(File out) throws IOException {
        return new SequenceEncoder(NIOUtils.writableChannel(out), Rational.R(24, 1), Format.MOV, Codec.H264, null);
    }

    public static SequenceEncoder createWithFps(SeekableByteChannel out, Rational fps) throws IOException {
        return new SequenceEncoder(out, fps, Format.MOV, Codec.H264, null);
    }

    public SequenceEncoder(SeekableByteChannel out, Rational fps, Format outputFormat, Codec outputVideoCodec, Codec outputAudioCodec) throws IOException {
        this.fps = fps;
        this.sink = SinkImpl.createWithStream(out, outputFormat, outputVideoCodec, outputAudioCodec);
        this.sink.init();
        if (this.sink.getInputColor() != null) {
            this.transform = ColorUtil.getTransform(ColorSpace.RGB, this.sink.getInputColor());
        }
        this.pixelStore = new PixelStoreImpl();
    }

    public void encodeNativeFrame(Picture pic) throws IOException {
        if (pic.getColor() != ColorSpace.RGB) {
            throw new IllegalArgumentException("The input images is expected in RGB color.");
        } else {
            ColorSpace sinkColor = this.sink.getInputColor();
            PixelStore.LoanerPicture toEncode;
            if (sinkColor != null) {
                toEncode = this.pixelStore.getPicture(pic.getWidth(), pic.getHeight(), sinkColor);
                this.transform.transform(pic, toEncode.getPicture());
            } else {
                toEncode = new PixelStore.LoanerPicture(pic, 0);
            }
            Packet pkt = Packet.createPacket(null, (long) this.timestamp, this.fps.getNum(), (long) this.fps.getDen(), (long) this.frameNo, Packet.FrameType.KEY, null);
            this.sink.outputVideoFrame(new VideoFrameWithPacket(pkt, toEncode));
            if (sinkColor != null) {
                this.pixelStore.putBack(toEncode);
            }
            this.timestamp = this.timestamp + this.fps.getDen();
            this.frameNo++;
        }
    }

    public void finish() throws IOException {
        this.sink.finish();
    }
}