package com.github.alexthe666.citadel.repack.jcodec.codecs.aac;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.mp4.EsdsBox;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ChannelLabel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.BoxUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import java.nio.ByteBuffer;

public class AACUtils {

    private static ChannelLabel[][] AAC_DEFAULT_CONFIGS = new ChannelLabel[][] { null, { ChannelLabel.MONO }, { ChannelLabel.STEREO_LEFT, ChannelLabel.STEREO_RIGHT }, { ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT }, { ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_CENTER }, { ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT }, { ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT, ChannelLabel.LFE }, { ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.SIDE_LEFT, ChannelLabel.SIDE_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT, ChannelLabel.LFE } };

    private static int getObjectType(BitReader reader) {
        int objectType = reader.readNBit(5);
        if (objectType == ObjectType.AOT_ESCAPE.ordinal()) {
            objectType = 32 + reader.readNBit(6);
        }
        return objectType;
    }

    public static AACUtils.AACMetadata parseAudioInfo(ByteBuffer privData) {
        BitReader reader = BitReader.createBitReader(privData);
        int objectType = getObjectType(reader);
        int index = reader.readNBit(4);
        int sampleRate = index == 15 ? reader.readNBit(24) : AACConts.AAC_SAMPLE_RATES[index];
        int channelConfig = reader.readNBit(4);
        if (channelConfig != 0 && channelConfig < AAC_DEFAULT_CONFIGS.length) {
            ChannelLabel[] channels = AAC_DEFAULT_CONFIGS[channelConfig];
            return new AACUtils.AACMetadata(new AudioFormat(sampleRate, 16, channels.length, true, false), channels);
        } else {
            return null;
        }
    }

    public static AACUtils.AACMetadata getMetadata(SampleEntry mp4a) {
        if (!"mp4a".equals(mp4a.getFourcc())) {
            throw new IllegalArgumentException("Not mp4a sample entry");
        } else {
            ByteBuffer b = getCodecPrivate(mp4a);
            return b == null ? null : parseAudioInfo(b);
        }
    }

    public static ByteBuffer getCodecPrivate(SampleEntry mp4a) {
        Box.LeafBox b = NodeBox.findFirst(mp4a, Box.LeafBox.class, "esds");
        if (b == null) {
            b = NodeBox.findFirstPath(mp4a, Box.LeafBox.class, new String[] { null, "esds" });
        }
        if (b == null) {
            return null;
        } else {
            EsdsBox esds = BoxUtil.as(EsdsBox.class, b);
            return esds.getStreamInfo();
        }
    }

    public static ADTSParser.Header streamInfoToADTS(ByteBuffer si, boolean crcAbsent, int numAACFrames, int frameSize) {
        BitReader rd = BitReader.createBitReader(si.duplicate());
        int objectType = rd.readNBit(5);
        int samplingIndex = rd.readNBit(4);
        int chanConfig = rd.readNBit(4);
        return new ADTSParser.Header(objectType, chanConfig, crcAbsent ? 1 : 0, numAACFrames, samplingIndex, 7 + frameSize);
    }

    public static class AACMetadata {

        private AudioFormat format;

        private ChannelLabel[] labels;

        public AACMetadata(AudioFormat format, ChannelLabel[] labels) {
            this.format = format;
            this.labels = labels;
        }

        public AudioFormat getFormat() {
            return this.format;
        }

        public ChannelLabel[] getLabels() {
            return this.labels;
        }
    }
}