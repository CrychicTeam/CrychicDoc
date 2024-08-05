package com.github.alexthe666.citadel.repack.jcodec.codecs.wav;

import com.github.alexthe666.citadel.repack.jcodec.api.UnhandledStateException;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ChannelLabel;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WavHeader {

    static ChannelLabel[] mapping = new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.LFE, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT, ChannelLabel.FRONT_CENTER_LEFT, ChannelLabel.FRONT_CENTER_RIGHT, ChannelLabel.REAR_CENTER, ChannelLabel.SIDE_LEFT, ChannelLabel.SIDE_RIGHT, ChannelLabel.CENTER, ChannelLabel.FRONT_LEFT, ChannelLabel.CENTER, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_CENTER, ChannelLabel.REAR_RIGHT, ChannelLabel.STEREO_LEFT, ChannelLabel.STEREO_RIGHT };

    public String chunkId;

    public int chunkSize;

    public String format;

    public WavHeader.FmtChunk fmt;

    public int dataOffset;

    public long dataSize;

    public static final int WAV_HEADER_SIZE = 44;

    public WavHeader(String chunkId, int chunkSize, String format, WavHeader.FmtChunk fmt, int dataOffset, long dataSize) {
        this.chunkId = chunkId;
        this.chunkSize = chunkSize;
        this.format = format;
        this.fmt = fmt;
        this.dataOffset = dataOffset;
        this.dataSize = dataSize;
    }

    public static WavHeader copyWithRate(WavHeader header, int rate) {
        WavHeader result = new WavHeader(header.chunkId, header.chunkSize, header.format, copyFmt(header.fmt), header.dataOffset, header.dataSize);
        result.fmt.sampleRate = rate;
        return result;
    }

    public static WavHeader copyWithChannels(WavHeader header, int channels) {
        WavHeader result = new WavHeader(header.chunkId, header.chunkSize, header.format, copyFmt(header.fmt), header.dataOffset, header.dataSize);
        result.fmt.numChannels = (short) channels;
        return result;
    }

    private static WavHeader.FmtChunk copyFmt(WavHeader.FmtChunk fmt) {
        if (fmt instanceof WavHeader.FmtChunkExtended) {
            WavHeader.FmtChunkExtended fmtext = (WavHeader.FmtChunkExtended) fmt;
            fmt = new WavHeader.FmtChunkExtended(fmtext, fmtext.cbSize, fmtext.bitsPerCodedSample, fmtext.channelLayout, fmtext.guid);
        } else {
            fmt = new WavHeader.FmtChunk(fmt.audioFormat, fmt.numChannels, fmt.sampleRate, fmt.byteRate, fmt.blockAlign, fmt.bitsPerSample);
        }
        return fmt;
    }

    public static WavHeader createWavHeader(AudioFormat format, int samples) {
        return new WavHeader("RIFF", 40, "WAVE", new WavHeader.FmtChunk((short) 1, (short) format.getChannels(), format.getSampleRate(), format.getSampleRate() * format.getChannels() * (format.getSampleSizeInBits() >> 3), (short) (format.getChannels() * (format.getSampleSizeInBits() >> 3)), (short) format.getSampleSizeInBits()), 44, calcDataSize(format.getChannels(), format.getSampleSizeInBits() >> 3, (long) samples));
    }

    public static WavHeader stereo48k() {
        return stereo48kWithSamples(0L);
    }

    public static WavHeader stereo48kWithSamples(long samples) {
        return new WavHeader("RIFF", 40, "WAVE", new WavHeader.FmtChunk((short) 1, (short) 2, 48000, 192000, (short) 4, (short) 16), 44, calcDataSize(2, 2, samples));
    }

    public static WavHeader mono48k(long samples) {
        return new WavHeader("RIFF", 40, "WAVE", new WavHeader.FmtChunk((short) 1, (short) 1, 48000, 96000, (short) 2, (short) 16), 44, calcDataSize(1, 2, samples));
    }

    public static WavHeader emptyWavHeader() {
        return new WavHeader("RIFF", 40, "WAVE", newFmtChunk(), 44, 0L);
    }

    private static WavHeader.FmtChunk newFmtChunk() {
        return new WavHeader.FmtChunk((short) 1, (short) 0, 0, 0, (short) 0, (short) 0);
    }

    public static WavHeader read(File file) throws IOException {
        ReadableByteChannel is = null;
        WavHeader var2;
        try {
            is = NIOUtils.readableChannel(file);
            var2 = readChannel(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return var2;
    }

    public static WavHeader readChannel(ReadableByteChannel _in) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(128);
        buf.order(ByteOrder.LITTLE_ENDIAN);
        _in.read(buf);
        if (buf.remaining() > 0) {
            throw new IOException("Incomplete wav header found");
        } else {
            buf.flip();
            String chunkId = NIOUtils.readString(buf, 4);
            int chunkSize = buf.getInt();
            String format = NIOUtils.readString(buf, 4);
            WavHeader.FmtChunk fmt = null;
            if ("RIFF".equals(chunkId) && "WAVE".equals(format)) {
                int size = 0;
                String fourcc;
                do {
                    fourcc = NIOUtils.readString(buf, 4);
                    size = buf.getInt();
                    if ("fmt ".equals(fourcc) && size >= 14 && size <= 1048576) {
                        switch(size) {
                            case 16:
                                fmt = WavHeader.FmtChunk.get(buf);
                                break;
                            case 18:
                                fmt = WavHeader.FmtChunk.get(buf);
                                NIOUtils.skip(buf, 2);
                                break;
                            case 28:
                                fmt = WavHeader.FmtChunkExtended.get(buf);
                                break;
                            case 40:
                                fmt = WavHeader.FmtChunkExtended.get(buf);
                                NIOUtils.skip(buf, 12);
                                break;
                            default:
                                throw new UnhandledStateException("Don't know how to handle fmt size: " + size);
                        }
                    } else if (!"data".equals(fourcc)) {
                        NIOUtils.skip(buf, size);
                    }
                } while (!"data".equals(fourcc));
                return new WavHeader(chunkId, chunkSize, format, fmt, buf.position(), (long) size);
            } else {
                return null;
            }
        }
    }

    public static WavHeader multiChannelWavFromFiles(File[] files) throws IOException {
        WavHeader[] headers = new WavHeader[files.length];
        for (int i = 0; i < files.length; i++) {
            headers[i] = read(files[i]);
        }
        return multiChannelWav(headers);
    }

    public static WavHeader multiChannelWav(WavHeader[] headers) {
        WavHeader w = emptyWavHeader();
        int totalSize = 0;
        for (int i = 0; i < headers.length; i++) {
            WavHeader wavHeader = headers[i];
            totalSize = (int) ((long) totalSize + wavHeader.dataSize);
        }
        w.dataSize = (long) totalSize;
        WavHeader.FmtChunk fmt = headers[0].fmt;
        int bitsPerSample = fmt.bitsPerSample;
        int bytesPerSample = bitsPerSample / 8;
        int sampleRate = fmt.sampleRate;
        w.fmt.bitsPerSample = (short) bitsPerSample;
        w.fmt.blockAlign = (short) (headers.length * bytesPerSample);
        w.fmt.byteRate = headers.length * bytesPerSample * sampleRate;
        w.fmt.numChannels = (short) headers.length;
        w.fmt.sampleRate = sampleRate;
        return w;
    }

    public void write(WritableByteChannel out) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(44);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        long chunkSize;
        if (this.dataSize <= 4294967295L) {
            chunkSize = this.dataSize + 36L;
        } else {
            chunkSize = 40L;
        }
        bb.put(JCodecUtil2.asciiString("RIFF"));
        bb.putInt((int) chunkSize);
        bb.put(JCodecUtil2.asciiString("WAVE"));
        bb.put(JCodecUtil2.asciiString("fmt "));
        bb.putInt(this.fmt.size());
        this.fmt.put(bb);
        bb.put(JCodecUtil2.asciiString("data"));
        if (this.dataSize <= 4294967295L) {
            bb.putInt((int) this.dataSize);
        } else {
            bb.putInt(0);
        }
        bb.flip();
        out.write(bb);
    }

    public static long calcDataSize(int numChannels, int bytesPerSample, long samples) {
        return samples * (long) numChannels * (long) bytesPerSample;
    }

    public static WavHeader create(AudioFormat af, int size) {
        WavHeader w = emptyWavHeader();
        w.dataSize = (long) size;
        WavHeader.FmtChunk fmt = newFmtChunk();
        int bitsPerSample = af.getSampleSizeInBits();
        int bytesPerSample = bitsPerSample / 8;
        int sampleRate = af.getSampleRate();
        w.fmt.bitsPerSample = (short) bitsPerSample;
        w.fmt.blockAlign = af.getFrameSize();
        w.fmt.byteRate = af.getFrameRate() * af.getFrameSize();
        w.fmt.numChannels = (short) af.getChannels();
        w.fmt.sampleRate = af.getSampleRate();
        return w;
    }

    public ChannelLabel[] getChannelLabels() {
        if (this.fmt instanceof WavHeader.FmtChunkExtended) {
            return ((WavHeader.FmtChunkExtended) this.fmt).getLabels();
        } else {
            switch(this.fmt.numChannels) {
                case 1:
                    return new ChannelLabel[] { ChannelLabel.MONO };
                case 2:
                    return new ChannelLabel[] { ChannelLabel.STEREO_LEFT, ChannelLabel.STEREO_RIGHT };
                case 3:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_CENTER };
                case 4:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT };
                case 5:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT };
                case 6:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.LFE, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT };
                case 7:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.LFE, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT, ChannelLabel.REAR_CENTER };
                case 8:
                    return new ChannelLabel[] { ChannelLabel.FRONT_LEFT, ChannelLabel.FRONT_RIGHT, ChannelLabel.CENTER, ChannelLabel.LFE, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT, ChannelLabel.REAR_LEFT, ChannelLabel.REAR_RIGHT };
                default:
                    ChannelLabel[] labels = new ChannelLabel[this.fmt.numChannels];
                    Arrays.fill(labels, ChannelLabel.MONO);
                    return labels;
            }
        }
    }

    public AudioFormat getFormat() {
        return new AudioFormat(this.fmt.sampleRate, this.fmt.bitsPerSample, this.fmt.numChannels, true, false);
    }

    public static class FmtChunk {

        public short audioFormat;

        public short numChannels;

        public int sampleRate;

        public int byteRate;

        public short blockAlign;

        public short bitsPerSample;

        public FmtChunk(short audioFormat, short numChannels, int sampleRate, int byteRate, short blockAlign, short bitsPerSample) {
            this.audioFormat = audioFormat;
            this.numChannels = numChannels;
            this.sampleRate = sampleRate;
            this.byteRate = byteRate;
            this.blockAlign = blockAlign;
            this.bitsPerSample = bitsPerSample;
        }

        public static WavHeader.FmtChunk get(ByteBuffer bb) throws IOException {
            ByteOrder old = bb.order();
            WavHeader.FmtChunk var2;
            try {
                bb.order(ByteOrder.LITTLE_ENDIAN);
                var2 = new WavHeader.FmtChunk(bb.getShort(), bb.getShort(), bb.getInt(), bb.getInt(), bb.getShort(), bb.getShort());
            } finally {
                bb.order(old);
            }
            return var2;
        }

        public void put(ByteBuffer bb) throws IOException {
            ByteOrder old = bb.order();
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort(this.audioFormat);
            bb.putShort(this.numChannels);
            bb.putInt(this.sampleRate);
            bb.putInt(this.byteRate);
            bb.putShort(this.blockAlign);
            bb.putShort(this.bitsPerSample);
            bb.order(old);
        }

        public int size() {
            return 16;
        }
    }

    public static class FmtChunkExtended extends WavHeader.FmtChunk {

        short cbSize;

        short bitsPerCodedSample;

        int channelLayout;

        int guid;

        public FmtChunkExtended(WavHeader.FmtChunk other, short cbSize, short bitsPerCodedSample, int channelLayout, int guid) {
            super(other.audioFormat, other.numChannels, other.sampleRate, other.byteRate, other.blockAlign, other.bitsPerSample);
            this.cbSize = cbSize;
            this.bitsPerCodedSample = bitsPerCodedSample;
            this.channelLayout = channelLayout;
            this.guid = guid;
        }

        public static WavHeader.FmtChunk read(ByteBuffer bb) throws IOException {
            WavHeader.FmtChunk fmtChunk = WavHeader.FmtChunk.get(bb);
            ByteOrder old = bb.order();
            WavHeader.FmtChunkExtended var3;
            try {
                bb.order(ByteOrder.LITTLE_ENDIAN);
                var3 = new WavHeader.FmtChunkExtended(fmtChunk, bb.getShort(), bb.getShort(), bb.getInt(), bb.getInt());
            } finally {
                bb.order(old);
            }
            return var3;
        }

        @Override
        public void put(ByteBuffer bb) throws IOException {
            super.put(bb);
            ByteOrder old = bb.order();
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.putShort(this.cbSize);
            bb.putShort(this.bitsPerCodedSample);
            bb.putInt(this.channelLayout);
            bb.putInt(this.guid);
            bb.order(old);
        }

        @Override
        public int size() {
            return super.size() + 12;
        }

        public ChannelLabel[] getLabels() {
            List<ChannelLabel> labels = new ArrayList();
            for (int i = 0; i < WavHeader.mapping.length; i++) {
                if ((this.channelLayout & 1 << i) != 0) {
                    labels.add(WavHeader.mapping[i]);
                }
            }
            return (ChannelLabel[]) labels.toArray(new ChannelLabel[0]);
        }
    }
}