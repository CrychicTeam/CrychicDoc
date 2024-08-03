package com.github.alexthe666.citadel.repack.jcodec.containers.flv;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import java.nio.ByteBuffer;

public class FLVTag {

    private FLVTag.Type type;

    private long position;

    private FLVTag.TagHeader tagHeader;

    private int pts;

    private ByteBuffer data;

    private boolean keyFrame;

    private long frameNo;

    private int streamId;

    private int prevPacketSize;

    public FLVTag(FLVTag.Type type, long position, FLVTag.TagHeader tagHeader, int pts, ByteBuffer data, boolean keyFrame, long frameNo, int streamId, int prevPacketSize) {
        this.type = type;
        this.position = position;
        this.tagHeader = tagHeader;
        this.pts = pts;
        this.data = data;
        this.keyFrame = keyFrame;
        this.frameNo = frameNo;
        this.streamId = streamId;
        this.prevPacketSize = prevPacketSize;
    }

    public FLVTag.Type getType() {
        return this.type;
    }

    public long getPosition() {
        return this.position;
    }

    public FLVTag.TagHeader getTagHeader() {
        return this.tagHeader;
    }

    public int getPts() {
        return this.pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getStreamId() {
        return this.streamId;
    }

    public void setStreamId(int streamId) {
        this.streamId = streamId;
    }

    public int getPrevPacketSize() {
        return this.prevPacketSize;
    }

    public void setPrevPacketSize(int prevPacketSize) {
        this.prevPacketSize = prevPacketSize;
    }

    public ByteBuffer getData() {
        return this.data;
    }

    public double getPtsD() {
        return (double) this.pts / 1000.0;
    }

    public boolean isKeyFrame() {
        return this.keyFrame;
    }

    public long getFrameNo() {
        return this.frameNo;
    }

    public static class AacAudioTagHeader extends FLVTag.AudioTagHeader {

        private int packetType;

        public AacAudioTagHeader(Codec codec, AudioFormat audioFormat, int packetType) {
            super(codec, audioFormat);
            this.packetType = packetType;
        }

        public int getPacketType() {
            return this.packetType;
        }
    }

    public static class AudioTagHeader extends FLVTag.TagHeader {

        private AudioFormat audioFormat;

        public AudioTagHeader(Codec codec, AudioFormat audioFormat) {
            super(codec);
            this.audioFormat = audioFormat;
        }

        public AudioFormat getAudioFormat() {
            return this.audioFormat;
        }
    }

    public static class AvcVideoTagHeader extends FLVTag.VideoTagHeader {

        private int compOffset;

        private byte avcPacketType;

        public AvcVideoTagHeader(Codec codec, int frameType, byte avcPacketType, int compOffset) {
            super(codec, frameType);
            this.avcPacketType = avcPacketType;
            this.compOffset = compOffset;
        }

        public int getCompOffset() {
            return this.compOffset;
        }

        public byte getAvcPacketType() {
            return this.avcPacketType;
        }
    }

    public static class TagHeader {

        private Codec codec;

        public TagHeader(Codec codec) {
            this.codec = codec;
        }

        public Codec getCodec() {
            return this.codec;
        }
    }

    public static enum Type {

        VIDEO, AUDIO, SCRIPT
    }

    public static class VideoTagHeader extends FLVTag.TagHeader {

        private int frameType;

        public VideoTagHeader(Codec codec, int frameType) {
            super(codec);
            this.frameType = frameType;
        }

        public int getFrameType() {
            return this.frameType;
        }
    }
}