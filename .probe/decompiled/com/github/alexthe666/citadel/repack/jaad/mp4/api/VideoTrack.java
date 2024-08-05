package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.ESDBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.SampleDescriptionBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.VideoMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.VideoSampleEntry;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class VideoTrack extends Track {

    private final VideoMediaHeaderBox vmhd;

    private final VideoSampleEntry sampleEntry;

    private final Track.Codec codec;

    public VideoTrack(Box trak, MP4InputStream in) {
        super(trak, in);
        Box minf = trak.getChild(1835297121L).getChild(1835626086L);
        this.vmhd = (VideoMediaHeaderBox) minf.getChild(1986881636L);
        Box stbl = minf.getChild(1937007212L);
        SampleDescriptionBox stsd = (SampleDescriptionBox) stbl.getChild(1937011556L);
        if (stsd.getChildren().get(0) instanceof VideoSampleEntry) {
            this.sampleEntry = (VideoSampleEntry) stsd.getChildren().get(0);
            long type = this.sampleEntry.getType();
            if (type == 1836070006L) {
                this.findDecoderSpecificInfo((ESDBox) this.sampleEntry.getChild(1702061171L));
            } else if (type != 1701733238L && type != 1685220723L) {
                this.decoderInfo = DecoderInfo.parse((CodecSpecificBox) this.sampleEntry.getChildren().get(0));
            } else {
                this.findDecoderSpecificInfo((ESDBox) this.sampleEntry.getChild(1702061171L));
                this.protection = Protection.parse(this.sampleEntry.getChild(1936289382L));
            }
            this.codec = VideoTrack.VideoCodec.forType(this.sampleEntry.getType());
        } else {
            this.sampleEntry = null;
            this.codec = VideoTrack.VideoCodec.UNKNOWN_VIDEO_CODEC;
        }
    }

    @Override
    public Type getType() {
        return Type.VIDEO;
    }

    @Override
    public Track.Codec getCodec() {
        return this.codec;
    }

    public int getWidth() {
        return this.sampleEntry != null ? this.sampleEntry.getWidth() : 0;
    }

    public int getHeight() {
        return this.sampleEntry != null ? this.sampleEntry.getHeight() : 0;
    }

    public double getHorizontalResolution() {
        return this.sampleEntry != null ? this.sampleEntry.getHorizontalResolution() : 0.0;
    }

    public double getVerticalResolution() {
        return this.sampleEntry != null ? this.sampleEntry.getVerticalResolution() : 0.0;
    }

    public int getFrameCount() {
        return this.sampleEntry != null ? this.sampleEntry.getFrameCount() : 0;
    }

    public String getCompressorName() {
        return this.sampleEntry != null ? this.sampleEntry.getCompressorName() : "";
    }

    public int getDepth() {
        return this.sampleEntry != null ? this.sampleEntry.getDepth() : 0;
    }

    public int getLayer() {
        return this.tkhd.getLayer();
    }

    public static enum VideoCodec implements Track.Codec {

        AVC, H263, MP4_ASP, UNKNOWN_VIDEO_CODEC;

        static Track.Codec forType(long type) {
            Track.Codec ac;
            if (type == 1635148593L) {
                ac = AVC;
            } else if (type == 1932670515L) {
                ac = H263;
            } else if (type == 1836070006L) {
                ac = MP4_ASP;
            } else {
                ac = UNKNOWN_VIDEO_CODEC;
            }
            return ac;
        }
    }
}