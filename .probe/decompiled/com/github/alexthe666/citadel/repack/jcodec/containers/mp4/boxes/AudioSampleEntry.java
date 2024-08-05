package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ChannelLabel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Label;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.channel.ChannelLayout;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AudioSampleEntry extends SampleEntry {

    public static int kAudioFormatFlagIsFloat = 1;

    public static int kAudioFormatFlagIsBigEndian = 2;

    public static int kAudioFormatFlagIsSignedInteger = 4;

    public static int kAudioFormatFlagIsPacked = 8;

    public static int kAudioFormatFlagIsAlignedHigh = 16;

    public static int kAudioFormatFlagIsNonInterleaved = 32;

    public static int kAudioFormatFlagIsNonMixable = 64;

    private short channelCount;

    private short sampleSize;

    private float sampleRate;

    private short revision;

    private int vendor;

    private int compressionId;

    private int pktSize;

    private int samplesPerPkt;

    private int bytesPerPkt;

    private int bytesPerFrame;

    private int bytesPerSample;

    private short version;

    private int lpcmFlags;

    private static final List<Label> MONO = Arrays.asList(Label.Mono);

    private static final List<Label> STEREO = Arrays.asList(Label.Left, Label.Right);

    private static final List<Label> MATRIX_STEREO = Arrays.asList(Label.LeftTotal, Label.RightTotal);

    public static final Label[] EMPTY = new Label[0];

    public static Set<String> pcms = new HashSet();

    private static Map<Label, ChannelLabel> translationStereo = new HashMap();

    private static Map<Label, ChannelLabel> translationSurround = new HashMap();

    public static AudioSampleEntry createAudioSampleEntry(Header header, short drefInd, short channelCount, short sampleSize, int sampleRate, short revision, int vendor, int compressionId, int pktSize, int samplesPerPkt, int bytesPerPkt, int bytesPerFrame, int bytesPerSample, short version) {
        AudioSampleEntry audio = new AudioSampleEntry(header);
        audio.drefInd = drefInd;
        audio.channelCount = channelCount;
        audio.sampleSize = sampleSize;
        audio.sampleRate = (float) sampleRate;
        audio.revision = revision;
        audio.vendor = vendor;
        audio.compressionId = compressionId;
        audio.pktSize = pktSize;
        audio.samplesPerPkt = samplesPerPkt;
        audio.bytesPerPkt = bytesPerPkt;
        audio.bytesPerFrame = bytesPerFrame;
        audio.bytesPerSample = bytesPerSample;
        audio.version = version;
        return audio;
    }

    public AudioSampleEntry(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.version = input.getShort();
        this.revision = input.getShort();
        this.vendor = input.getInt();
        this.channelCount = input.getShort();
        this.sampleSize = input.getShort();
        this.compressionId = input.getShort();
        this.pktSize = input.getShort();
        long sr = Platform.unsignedInt(input.getInt());
        this.sampleRate = (float) sr / 65536.0F;
        if (this.version == 1) {
            this.samplesPerPkt = input.getInt();
            this.bytesPerPkt = input.getInt();
            this.bytesPerFrame = input.getInt();
            this.bytesPerSample = input.getInt();
        } else if (this.version == 2) {
            input.getInt();
            this.sampleRate = (float) Double.longBitsToDouble(input.getLong());
            this.channelCount = (short) input.getInt();
            input.getInt();
            this.sampleSize = (short) input.getInt();
            this.lpcmFlags = input.getInt();
            this.bytesPerFrame = input.getInt();
            this.samplesPerPkt = input.getInt();
        }
        this.parseExtensions(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(this.version);
        out.putShort(this.revision);
        out.putInt(this.vendor);
        if (this.version < 2) {
            out.putShort(this.channelCount);
            if (this.version == 0) {
                out.putShort(this.sampleSize);
            } else {
                out.putShort((short) 16);
            }
            out.putShort((short) this.compressionId);
            out.putShort((short) this.pktSize);
            out.putInt((int) Math.round((double) this.sampleRate * 65536.0));
            if (this.version == 1) {
                out.putInt(this.samplesPerPkt);
                out.putInt(this.bytesPerPkt);
                out.putInt(this.bytesPerFrame);
                out.putInt(this.bytesPerSample);
            }
        } else if (this.version == 2) {
            out.putShort((short) 3);
            out.putShort((short) 16);
            out.putShort((short) -2);
            out.putShort((short) 0);
            out.putInt(65536);
            out.putInt(72);
            out.putLong(Double.doubleToLongBits((double) this.sampleRate));
            out.putInt(this.channelCount);
            out.putInt(2130706432);
            out.putInt(this.sampleSize);
            out.putInt(this.lpcmFlags);
            out.putInt(this.bytesPerFrame);
            out.putInt(this.samplesPerPkt);
        }
        this.writeExtensions(out);
    }

    public short getChannelCount() {
        return this.channelCount;
    }

    public int calcFrameSize() {
        return this.version != 0 && this.bytesPerFrame != 0 ? this.bytesPerFrame : (this.sampleSize >> 3) * this.channelCount;
    }

    public int calcSampleSize() {
        return this.calcFrameSize() / this.channelCount;
    }

    public short getSampleSize() {
        return this.sampleSize;
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public int getBytesPerFrame() {
        return this.bytesPerFrame;
    }

    public int getBytesPerSample() {
        return this.bytesPerSample;
    }

    public short getVersion() {
        return this.version;
    }

    public ByteOrder getEndian() {
        EndianBox endianBox = NodeBox.findFirstPath(this, EndianBox.class, new String[] { WaveExtension.fourcc(), EndianBox.fourcc() });
        if (endianBox == null) {
            if ("twos".equals(this.header.getFourcc())) {
                return ByteOrder.BIG_ENDIAN;
            } else if ("lpcm".equals(this.header.getFourcc())) {
                return (this.lpcmFlags & kAudioFormatFlagIsBigEndian) != 0 ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
            } else {
                return "sowt".equals(this.header.getFourcc()) ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
            }
        } else {
            return endianBox.getEndian();
        }
    }

    public boolean isFloat() {
        return "fl32".equals(this.header.getFourcc()) || "fl64".equals(this.header.getFourcc()) || "lpcm".equals(this.header.getFourcc()) && (this.lpcmFlags & kAudioFormatFlagIsFloat) != 0;
    }

    public boolean isPCM() {
        return pcms.contains(this.header.getFourcc());
    }

    public AudioFormat getFormat() {
        return new AudioFormat((int) this.sampleRate, this.calcSampleSize() << 3, this.channelCount, true, this.getEndian() == ByteOrder.BIG_ENDIAN);
    }

    public ChannelLabel[] getLabels() {
        ChannelBox channelBox = NodeBox.findFirst(this, ChannelBox.class, "chan");
        if (channelBox != null) {
            Label[] labels = getLabelsFromChan(channelBox);
            return this.channelCount == 2 ? this.translate(translationStereo, labels) : this.translate(translationSurround, labels);
        } else {
            switch(this.channelCount) {
                case 1:
                    return new ChannelLabel[] { ChannelLabel.MONO };
                case 2:
                    return new ChannelLabel[] { ChannelLabel.STEREO_LEFT, ChannelLabel.STEREO_RIGHT };
                case 6:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.LFE, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT };
                default:
                    ChannelLabel[] lbl = new ChannelLabel[this.channelCount];
                    Arrays.fill(lbl, ChannelLabel.MONO);
                    return lbl;
            }
        }
    }

    private ChannelLabel[] translate(Map<Label, ChannelLabel> translation, Label[] labels) {
        ChannelLabel[] result = new ChannelLabel[labels.length];
        int i = 0;
        for (int j = 0; j < labels.length; j++) {
            Label label = labels[j];
            result[i++] = (ChannelLabel) translation.get(label);
        }
        return result;
    }

    public static AudioSampleEntry compressedAudioSampleEntry(String fourcc, int drefId, int sampleSize, int channels, int sampleRate, int samplesPerPacket, int bytesPerPacket, int bytesPerFrame) {
        return createAudioSampleEntry(Header.createHeader(fourcc, 0L), (short) drefId, (short) channels, (short) 16, sampleRate, (short) 0, 0, 65534, 0, samplesPerPacket, bytesPerPacket, bytesPerFrame, 2, (short) 0);
    }

    public static AudioSampleEntry audioSampleEntry(String fourcc, int drefId, int sampleSize, int channels, int sampleRate, ByteOrder endian) {
        AudioSampleEntry ase = createAudioSampleEntry(Header.createHeader(fourcc, 0L), (short) drefId, (short) channels, (short) 16, sampleRate, (short) 0, 0, 65535, 0, 1, sampleSize, channels * sampleSize, sampleSize, (short) 1);
        NodeBox wave = new NodeBox(new Header("wave"));
        ase.add(wave);
        wave.add(FormatBox.createFormatBox(fourcc));
        wave.add(EndianBox.createEndianBox(endian));
        wave.add(Box.terminatorAtom());
        return ase;
    }

    public static String lookupFourcc(AudioFormat format) {
        if (format.getSampleSizeInBits() == 16 && !format.isBigEndian()) {
            return "sowt";
        } else if (format.getSampleSizeInBits() == 24) {
            return "in24";
        } else {
            throw new NotSupportedException("Audio format " + format + " is not supported.");
        }
    }

    public static AudioSampleEntry audioSampleEntryPCM(AudioFormat format) {
        return audioSampleEntry(lookupFourcc(format), 1, format.getSampleSizeInBits() >> 3, format.getChannels(), format.getSampleRate(), format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
    }

    public static Label[] getLabelsFromSampleEntry(AudioSampleEntry se) {
        ChannelBox channel = NodeBox.findFirst(se, ChannelBox.class, "chan");
        if (channel != null) {
            return getLabelsFromChan(channel);
        } else {
            short channelCount = se.getChannelCount();
            switch(channelCount) {
                case 1:
                    return new Label[] { Label.Mono };
                case 2:
                    return new Label[] { Label.Left, Label.Right };
                case 3:
                    return new Label[] { Label.Left, Label.Right, Label.Center };
                case 4:
                    return new Label[] { Label.Left, Label.Right, Label.LeftSurround, Label.RightSurround };
                case 5:
                    return new Label[] { Label.Left, Label.Right, Label.Center, Label.LeftSurround, Label.RightSurround };
                case 6:
                    return new Label[] { Label.Left, Label.Right, Label.Center, Label.LFEScreen, Label.LeftSurround, Label.RightSurround };
                default:
                    Label[] res = new Label[channelCount];
                    Arrays.fill(res, Label.Mono);
                    return res;
            }
        }
    }

    public static Label[] getLabelsFromTrack(TrakBox trakBox) {
        return getLabelsFromSampleEntry((AudioSampleEntry) trakBox.getSampleEntries()[0]);
    }

    public static void setLabel(TrakBox trakBox, int channel, Label label) {
        Label[] labels = getLabelsFromTrack(trakBox);
        labels[channel] = label;
        _setLabels(trakBox, labels);
    }

    public static void _setLabels(TrakBox trakBox, Label[] labels) {
        ChannelBox channel = NodeBox.findFirstPath(trakBox, ChannelBox.class, new String[] { "mdia", "minf", "stbl", "stsd", null, "chan" });
        if (channel == null) {
            channel = ChannelBox.createChannelBox();
            NodeBox.<SampleEntry>findFirstPath(trakBox, SampleEntry.class, new String[] { "mdia", "minf", "stbl", "stsd", null }).add(channel);
        }
        setLabels(labels, channel);
    }

    public static void setLabels(Label[] labels, ChannelBox channel) {
        channel.setChannelLayout(ChannelLayout.kCAFChannelLayoutTag_UseChannelDescriptions.getCode());
        ChannelBox.ChannelDescription[] list = new ChannelBox.ChannelDescription[labels.length];
        for (int i = 0; i < labels.length; i++) {
            list[i] = new ChannelBox.ChannelDescription(labels[i].getVal(), 0, new float[] { 0.0F, 0.0F, 0.0F });
        }
        channel.setDescriptions(list);
    }

    public static Label[] getLabelsByBitmap(long channelBitmap) {
        List<Label> result = new ArrayList();
        Label[] values = Label.values();
        for (int i = 0; i < values.length; i++) {
            Label label = values[i];
            if ((label.bitmapVal & channelBitmap) != 0L) {
                result.add(label);
            }
        }
        return (Label[]) result.toArray(new Label[0]);
    }

    public static Label[] extractLabels(ChannelBox.ChannelDescription[] descriptions) {
        Label[] result = new Label[descriptions.length];
        for (int i = 0; i < descriptions.length; i++) {
            result[i] = descriptions[i].getLabel();
        }
        return result;
    }

    public static Label[] getLabelsFromChan(ChannelBox box) {
        long tag = (long) box.getChannelLayout();
        if (tag >> 16 != 147L) {
            ChannelLayout[] values = ChannelLayout.values();
            for (int i = 0; i < values.length; i++) {
                ChannelLayout layout = values[i];
                if ((long) layout.getCode() == tag) {
                    if (layout == ChannelLayout.kCAFChannelLayoutTag_UseChannelDescriptions) {
                        return extractLabels(box.getDescriptions());
                    }
                    if (layout == ChannelLayout.kCAFChannelLayoutTag_UseChannelBitmap) {
                        return getLabelsByBitmap((long) box.getChannelBitmap());
                    }
                    return layout.getLabels();
                }
            }
            return EMPTY;
        } else {
            int n = (int) tag & 65535;
            Label[] res = new Label[n];
            for (int ix = 0; ix < n; ix++) {
                res[ix] = Label.getByVal(65536 | ix);
            }
            return res;
        }
    }

    static {
        pcms.add("raw ");
        pcms.add("twos");
        pcms.add("sowt");
        pcms.add("fl32");
        pcms.add("fl64");
        pcms.add("in24");
        pcms.add("in32");
        pcms.add("lpcm");
        translationStereo.put(Label.Left, ChannelLabel.STEREO_LEFT);
        translationStereo.put(Label.Right, ChannelLabel.STEREO_RIGHT);
        translationStereo.put(Label.HeadphonesLeft, ChannelLabel.STEREO_LEFT);
        translationStereo.put(Label.HeadphonesRight, ChannelLabel.STEREO_RIGHT);
        translationStereo.put(Label.LeftTotal, ChannelLabel.STEREO_LEFT);
        translationStereo.put(Label.RightTotal, ChannelLabel.STEREO_RIGHT);
        translationStereo.put(Label.LeftWide, ChannelLabel.STEREO_LEFT);
        translationStereo.put(Label.RightWide, ChannelLabel.STEREO_RIGHT);
        translationSurround.put(Label.Left, ChannelLabel.FRONT_LEFT);
        translationSurround.put(Label.Right, ChannelLabel.FRONT_RIGHT);
        translationSurround.put(Label.LeftCenter, ChannelLabel.FRONT_CENTER_LEFT);
        translationSurround.put(Label.RightCenter, ChannelLabel.FRONT_CENTER_RIGHT);
        translationSurround.put(Label.Center, ChannelLabel.CENTER);
        translationSurround.put(Label.CenterSurround, ChannelLabel.REAR_CENTER);
        translationSurround.put(Label.CenterSurroundDirect, ChannelLabel.REAR_CENTER);
        translationSurround.put(Label.LeftSurround, ChannelLabel.REAR_LEFT);
        translationSurround.put(Label.LeftSurroundDirect, ChannelLabel.REAR_LEFT);
        translationSurround.put(Label.RightSurround, ChannelLabel.REAR_RIGHT);
        translationSurround.put(Label.RightSurroundDirect, ChannelLabel.REAR_RIGHT);
        translationSurround.put(Label.RearSurroundLeft, ChannelLabel.SIDE_LEFT);
        translationSurround.put(Label.RearSurroundRight, ChannelLabel.SIDE_RIGHT);
        translationSurround.put(Label.LFE2, ChannelLabel.LFE);
        translationSurround.put(Label.LFEScreen, ChannelLabel.LFE);
        translationSurround.put(Label.LeftTotal, ChannelLabel.STEREO_LEFT);
        translationSurround.put(Label.RightTotal, ChannelLabel.STEREO_RIGHT);
        translationSurround.put(Label.LeftWide, ChannelLabel.STEREO_LEFT);
        translationSurround.put(Label.RightWide, ChannelLabel.STEREO_RIGHT);
    }
}