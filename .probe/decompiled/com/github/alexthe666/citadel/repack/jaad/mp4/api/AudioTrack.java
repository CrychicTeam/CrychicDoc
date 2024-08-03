package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.ESDBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.SampleDescriptionBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.SoundMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.AudioSampleEntry;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public class AudioTrack extends Track {

    private final SoundMediaHeaderBox smhd;

    private final AudioSampleEntry sampleEntry;

    private Track.Codec codec;

    public AudioTrack(Box trak, MP4InputStream in) {
        super(trak, in);
        Box mdia = trak.getChild(1835297121L);
        Box minf = mdia.getChild(1835626086L);
        this.smhd = (SoundMediaHeaderBox) minf.getChild(1936549988L);
        Box stbl = minf.getChild(1937007212L);
        SampleDescriptionBox stsd = (SampleDescriptionBox) stbl.getChild(1937011556L);
        if (stsd.getChildren().get(0) instanceof AudioSampleEntry) {
            this.sampleEntry = (AudioSampleEntry) stsd.getChildren().get(0);
            long type = this.sampleEntry.getType();
            if (this.sampleEntry.hasChild(1702061171L)) {
                this.findDecoderSpecificInfo((ESDBox) this.sampleEntry.getChild(1702061171L));
            } else {
                this.decoderInfo = DecoderInfo.parse((CodecSpecificBox) this.sampleEntry.getChildren().get(0));
            }
            if (type != 1701733217L && type != 1685220723L) {
                this.codec = AudioTrack.AudioCodec.forType(this.sampleEntry.getType());
            } else {
                this.findDecoderSpecificInfo((ESDBox) this.sampleEntry.getChild(1702061171L));
                this.protection = Protection.parse(this.sampleEntry.getChild(1936289382L));
                this.codec = this.protection.getOriginalFormat();
            }
        } else {
            this.sampleEntry = null;
            this.codec = AudioTrack.AudioCodec.UNKNOWN_AUDIO_CODEC;
        }
    }

    @Override
    public Type getType() {
        return Type.AUDIO;
    }

    @Override
    public Track.Codec getCodec() {
        return this.codec;
    }

    public double getBalance() {
        return this.smhd.getBalance();
    }

    public int getChannelCount() {
        return this.sampleEntry.getChannelCount();
    }

    public int getSampleRate() {
        return this.sampleEntry.getSampleRate();
    }

    public int getSampleSize() {
        return this.sampleEntry.getSampleSize();
    }

    public double getVolume() {
        return this.tkhd.getVolume();
    }

    public static enum AudioCodec implements Track.Codec {

        AAC,
        AC3,
        AMR,
        AMR_WIDE_BAND,
        EVRC,
        EXTENDED_AC3,
        QCELP,
        SMV,
        UNKNOWN_AUDIO_CODEC;

        static Track.Codec forType(long type) {
            Track.Codec ac;
            if (type == 1836069985L) {
                ac = AAC;
            } else if (type == 1633889587L) {
                ac = AC3;
            } else if (type == 1935764850L) {
                ac = AMR;
            } else if (type == 1935767394L) {
                ac = AMR_WIDE_BAND;
            } else if (type == 1936029283L) {
                ac = EVRC;
            } else if (type == 1700998451L) {
                ac = EXTENDED_AC3;
            } else if (type == 1936810864L) {
                ac = QCELP;
            } else if (type == 1936944502L) {
                ac = SMV;
            } else {
                ac = UNKNOWN_AUDIO_CODEC;
            }
            return ac;
        }
    }
}